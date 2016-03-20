package com.infra.qrys_wallet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infra.qrys_wallet.Models.AddAccounts;
import com.infra.qrys_wallet.R;

import java.util.ArrayList;

/**
 * Created by vishvendu.palawat on 04-11-2015.
 */
public class ListOfAddAccount_Adapter extends ArrayAdapter<AddAccounts> {

    private final Context context;
    private final ArrayList<com.infra.qrys_wallet.Models.AddAccounts> itemsArrayList;

        public ListOfAddAccount_Adapter(Context context, ArrayList<AddAccounts> itemsArrayList) {

            super(context, R.layout.accounts_listview, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.accounts_listview, parent, false);
            TextView AddAccountName = (TextView) rowView.findViewById(R.id.AddAccountName);
        TextView AddAccountBankName = (TextView) rowView.findViewById(R.id.AddAccountBankName);
        TextView AddAccountAccountNo = (TextView) rowView.findViewById(R.id.AddAccountAccountNo);
        AddAccountName.setText(itemsArrayList.get(position).getAddAccount_Name());
        AddAccountBankName.setText(itemsArrayList.get(position).getAddAccount_BankName());
        AddAccountAccountNo.setText(itemsArrayList.get(position).getAddAccount_AccountNo());
        return rowView;
    }
}
