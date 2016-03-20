package com.infra.qrys_wallet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infra.qrys_wallet.R;

import java.util.ArrayList;

/**
 * Created by sandeep.devhare on 16-10-2015.
 */
public class Individual_BankListAdapter extends ArrayAdapter<com.infra.qrys_wallet.Models.BankListModel> {

    private final Context context;
    private final ArrayList<com.infra.qrys_wallet.Models.BankListModel> itemsArrayList;

    public Individual_BankListAdapter(Context context, ArrayList<com.infra.qrys_wallet.Models.BankListModel> itemsArrayList) {

        super(context, R.layout.banklist_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.banklist_row, parent, false);
        TextView bankNameView = (TextView) rowView.findViewById(R.id.tvbankname);
        TextView bankTypeView = (TextView) rowView.findViewById(R.id.tvbanktype);
        TextView bankDescriptionView = (TextView) rowView.findViewById(R.id.tvbankdescription);
        TextView cbsType = (TextView) rowView.findViewById(R.id.cbsType);
        bankNameView.setText(itemsArrayList.get(position).getBankName());
        bankTypeView.setText(itemsArrayList.get(position).getBankType());
        bankDescriptionView.setText(itemsArrayList.get(position).getBankDescription());
        cbsType.setText(itemsArrayList.get(position).getCbsType());
        return rowView;
    }
}