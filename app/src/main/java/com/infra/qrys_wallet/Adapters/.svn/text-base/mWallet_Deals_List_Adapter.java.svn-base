package com.infra.qrys_wallet.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infra.qrys_wallet.Fragments.mWalletOffers;
import com.infra.qrys_wallet.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sandeep.devhare on 26-10-2015.
 */
public class mWallet_Deals_List_Adapter extends BaseAdapter {
    List<HashMap<String,String>> dealDataCollection =
            new ArrayList<HashMap<String,String>>();
    Context mContext;
    String status;
    StringBuffer finalString;
    public mWallet_Deals_List_Adapter(Context mContext, List<HashMap<String, String>> dealDataCollection) {
       this.dealDataCollection=dealDataCollection;
        this.mContext =mContext;
    }

    @Override
    public int getCount() {
        if(dealDataCollection.size()<=0)
            return 1;
        return dealDataCollection.size();
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
        View vi=convertView;
        ViewHolder holder;
        holder = new ViewHolder();
        if(convertView==null){
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.mwallet_offers_single_row, null);

            holder.DEALNAME = (TextView)vi.findViewById(R.id.offerName); // offerName
            holder.DEALDESCRIPTION = (TextView)vi.findViewById(R.id.offerDetail); // offerDetail
            holder.imagePath = (ImageView)vi.findViewById(R.id.imgOffer); // status image
            vi.setTag(holder);
        }
        else{
            holder = (ViewHolder)vi.getTag();
        }

        Log.d("dealDataCollection", dealDataCollection.toString());

        holder.DEALNAME.setText(dealDataCollection.get(position).get(mWalletOffers.DealName));
        holder.DEALDESCRIPTION.setText(dealDataCollection.get(position).get(mWalletOffers.DealDescription));

        //if (holder.imagePath != null) {
        //    new ImageDownloaderTask(holder.imagePath).execute(dealDataCollection.get(position).get(mWalletOffers.DealImage));
        //}

        Bitmap temp = getBitmapFromURL(dealDataCollection.get(position).get(mWalletOffers.DealImage));
        if (temp == null){
            holder.imagePath.setImageResource(R.drawable.dealplaceholder);
        } else {
            holder.imagePath.setImageBitmap(temp);
      }
        //holder.imgStatus.setImageResource(dealDataCollection.get(position).get(TransactionHistory.RS));
        return vi;
    }
    static class ViewHolder{
        TextView DEALDESCRIPTION;
        ImageView imagePath;
        String DESCRIPTION;
        TextView DEALNAME;
        String txn_amount;
        String DEALDAYS;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            //Bitmap placeholderImage = BitmapFactory.decodeResource(getView().getResources(), R.drawable.dealplaceholder);
            return null;
        }
    }
    /*class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.dealplaceholder);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 400) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }*/
}
