package com.infra.qrys_wallet.Fragments_UPI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.infra.qrys_wallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetTpinFragment extends Fragment {

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    public SetTpinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView=  inflater.inflate(R.layout.fragment_set_tpin, container, false);

        EditText SetTpin=(EditText)rootView.findViewById(R.id.settpin);
        Button setTPINButton=(Button)rootView.findViewById(R.id.setTpinbutton);

        setTPINButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmTPIN confirmTPIN=new ConfirmTPIN();
                fragmentManager=getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container,confirmTPIN);
                ft.commit();

            }
        });

        return rootView;
    }


}
