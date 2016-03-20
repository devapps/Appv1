package com.infra.qrys_wallet.Fragments;

/**
 * Created by sandeep.devhare on 27-11-2015.
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

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;

public class RequestDialogFragment extends DialogFragment implements View.OnClickListener{
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
        DashBoardWallet.fragmentID = 10;
        View rootView = inflater.inflate(R.layout.dialog_forwalletrequest, container,
                false);
        getDialog().setTitle("Request Money");
        

        ImageView contactRequestMoney = (ImageView) rootView.findViewById(R.id.imgrequestmoney);
        ImageView shareQR = (ImageView) rootView.findViewById(R.id.imgrequestqr);
        contactRequestMoney.setOnClickListener(this);
        shareQR.setOnClickListener(this);
        return rootView;
    }

    /*Send Option Click Handling*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgrequestmoney:

                Contacts phoneBook = new Contacts();
                fragmentManager = getFragmentManager();
               // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
               /* for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }*/
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, phoneBook);
               // ft.addToBackStack("fragback");
                ft.commit();
                getDialog().dismiss();
                break;
            case R.id.imgrequestqr:
               /* Intent shareQrCall = new Intent(getActivity(), ShareQR.class);
                getActivity().startActivity(shareQrCall);
                getActivity().finish();*/

               ShareQR qrshare = new ShareQR();
               // getFragmentManager().popBackStack();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
               /* for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }*/
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, qrshare);
               // ft.addToBackStack("fragback");
                ft.commit();
                getDialog().dismiss();
                break;
        }
    }
}