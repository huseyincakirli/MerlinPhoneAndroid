package vaxsoft.com.vaxphone.MainAPP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import vaxsoft.com.vaxphone.VaxPhoneSIP;

public class BroadcastReceiverEx extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String sAction = intent.getAction();

        if (sAction == null)
            return;

        if (sAction.equals(Intent.ACTION_BOOT_COMPLETED))
        {
            VaxPhoneSIP.StartService();
            Log.e("VaxPhoneSIP", "+++ ACTION_BOOT_COMPLETED +++");
        }

        else if (intent.getAction().equals("VaxPhoneSIP.RESTART_VAX_SERVICE"))
        {
            Log.e("VaxPhoneSIP", "+++ RESTART_VAX_SERVICE +++");
            VaxPhoneSIP.StartService();
        }
    }
}
