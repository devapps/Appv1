package com.infra.qrys_wallet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infra.qrys_wallet.Models.UPI_SelectedBankAccountsModel;
import com.infra.qrys_wallet.R;

import java.util.ArrayList;

/**
 * Created by vishvendu.palawat on 25-02-2016.
 */
public class UPI_SelectedBankAccountAdapter extends ArrayAdapter<UPI_SelectedBankAccountsModel> {

    private final Context context;
    private final ArrayList<UPI_SelectedBankAccountsModel> itemsArrayList;
    private LayoutInflater inflater;
   // String[] itemsArrayList;
    public UPI_SelectedBankAccountAdapter(Context context,ArrayList<UPI_SelectedBankAccountsModel>itemsArrayList){
        //super();
        super(context,R.layout.upi_bankaccountlistadapter,itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.upi_bankaccountlistadapter, parent, false);
        TextView AccountType = (TextView) rowView.findViewById(R.id.DynamicsavingAccount);
       // AccountType.setText("SAVINGS");
        TextView IFSC = (TextView) rowView.findViewById(R.id.dynamicIFCS);
       // IFSC.setText("MAPP0000103");
        TextView AccountNo = (TextView) rowView.findViewById(R.id.DynamicsavingAccount);
       // AccountNo.setText("45151545548799");
        TextView cbsType = (TextView) rowView.findViewById(R.id.cbsType);
        AccountType.setText(itemsArrayList.get(position).getAccountType());
        IFSC.setText(itemsArrayList.get(position).getIFSCCode());
        AccountNo.setText(itemsArrayList.get(position).getAccountNo());


        return rowView;
    }


}
