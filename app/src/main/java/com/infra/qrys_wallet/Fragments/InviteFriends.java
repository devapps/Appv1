package com.infra.qrys_wallet.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class InviteFriends extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Invite Friends");
        DashBoardWallet.fragmentID=4;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView = inflater.inflate(R.layout.invite_friends, container, false);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out  QRys app at https://play.google.com/store/apps/details?id=com.infra.qrys");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        return rootView;

    }



}
