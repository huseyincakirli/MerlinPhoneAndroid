package vaxsoft.com.vaxphone.MainTab.CallTab;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import vaxsoft.com.vaxphone.MainTab.MainTabActivity;
import vaxsoft.com.vaxphone.PhoneSIP.Contacts.Contacts;
import vaxsoft.com.vaxphone.R;

public class ContactDialog extends Dialog implements android.view.View.OnClickListener  {
    public Activity c;
    public Dialog d;
    public Fragment fragment;
    public Button mPhone, mExtension;
    Contacts m_objContacts = null;
    private final int CONTACT_PICKER = 2000;

    ContactDialog(Activity a)
    {
        super(a);
        setOwnerActivity(a);
        //fragment = frag;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_dialog);
        mPhone = (Button) findViewById(R.id.btn_phone);
        mExtension = (Button) findViewById(R.id.btn_extension);
        mPhone.setOnClickListener(this);
        mExtension.setOnClickListener(this);
        m_objContacts = new Contacts();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone:
                onClickContactButton();
                dismiss();
                break;
            case R.id.btn_extension:

                onClickExtensionButton();
                dismiss();
                //dismiss();
                break;
            default:
                break;
        }
    }


    private void onClickContactButton()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

       // m_objContacts.ShowContactView(getOwnerActivity());


    }

    private void onClickExtensionButton()
    {
        Intent myIntent = new Intent(getOwnerActivity(), MainTabActivity.class);
        myIntent.putExtra("One", 2);
        getOwnerActivity().startActivity(myIntent);
    }
}
