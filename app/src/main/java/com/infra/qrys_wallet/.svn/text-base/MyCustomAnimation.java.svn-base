package com.infra.qrys_wallet;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class MyCustomAnimation extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View mView;
    private int mEndHeight;
    private int mType;
    private RelativeLayout.LayoutParams mLayoutParams;

    public MyCustomAnimation(View view, int duration, int type) {

        setDuration(duration);
        mView = view;
        mEndHeight = mView.getWidth();
        mLayoutParams = ((RelativeLayout.LayoutParams) view.getLayoutParams());
        mType = type;
        if(mType == EXPAND) {
            mLayoutParams.width = 0;
        } else {
            mLayoutParams.width = LayoutParams.FILL_PARENT;;
        }
        view.setVisibility(View.VISIBLE);
    }

    public int getHeight(){
        return mView.getWidth();
    }

    public void setHeight(int height){
        mEndHeight = height;
    }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            System.out.println("mType : "+mType);
            System.out.println("interpolatedTime : "+interpolatedTime);

            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                if(mType == EXPAND) {
                    System.out.println("mend*int");
                    mLayoutParams.width =  (int)(mEndHeight * interpolatedTime);
                } else {
                    System.out.println("mend-1");
                    mLayoutParams.width = (int) (mEndHeight * (1 - interpolatedTime));
                }
                mView.requestLayout();
            } else {
                if(mType == EXPAND) {
                    System.out.println("WRAP");
                    mLayoutParams.width = mEndHeight;
                    mView.requestLayout();
                }else{
                    System.out.println("GONE");
                    mView.setVisibility(View.GONE);
                }
            }
        }
    }
