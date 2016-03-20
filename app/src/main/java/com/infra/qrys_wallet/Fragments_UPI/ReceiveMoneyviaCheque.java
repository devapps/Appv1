package com.infra.qrys_wallet.Fragments_UPI;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.SharedPreference;

/**
 * Created by vaibhav.ghanekar on 18-03-2016.
 */
public class ReceiveMoneyviaCheque extends Fragment {

    View rootView;
    Button buttonProceed;
    EditText editText;
    String chequeNum;
    FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction ft;
    SharedPreference appPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_receive_moneyvia_cheque, container, false);


        Bundle bundle = this.getArguments();
        chequeNum =bundle.getString("ChequeNum");


        buttonProceed = (Button) rootView.findViewById(R.id.buttonProceed);
        editText = (EditText)rootView.findViewById(R.id.editTextChequeNumber);
        editText.setText(chequeNum);

        //chequeNum = editText.getText().toString();


        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPref.getInstance().setString("chequeNum", chequeNum);

                Fragment_RecieveMoney_Cheque_Data_Upi fragment_recieveMoney_cheque_data_upi = new Fragment_RecieveMoney_Cheque_Data_Upi();
                //fragment_recieveMoney_cheque_data_upi.setArguments(bundle);
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();

                ft.replace(R.id.frame_container, fragment_recieveMoney_cheque_data_upi);
                ft.commit();
            }
        });
        return rootView;
    }
}
