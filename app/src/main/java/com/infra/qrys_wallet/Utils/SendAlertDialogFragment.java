package com.infra.qrys_wallet.Utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.infra.qrys_wallet.R;

/**
 * Created by sandeep.devhare on 27-11-2015.
 */
public class SendAlertDialogFragment  extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.qrys_logo)
                        // Set Dialog Title
                .setTitle("Alert DialogFragment")
                        // Set Dialog Message
                .setMessage("Alert DialogFragment Tutorial")

                        // Positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                })

                        // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        // Do something else
                    }
                }).create();
    }
}