package com.infra.qrys_wallet.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infra.qrys_wallet.Fragments.TransactionHistory;
import com.infra.qrys_wallet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sandeep.devhare on 26-10-2015.
 */
public class TransactionHistory_Adapter extends BaseAdapter {
    List<HashMap<String, String>> transactionDataCollection =
            new ArrayList<HashMap<String, String>>();
    Context mContext;
    String status;
    StringBuffer finalString;

    public TransactionHistory_Adapter(Context mContext, List<HashMap<String, String>> transactionDataCollection) {
        this.transactionDataCollection = transactionDataCollection;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (transactionDataCollection.size() <= 0)
            return 1;
        return transactionDataCollection.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        holder = new ViewHolder();
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.transaction_history_single_row, null);
            holder.tvDate = (TextView) vi.findViewById(R.id.tvdate); // Date
            holder.tvMonth = (TextView) vi.findViewById(R.id.tvmonth); // month
            holder.tvYear = (TextView) vi.findViewById(R.id.tvyear); // year
            holder.tvMsg = (TextView) vi.findViewById(R.id.tvmsg); // msg
            holder.tvRemark = (TextView) vi.findViewById(R.id.tvremark); // remark
            holder.tvRs = (TextView) vi.findViewById(R.id.tvammount); // Ammount
            holder.imgStatus = (ImageView) vi.findViewById(R.id.imgstatus); // status image
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
// Setting all values in listview
        String tempString = transactionDataCollection.get(position).get(TransactionHistory.MSG);
        finalString = new StringBuffer();
        int index = 0;
       /* while (index < tempString.length()) {
            Log.i("TestingSubString", "test = " + tempString.substring(index, Math.min(index + 25, tempString.length())));
            finalString.append(tempString.substring(index, Math.min(index + 25,tempString.length()))+"\n");
            index += 25;
        }*/
        /*----------*/
        String[] stringArray = tempString.split("\\s+");
        String tmpString = "";
        for (String singleWord : stringArray) {
            Log.d("Split", "singleWord = " + singleWord);
            if ((tmpString + singleWord + " ").length() > 20) {
                finalString.append(tmpString + "\n");
                Log.e("Split", "finalString = " + finalString);
                tmpString = singleWord + " ";
            } else {
                tmpString = tmpString + singleWord + " ";
            }
            Log.d("Split", "tmpString = " + tmpString);
        }
        if (tmpString.length() > 0) {
            finalString.append(tmpString);
            Log.e("Split", "last finalString = " + finalString);
        }
        /*------------*/
        System.out.println("Final String" + finalString);
        holder.tvDate.setText(transactionDataCollection.get(position).get(TransactionHistory.DATE));
        holder.tvMonth.setText(transactionDataCollection.get(position).get(TransactionHistory.MONTH));
        holder.tvYear.setText(transactionDataCollection.get(position).get(TransactionHistory.YEAR));
        holder.tvMsg.setText(finalString);
        holder.tvRemark.setText(transactionDataCollection.get(position).get(TransactionHistory.REMARK));
        holder.tvRs.setText(transactionDataCollection.get(position).get(TransactionHistory.RS));
        status = transactionDataCollection.get(position).get(TransactionHistory.STATUS);
        System.out.println("Status" + status);
        if (status.equals("PAID")) {
            holder.imgStatus.setImageResource(R.drawable.addnew);
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_faild);
        }
        //  holder.imgStatus.setImageResource(transactionDataCollection.get(position).get(TransactionHistory.RS));
        return vi;
    }

    static class ViewHolder {
        TextView tvDate;
        TextView tvMonth;
        TextView tvYear;
        TextView tvMsg;
        TextView tvRemark;
        TextView tvRs;
        ImageView imgStatus;
    }
}
