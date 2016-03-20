package com.infra.qrys_wallet.Adapters;
/**
 * Created by sandeep.devhare on 12-09-2015.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.R;

public class DashboardScreen_Adapter extends RecyclerView.Adapter<DashboardScreen_Adapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    Context context;
    private String mNavTitles[];
    private int mIcons[];
    private String name;
    private Bitmap profile;
    private String email;

    public DashboardScreen_Adapter(String Titles[], int Icons[], String Name, String Email, Bitmap Profile, Context passedContext) { // DashboardScreen_Adapter Constructor with titles and icons parameter
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;
        email = Email;
        profile = Profile;
        this.context = passedContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_row, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType, context);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType, context);
            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position - 1]);
        } else {
            if(profile!=null)
            {
                holder.profile.setImageBitmap(profile);
            }else
            {
                holder.profile.setImageResource(R.drawable.profile_pic);
            }

            //holder.profile.setImageResource(profile);
            holder.Name.setText(name);
            holder.email.setText(email);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;
        Context contxt;

        public ViewHolder(View itemView, int ViewType, Context c) {
            super(itemView);
            contxt = c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
            } else {
                Name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = 0;
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(contxt, "The Item Clicked is: " + getPosition(), Toast.LENGTH_SHORT).show();
        }

    }
}