package com.infra.qrys_wallet.Fragments;

/**
 * Created by sandeep.devhare on 01-12-2015.
 */
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;

public class LoadDialogFragment extends DialogFragment implements View.OnClickListener{
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("QRys Wallet");
        DashBoardWallet.fragmentID=12;
        View rootView = inflater.inflate(R.layout.dialog_forwalletload, container,
                false);
        getDialog().setTitle("Load Money");

        // Do something else
        ImageView loadFromWallet = (ImageView) rootView.findViewById(R.id.loadfromwallet);
        ImageView loadFromBankAccnt = (ImageView) rootView.findViewById(R.id.loadfrombankaccnt);
        loadFromWallet.setOnClickListener(this);
        loadFromBankAccnt.setOnClickListener(this);
        return rootView;
    }

    /*Send Option Click Handling*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.loadfromwallet:
                Toast.makeText(LoadDialogFragment.this.getActivity(), "Load Money Wallet", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                break;
            case R.id.loadfrombankaccnt:
                /*this is just for testing the UI of SendMoney Screen, plz see below commented code for actual*/
                Toast.makeText(LoadDialogFragment.this.getActivity(), "Load Money Account", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                break;
        }
    }
}