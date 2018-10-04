package vaxsoft.com.vaxphone;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.app.NotificationCompat;
import android.telecom.DisconnectCause;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import VaxVoIP.VaxUserAgentLib.VaxUserAgentLib;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.AudioCodecsDialog;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.DigitDTMFDialog;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.NetworkFragment;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.RingtonesDialog;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.SettingMicSpk;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.SettingsFragment;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.TunnelFragment;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.VideoBitrateDialog;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.VideoCodecsDialog;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.VideoQualityDialog;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerSettings.VoiceChangerDialog;
import vaxsoft.com.vaxphone.AccountLogin.AccountLoginActivity;
import vaxsoft.com.vaxphone.MainAPP.VaxPhoneAPP;
import vaxsoft.com.vaxphone.MainTab.CallTab.DialpadFragment;
import vaxsoft.com.vaxphone.PhoneSIP.BusyRing.BusyRing;
import vaxsoft.com.vaxphone.PhoneSIP.CallInfo.CallInfo;
import vaxsoft.com.vaxphone.PhoneSIP.DialRing.DialRing;
import vaxsoft.com.vaxphone.PhoneSIP.DigitTone.PlayDTMF;
import vaxsoft.com.vaxphone.PhoneSIP.ProximitySensor.ProximitySensor;
import vaxsoft.com.vaxphone.PhoneSIP.RingTone.RingTone;
import vaxsoft.com.vaxphone.MainTab.CallTab.CallTabFragment;
import vaxsoft.com.vaxphone.MainTab.ChatTab.ChatContactTabFragment;
import vaxsoft.com.vaxphone.MainTab.ChatTab.ChatContactRecyclerView;
import vaxsoft.com.vaxphone.MainTab.RecentTab.RecentRecyclerView;
import vaxsoft.com.vaxphone.MainTab.MainTabActivity;
import vaxsoft.com.vaxphone.MainUtil.DialogUtil;
import vaxsoft.com.vaxphone.VaxStorage.Store.StoreChatContact;
import vaxsoft.com.vaxphone.VaxStorage.Store.StoreChatMsg;
import vaxsoft.com.vaxphone.VaxStorage.Store.StoreLoginInfo;
import vaxsoft.com.vaxphone.VaxStorage.Store.StoreRecent;



public class VaxPhoneSIP extends VaxUserAgentLib
{

    public static VaxPhoneSIP m_objVaxVoIP = null;
    public static boolean isMerlin = false;

    private String m_sIncomingCallId = "";
    private String m_sLogFile = "";
    private String m_CallerId = "";
    private String m_CallerName = "";

    private boolean m_bVideoEnabled = true;
    private boolean m_bOnline = false;
    private boolean m_bVideoDeviceCaptured = false;
    private boolean m_bFrontVideoCameraCaptured = true;
    private boolean m_bMuteMic = false;
    private boolean m_bMuteSpk = false;
    private boolean m_bVoiceChangerEnabled = false;
    private boolean m_bVideoCaptureErrorShown = false;




    private DialRing m_objDialRing = null;
    private BusyRing m_objBusyRing = null;
    private RingTone m_objRingTone = null;
    private PlayDTMF m_objPlayDTMF = null;
    private ProximitySensor m_objProximitySensor = null;

    public VaxPhoneSIP()
    {
        super(VaxPhoneAPP.getAppContext());

        m_objVaxVoIP = this;

        m_objDialRing = new DialRing();
        m_objBusyRing = new BusyRing();

        m_objRingTone = new RingTone();
        m_objPlayDTMF = new PlayDTMF();

        m_objProximitySensor = new ProximitySensor();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    public boolean Initialize(String sDisplayName, String sUserName, String sAuthLogin, String sAuthPwd, String sDomainRealm, String sServerAddr, int nServerPort, boolean bRegistrationSIP, Context objContext)
    {

        //Production 6 2.8
        super.SetLicenceKey("VAXVOIP.COM-128I182I84I11I237I90I7I127I215I236I226I57I144I106I175I218I96I86I156I89I148I74I200I97I6I77I104I232I86I210I42I198I169I187I115I34I46I68I220I150I233I30I232I171I31I203I245I143I31I45I141I172I91I231I180I122I45I126I94I27I196I164I96I139I88I69I201I102I236I7I21I172I184I201I114I203I75I166I67I198I192I177I234I152I107I191I0I181I173I171I67I18I175I30I170I182I235I120I222I202I157I239I168I107I19I91I31I243I241I172I119I12I219I125I5I213I42I21I234I93I85I203I235I110I158I168I41I188I207I70I149I27I236I71I30I43I229I0I41I187I232I132I187I19I128I239I124I22I77I184I236I30I11I71I117I208I159I90I111I124I186I239I167I4I177I61I53I221I171I228I142I98I212I8I32I91I184I65I125I26I154I19I180I150I196I182I87I14I61I85I203I96I66I161I12I189I195I167I88I208I230I149I169I74I241I86I152I241I71I80I6I110I107I157I163I104I43I29I35I143I24I189I69I243I161I7I146I247I158I19I61I191I146I249I150I207I196I255I65I217I101I127I153I47I71I60I95I58I164I208I145I110I24I75I70I44I215I153I242I94I55I2I218I19I117I247I8I231I32I109I223I121I251I149I255I141I9I32I154I182I117I196I60I101I56I239I88I236I5I72I154I159I5I160I251I70I166I140I100I245I112I144I76I58I183I238I97I172I254I129I207I146I87I188I101I22I249I1I181I148I56I21I236I99I140I11I58I216I17I108I92I133I-50.G729.UNLIMITED.MERLINYAZILIM.COM.TR",objContext);
        ////Production 5 2.7
        //super.SetLicenceKey("VAXVOIP.COM-74I53I186I0I221I48I119I217I188I180I59I140I144I106I175I218I96I86I156I89I148I74I200I97I6I77I104I232I86I210I42I198I169I187I115I34I46I68I220I150I233I30I232I171I31I203I245I143I31I45I141I172I91I231I180I122I45I126I94I27I196I164I96I139I88I69I201I102I236I7I21I172I184I201I114I203I75I166I67I198I192I177I234I152I107I191I0I181I173I171I67I18I175I30I170I182I235I120I222I202I157I239I168I107I19I91I31I243I241I172I119I12I219I125I5I213I42I21I234I93I85I203I235I110I158I168I41I188I207I70I149I27I236I71I30I43I229I0I41I187I232I132I187I19I128I239I124I22I77I184I236I30I11I71I117I208I159I90I111I124I186I239I167I4I177I61I53I221I171I228I142I98I212I8I32I91I184I65I125I26I154I19I180I150I196I182I87I14I61I85I203I96I66I161I12I189I195I167I88I208I230I149I169I74I241I86I152I241I71I80I6I110I107I157I163I104I43I29I35I143I24I189I69I243I161I7I146I247I158I19I61I191I146I249I150I207I196I255I65I217I101I127I153I47I71I60I95I58I164I208I145I110I24I75I70I44I215I153I242I94I53I236I77I127I250I166I214I206I32I109I223I121I251I149I255I141I9I32I154I182I117I196I60I101I56I239I88I236I5I72I154I159I248I107I81I143I121I251I100I254I112I144I76I58I183I238I97I172I254I129I207I146I87I188I101I22I249I1I181I148I56I21I236I99I140I11I58I216I17I108I92I133I-50.G729.UNLIMITED.MERLINYAZILIM.COM.TR",objContext);

        ///////////////////////////////////////////////////////////////////////////////4 - 2.5
        //super.SetLicenceKey("VAXVOIP.COM-59I252I209I20I108I108I44I62I228I169I116I217I144I106I175I218I96I86I156I89I148I74I200I97I6I77I104I232I86I210I42I198I169I187I115I34I46I68I220I150I233I30I232I171I31I203I245I143I31I45I141I172I91I231I180I122I45I126I94I27I196I164I96I139I88I69I201I102I236I7I21I172I184I201I114I203I75I166I67I198I192I177I234I152I107I191I0I181I173I171I67I18I175I30I170I182I235I120I222I202I157I239I168I107I19I91I31I243I241I172I119I12I219I125I5I213I42I21I234I93I85I203I235I110I158I168I41I188I207I70I149I27I236I71I30I43I229I0I41I187I232I132I187I19I128I239I124I22I77I184I236I30I11I71I117I208I159I90I111I124I186I239I167I4I177I61I53I221I171I228I142I98I212I8I32I91I184I65I125I26I154I19I180I150I196I182I87I14I61I85I203I96I66I161I12I189I195I167I88I208I230I149I169I74I241I86I152I241I71I80I6I110I107I157I163I104I43I29I35I143I24I189I69I243I161I7I146I247I158I19I61I191I146I249I150I207I196I255I65I217I101I127I153I47I71I60I95I58I164I208I145I110I24I75I70I44I215I153I242I94I82I243I68I118I54I128I186I42I32I109I223I121I251I149I255I141I9I32I154I182I117I196I60I101I56I239I88I236I5I72I154I159I246I136I147I46I166I167I14I221I112I144I76I58I183I238I97I172I254I129I207I146I87I188I101I22I249I1I181I148I56I21I236I99I140I11I58I216I17I108I92I133I-50.G729.UNLIMITED.MERLINYAZILIM.COM.TR", objContext);


        ////Prodcution
        //super.SetLicenceKey("VAXVOIP.COM-93I208I17I0I27I130I109I97I71I30I183I91I144I106I175I218I96I86I156I89I148I74I200I97I6I77I104I232I86I210I42I198I169I187I115I34I46I68I220I150I233I30I232I171I31I203I245I143I31I45I141I172I91I231I180I122I45I126I94I27I196I164I96I139I88I69I201I102I236I7I21I172I184I201I114I203I75I166I67I198I192I177I234I152I107I191I0I181I173I171I67I18I175I30I170I182I235I120I222I202I157I239I168I107I19I91I31I243I241I172I119I12I219I125I5I213I42I21I234I93I85I203I235I110I158I168I41I188I207I70I149I27I236I71I30I43I229I0I41I187I232I132I187I19I128I239I124I22I77I184I236I30I11I71I117I208I159I90I111I124I186I239I167I4I177I61I53I221I171I228I142I98I212I8I32I91I184I65I125I26I154I19I180I150I196I182I87I14I61I85I203I96I66I161I12I189I195I167I88I208I230I149I169I74I241I86I152I241I71I80I6I110I107I157I163I104I43I29I35I143I24I189I69I243I161I7I146I247I158I19I61I191I146I249I150I207I196I255I65I217I101I127I153I47I71I60I95I58I164I208I145I110I24I75I70I44I215I153I242I94I227I15I62I10I143I82I9I9I32I109I223I121I251I149I255I141I9I32I154I182I117I196I60I101I56I239I88I236I5I72I154I159I79I145I16I232I171I98I206I114I112I144I76I58I183I238I97I172I254I129I207I146I87I188I101I22I249I1I181I148I56I21I236I99I140I11I58I216I17I108I92I133I-50.G729.UNLIMITED.MERLINYAZILIM.COM.TR", objContext);

        ///////////////////////////////////////////////////////////////////////////////

        int nListenPortSIP = -1;

        if(!NetworkFragment.IsRandomPortSIP())
            nListenPortSIP = NetworkFragment.GetNetworkPortSIP();

        if(!super.Initialize("", nListenPortSIP, sDisplayName, sUserName, sAuthLogin, sAuthPwd, sDomainRealm, sServerAddr, nServerPort, "", -1, true))
            return false;

        if(bRegistrationSIP)
        {
            if(!super.RegisterToProxy(1800))
                return false;
        }

        super.AutoRegistration(true, -1, 20);
        super.EnableKeepAlive(10); // 10 seconds keep alive;
         boolean isAgentSip = super.SetUserAgentSIP("Merlin Fenix IpPbx Android Client");
         boolean isSubjectSdp =  super.SetSubjectSDP("Merlin Fenix IpPbx Android Client");
        isMerlin = isAgentSip && isSubjectSdp;



        m_bOnline = true;
        return true;
    }

    public void UnInitialize()
    {
        m_bOnline = false;
        super.UnInitialize();

        m_bVideoCaptureErrorShown = false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void GetLoginInfo(StringBuilder sUsername, StringBuilder sDisplayName, StringBuilder sAuthLogin, StringBuilder sAuthPassword, StringBuilder sDoaminRealm,
                             StringBuilder sServerIP, StringBuilder sServerPort, AtomicBoolean bRegistrationSIP,StringBuilder sRedisServer,StringBuilder sRedisPassword,StringBuilder sRedisPort,StringBuilder systemId)
    {
        StoreLoginInfo objStore = new StoreLoginInfo();
        objStore.GetLoginInfo(sUsername, sDisplayName, sAuthLogin, sAuthPassword, sDoaminRealm, sServerIP, sServerPort, bRegistrationSIP,sRedisServer,sRedisPassword,sRedisPort,systemId);
    }

    public void SetLoginInfo(String sUsername, String sDisplayName, String sAuthLogin, String sAuthPassword, String sDoaminRealm, String sServerIP, String sServerPort,
                             boolean bRegistrationSIP,String sRedisServer,String sRedisPassword,String sRedisPort,String systemId)
    {
        StoreLoginInfo objStore = new StoreLoginInfo();
        objStore.SetLoginInfo(sUsername, sDisplayName, sAuthLogin, sAuthPassword, sDoaminRealm, sServerIP, sServerPort, bRegistrationSIP,sRedisServer,sRedisPassword,sRedisPort,systemId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public int GetChatContactCount()
    {
        StoreChatContact objStore = new StoreChatContact();
        return objStore.GetChatContactCount();
    }

    public void LoadChatContactAll()
    {
        StoreChatContact objStore = new StoreChatContact();
        ArrayList aContactDataList = objStore.GetChatContactAll();

        for (int nIndex = 0; nIndex < aContactDataList.size(); nIndex++)
        {
            ArrayList aSubList = (ArrayList) aContactDataList.get(nIndex);

            String sContactName = (String) aSubList.get(1);

            boolean bPresence = Boolean.parseBoolean(String.valueOf(aSubList.get(3)));
            long nMissedCount = Long.valueOf(String.valueOf(aSubList.get(2)));

            super.ChatAddContact(sContactName, bPresence);
            ChatContactRecyclerView.PostChatContactAdded(sContactName, nMissedCount, bPresence);
        }
    }

    public boolean ChatAddContact(String sUserName, boolean bPresence)
    {
        if(!super.ChatAddContact(sUserName, bPresence))
            return false;

        StoreChatContact objStore = new StoreChatContact();
        objStore.AddChatContact(sUserName, 0, bPresence);

        ChatContactTabFragment.PostChatContactAdded();
        ChatContactRecyclerView.PostChatContactAdded(sUserName, 0, bPresence);

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public boolean ChatSetMyStatus(int nStatusId)
    {
        return super.ChatSetMyStatus(nStatusId);
    }

    public boolean ChatSendMessageText(String sUserName, String sMsgText)
    {
        if(!super.ChatSendMessageText(sUserName, sMsgText, 0, 0))
            return false;

        StoreChatMsg objStore = new StoreChatMsg();
        objStore.AddChatMsg(sUserName, sMsgText, true);

        ChatContactRecyclerView.PostChatMessageText(sUserName, super.ChatFindContact(sUserName), sMsgText, true);

        return true;
    }

    public boolean ChatRemoveContact(String sUserName)
    {
        if(!super.ChatRemoveContact(sUserName))
            return false;

        StoreChatContact objContactStore = new StoreChatContact();
        objContactStore.RemoveChatContact(sUserName);

        StoreChatMsg objMsgStore = new StoreChatMsg();
        objMsgStore.ClearContactChatMsgs(sUserName);

        ChatContactTabFragment.PostChatContactDeleted();

        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////        RECENT         //////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public int GetCallHistoryCount()
    {
        StoreRecent objStore = new StoreRecent();
        return objStore.GetCallCount();
    }

    public void RemoveCallRecord(int nId)
    {
        StoreRecent objStore = new StoreRecent();
        objStore.RemoveCallRecord(nId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////        SETTINGS         //////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void ApplyVideoQuality()
    {
        if(!m_bVideoDeviceCaptured)
            return;

        CloseVideoDevice();
        OpenVideoDevice();
    }

    public boolean PlayDTMF(String sDigit)
    {
       m_objPlayDTMF.PlayTone(sDigit);
       return super.DigitDTMF(0, sDigit);
    }

    public void SetRingtone(int nRingtone)
    {
        m_objRingTone.SetRingTone(nRingtone);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public boolean OpenVideoDevice()
    {
        if(m_bVideoDeviceCaptured == true)
            return true;

        CloseVideoDevice();

        int nQuality = VideoQualityDialog.GetVideoQuality();

        int nDeviceId = 0;

        if(m_bFrontVideoCameraCaptured)
            nDeviceId = 1;

        if(!super.OpenVideoDev(nDeviceId, nQuality))
        {
            return false;
        }

        m_bVideoDeviceCaptured = true;
        return true;
    }

    public void CloseVideoDevice()
    {
        m_bVideoDeviceCaptured = false;
        super.CloseVideoDev();
    }

    public void SwitchVideoDevice()
    {
        if(!m_bVideoDeviceCaptured)
            return;

        m_bFrontVideoCameraCaptured = !m_bFrontVideoCameraCaptured;

        CloseVideoDevice();
        OpenVideoDevice();
    }

    public void ApplyVideoBitrate(int nQuality)
    {
        VideoCodecBitRate(VAX_CODEC_VP8, nQuality);
        VideoCodecBitRate(VAX_CODEC_H263P, nQuality);
        VideoCodecBitRate(VAX_CODEC_H263, nQuality);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    public boolean IsMuteMic()
    {
        return m_bMuteMic;
    }

    public boolean MuteMic(boolean bMute)
    {
        if(!super.MuteMic(bMute))
        return false;

        m_bMuteMic = bMute;
        return true;
    }

    public boolean IsMuteSpk()
    {
        return m_bMuteSpk;
    }

    public boolean MuteSpk(boolean bMute)
    {
        if(!super.MuteSpk(bMute))
            return false;

        m_bMuteSpk = bMute;
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    public boolean IsLineBusy()
    {
        return super.IsLineBusy(0);
    }

    public boolean IsLineConnected()
    {
        return super.IsLineConnected(0);
    }

    public boolean IsLineHold()
    {
        return super.IsLineHold(0);
    }

    public boolean IsOnline()
    {
        return m_bOnline;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public boolean HoldLine()
    {
        return super.HoldLine(0);
    }

    public boolean UnHoldLine()
    {
        return super.UnHoldLine(0);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public boolean IsVideoEnabled()
    {
        return m_bVideoEnabled;
    }

    public boolean EnableVideo(boolean bEnable)
    {
        if(!super.EnableVideo(0, bEnable, bEnable))
        {
            return false;
        }

        m_bVideoEnabled = bEnable;
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    private boolean OpenLine()
    {
        super.CloseLine(0);

        int nLocalPortAudioRTP = -1;
        int nLocalPortVideoRTP = -1;

        if (!NetworkFragment.IsRandomPortRTP())
        {
            nLocalPortAudioRTP = NetworkFragment.GetNetworkPortRTP();
            nLocalPortVideoRTP = nLocalPortAudioRTP + 2;
        }

        return super.OpenLine(0, "", nLocalPortAudioRTP, nLocalPortVideoRTP);
    }

    public boolean DialCall(String sDialNo)
    {
        if(!OpenLine())
        {
            String errorMsg = this.GetVaxErrorMsg();
            Integer errorCode = this.GetVaxErrorCode();
            return false;
        }


        return super.DialCall(0, "", "", sDialNo, -1, -1);
    }

    public boolean DisconnectCall()
    {
        if (!IsLineBusy())
            return false;

        boolean bResult = super.DisconnectCall(0);

        OnVaxStatusMsg("Call", "Disconnected");
        OnDisconnectedCall();

        return bResult;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public void DiagnosticLog(boolean bEnable)
    {
        if(!bEnable)
        {
            m_sLogFile = "";
            return;
        }

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            boolean bCreated = true;

            String sPath = Environment.getExternalStorageDirectory() + "/Android/data/" + VaxPhoneAPP.getAppContext().getPackageName();

            File objPkgDirectory = new File(sPath);

            if (!objPkgDirectory.exists())
            {
                bCreated = objPkgDirectory.mkdirs();
            }

            if (bCreated)
            {
                File objFile = new File(sPath, "VaxLog.txt");
                m_sLogFile = objFile.getPath();
            }
            else
            {
                m_sLogFile = "";
            }

            return;
        }

        File AppDir = VaxPhoneAPP.getAppContext().getExternalFilesDir(null);
        if (AppDir == null) return;

        File objFile = new File(AppDir, "VaxLog.txt");
        m_sLogFile = objFile.getPath();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public boolean AutoGainSpk(boolean bEnable)
    {
        return super.AutoGainSpk(bEnable, 20);
    }

    public boolean AutoGainMic(boolean bEnable)
    {
        return super.AutoGainMic(bEnable,20);
    }

    public boolean SetVolumeBoostSpk(boolean bEnable)
    {
        int nBoostVol = 0;

        if(bEnable)
            nBoostVol = 20;

        return super.SetVolumeSpk(nBoostVol);
    }

    public boolean SetVolumeBoostMic(boolean bEnable)
    {
        int nBoostVol = 0;

        if(bEnable)
            nBoostVol = 20;

        return super.SetVolumeMic(nBoostVol);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public boolean IsVoiceChangerEnabled()
    {
        return m_bVoiceChangerEnabled;
    }

    public boolean VoiceChanger(boolean bEnable)
    {
        if(!bEnable)
        {
            m_bVoiceChangerEnabled = false;
            return super.VoiceChanger(-1);
        }

        if(!super.VoiceChanger(VoiceChangerDialog.GetSelectedPitchNo()))
            return false;

        m_bVoiceChangerEnabled = true;
        return true;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public boolean ForceDigitDTMF(int nTypeId, boolean bEnable)
    {
        return super.ForceDigitDTMF(0, nTypeId, bEnable);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    public void RejectCall()
    {
        super.RejectCall(m_sIncomingCallId);
    }

    public void AcceptCall()
    {
        if(!OpenLine())
            return;

        super.AcceptCall(0, m_sIncomingCallId, -1,-1);
    }

    public void MuteRingTone()
    {
        m_objRingTone.MuteRingTone();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnInitialized()
    {
        OnVaxStatusMsg("Account", "Account is online");

        m_objDialRing.Enable();
        m_objBusyRing.Enable();

        RingtonesDialog.PostInitialized();
        ChatContactRecyclerView.PostInitialized();
        SettingsFragment.PostInitialized();

        VideoBitrateDialog.PostInitialized();
        TunnelFragment.PostInitialized();

        VideoCodecsDialog.PostInitialize();
        AudioCodecsDialog.PostInitialize();

        SettingMicSpk.PostInitialize();
        DigitDTMFDialog.PostInitialize();

        CallTabFragment.PostInitialized();
    }

    @Override
    public void OnUnInitialized()
    {
        CloseVideoDevice();
        OnVaxStatusMsg("Account", "Account is offline");
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnConnectingToRegister()
    {
        OnVaxStatusMsg("Register", "Trying to connect");
    }

    @Override
    public void OnTryingToRegister()
    {
        OnVaxStatusMsg("Register", "Registering Account");
    }

    @Override
    public void OnFailToRegister(int nStatusCode, String sReasonPhrase)
    {
        OnVaxStatusMsg("Register", sReasonPhrase);
    }

    @Override
    public void OnSuccessToRegister()
    {
        OnVaxStatusMsg("Register", "Account Registered");
        AccountLoginActivity.PostSuccessToRegister();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnConnectingToReRegister()
    {

    }

    @Override
    public void OnTryingToReRegister()
    {

    }

    @Override
    public void OnFailToReRegister(int nStatusCode, String sReasonPhrase)
    {
        OnVaxStatusMsg("Re-Register", sReasonPhrase);
    }

    @Override
    public void OnSuccessToReRegister()
    {

    }

    @Override
    public void OnTryingToUnRegister()
    {
        OnVaxStatusMsg("Unregister", "Un-Registering Account");
    }

    @Override
    public void OnFailToUnRegister()
    {
        OnVaxStatusMsg("Unregister", "Un-Register: Failed");
    }

    @Override
    public void OnSuccessToUnRegister()
    {
        OnVaxStatusMsg("Unregister", "Account Un-Registered");
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnTryingToRegisterREC()
    {

    }

    @Override
    public void OnFailToRegisterREC(int nStatusCode, String sReasonPhrase)
    {

    }

    @Override
    public void OnSuccessToRegisterREC()
    {

    }

    @Override
    public void OnTryingToReRegisterREC()
    {

    }

    @Override
    public void OnFailToReRegisterREC(int nStatusCode, String sReasonPhrase)
    {

    }

    @Override
    public void OnSuccessToReRegisterREC()
    {

    }

    @Override
    public void OnTryingToUnRegisterREC()
    {

    }

    @Override
    public void OnFailToUnRegisterREC()
    {

    }

    @Override
    public void OnSuccessToUnRegisterREC()
    {

    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnDialCallStarted(int nLineNo, String sCallerName, String sCallerId, String sDialNo)
    {
        OnVaxStatusMsg("Call", "Connecting");
        CallTabFragment.PostDialCallStarted();
    }

    @Override
    public void OnDialingCall(int nLineNo, int nStatusCode, String sReasonPhrase)
    {
        OnVaxStatusMsg("Call", "Dialing");
    }

    @Override
    public void OnDialCallFailed(int nLineNo, int nStatusCode, String sReasonPhrase, String sContact)
    {
        OnVaxStatusMsg("Call", sReasonPhrase);
        CallTabFragment.PostDialCallFailed(nStatusCode, sReasonPhrase);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnConnectedCall(int nLineNo, String sToRTPIP, int nToRTPPort)
    {
        OnVaxStatusMsg("Call", "Connected");
        CallTabFragment.PostConnectedCall();

        m_objProximitySensor.SetProximityMonitoringEnabled(true);
    }

    @Override
    public void OnHungupCall(int nLineNo)
    {
        OnVaxStatusMsg("Call", "Hungup");
        OnDisconnectedCall();
    }

    private void OnDisconnectedCall()
    {
        CallTabFragment.PostDisconnectedCall();

        m_objProximitySensor.SetProximityMonitoringEnabled(false);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnIncomingCallStarted(String sCallId, String sCallerName, String sCallerId, String sDialNo, String sFromURI, String sToURI)
    {



        m_sIncomingCallId = sCallId;

        StringBuilder sResultCallerName = new StringBuilder();
        StringBuilder sResultCallerId = new StringBuilder();
        StringBuilder sResultContactId = new StringBuilder();

        CallInfo.PrepareCallInfo(sCallerName, sCallerId, sResultCallerName, sResultCallerId, sResultContactId);
        m_CallerId = sResultCallerId.toString();
        m_CallerName = sResultCallerName.toString();
        MainTabActivity.PostIncomingCall(sResultCallerName.toString(), sResultCallerId.toString(),sCallId);
        sendIncomingBroadcast(sCallId,sCallerId,sCallerName);

    }

    @Override
    public void OnIncomingCallEnded(String sCallId)
    {
        //MerlinConnectionService.activeConnection.setDisconnected(new DisconnectCause(DisconnectCause.REMOTE));
        sendLocalBroadcast(ActionName.END_CALL,sCallId);
        MainTabActivity.PostIncomingCallEnded(sCallId,m_CallerId,m_CallerName);
        m_CallerId = "";
        m_CallerName = "";
    }
    public void sendLocalBroadcast(String action,String sCallId){

        Intent intent = new Intent("ConnectionService");
        intent.putExtra("Action", action);
        intent.putExtra("CallId", sCallId);
        LocalBroadcastManager.getInstance(VaxPhoneAPP.getAppContext()).sendBroadcast(intent);
    }

    public void sendIncomingBroadcast(String sCallId,String sCallerId,String sCallerName)
    {
        Intent intent = new Intent("ConnectionService");
        intent.putExtra("Action", ActionName.START_CALL);
        intent.putExtra("CallId", sCallId);
        intent.putExtra("CallerId",sCallerId);
        intent.putExtra("CallerName",sCallerName);

        LocalBroadcastManager.getInstance(VaxPhoneAPP.getAppContext()).sendBroadcast(intent);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override///////////////////////////////////////////////////////////////////////////////
    public void OnTransferCallAccepted(int nLineNo)
    {
        OnVaxStatusMsg("Transfer", "Accepted");
    }

    @Override
    public void OnTransferCallFailed(int nLineNo, int nStatusCode, String sReasonPhrase)
    {
        OnVaxStatusMsg("Transfer", sReasonPhrase);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnPlayWaveDone(int nLineNo)
    {

    }

    @Override
    public void OnDigitDTMF(int nLineNo, String sDigit)
    {
    }

    @Override
    public void OnMsgNOTIFY(String sMsg)
    {

    }

    @Override
    public void OnVoiceMailMsg(boolean bIsMsgWaiting, int nNewMsgCount, int nOldMsgCount, int nNewUrgentMsgCount, int nOldUrgentMsgCount, String sMsgAccount)
    {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnRingToneStarted(String sCallId)
    {
        m_objRingTone.StartRingTone();
    }

    @Override
    public void OnRingToneEnded(String sCallId)
    {
        m_objRingTone.StopRingTone();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void WriteToLogFile(String sText)
    {
        if(m_sLogFile.length() == 0)
            return;

        FileWriter objFileWriter;
        try
        {
            objFileWriter = new FileWriter(m_sLogFile, true);
            objFileWriter.write(sText);
            objFileWriter.flush();
            objFileWriter.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnIncomingDiagnostic(String sMsgSIP, String sFromIP, int nFromPort)
    {
        if (sMsgSIP.contains("SIP/2.0 200 OK"))
        {
            if (!sMsgSIP.contains("User-Agent: Merlin Fenix IpPbx"))
            {
                VaxPhoneSIP.m_objVaxVoIP.OnFailToRegister(405,"NotAllowed");
                VaxPhoneSIP.m_objVaxVoIP.UnInitialize();
                isMerlin = false;
                OnVaxStatusMsg("Register","Only Merlin IpPbx");
            }
            else
            {
                isMerlin =true;
            }
        }


        if(m_sLogFile.equals(""))
            return;

        String sLogPacket = "Received: " + sFromIP + " \n " +  nFromPort + " \n " +  sMsgSIP;

        this.WriteToLogFile(sLogPacket);


    }

    @Override
    public void OnOutgoingDiagnostic(String sMsgSIP, String sToIP, int nToPort)
    {
        if(m_sLogFile.equals(""))
            return;

        String sLogPacket = "Sent: " + sToIP + " \n " +  nToPort + " \n " +  sMsgSIP;

        this.WriteToLogFile(sLogPacket);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnAudioSessionLost(int nLineNo)
    {

    }

    @Override
    public void OnSuccessToHold(int nLineNo)
    {
        OnVaxStatusMsg("Hold", "Successful");
    }

    @Override
    public void OnTryingToHold(int nLineNo)
    {
        OnVaxStatusMsg("Hold", "Trying");
    }

    @Override
    public void OnFailToHold(int nLineNo)
    {
        OnVaxStatusMsg("Hold", "Failed");
    }

    @Override
    public void OnSuccessToUnHold(int nLineNo)
    {
        OnVaxStatusMsg("Unhold", "Successful");
    }

    @Override
    public void OnTryingToUnHold()
    {
        OnVaxStatusMsg("Unhold", "Trying");
    }

    @Override
    public void OnFailToUnHold(int nLineNo)
    {
        OnVaxStatusMsg("Unhold", "Failed");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnChatContactStatus(String sUserName, int nStatusId)
    {
        ChatContactRecyclerView.PostChatContactStatus(sUserName, nStatusId);
    }

    @Override
    public void OnChatSendMsgTextSuccess(String sUserName, String sMsgText, int nUserValue32bit)
    {

    }

    @Override
    public void OnChatSendMsgTextFail(String sUserName, int nStatusCode, String sReasonPhrase, String sMsgText, int nUserValue32bit)
    {

    }

    @Override
    public void OnChatSendMsgTypingSuccess(String sUserName, int nUserValue32bit)
    {

    }

    @Override
    public void OnChatSendMsgTypingFail(String sUserName, int nStatusCode, String sReasonPhrase, int nUserValue32bit)
    {

    }

    @Override
    public void OnChatRecvMsgText(String sUserName, String sMsgText, boolean bIsChatContact)
    {
        if (!bIsChatContact)
            ChatAddContact(sUserName, false);

        StoreChatMsg objStore = new StoreChatMsg();
        objStore.AddChatMsg(sUserName, sMsgText, false);

        ChatContactRecyclerView.PostChatMessageText(sUserName, bIsChatContact, sMsgText, false);
    }

    @Override
    public void OnChatRecvMsgTypingStart(String sUserName)
    {

    }

    @Override
    public void OnChatRecvMsgTypingStop(String sUserName)
    {

    }

    @Override
    public void OnVoiceStreamPCM(int nLineNo, byte[] pDataPCM, int nSizePCM)
    {

    }

    @Override
    public void OnDetectedAMD(int nLineNo, boolean bIsHuman)
    {

    }

    @Override
    public void OnHoldCall(int nLineNo)
    {

    }

    @Override
    public void OnUnHoldCall(int nLineNo)
    {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnVideoDeviceFrameRGB(int nDeviceId, byte[] pFrameRGB, int nFrameSize, int nFrameWidth, int nFrameHeight)
    {
        CallTabFragment.PostVideoDeviceFrameRGB(nDeviceId, pFrameRGB, nFrameSize, nFrameWidth, nFrameHeight);
    }

    @Override
    public void OnVideoRemoteFrameRGB(int nLineNo, byte[] pFrameRGB, int nFrameSize, int nFrameWidth, int nFrameHeight)
    {
        CallTabFragment.PostOnVideoRemoteFrameRGB(nLineNo, pFrameRGB, nFrameSize, nFrameWidth, nFrameHeight);
    }

    @Override
    public void OnVideoRemoteStarted(int nLineNo)
    {
        CallTabFragment.OnVideoRemoteStarted(nLineNo);
    }

    @Override
    public void OnVideoRemoteEnded(int nLineNo)
    {
        CallTabFragment.OnVideoRemoteEnded(nLineNo);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnServerConnectingREC(int nLineNo, int nStatusCode, String sReasonPhrase)
    {

    }

    @Override
    public void OnServerConnectedREC(int nLineNo)
    {

    }

    @Override
    public void OnServerFailedREC(int nLineNo, int nStatusCode, String sReasonPhrase)
    {

    }

    @Override
    public void OnServerHungupREC(int nLineNo)
    {

    }

    @Override
    public void OnAddCallHistory(boolean bOutboundCallType, String sCallerName, String sCallerId, String sDialNo, long nStartTime, long nEndTime, long nDuration, long nDayNo, int nHistoryTypeId)
    {
        StringBuilder sResultName = new StringBuilder();
        StringBuilder sResultPhoneNo = new StringBuilder();
        StringBuilder sResultContactId = new StringBuilder();

        if (bOutboundCallType)
            CallInfo.PrepareCallInfo(sCallerName, sDialNo, sResultName, sResultPhoneNo, sResultContactId);
        else
        {
            CallInfo.PrepareCallInfo(sCallerName, sCallerId, sResultName, sResultPhoneNo, sResultContactId);
        }

        StoreRecent objStore = new StoreRecent();
        int nRecordId = objStore.AddCallHistory(bOutboundCallType, sResultName.toString(), sResultPhoneNo.toString(), nStartTime, nEndTime, nDuration, nDayNo, nHistoryTypeId);

        RecentRecyclerView.PostAddCallHistory(nRecordId, bOutboundCallType, sResultName.toString(), sResultPhoneNo.toString(), nStartTime, nEndTime, nDuration, nDayNo, nHistoryTypeId);
    }

    @Override
    public void OnNetworkReachability(boolean bAvailable)
    {
        String sMsg = "";

        if (bAvailable)
        {
            sMsg = "Network is available";
        }
        else
        {
            sMsg = "Network is not available";
        }

        OnVaxStatusMsg("Network", sMsg);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnAudioDeviceMicVU(int nLevelVU)
    {

    }

    @Override
    public void OnAudioDeviceSpkVU(int nLevelVU)
    {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void OnVaxErrorMsg(String sFuncName, String sErrorMsg, int nErrorCode)
    {
        if (sFuncName.equals("OpenVideoDev"))
        {
            if (m_bVideoCaptureErrorShown)
                return;

            m_bVideoCaptureErrorShown = true;
        }

        AccountLoginActivity.PostStatusText(sErrorMsg);
        CallTabFragment.PostStatusText(sErrorMsg);
        DialpadFragment.PostStatusText(sErrorMsg);
    }

    private void OnVaxStatusMsg(String sFuncType, String sMsg)
    {
        if(sFuncType == "Account" || sFuncType == "Network")
        {
            AccountLoginActivity.PostStatusText(sMsg);
            CallTabFragment.PostStatusText(sMsg);
            DialpadFragment.PostStatusText(sMsg);
            return;
        }

        if(sFuncType == "Register" || sFuncType == "Re-Register" ||sFuncType == "Unregister")
        {
            AccountLoginActivity.PostStatusText(sMsg);
        }

        String sShowMsg = sFuncType + ": " + sMsg;

        CallTabFragment.PostStatusText(sShowMsg);
        DialpadFragment.PostStatusText(sShowMsg);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
}
