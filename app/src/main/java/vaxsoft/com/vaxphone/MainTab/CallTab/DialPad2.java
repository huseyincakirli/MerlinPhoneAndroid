package vaxsoft.com.vaxphone.MainTab.CallTab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import vaxsoft.com.vaxphone.CustomViews.TabFragment.CustomTabFragment;
import vaxsoft.com.vaxphone.MainTab.MainTabActivity;
import vaxsoft.com.vaxphone.PhoneSIP.Contacts.Contacts;
import vaxsoft.com.vaxphone.R;
import vaxsoft.com.vaxphone.VaxPhoneSIP;
import vaxsoft.com.vaxphone.VaxStorage.Store.StoreDialNo;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DialPad2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DialPad2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialPad2 extends CustomTabFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Contacts m_objContacts = null;
    Button button1,button2,button3,button4,button5,button6,
    button7,button8,button9,buttonstar,button0,buttoncall,buttonhash,buttoncontacts,buttonback;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText EditText_DialNo;
    private OnFragmentInteractionListener mListener;

    public DialPad2() {
        // Required empty public constructor
    }
    private void LoadViewAll(View view) {
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);
        button5 = view.findViewById(R.id.button5);
        button6 = view.findViewById(R.id.button6);
        button7 = view.findViewById(R.id.button7);
        button8 = view.findViewById(R.id.button8);
        button9 = view.findViewById(R.id.button9);
        buttonstar = view.findViewById(R.id.buttonstar);
        button0 = view.findViewById(R.id.button0);
        buttonhash = view.findViewById(R.id.buttonhash);
        buttoncontacts = view.findViewById(R.id.buttoncontact);
        buttoncall = view.findViewById(R.id.buttoncall);
        buttonback = view.findViewById(R.id.buttonback);
        EditText_DialNo = view.findViewById(R.id.editText);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitListeners()
    {
        button1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button1.setBackgroundResource(R.drawable.adialerdown01);
                        PlayDTMF("1");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button1.setBackgroundResource(R.drawable.adialerup01);
                        return true;
                }
                return false;
            }
        });
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button2.setBackgroundResource(R.drawable.adialerdown02);
                        PlayDTMF("2");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button2.setBackgroundResource(R.drawable.adialerup02);
                        return true;
                }
                return false;
            }
        });
        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button3.setBackgroundResource(R.drawable.adialerdown03);
                        PlayDTMF("3");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button3.setBackgroundResource(R.drawable.adialerup03);
                        return true;
                }
                return false;
            }
        });
        button4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button4.setBackgroundResource(R.drawable.adialerdown04);
                        PlayDTMF("4");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button4.setBackgroundResource(R.drawable.adialerup04);
                        return true;
                }
                return false;
            }
        });
        button5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button5.setBackgroundResource(R.drawable.adialerdown05);
                        PlayDTMF("5");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button5.setBackgroundResource(R.drawable.adialerup05);
                        return true;
                }
                return false;
            }
        });
        button6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button6.setBackgroundResource(R.drawable.adialerdown06);
                        PlayDTMF("6");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button6.setBackgroundResource(R.drawable.adialerup06);
                        return true;
                }
                return false;
            }
        });
        button7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button7.setBackgroundResource(R.drawable.adialerdown07);
                        PlayDTMF("7");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button7.setBackgroundResource(R.drawable.adialerup07);
                        return true;
                }
                return false;
            }
        });
        button8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button8.setBackgroundResource(R.drawable.adialerdown08);
                        PlayDTMF("8");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button8.setBackgroundResource(R.drawable.adialerup08);
                        return true;
                }
                return false;
            }
        });
        button9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button9.setBackgroundResource(R.drawable.adialerdown09);
                        PlayDTMF("9");
                        return true;
                    case MotionEvent.ACTION_UP:
                        button9.setBackgroundResource(R.drawable.adialerup09);
                        return true;
                }
                return false;
            }
        });
        buttonstar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonstar.setBackgroundResource(R.drawable.adialerdownasterisk);
                        PlayDTMF("*");
                        return true;
                    case MotionEvent.ACTION_UP:
                        buttonstar.setBackgroundResource(R.drawable.adialerupasterisk);
                        return true;
                }
                return false;
            }
        });
        button0.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    button0.setBackgroundResource(R.drawable.adialerdown00);
                    PlayDTMF("0");
                    return true;
                case MotionEvent.ACTION_UP:
                    button0.setBackgroundResource(R.drawable.adialerup00);
                    return true;
            }
            return false;
        }
    });
        buttonhash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonhash.setBackgroundResource(R.drawable.adialerdownhash);
                        PlayDTMF("#");
                        return true;
                    case MotionEvent.ACTION_UP:
                        buttonhash.setBackgroundResource(R.drawable.adialeruphash);
                        return true;
                }
                return false;
            }
        });
        buttoncontacts.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    buttoncontacts.setBackgroundResource(R.drawable.adialerdowncontacts);
                    return true;
                case MotionEvent.ACTION_UP:
                    buttoncontacts.setBackgroundResource(R.drawable.adialerupcontacts);
                    return true;
            }
            return false;
        }
    });
        buttoncall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttoncall.setBackgroundResource(R.drawable.adialerdowncall);
                        onClickDialButton();
                        return true;
                    case MotionEvent.ACTION_UP:
                        buttoncall.setBackgroundResource(R.drawable.adialerupcall);
                        return true;
                }
                return false;
            }
        });
        buttonback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonback.setBackgroundResource(R.drawable.adialerdownbackspace);
                        onClickDeleteButton();
                        return true;
                    case MotionEvent.ACTION_UP:
                        buttonback.setBackgroundResource(R.drawable.adialerupbackspace);
                        return true;
                }
                return false;
            }
        });


        EditText_DialNo.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch (View v, MotionEvent event) {
                return true; // the listener has consumed the event
            }
        });

    }

    private void InitObjects()
    {
        m_objContacts = new Contacts();
    }

    private void onClickDeleteButton()
    {
        String sNum = EditText_DialNo.getText().toString().trim();

        if (sNum.length() <= 1)
        {
            EditText_DialNo.getText().clear();
            return;
        }

        sNum = sNum.substring(0, sNum.length() - 1);
        EditText_DialNo.setText(sNum);
       // TextViewName.setText("");
    }

    private void onClickDialButton()
    {
        boolean bIsLineBusy = VaxPhoneSIP.m_objVaxVoIP.IsLineConnected();

        if (bIsLineBusy)
        {
            if (VaxPhoneSIP.m_objVaxVoIP.DisconnectCall())
            {
               // DialIconSelected(false);
                //DialBtnText.setText("DIAL");
                buttoncall.setBackgroundResource(R.drawable.adialerupcall);
            }

            return;
        }

        String sDialNo = EditText_DialNo.getText().toString().trim();
        if (sDialNo.length() == 0) return;

        if (VaxPhoneSIP.m_objVaxVoIP.DialCall(sDialNo))
        {
            StoreDialNo objStoreDialNo = new StoreDialNo();
            objStoreDialNo.SaveDialNo(sDialNo);
            buttoncall.setBackgroundResource(R.drawable.adialerupend);
         //   DialIconSelected(true);
         //   DialBtnText.setText("END");
        }
       // Intent myIntent = new Intent(getActivity(), MainTabActivity.class);
      //  startActivity(myIntent);
        //getActivity().onBackPressed();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialPad2.
     */
    // TODO: Rename and change types and number of parameters
    public static DialPad2 newInstance(String param1, String param2) {
        DialPad2 fragment = new DialPad2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dial_pad2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        LoadViewAll(view);
        InitListeners();
        InitObjects();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }
private void  PlayDTMF(String m_sDigit)
{
    EditText_DialNo.append(m_sDigit);

    VaxPhoneSIP.m_objVaxVoIP.PlayDTMF(m_sDigit);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class OnClickListenerEx implements View.OnClickListener
    {
        String m_sDigit;

        OnClickListenerEx(String sTextNo)
        {
            m_sDigit = sTextNo;
        }

        public void onClick(View v)
        {
            PlayDTMF(m_sDigit);
        }
    }
}
