package com.infra.qrys_wallet.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.Fragments_UPI.ChequeData;
import com.infra.qrys_wallet.Fragments_UPI.MICR_Scanner;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.SharedPreference;


public class SetPaymentAddressFragment extends Fragment implements View.OnClickListener{

    Button Next,Back;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    EditText PaymentAddress;
    SharedPreference appPref;
    String PaymentAddressStr, ChequeNum;
    View rootView;
    public SetPaymentAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment Address");
        rootView = inflater.inflate(R.layout.fragment_set_payment_address, container, false);
        DashBoardWallet.fragmentID=17;

        Next=(Button)rootView.findViewById(R.id.nextPaymentAddressButton);
        PaymentAddress=(EditText)rootView.findViewById(R.id.paymentAddress);
        PaymentAddressStr=PaymentAddress.getText().toString();
        System.out.println("PaymentAddressStr "+PaymentAddressStr);
        System.out.println("PaymentAddressStr "+PaymentAddressStr.length());
        Back=(Button)rootView.findViewById(R.id.cancelPaymentAddressButton);

        Next.setOnClickListener(this);
        Back.setOnClickListener(this);


        //Edited by Vaibhav...
        Bundle bundle = this.getArguments();
        if(bundle !=null){
            String MICROutput = String.valueOf(bundle.getString("micrOutput"));
            ChequeNum = bundle.getString("ChequeNum");
            Log.d("ChequeNum1",ChequeNum);
            Log.d("MICROutputHere", MICROutput);
            PaymentAddress.setText(MICROutput);
        }


        return rootView;
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){


            case R.id.nextPaymentAddressButton :

                System.out.println("PaymentAddressStr.length() "+PaymentAddressStr.length());

                if (PaymentAddressStr.length()==0){

                    Toast.makeText(SetPaymentAddressFragment.this.getActivity(), "Please Enter Payment Address", Toast.LENGTH_SHORT).show();

                }


                if(MICR_Scanner.chequePaymentAddress){

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("Do you want to issue a Virtual Cheque?");

                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ChequeData chequeData = new ChequeData();
                            Bundle bundle = new Bundle();
                            Log.d("ChequeNum2",ChequeNum);
                            bundle.putString("chequeNumber", ChequeNum);
                            chequeData.setArguments(bundle);

                            fragmentManager = getFragmentManager();
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.frame_container, chequeData);
                            ft.commit();
                        }
                    });

                    alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    });

                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    MICR_Scanner.chequePaymentAddress=false;

                }
                else{
                    PaymentAddress=(EditText)rootView.findViewById(R.id.paymentAddress);
                    PaymentAddressStr=PaymentAddress.getText().toString();
                    appPref.getInstance().setString("PaymentAddressStr_upi", PaymentAddressStr);
                    UPI_BankListFragment upi_bankListFragment=new UPI_BankListFragment();
                    fragmentManager = getFragmentManager();
                    ft=fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container, upi_bankListFragment);
                    ft.commit();

                }

                break;



            case R.id.cancelPaymentAddressButton:

                break;
        }
    }
}
