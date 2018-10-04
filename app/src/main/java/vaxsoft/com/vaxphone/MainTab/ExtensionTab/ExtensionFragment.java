package vaxsoft.com.vaxphone.MainTab.ExtensionTab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import vaxsoft.com.vaxphone.CryptoHelper;
import vaxsoft.com.vaxphone.CustomViews.TabFragment.CustomTabFragment;
import vaxsoft.com.vaxphone.MainTab.CallTab.CallTabFragment;
import vaxsoft.com.vaxphone.MainTab.CallTab.DialpadFragment;
import vaxsoft.com.vaxphone.R;
import vaxsoft.com.vaxphone.RedisIntentService;
import vaxsoft.com.vaxphone.VaxPhoneSIP;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link// ExtensionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExtensionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExtensionFragment extends CustomTabFragment implements View.OnClickListener,SearchView.OnQueryTextListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    private boolean isRegistered;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ResponseReceiver receiver;
    List<SipStatus> data;
    @Override
    public boolean onQueryTextSubmit(String query) {


        List<SipStatus> filteredData = FilterData(query);
        BindDataToView(filteredData);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        List<SipStatus> filteredData = FilterData(newText);
        BindDataToView(filteredData);
        return true;
    }

    private List<SipStatus> FilterData(String query)
    {
        final String lowerCaseQuery = query.toLowerCase();
        final List<SipStatus> filteredData = new ArrayList<SipStatus>();
        for (SipStatus item:data) {
            if (item.DisplayName.toLowerCase().contains(lowerCaseQuery) || item.Extension.toLowerCase().contains(lowerCaseQuery))
            {
                filteredData.add(item);
            }
        }

        return filteredData;
    }
    public class ResponseReceiver extends BroadcastReceiver {

        public static final String ACTION_RESP = "com.mamlambo.intent.action.MESSAGE_PROCESSED";
        @Override
        public void onReceive(Context context, Intent intent) {
            String channel = intent.getStringExtra("Channel");
            String message = intent.getStringExtra("Message");

            System.out.println("!!!!!!!! MESSAGE RECEIVED !!!!!!");
            try{
                String json = CryptoHelper.decrypt(message,CryptoHelper.MsgKey);
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<SipStatus>>(){}.getType();
                data = gson.fromJson(json,listType);

                BindDataToView(data);

              //  System.out.println(json);

            }catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }

            //Toast toast = new Toast(getApplicationContext());
            //toast.setText(message);
            //toast.show();
        }
    }



    private  void OpenDialPad(String sCallerNum,String name)
    {
        Bundle objBundle = new Bundle();
        objBundle.putString("CallerNum", sCallerNum);
        objBundle.putString("Name",name);
        DialpadFragment objDialpadFragment = new DialpadFragment();
        objDialpadFragment.setArguments(objBundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, objDialpadFragment).addToBackStack(null).commit();
    }

    private  void  OpenCallPad()
    {
//        CallTabFragment callTabFragment = new CallTabFragment();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content, callTabFragment).addToBackStack(null).commit();

        TabLayout tabhost = getActivity().findViewById(R.id.tabs);
        tabhost.getTabAt(1).select();
    }
    public ExtensionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExtensionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExtensionFragment newInstance(String param1, String param2) {
        ExtensionFragment fragment = new ExtensionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);

    }

    @Override
    public void onPause() {
        super.onPause();
//        if (isRegistered)
//        {
//            getActivity().unregisterReceiver(receiver);
//            isRegistered = false;
//        }

        //unregisterReceiver(yourReceiver);
    }

    @Override
    public void onStop()
    {
        UnregisterRedis();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);


        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null)
        {
            final SearchView searchView =(SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(this);
        }

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!isRegistered)
        {
            RegisterReceiver();
        }
    }

    public void RegisterReceiver()
    {
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        getActivity().registerReceiver(receiver,filter);
        isRegistered = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View RootView = inflater.inflate(R.layout.fragment_extension, container, false);

        mRecyclerView = RootView.findViewById(R.id.extension_recycler_view);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        data = new ArrayList<>();
        //SetSampleData();
        BindDataToView(data);

        InitRedis();
        SendRedisMessage();
        return RootView;


    }

    private void SendRedisMessage()
    {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    StringBuilder RedisServer = new StringBuilder();
                    StringBuilder RedisPassword = new StringBuilder();
                    StringBuilder RedisPort = new StringBuilder();
                    StringBuilder SystemId = new StringBuilder();

                    VaxPhoneSIP.m_objVaxVoIP.GetLoginInfo(null, null, null, null, null, null,null,null
                            ,RedisServer,RedisPassword,RedisPort,SystemId);

                    String redisServer = RedisServer.toString();
                    Integer redisPort = Integer.parseInt(RedisPort.toString());
                    String redisPassword = RedisPassword.toString();

                    Jedis  jedis = new Jedis(redisServer,redisPort);
                    if (!redisPassword.isEmpty())
                    {
                        jedis.auth(redisPassword);
                    }

                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("Type","69");
                    jsonObj.put("TypeStr","SubscribeUserData");
                    jsonObj.put("CommandStr","Add");
                    jsonObj.put("Command","0");
                    jsonObj.put("Data",SystemId.toString());

                    jedis.publish("Merlin.WebChannel",jsonObj.toString());
                    System.out.println("!!!Redis message sent!!!");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private void DisconnectRedis()
    {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    StringBuilder RedisServer = new StringBuilder();
                    StringBuilder RedisPassword = new StringBuilder();
                    StringBuilder RedisPort = new StringBuilder();
                    StringBuilder SystemId = new StringBuilder();

                    VaxPhoneSIP.m_objVaxVoIP.GetLoginInfo(null, null, null, null, null, null,null,null
                            ,RedisServer,RedisPassword,RedisPort,SystemId);

                    String redisServer = RedisServer.toString();
                    Integer redisPort = Integer.parseInt(RedisPort.toString());
                    String redisPassword = RedisPassword.toString();

                    Jedis  jedis = new Jedis(redisServer,redisPort);
                    if (!redisPassword.isEmpty())
                    {
                        jedis.auth(redisPassword);
                    }
                    JedisPubSub jedisPubSub = new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            super.onMessage(channel, message);
                        }
                    };
                    String channel = "Merlin.Fenix.MobileClient_" + SystemId.toString();
                    jedis.subscribe(jedisPubSub,channel);
                    jedisPubSub.unsubscribe();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
    }


    private void BindDataToView(List<SipStatus> data)
    {
        mAdapter = new ExtensionAdapter(getContext(), data, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, String no,String name) {
                if (VaxPhoneSIP.m_objVaxVoIP.IsLineConnected())
                {
                    VaxPhoneSIP.m_objVaxVoIP.TransferCallBlind(0,no);
                    OpenCallPad();
                }
                else
                {
                    OpenDialPad(no,name);
                }

                //Toast.makeText(getContext(), "Clicked Item: "+position,Toast.LENGTH_SHORT).show(); //(RecyclerViewActivity.this, "Clicked Item: "+position,Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    public void InitRedis() {

        Intent cbIntent =  new Intent();
        cbIntent.setClass(getContext(), RedisIntentService.class);
        getActivity().startService(cbIntent);

    }

    public void StartRedis()
    {
       // InitRedis();
       // RegisterReceiver();
          SendRedisMessage();
    }

    public void StopRedis()
    {
        // Unsubscribe();
        //  UnregisterRedis();
       // DisconnectRedis();
    }

    public void UnregisterRedis()
    {
        if (isRegistered)
        {
            getActivity().unregisterReceiver(receiver);
            isRegistered = false;

        }
    }
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);


//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        UnregisterRedis();
        super.onDetach();

        //mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
