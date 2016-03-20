package com.infra.qrys_wallet.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.infra.qrys_wallet.R;


public class paymentAddressDialog extends DialogFragment implements View.OnClickListener {

    Button Later,Now;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("UPI");
        getDialog().setTitle("Information");
        View rootView= inflater.inflate(R.layout.fragment_payment_address_dialog, container, false);
        getDialog().setTitle("Payment Address");
        Later=(Button)rootView.findViewById(R.id.btncancel);
        Now=(Button)rootView.findViewById(R.id.now);
        Now.setOnClickListener(this);
        Later.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View view) {

        System.out.println("view ID "+ view.getId());
        switch(view.getId()){



            case R.id.now:

                SetPaymentAddressFragment PayAdd = new SetPaymentAddressFragment();
                fragmentManager =getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container,PayAdd);
                ft.commit();
                getDialog().dismiss();
                break;
        }

    }
}
