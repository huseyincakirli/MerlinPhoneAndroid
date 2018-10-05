package vaxsoft.com.vaxphone.MainTab;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import vaxsoft.com.vaxphone.ActionName;
import vaxsoft.com.vaxphone.MainAPP.VaxPhoneAPP;
import vaxsoft.com.vaxphone.MainDrawer.MainDrawerMenu;
import vaxsoft.com.vaxphone.MainTab.CallTab.CallTabFragment;
import vaxsoft.com.vaxphone.MainTab.CallTab.DialPad2;
import vaxsoft.com.vaxphone.MainTab.ChatTab.ChatContactTabFragment;
import vaxsoft.com.vaxphone.MainTab.ExtensionTab.ExtensionFragment;
import vaxsoft.com.vaxphone.MainUtil.DialogUtil;
import vaxsoft.com.vaxphone.MainUtil.IncomingCallDialog;
//import vaxsoft.com.vaxphone.MainUtil.NotificationReceiver;
import vaxsoft.com.vaxphone.MerlinConnectionService;
import vaxsoft.com.vaxphone.PhoneSIP.CallInfo.CallInfo;
import vaxsoft.com.vaxphone.R;
import vaxsoft.com.vaxphone.MainTab.RecentTab.RecentTabFragment;
import vaxsoft.com.vaxphone.VaxPhoneSIP;

public class MainTabActivity extends AppCompatActivity
{
    private static final int RECENT_FRAGMENT_TAB_ID = 0;
    private static final int CALL_FRAGMENT_TAB_ID   = 1;
    private static  final  int EXTENSION_TAB_ID = 2;

    //private static final int CHAT_FRAGMENT_TAB_ID   = 2;

    private final int TOTAL_FRAGMENT_TAB_COUNT = 3;

    private Toolbar mToolbar;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public ViewPager mViewPager;
    private TabLayout mTabLayout;

    private Fragment[] m_aFragTabs;
    private ActionBar mActionBar;

    private IncomingCallDialog m_objIncomingCallDialog = null;
    private MainDrawerMenu m_objMainDrawerMenu = null;

    private static int m_nSelectedTabId = CALL_FRAGMENT_TAB_ID;

    @SuppressLint("StaticFieldLeak")
    private static MainTabActivity mMainTab = null;
    PhoneAccount account;
    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaxphone_tabbed);

        mMainTab = this;

        m_objMainDrawerMenu = new MainDrawerMenu(this);
        m_aFragTabs = new Fragment[TOTAL_FRAGMENT_TAB_COUNT];



        LoadViewAll();
        LoadFragmentTabAll();

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        m_objMainDrawerMenu.SetMainDrawerMenu(mToolbar);
        m_objMainDrawerMenu.UpdateDrawerUI();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        ActivateTabSelectedListener();
        mViewPager.setCurrentItem(CALL_FRAGMENT_TAB_ID);
        mTabLayout.setupWithViewPager(mViewPager);

        AdjustTabLayout();


        int page = getIntent().getIntExtra("One", m_nSelectedTabId);
        mViewPager.setCurrentItem(page);

      //  TelecomManager telecomManager = (TelecomManager) getApplicationContext().getSystemService(getApplicationContext().TELECOM_SERVICE);


//        PhoneAccountHandle phoneAccount = new PhoneAccountHandle(
//                new ComponentName(getApplicationContext(), MerlinConnectionService.class.getName()),
//                ActionName.PHONE_ACCOUNT_ID);
        // register phone account
//        PhoneAccount.Builder builder =  PhoneAccount.builder(phoneAccount, ActionName.PHONE_ACCOUNT_ID).addSupportedUriScheme(PhoneAccount.SCHEME_SIP)
//                .setCapabilities(
//                      //  PhoneAccount.CAPABILITY_CALL_PROVIDER |
//                        PhoneAccount.CAPABILITY_CONNECTION_MANAGER |
//                        PhoneAccount.CAPABILITY_CALL_SUBJECT|
//                       // PhoneAccount.CAPABILITY_SELF_MANAGED |
//                        PhoneAccount.CAPABILITY_CONNECTION_MANAGER |
//                        PhoneAccount.CAPABILITY_CALL_PROVIDER
//                );
//
//         account = builder.build();
//
//        telecomManager.registerPhoneAccount(account);

//        if (!isPhoneAccountEnabled())
//        {
//            startActivity(new Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS));
//
//        }



//        String action =  getIntent().getAction();
//        if (action == "notify")
//        {
//            if (VaxPhoneSIP.m_objVaxVoIP.IsOnline())
//            {
//                String callerId = getIntent().getStringExtra("CallerId");
//                String callerName = getIntent().getStringExtra("CallerName");
//                mMainTab.OnIncomingCall(callerName,callerId);
//            }
//
//        }

    }

    private boolean isPhoneAccountEnabled()
    {
        TelecomManager telecomManager = (TelecomManager) getApplicationContext().getSystemService(getApplicationContext().TELECOM_SERVICE);
        PhoneAccount phoneAccount = GetPhoneAccount(telecomManager);
        return phoneAccount.isEnabled();
    }

    private PhoneAccount GetPhoneAccount(TelecomManager telecomManager)
    {
        PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
                new ComponentName(getApplicationContext(), MerlinConnectionService.class.getName()),
                ActionName.PHONE_ACCOUNT_ID);
        PhoneAccount phoneAccount = telecomManager.getPhoneAccount(phoneAccountHandle);

        return phoneAccount;

    }

    private void LoadViewAll()
    {
        mViewPager = findViewById(R.id.container);
        mTabLayout = findViewById(R.id.tabs);
        mToolbar = findViewById(R.id.toolbar);
    }

    private void AdjustTabLayout()
    {
        mTabLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                for (int nTabId = 0; nTabId < TOTAL_FRAGMENT_TAB_COUNT; nTabId++)
                {
                    TabLayout.Tab objTab = mTabLayout.getTabAt(nTabId);
                    if (objTab == null) continue;

                    objTab.setIcon(mSectionsPagerAdapter.getPageIcon(nTabId));

                    Drawable TabIcon = objTab.getIcon();
                    if (TabIcon == null) continue;

                    TabIcon.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

                     if (nTabId == CALL_FRAGMENT_TAB_ID )
                        TabIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                }
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    private void LoadMissedCounts()
    {
        RecentTabFragment.LoadMissedCount();
       // ChatContactTabFragment.LoadMissedCount();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (m_objMainDrawerMenu.IsDrawerOpen())
            m_objMainDrawerMenu.CloseDrawer();
        else
            super.onBackPressed();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void LoadFragmentTabAll()
    {
        m_aFragTabs[RECENT_FRAGMENT_TAB_ID] = new RecentTabFragment();
        m_aFragTabs[CALL_FRAGMENT_TAB_ID] = new DialPad2();
        m_aFragTabs[EXTENSION_TAB_ID] = new ExtensionFragment();
    }

    private void ActivateTabSelectedListener()
    {
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager)
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                int nPosition = tab.getPosition();
                OnTabSelected(nPosition);

                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                int nPosition = tab.getPosition();
                OnTabUnSelected(nPosition);

                if (tab.getIcon() != null)
                   tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private void OnTabSelected(int nTabId)
    {
        m_nSelectedTabId = nTabId;

        switch (nTabId)
        {
            case RECENT_FRAGMENT_TAB_ID:

                SetActionBarTitle("Recent");

                RecentTabFragment objRecentFragment = (RecentTabFragment) m_aFragTabs[RECENT_FRAGMENT_TAB_ID];
                objRecentFragment.OnTabSelected(true);

                break;

            case CALL_FRAGMENT_TAB_ID:

                SetActionBarTitle("Call");

                DialPad2 objCallFragment = (DialPad2) m_aFragTabs[CALL_FRAGMENT_TAB_ID];
                objCallFragment.OnTabSelected(true);

                break;

            case EXTENSION_TAB_ID:

                SetActionBarTitle("Extensions");

                ExtensionFragment objExtensionFragment = (ExtensionFragment) m_aFragTabs[EXTENSION_TAB_ID];
                objExtensionFragment.OnTabSelected(true);
                objExtensionFragment.StartRedis();
                break;
        }
    }

    private void OnTabUnSelected(int nTabId)
    {
        switch (nTabId)
        {
            case RECENT_FRAGMENT_TAB_ID:

                RecentTabFragment objRecentFragment = (RecentTabFragment) m_aFragTabs[RECENT_FRAGMENT_TAB_ID];
                objRecentFragment.OnTabSelected(false);

                break;

            case CALL_FRAGMENT_TAB_ID:

                DialPad2 objCallFragment = (DialPad2) m_aFragTabs[CALL_FRAGMENT_TAB_ID];
                objCallFragment.OnTabSelected(false);

                break;

            case EXTENSION_TAB_ID:

                ExtensionFragment objExtensionFragment= (ExtensionFragment) m_aFragTabs[EXTENSION_TAB_ID];
                objExtensionFragment.OnTabSelected(false);
                objExtensionFragment.StopRedis();

                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void SetActionBarTitle(String sTitle)
    {
        if (mActionBar != null)
            mActionBar.setTitle(sTitle);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case RECENT_FRAGMENT_TAB_ID:
                    return m_aFragTabs[RECENT_FRAGMENT_TAB_ID];

                case CALL_FRAGMENT_TAB_ID:
                    return m_aFragTabs[CALL_FRAGMENT_TAB_ID];

                case EXTENSION_TAB_ID:
                    return m_aFragTabs[EXTENSION_TAB_ID];
            }

            return null;
        }

        @Override
        public int getCount()
        {
            return TOTAL_FRAGMENT_TAB_COUNT;
        }

        int getPageIcon(int position)
        {
            switch (position)
            {
                case RECENT_FRAGMENT_TAB_ID:
                    return R.drawable.ic_recent;

                case CALL_FRAGMENT_TAB_ID:
                    return R.drawable.ic_call;

                case EXTENSION_TAB_ID:
                    return R.drawable.ic_contact;
            }
            return 0;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public static void PostIncomingCall(String sCallerName, String sCallerId,String sCallId)
    {
        if (mMainTab != null) {
            generateNotification(mMainTab.getApplicationContext(), sCallerId,sCallerName,sCallId);
            mMainTab.OnIncomingCall(sCallerName, sCallerId);

        }

    }

    private static void generateNotification(Context context, String callerId,String callerName,String sCallId) {



         if (mMainTab.isPhoneAccountEnabled())
         {
             TelecomManager telecomManager = (TelecomManager)mMainTab.getSystemService(Context.TELECOM_SERVICE);
             PhoneAccount account = mMainTab.GetPhoneAccount(telecomManager);
             PhoneAccountHandle handle = account.getAccountHandle();
             Bundle bundle = new Bundle();
             bundle.putString("CallerId",callerId);
             bundle.putString("CallerName",callerName);
             bundle.putString("CallId",sCallId);
             //bundle.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, handle);
             telecomManager.addNewIncomingCall(handle,bundle);
         }
         else
         {
                NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
                 Notification.Builder notification = new Notification.Builder(context)
                         .setContentTitle("Merlin Phone")
                         .setContentText("Call : "+callerId)
                         .setSmallIcon(R.drawable.app_icon)
                         .setPriority(Notification.PRIORITY_HIGH)
                         .setDefaults(Notification.DEFAULT_ALL)
                         .setAutoCancel(true);

                 Intent notificationIntent = new Intent(context, MainTabActivity.class);
                 notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                 notificationIntent.setAction("notify");
                 notificationIntent.putExtra("CallerId",callerId);
                 notificationIntent.putExtra("CallerName",callerName);
                 PendingIntent pendingIntent = PendingIntent.getActivity(context,1,notificationIntent,0);
                 notification.setContentIntent(pendingIntent);
                 notificationManager.notify(0, notification.build());
         }
    }



    private static void callEndedNotification(Context context)
    {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);
    }


    private static void missedCallNotification(Context context,String callerId)
    {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);


        Notification.Builder notification = new Notification.Builder(context)
                .setContentTitle("Merlin Phone")
                .setContentText("Missed Call : "+callerId)
                .setSmallIcon(R.drawable.app_icon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        Intent notificationIntent = new Intent(context, MainTabActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // set intent so it does not start a new activity
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,notificationIntent,0);
        notification.setContentIntent(pendingIntent);
        notificationManager.notify(1, notification.build());
    }

    private void OnIncomingCall(String sCallerName, String sCallerId)
    {
        m_objIncomingCallDialog = new IncomingCallDialog(this, sCallerName, sCallerId);
        m_objIncomingCallDialog.ShowDialog();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public static void PostIncomingCallEnded(String sCallId,String sCallerId,String sCallerName)
    {
        if (mMainTab != null)
            mMainTab.OnIncomingCallEnded(sCallId,sCallerId,sCallerName);
    }

    private void OnIncomingCallEnded(String sCallId,String sCallerId,String sCallerName)
    {
        callEndedNotification(getApplicationContext());

        if (!VaxPhoneSIP.m_objVaxVoIP.IsLineConnected())
            missedCallNotification(getApplicationContext(),sCallerId);

        if (m_objIncomingCallDialog == null)
            return;

        m_objIncomingCallDialog.HideDialog();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

//    public static void SetChatContactBadgeCount(int nMissedCount)
//    {
//        if (mMainTab != null)
//            mMainTab.OnSetChatContactBadgeCount(nMissedCount);
//    }

//    private void OnSetChatContactBadgeCount(int nMissedCount)
//    {
//        View ChatView;
//
//        TabLayout.Tab objChatTab = mTabLayout.getTabAt(CHAT_FRAGMENT_TAB_ID);
//
//        if (objChatTab == null)
//            return;
//
//        ChatView = objChatTab.getCustomView();
//
//        if (ChatView == null && nMissedCount == 0)
//            return;
//
//        if (ChatView != null)
//        {
//            TextView BadgeView = ChatView.findViewById(R.id.tv_badge);
//
//            if (nMissedCount == 0)
//            {
//                BadgeView.setVisibility(View.GONE);
//                return;
//            }
//
//            BadgeView.setText(String.valueOf(nMissedCount));
//            BadgeView.setVisibility(View.VISIBLE);
//
//            return;
//        }
//
//        View view = getLayoutInflater().inflate(R.layout.layout_chat_badge_view, null);
//
//        TextView Badge = view.findViewById(R.id.tv_badge);
//        Badge.setText(String.valueOf(nMissedCount));
//
//        objChatTab.setCustomView(view);
//    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public static void SetRecentBadgeCount(int nMissedCount)
    {
        if (mMainTab != null)
            mMainTab.OnSetRecentBadgeCount(nMissedCount);
    }

    private void OnSetRecentBadgeCount(int nMissedCount)
    {
        View RecentView;

        TabLayout.Tab objRecentTab = mTabLayout.getTabAt(RECENT_FRAGMENT_TAB_ID);

        if (objRecentTab == null)
            return;

        RecentView = objRecentTab.getCustomView();

        if (RecentView == null && nMissedCount == 0)
            return;

        if (RecentView != null)
        {
            TextView BadgeView = RecentView.findViewById(R.id.tv_badge);

            if (nMissedCount == 0)
            {
                BadgeView.setVisibility(View.GONE);
                return;
            }

            BadgeView.setText(String.valueOf(nMissedCount));
            BadgeView.setVisibility(View.VISIBLE);

            return;
        }

        View view = getLayoutInflater().inflate(R.layout.layout_recent_badge_view, null);

        TextView Badge = view.findViewById(R.id.tv_badge);
        Badge.setText(String.valueOf(nMissedCount));

        objRecentTab.setCustomView(view);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LoadMissedCounts();
    }

    @Override
    protected void onDestroy()
    {
        mMainTab = null;
        super.onDestroy();
    }
}
