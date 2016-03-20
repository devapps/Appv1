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
 * Created by vishvendu.palawat on 23-02-2016.
 */
public class UPI_VirtualAddressListAdapter extends ArrayAdapter<com.infra.qrys_wallet.Models.VirtualAddressList> {

    private final Context context;
    private final ArrayList<com.infra.qrys_wallet.Models.VirtualAddressList> itemsArrayList;



    public UPI_VirtualAddressListAdapter(Context context, ArrayList<com.infra.qrys_wallet.Models.VirtualAddressList> itemsArrayList) {
        super(context, R.layout.upi_virtualaddresslistadapter, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.upi_virtualaddresslistadapter, parent, false);
        TextView virtualAddressView = (TextView) rowView.findViewById(R.id.virtualAddressListTextview);

        virtualAddressView.setText(itemsArrayList.get(position).getVirtualAddressList());

        return rowView;
    }

}
