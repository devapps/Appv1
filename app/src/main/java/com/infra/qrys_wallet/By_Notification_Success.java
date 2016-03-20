package com.infra.qrys_wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class By_Notification_Success extends Activity implements View.OnClickListener{
Button home;
    TextView transactionID;
    String trnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.by__notification__success);
        transactionID = (TextView)findViewById(R.id.idinnumber);
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        Intent intent = getIntent();
        if(intent!=null)
        {
            trnId = intent.getStringExtra("transactionRefId");
            transactionID.setText(trnId);

        }else
        {
            Log.e("Data not received","here");
        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.home:
                //exit app or it goes to home.
                System.exit(0);
                break;
            default:
                break;
        }
    }
}
