package com.infra.qrys_wallet.Registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.SharedPreference;

/**
 * Created by sandeep.devhare on 15-10-2015.
 */
public class IndividualGetStarted extends Activity {
    Button getStart;
    TextView tvuserName;
SharedPreference appPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started_screen);
        appPref.getInstance().Initialize(getApplicationContext());
        tvuserName = (TextView) findViewById(R.id.tvusername);
      //  tvuserName.setText(DataHolderClass.getInstance().getIndividualPersonfName() + " " + DataHolderClass.getInstance().getIndividualPersonLname());
        tvuserName.setText(appPref.getInstance().getString("fName") + " " + appPref.getInstance().getString("lName"));
        getStart = (Button) findViewById(R.id.btngonext);
        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(IndividualGetStarted.this, DashBoardWallet.class);
                startActivity(in);
                finish();
            }
        });
    }
}
