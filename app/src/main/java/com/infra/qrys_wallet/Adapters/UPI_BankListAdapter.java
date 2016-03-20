package com.infra.qrys_wallet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infra.qrys_wallet.Models.UPI_BankList;
import com.infra.qrys_wallet.R;

import java.util.ArrayList;

/**
 * Created by vishvendu.palawat on 24-02-2016.
 */
public class UPI_BankListAdapter extends ArrayAdapter<UPI_BankList> {

    private final Context context;
    private final ArrayList<UPI_BankList> itemsArrayList;
    private LayoutInflater inflater;
    ViewHolderItem viewHolder;

    public UPI_BankListAdapter(Context context, ArrayList<com.infra.qrys_wallet.Models.UPI_BankList> itemsArrayList) {
        super(context, R.layout.upi_virtualaddresslistadapter, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
      //  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        System.out.println("size of List"+itemsArrayList.size());
        return itemsArrayList.size();
    }

    @Override
    public UPI_BankList getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d("test id", position + "");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem viewHolder;


        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.upi_banklistadapter, parent, false);
            viewHolder = new ViewHolderItem();

            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.upi_banklisttextview);
            System.out.println("Items " + itemsArrayList.get(position).getBanklistupi());

            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();

        }
        viewHolder.textViewItem.setText(itemsArrayList.get(position).getBanklistupi());
        return convertView;
    }

    static class ViewHolderItem {
        TextView textViewItem;
    }
}
