package vaxsoft.com.vaxphone.AccountLogin;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.concurrent.atomic.AtomicBoolean;

//import io.lettuce.core.RedisChannelHandler;
//import io.lettuce.core.RedisClient;
//import io.lettuce.core.RedisConnectionStateListener;
//import io.lettuce.core.pubsub.RedisPubSubListener;
//import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import io.wf9a5m75.redis.RedisAndroid;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import vaxsoft.com.vaxphone.CryptoHelper;
import vaxsoft.com.vaxphone.MainAPP.VaxPhoneAPP;
import vaxsoft.com.vaxphone.R;
import vaxsoft.com.vaxphone.MainTab.MainTabActivity;
import vaxsoft.com.vaxphone.MainUtil.DialogUtil;
import vaxsoft.com.vaxphone.RedisIntentService;
import vaxsoft.com.vaxphone.VaxPhoneSIP;
import vaxsoft.com.vaxphone.VaxStorage.Store.StoreSettings.StoreAudioCodecs;

public class AccountLoginActivity extends AppCompatActivity
{
    private static AccountLoginActivity mLoginActivity = null;
    private static String m_sLastStatusText = "Account is offline";

    private final int ID_REQUEST_READ_CONTACTS = 1000;

    EditText EditTextUsername, EditTextPassword, EditTextServerAddr;
    TextView TextViewStatus;
    Button BtnLogin;

    private IntentIntegrator qrScan;
    private Button buttonScan;


    private Handler mDelayHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        if (VaxPhoneSIP.m_objVaxVoIP.IsOnline())
        {
            Intent myIntent = new Intent(this, MainTabActivity.class);
            startActivity(myIntent);
        }

        CheckIfFromLink();


        setContentView(R.layout.activity_log_in);

        mLoginActivity = this;
        mDelayHandler = null;

        //VaxPhoneSIP.m_objVaxVoIP.NetworkReachability(true);
        qrScan = new IntentIntegrator(this);
        InitViews();
        UpdateUI();

       if (getIntent().getAction() != "logout")
       {
           if (EditTextServerAddr.getText().toString() != "" || EditTextPassword.getText().toString() != "" || EditTextUsername.getText().toString() != "")
           {
               Initialize();
           }
       }

       // Initialize();

    }
//    @Override
//    protected void onCreateView(String name, Context context, AttributeSet attrs)
//    {
//
//    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckIfFromLink();
        // unregisterReceiver(receiver);
    }
    @Override
    protected void onStop() {
        super.onStop();
       // unregisterReceiver(receiver);
    }
    @Override
    protected void onPause() {
        //super.onStop();
        super.onPause();
       // unregisterReceiver(receiver);
    }



    private void CheckIfFromLink()
    {

        Uri data = getIntent().getData();

       // Toast.makeText(getApplicationContext(),3)
      //  Toast.makeText(getApplicationContext(),"URL : " +data,Toast.LENGTH_LONG);

        if (data != null)
        {
            String key = data.getQueryParameter("key");

            if (key != null)
            {
               // Toast.makeText(getApplicationContext(),"KEY : " +key,Toast.LENGTH_LONG);
                GetDataAndLogin(key);
            }
        }

    }

    private void InitViews()
    {
        EditTextUsername = findViewById(R.id.username);
        EditTextPassword = findViewById(R.id.password);
        EditTextServerAddr = findViewById(R.id.server_ip_domain);
       // SwitchRegistrationSIP = findViewById(R.id.sip_registration);
     // buttonScan = findViewById(R.id.btnQrScan);
        BtnLogin = findViewById(R.id.BtnLogin);
        TextViewStatus = findViewById(R.id.status);
    }

    public void UpdateUI()
    {
        StringBuilder Username = new StringBuilder();
        StringBuilder DisplayName = new StringBuilder();
        StringBuilder AuthLogin = new StringBuilder();
        StringBuilder AuthPassword = new StringBuilder();
        StringBuilder DoaminRealm = new StringBuilder();
        StringBuilder ServerIP = new StringBuilder();
        StringBuilder ServerPort = new StringBuilder();
        AtomicBoolean RegistrationSIP = new AtomicBoolean();
        StringBuilder RedisServer = new StringBuilder();
        StringBuilder RedisPassword = new StringBuilder();
        StringBuilder RedisPort = new StringBuilder();

        VaxPhoneSIP.m_objVaxVoIP.GetLoginInfo(Username, DisplayName, AuthLogin, AuthPassword, DoaminRealm, ServerIP, ServerPort, RegistrationSIP,RedisServer,RedisPassword,RedisPort,null);

        String sPassword = AuthPassword.toString();
        String sServerIP = ServerIP.toString();
        String sServerPort = ServerPort.toString();

        EditTextUsername.setText(AuthLogin);
        EditTextPassword.setText(sPassword);

        String sServerAddr = "";

        if (sServerIP.length() != 0)
        {
            if(sServerPort.length() == 0)
                sServerPort = "5060";

            sServerAddr = sServerIP + ":" + sServerPort;
        }

        EditTextServerAddr.setText(sServerAddr);
        //SwitchRegistrationSIP.setChecked(bRegistrationSIP);

        if(VaxPhoneSIP.m_objVaxVoIP.IsOnline())
        {
            BtnLogin.setText("Çıkış");
        }
        else
        {
            BtnLogin.setText("Bağlan");
        }

        TextViewStatus.setText(m_sLastStatusText);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    public void BtnAccountInfo(View view)
    {
        UpdateLogInInfo();
        AccountInfoFragment objAccountInfo = new AccountInfoFragment();
        FragmentManager objFragManager = getSupportFragmentManager();
        FragmentTransaction objFragTransaction = objFragManager.beginTransaction();
        objFragTransaction.add(android.R.id.content, objAccountInfo ,"Account Info");
        objFragTransaction.addToBackStack("Account Info");
        objFragTransaction.commit();
    }

    public void BtnLogIn(View view)
    {
        if (!UpdateLogInInfo())
            return;

        if(VaxPhoneSIP.m_objVaxVoIP.IsOnline())
        {
            StopOpenMainTabWithDelay();

            VaxPhoneSIP.m_objVaxVoIP.UnInitialize();
            BtnLogin.setText("Bağlan");

            return;
        }

        if(!Initialize())
            return;

        BtnLogin.setText("Çıkış");
    }

    private boolean Initialize()
    {
        StringBuilder Username = new StringBuilder();
        StringBuilder DisplayName = new StringBuilder();
        StringBuilder AuthLogin = new StringBuilder();
        StringBuilder AuthPassword = new StringBuilder();
        StringBuilder DomainRealm = new StringBuilder();
        StringBuilder ServerIP = new StringBuilder();
        StringBuilder ServerPort = new StringBuilder();
        AtomicBoolean RegistrationSIP = new AtomicBoolean();
        StringBuilder sRedisServer = new StringBuilder();
        StringBuilder sRedisPassword = new StringBuilder();
        StringBuilder sRedisPort = new StringBuilder();
        VaxPhoneSIP.m_objVaxVoIP.GetLoginInfo(Username, DisplayName, AuthLogin, AuthPassword, DomainRealm, ServerIP, ServerPort, RegistrationSIP,sRedisServer,sRedisPassword,sRedisPort,null);

        String sUsername = Username.toString();
        String sDisplayName = DisplayName.toString();
        String sAuthLogin = AuthLogin.toString();
        String sAuthPassword = AuthPassword.toString();
        String sDomainRealm = DomainRealm.toString();
        String sServerIP = ServerIP.toString();
        int nServerPort = Integer.parseInt(ServerPort.toString());
        boolean bRegistrationSIP = RegistrationSIP.get();

        if(!VaxPhoneSIP.m_objVaxVoIP.Initialize(sDisplayName, sUsername, sAuthLogin, sAuthPassword, sDomainRealm, sServerIP, nServerPort, bRegistrationSIP, this))
            return false;

        StartOpenMainTabWithDelay();

        return true;
    }





    public void BtnQrScanClick(View view)
    {
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
               GetDataAndLogin(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void GetDataAndLogin(String key)
    {
        Toast.makeText(getApplicationContext(),key,Toast.LENGTH_LONG);
        try {

            key = key.replace(" ","+");
            String decryptedJson = CryptoHelper.decrypt(key,CryptoHelper.QrKey);

            JSONObject obj = new JSONObject(decryptedJson);
            String sUsername = obj.getString("UserName");
            String sServerIP = obj.getString("IpPbxServerAddress");
            String sDisplayName = obj.getString("DisplayName");
            String sAuthLogin = obj.getString("UserName");
            String sAuthPassword = obj.getString("Password");
            String sDoaminRealm = obj.getString("DomainRealm");
            String sServerPort = obj.getString("ServerPort");
            String sRedisServer = obj.getString("RedisServer");
            String sRedisPort = obj.getString("RedisPort");
            String sRedisPassword = obj.getString("RedisServerPassword");
            String codecs = obj.getString("Codec");
            String systemId = obj.getString("SystemId");

            String[] splitArray = codecs.split(";");

            boolean bRegistrationSIP = true;
            VaxPhoneSIP.m_objVaxVoIP.SetLoginInfo(sUsername, sDisplayName, sAuthLogin,
                    sAuthPassword, sDoaminRealm, sServerIP, sServerPort, bRegistrationSIP,sRedisServer,sRedisPassword,sRedisPort,systemId);

            EditTextUsername.setText(sAuthLogin);
            EditTextPassword.setText(sAuthPassword);
            EditTextServerAddr.setText(sServerIP);

            StoreAudioCodecs cStore = new StoreAudioCodecs();
          //  VaxPhoneSIP.m_objVaxVoIP.DeselectAllVoiceCodec();

            for (String item:splitArray) {
                try
                {
                    int voiceCodec = Integer.parseInt(item);
                   // VaxPhoneSIP.m_objVaxVoIP.SelectVoiceCodec(voiceCodec);
                    cStore.SetAudioCodec(voiceCodec);

                }catch(Exception ex){
                    //ss
                }
            }

            if (!UpdateLogInInfo())
                return;

            if(VaxPhoneSIP.m_objVaxVoIP.IsOnline())
            {
                StopOpenMainTabWithDelay();

                VaxPhoneSIP.m_objVaxVoIP.UnInitialize();
                BtnLogin.setText("LOG IN");

                return;
            }

            if(!Initialize())
                return;

            BtnLogin.setText("LOG OUT");


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
          //  Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean UpdateLogInInfo()
    {
        String sAuthLogin = EditTextUsername.getText().toString();
        String sServerAddr = EditTextServerAddr.getText().toString();

        if (sAuthLogin.length() <= 0)
        {
            DialogUtil.ShowDialog(this, "Please enter Auth Login");
            return false;
        }

        if (sServerAddr.length() <= 0)
        {
            DialogUtil.ShowDialog(this, "Please enter SIP Server address");
            return false;
        }

        StringBuilder AuthLogin = new StringBuilder();
        StringBuilder Username = new StringBuilder();
        StringBuilder DisplayName = new StringBuilder();
        StringBuilder DomainRealm = new StringBuilder();

        VaxPhoneSIP.m_objVaxVoIP.GetLoginInfo(Username, DisplayName, AuthLogin, null, DomainRealm, null, null, null,
                null,null,null,null);

        String sServerIP;
        String sServerPort;

        if (sServerAddr.contains(":"))
        {
            String aServerAddr[] = sServerAddr.split(":");

            sServerIP = aServerAddr[0];
            sServerPort = aServerAddr[aServerAddr.length - 1];
        }
        else
        {
            sServerIP = sServerAddr;
            sServerPort = "5060";
        }

        boolean bRegistrationSIP = true; //SwitchRegistrationSIP.isChecked();

        if (!AuthLogin.toString().equals(sAuthLogin))
        {
            Username.setLength(0);
            Username.append(sAuthLogin);

            DisplayName.setLength(0);
            DisplayName.append(sAuthLogin);

            DomainRealm.setLength(0);
            DomainRealm.append(sServerIP);
        }

        String sAuthPwd = EditTextPassword.getText().toString();

        VaxPhoneSIP.m_objVaxVoIP.SetLoginInfo(Username.toString(), DisplayName.toString(), sAuthLogin, sAuthPwd, DomainRealm.toString(), sServerIP, sServerPort, bRegistrationSIP,null,null,null,null);
        return true;
    }

    void StartOpenMainTabWithDelay()
    {

        if (VaxPhoneSIP.isMerlin)
        {
            StopOpenMainTabWithDelay();

            mDelayHandler = new Handler();

            mDelayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent objIntent = new Intent(AccountLoginActivity.this, MainTabActivity.class);
                    startActivity(objIntent);

                    mDelayHandler = null;
                    finish();
                }
            }, 1000);
        }
        else   {
            TextViewStatus.setText("Only Merlin IpPbx");
        }



    }

    void StopOpenMainTabWithDelay()
    {
        if (mDelayHandler != null)
        {
            mDelayHandler.removeCallbacksAndMessages(null);
            mDelayHandler = null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    private void OnStatusText(String sMsg)
    {
        TextViewStatus.setText(sMsg);
    }

    public void OnSuccessToRegister()
    {
        StartOpenMainTabWithDelay();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public static void PostStatusText(String sMsg)
    {
        m_sLastStatusText = sMsg;

        if (mLoginActivity == null)
            return;

        mLoginActivity.OnStatusText(sMsg);

    }

    public static void PostSuccessToRegister()
    {
        if (mLoginActivity == null)
            return;

        mLoginActivity.OnSuccessToRegister();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    
    private void AskContactPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, ID_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int nRequestCode, @NonNull String[] sPermissions, @NonNull int[] aGrantResults)
    {
        if (nRequestCode == ID_REQUEST_READ_CONTACTS)
        {
            /*
            if (aGrantResults.length > 0 && aGrantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                boolean bResult = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);

                if (bResult)
                    DialogUtil.ShowDialog(this, "Please allow VaxPhone to read contacts so you can use full features of app.");

                if (!bResult)
                    DialogUtil.ShowDialog(this, "Please Allow Manually");
            }
            */
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        AskContactPermission();
    }

    @Override
    protected void onDestroy()
    {
        mLoginActivity = null;
        //unregisterReceiver(receiver);
        super.onDestroy();
    }
}
