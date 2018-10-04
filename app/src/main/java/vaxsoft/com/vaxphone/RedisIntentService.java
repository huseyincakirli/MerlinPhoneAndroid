package vaxsoft.com.vaxphone;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import vaxsoft.com.vaxphone.AccountLogin.AccountLoginActivity;
import vaxsoft.com.vaxphone.MainTab.ExtensionTab.ExtensionFragment;

public class RedisIntentService extends IntentService{





    public RedisIntentService()
    {
        super("RedisIntentService");

    }

    public static void Unsubscribe()
    {
        //jedisPubSub.unsubscribe();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        System.out.println("Redis Subscribed !!!");
        try {
            StringBuilder RedisServer = new StringBuilder();
            StringBuilder RedisPassword = new StringBuilder();
            StringBuilder RedisPort = new StringBuilder();
            StringBuilder SystemId = new StringBuilder();

            VaxPhoneSIP.m_objVaxVoIP.GetLoginInfo(null, null, null, null, null, null, null, null
                    , RedisServer, RedisPassword, RedisPort, SystemId);

            String redisServer = RedisServer.toString();
            Integer redisPort = Integer.parseInt(RedisPort.toString());
            String redisPassword = RedisPassword.toString();

            if (!redisServer.isEmpty()) {
                Jedis jedis = new Jedis(redisServer, redisPort);
                if (!redisPassword.isEmpty()) {
                    jedis.auth(redisPassword);
                }
                JedisPubSub jedisPubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(ExtensionFragment.ResponseReceiver.ACTION_RESP);
                        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        broadcastIntent.putExtra("Message", message);
                        broadcastIntent.putExtra("Channel", channel);
                        sendBroadcast(broadcastIntent);
                        super.onMessage(channel, message);
                    }


                };
                String channel = "Merlin.Fenix.MobileClient_" + SystemId.toString();
                System.out.println("CHANNEL :" + channel);
                jedis.subscribe(jedisPubSub,channel);

            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            System.out.println(message);
        }
    }



}