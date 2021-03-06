package com.infra.qrys_wallet.Utils;

/**
 * Created by sandeep.devhare on 12-10-2015.
 */
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.Fragments.SendMoneyFragment;
import com.infra.qrys_wallet.Fragments_UPI.Insta_Pay;
import com.infra.qrys_wallet.Fragments_UPI.SendMoney_UPI;
import com.infra.qrys_wallet.R;

/**
 * Sample of scanning from a Fragment
 */
public class ScanQRMain extends Fragment {
    private String toast;
    String scanContents;
    private FragmentManager fragmentManager;
    FragmentTransaction ft1;
    public static boolean CheckQRScanrevertPositionSendMOney = false;
    public static boolean CheckQRScanrevertPositionInstaPay =false;
    public ScanQRMain() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        DashBoardWallet.fragmentID = 8;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scanqrthread);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scan QR");
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            scanFromFragment();
            //finish();
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            Toast.makeText(ScanQRMain.this.getActivity(), "QR Scanner not working", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
    }


    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //toast = "Cancelled from fragment";
            } else {
              //  toast = "Scanned QR Details: " + result.getContents();
                 scanContents = result.getContents();
                /*-----------------*/

                System.out.println("Content from QR Code  " + scanContents);
                Log.e("Content:", "is " + scanContents);

                if(SendMoney_UPI.CheckQRScanPositionSendMoney==true){

                    CheckQRScanrevertPositionSendMOney=true;
                    SendMoney_UPI sendMoney_upi= new SendMoney_UPI();
                    fragmentManager=getFragmentManager();
                    // getFragmentManager().popBackStack();
                    ft1 = fragmentManager.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("DetailsUPI", scanContents);
                    // set Fragmentclass Arguments

                    sendMoney_upi.setArguments(bundle);
                    ft1.replace(R.id.frame_container, sendMoney_upi);
                    // ft.addToBackStack("fragback");
                    ft1.commit();

                    SendMoney_UPI.CheckQRScanPositionSendMoney=false;

                }
                else if(Insta_Pay.CheckQRScanPositionInsta==true){

                    CheckQRScanrevertPositionInstaPay=true;
                    Insta_Pay insta_pay= new Insta_Pay();
                    fragmentManager=getFragmentManager();
                    // getFragmentManager().popBackStack();
                    ft1 = fragmentManager.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("DetailsUPI", scanContents);
                    // set Fragmentclass Arguments

                    insta_pay.setArguments(bundle);
                    ft1.replace(R.id.frame_container, insta_pay);
                    // ft.addToBackStack("fragback");
                    ft1.commit();
                    Insta_Pay.CheckQRScanPositionInsta=false;

                }
                else{

                    fragmentManager = getFragmentManager();
                    ft1 = fragmentManager.beginTransaction();
                    SendMoneyFragment sendQRDetails = new SendMoneyFragment();
                    Log.e("SendMoney 1:", "Fragment Calls ");
                    //--
                    Bundle bundle = new Bundle();
                    bundle.putString("Details", scanContents);
                    // set Fragmentclass Arguments

                    sendQRDetails.setArguments(bundle);
                    Log.e("SendMoney 2:", "Fragment Calls ");
                    //--
                    ft1.replace(R.id.frame_container, sendQRDetails);
                    // Append this transaction to the backstack
                    //  ft1.addToBackStack("optional tag");
                    ft1.commit();

                }


                /*-----------------*/
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }
}