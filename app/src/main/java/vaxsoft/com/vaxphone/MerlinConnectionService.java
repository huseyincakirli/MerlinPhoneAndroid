package vaxsoft.com.vaxphone;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.RemoteConference;
import android.telecom.RemoteConnection;
import android.telecom.TelecomManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vaxsoft.com.vaxphone.MainAPP.VaxPhoneAPP;

public class MerlinConnectionService extends ConnectionService {

    public static MerlinConnectionService mConnectionService;

    public MerlinConnectionService() {
        super();
    }

    private final List<MyConnection> mCalls = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mConnectionService = this;
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mSipEventReceiver, new IntentFilter("ConnectionService"));

    }


    private BroadcastReceiver mSipEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getStringExtra("Action");
            String callId = intent.getStringExtra("CallId");

            MyConnection connection = null;

            for (MyConnection con : mCalls){
                if (callId.equalsIgnoreCase(con.getCallId())){
                    connection = con;
                    break;
                }
            }

            if (connection == null){
                return;
            }

            switch (action){
                case ActionName.END_CALL:
                    connection.setDisconnected(new DisconnectCause(DisconnectCause.REMOTE));
                    destroyCall(connection);
                    connection.destroy();
                    break;

                case ActionName.START_CALL:
                    TelecomManager telecomManager = (TelecomManager)VaxPhoneAPP.getAppContext().getSystemService(Context.TELECOM_SERVICE);
                    PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
                            new ComponentName(getApplicationContext(), MerlinConnectionService.class.getName()),
                            ActionName.PHONE_ACCOUNT_ID);
                    PhoneAccount account = telecomManager.getPhoneAccount(phoneAccountHandle);
                    PhoneAccountHandle handle = account.getAccountHandle();
                    Bundle bundle = new Bundle();
                  //  bundle.putString("CallerId",callerId);
                  //  bundle.putString("CallerName",callerName);
                    bundle.putString("CallId",callId);
                    //bundle.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, handle);
                    telecomManager.addNewIncomingCall(handle,bundle);

                    break;
            }

        }
    };

    private void updateCallCapabilities(){
        for (MyConnection connection : mCalls){
            connection.setConnectionCapabilities(getCallCapabilities(connection, mCalls.size()));
        }
    }

    private int getCallCapabilities(Connection connection, int totalCall){
        int callCapabilities = 0;
        callCapabilities |= Connection.CAPABILITY_MUTE;
        callCapabilities |= Connection.CAPABILITY_SUPPORT_HOLD;

        //hold capability for only single call
        if (totalCall == 1){
            if (connection.getState() == Connection.STATE_ACTIVE || connection.getState() == Connection.STATE_HOLDING) {
                callCapabilities |= Connection.CAPABILITY_HOLD;
            }
        }
        if (totalCall > 1){
            callCapabilities |= Connection.CAPABILITY_MERGE_CONFERENCE;
            callCapabilities |= Connection.CAPABILITY_SEPARATE_FROM_CONFERENCE;
            callCapabilities |= Connection.CAPABILITY_SWAP_CONFERENCE;
            callCapabilities |= Connection.CAPABILITY_MANAGE_CONFERENCE;
        }

        return callCapabilities;
    }

    final class MyConnection extends Connection {

        private String mCallId;
        private boolean mIsActive = false;

        public MyConnection() {
            super();
        }

        public String getCallId() {
            return mCallId;
        }

        public void setCallId(String mCallId) {
            this.mCallId = mCallId;
        }

        public boolean isLocalActive(){
            return mIsActive;
        }

        public void setLocalActive(boolean isActive){
            mIsActive = isActive;
        }

        @Override
        public void onCallAudioStateChanged(CallAudioState state) {


            super.onCallAudioStateChanged(state);
        }

        @Override
        public void onStateChanged(int state) {

            super.onStateChanged(state);
        }

        @Override
        public void onPlayDtmfTone(char c) {
            super.onPlayDtmfTone(c);
        }

        @Override
        public void onStopDtmfTone() {
            super.onStopDtmfTone();
        }

        @Override
        public void onDisconnect() {
            VaxPhoneSIP.m_objVaxVoIP.DisconnectCall();
            super.onDisconnect();
        }

        @Override
        public void onSeparate() {
            super.onSeparate();
        }

        @Override
        public void onAbort() {
            VaxPhoneSIP.m_objVaxVoIP.DisconnectCall();
            setDisconnected(new DisconnectCause(DisconnectCause.REJECTED));
            destroy();
            super.onAbort();
        }

        @Override
        public void onHold() {
            VaxPhoneSIP.m_objVaxVoIP.HoldLine();
            setOnHold();
            super.onHold();
        }

        @Override
        public void onUnhold() {
            VaxPhoneSIP.m_objVaxVoIP.UnHoldLine();

            super.onUnhold();
        }

        @Override
        public void onAnswer(int videoState) {
            VaxPhoneSIP.m_objVaxVoIP.AcceptCall();
            setActive();
            super.onAnswer(videoState);
        }

        @Override
        public void onAnswer() {
            VaxPhoneSIP.m_objVaxVoIP.AcceptCall();
            setActive();
            super.onAnswer();
        }

        @Override
        public void onReject() {
            VaxPhoneSIP.m_objVaxVoIP.RejectCall();
            VaxPhoneSIP.m_objVaxVoIP.DisconnectCall();
            setDisconnected(new DisconnectCause(DisconnectCause.REJECTED));
            super.onReject();
        }

        @Override
        public void onReject(String replyMessage) {
            VaxPhoneSIP.m_objVaxVoIP.RejectCall();
            setDisconnected(new DisconnectCause(DisconnectCause.REJECTED));
            super.onReject(replyMessage);
        }

        @Override
        public void onPostDialContinue(boolean proceed) {
            super.onPostDialContinue(proceed);
        }

        @Override
        public void onPullExternalCall() {
            super.onPullExternalCall();
        }

        @Override
        public void onCallEvent(String event, Bundle extras) {
            super.onCallEvent(event, extras);
        }

        @Override
        public void onExtrasChanged(Bundle extras) {
            super.onExtrasChanged(extras);
        }

        @Override
        public void onShowIncomingCallUi() {
            super.onShowIncomingCallUi();
        }

        @Override
        public void sendConnectionEvent(String event, Bundle extras) {
            super.sendConnectionEvent(event, extras);
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        MyConnection connection = createConnectionFor();
        Bundle bundle = request.getExtras();
        String callerId =  bundle.getString("CallerId");
        String callerName = bundle.getString("CallerName");
        String callId = bundle.getString("CallId");
        connection.setCallerDisplayName(callerId, TelecomManager.PRESENTATION_ALLOWED);
        connection.setLocalActive(true);
        connection.setCallId(callId);
       // connection.setAddress(Uri.fromParts("sip", callerId, callerName), TelecomManager.PRESENTATION_ALLOWED);
        mCalls.add(connection);
       // updateCallCapabilities();
        return connection;
    }

    private MyConnection createConnectionFor() {
        MyConnection returnConnection = new MyConnection();
        returnConnection.setAudioModeIsVoip(true);


        return returnConnection;
    }
    @Override
    public void onCreateIncomingConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request);
    }

    @Override
    public void onCreateOutgoingConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request);
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        return super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request);
    }

    @Override
    public void onConference(Connection connection1, Connection connection2) {
        super.onConference(connection1, connection2);
    }

    @Override
    public void onRemoteConferenceAdded(RemoteConference conference) {
        super.onRemoteConferenceAdded(conference);
    }

    @Override
    public void onRemoteExistingConnectionAdded(RemoteConnection connection) {
        super.onRemoteExistingConnectionAdded(connection);
    }
    private void destroyCall(MyConnection connection) {
        mCalls.remove(connection);
        updateCallCapabilities();

    }
}
