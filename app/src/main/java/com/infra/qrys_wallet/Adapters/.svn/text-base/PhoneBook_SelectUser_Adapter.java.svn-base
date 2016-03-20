package com.infra.qrys_wallet.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.infra.qrys_wallet.Models.PhoneBook_SelectUser;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.RoundImage;
import com.infra.qrys_wallet.Utils.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by sandeep.devhare on 20-11-2015.
 */
public class PhoneBook_SelectUser_Adapter extends BaseAdapter {

    public List<PhoneBook_SelectUser> _data;
    private ArrayList<PhoneBook_SelectUser> arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;
    RoundedImageView roundImg;
    Bitmap bmp;
    //an array that stores the pixel values
    private int intArray[];
    public PhoneBook_SelectUser_Adapter(List<PhoneBook_SelectUser> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<PhoneBook_SelectUser>();
        this.arraylist.addAll(_data);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.phonebook_row_info, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.name);
        v.check = (CheckBox) view.findViewById(R.id.check);
        v.phone = (TextView) view.findViewById(R.id.no);
        v.imageView = (ImageView) view.findViewById(R.id.pic);

        final PhoneBook_SelectUser data = (PhoneBook_SelectUser) _data.get(i);
        v.title.setText(data.getName().toString());
       // v.check.setChecked(data.getCheckedBox());
        v.phone.setText(data.getPhone().toString());

        // Set image if exists
        try {

            if (data.getThumb() != null) {
                //v.imageView.setImageBitmap(data.getThumb());
                bmp = getCircularBitmap(data.getThumb());
                v.imageView.setImageBitmap(bmp);
            } else {
                v.imageView.setImageResource(R.drawable.profile_pic);
            }

        } catch (OutOfMemoryError e) {
            // Add default picture
            v.imageView.setImageDrawable(this._c.getDrawable(R.drawable.profile_pic));
            e.printStackTrace();
        }

        Log.e("Image Thumb", "--------------" + data.getThumb());

        // Set check box listener android
      /*  v.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    data.setCheckedBox(true);
                } else {
                    data.setCheckedBox(false);
                }
            }
        });*/

        view.setTag(data);
        return view;
    }
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;


        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (PhoneBook_SelectUser wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
        TextView title, phone;
        CheckBox check;
    }
}
