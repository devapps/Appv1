package com.infra.qrys_wallet.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sandeep.devhare on 25-11-2015.
 */
public class Wallet extends Fragment implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener{
    /*Chart Variables */
    public String[] mParties = new String[]{
            "Balance", "Spent", "NFC Payment", "QR Payment","Merchant"
    };
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    JSONObject json, jsonInner;
    private Typeface tf;
    protected static final String DEEPLINKING_URL_BASE = "upi://pay";
    Uri uri;
    private PieChart mChart;
    MWRequest mwRequest;
    SharedPreference appPref;
    String userAccountNo, cbsType, userMobileNo,  graphFieldtype, graphFieldPer,DeviceId,WalletBalance,WalletPoint;
    JSONArray DataSet = null;
    JSONArray InnerDataSet = null;
    ArrayList<String> GraphType, GraphPer;
    private FragmentManager fragmentManager;

    List<HashMap<String, String>> dealDataCollection;
    Button Load, Request, Send,  btnOne, btnTwo,PaymentAddress;
    TextView LayoutDealOffer;
    LinearLayout layoutDeal1, layoutDeal2;
    public ProgressDialog prgDialog;
    private FragmentTransaction ft;
	String opStatus, result, respParams,cBsType;

    HashMap<String, String> map = null;
    String DEALDESCRIPTION, COUPONCODE, DEALSTATUS, DEALDAYS, DEALID, DESCRIPTION, DEALNAME, imagePath, txn_amount;

    TextView dealTitle1,dealTitle2,dealDetail1, dealDetail2,dealAmount1, dealAmount2;

    public static String DealDescription = "DESCRIPTION";
    public static String DealName = "DEALNAME";
    public static String DealAmount = "txn_amount";
    public static String DealImage = "imagePath";
    public static String TAG = "Deals";
    public static String TAG1 = "Wallet";

    public static String DealDescStatus = "DEALDESCRIPTION";
    public static String DealCouponCode = "COUPONCODE";
    public static String DealStatus = "DEALSTATUS";
    public static String DealDays = "DEALDAYS";
    public static String DealID = "DEALID";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Finfinity");
        DashBoardWallet.fragmentID=0;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View rootView = inflater.inflate(R.layout.wallet, container, false);
 /* Wallet DashBoard code starts here */
        LayoutDealOffer = (TextView)rootView.findViewById(R.id.layoutDeal);
        layoutDeal1 = (LinearLayout)rootView.findViewById(R.id.layoutDeal1);
        layoutDeal2 = (LinearLayout)rootView.findViewById(R.id.layoutDeal2);

        mChart = (PieChart) rootView.findViewById(R.id.chart1);
        Load=(Button)rootView.findViewById(R.id.btnLoad);
        Request=(Button)rootView.findViewById(R.id.btnRequest);
        Send=(Button)rootView.findViewById(R.id.btnSend);
        PaymentAddress=(Button)rootView.findViewById(R.id.paymentAddress);
        prgDialog = new ProgressDialog(getActivity());
        dealDataCollection = new ArrayList<HashMap<String, String>>();

        mChart.setUsePercentValues(true);

        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        // tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //  mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));


        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);

        mChart.setTransparentCircleColor(R.color.ColorPrimary);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(50f);


        mChart.setTransparentCircleRadius(55f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);
        appPref.getInstance().Initialize(getActivity());
        cbsType = appPref.getInstance().getString("cBsType");
        userMobileNo = appPref.getInstance().getString("user_mobile");
        DeviceId = appPref.getInstance().getString("Device_Id");
        System.out.println("SharedPreference Details In TransactionHistory Fragment" + userMobileNo + cbsType);


        dealTitle1 = (TextView)rootView.findViewById(R.id.textDealType1);
        dealTitle2 = (TextView)rootView.findViewById(R.id.textDealType2);
        dealAmount1 = (TextView)rootView.findViewById(R.id.textDealAmount1);
        dealAmount2 = (TextView)rootView.findViewById(R.id.textDealAmount2);
        dealDetail1 = (TextView)rootView.findViewById(R.id.textDealDetail1);
        dealDetail2 = (TextView)rootView.findViewById(R.id.textDealDetail2);



        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(2f);
        l.setYOffset(10f);
        LayoutDealOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWalletOffers callDealOffer = new mWalletOffers();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
              /*  for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }*/
                ft.replace(R.id.frame_container, callDealOffer);
                //ft.addToBackStack("fragBack");
                ft.commit();
            }
        });

        layoutDeal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(DealDescription, dealDataCollection.get(0).get(DealDescription));
                b.putString(DealName, dealDataCollection.get(0).get(DealName));
                b.putString(DealAmount, dealDataCollection.get(0).get(DealAmount));
                b.putString(DealImage, dealDataCollection.get(0).get(DealImage));
                b.putString(DealDescStatus, dealDataCollection.get(0).get(DealDescStatus));
                b.putString(DealCouponCode, dealDataCollection.get(0).get(DealCouponCode));
                b.putString(DealStatus, dealDataCollection.get(0).get(DealStatus));
                b.putString(DealDays, dealDataCollection.get(0).get(DealDays));
                b.putString(DealID, dealDataCollection.get(0).get(DealID));
                fragmentManager = getFragmentManager();
                FragmentTransaction ft2 = fragmentManager.beginTransaction();
                /*for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }*/
                mWalletOffersDetail offerDetail = new mWalletOffersDetail();
                ft2.replace(R.id.frame_container, offerDetail);
                // ft2.addToBackStack("optional tag");
                offerDetail.setArguments(b);
                ft2.commit();
            }
        });

        layoutDeal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(DealDescription, dealDataCollection.get(1).get(DealDescription));
                b.putString(DealName, dealDataCollection.get(1).get(DealName));
                b.putString(DealAmount, dealDataCollection.get(1).get(DealAmount));
                b.putString(DealImage, dealDataCollection.get(1).get(DealImage));
                b.putString(DealDescStatus, dealDataCollection.get(1).get(DealDescStatus));
                b.putString(DealCouponCode, dealDataCollection.get(1).get(DealCouponCode));
                b.putString(DealStatus, dealDataCollection.get(1).get(DealStatus));
                b.putString(DealDays, dealDataCollection.get(1).get(DealDays));
                b.putString(DealID, dealDataCollection.get(1).get(DealID));
                fragmentManager = getFragmentManager();
                FragmentTransaction ft2 = fragmentManager.beginTransaction();
               /* for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }*/
                mWalletOffersDetail offerDetail = new mWalletOffersDetail();
                ft2.replace(R.id.frame_container, offerDetail);
                // ft2.addToBackStack("optional tag");
                offerDetail.setArguments(b);
                ft2.commit();
            }
        });

        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               LoadDialogFragment dFragment = new LoadDialogFragment();
                // Show DialogFragment
                fragmentManager = getFragmentManager();
                dFragment.show(fragmentManager, "Dialog Fragment");

            }
        });
        Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestDialogFragment dFragment = new RequestDialogFragment();
                // Show DialogFragment
                fragmentManager = getFragmentManager();
                dFragment.show(fragmentManager, "Dialog Fragment");
               // RequestDialog();
            }
        });
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // sendDialog();
                /*This is Dialog Calling In Fragment*/
                SendDialogFragment dFragment = new SendDialogFragment();
                // Show DialogFragment
                fragmentManager = getFragmentManager();
                dFragment.show(fragmentManager, "Dialog Fragment");


                /*AlertDialog Fragment calling on Fragment*/
              /*  SendAlertDialogFragment alertdFragment = new SendAlertDialogFragment();
                // Show Alert DialogFragment
                alertdFragment.show(fragmentManager, "Alert Dialog Fragment");*/
            }
        });
        PaymentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paymentAddressDialog dFragment =new paymentAddressDialog();
                fragmentManager=getFragmentManager();
                dFragment.show(fragmentManager,"Dilaog Fragment");
              /*  StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append(DEEPLINKING_URL_BASE).append("?")
                        .append("pa").append("=").append("payeeVpa").append("&")
                        .append("pn").append("=").append("payeeVpa").append("&")
                        .append("mc").append("=").append("payeeMcc").append("&")
                        .append("ti").append("=").append("txnId").append("&")
                        .append("tr").append("=").append("txnRef").append("&")
                        .append("tn").append("=").append("txnNote").append("&")
                        .append("am").append("=").append("payeeAmt").append("&")
                        .append("cu").append("=").append("payeeCur").append("&")
                        .append("appid").append("=").append("payeeAppId").append("&")
                        .append("appname").append("=").append("payeeAppName");
                String deepLinkUrl = urlBuilder.toString();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deepLinkUrl));
                String title = "Pay with";
// Create intent to show chooser. It will display the list of available PSP apps (which have the same url in
// the maifest)
                Intent chooser = Intent.createChooser(intent, title);
// Verify the intent will resolve to at least one activity
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(chooser, 1);


                } */
            }
        });

        //SERVICE CALL MUST BE AFTER ACTIVITY PREPARATION
        new LoadGraphData().execute();
        new LoadWalletBalance().execute();
        new LoadDeals().execute();
        return rootView;
    }


  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent responseIntent) {
        super.onActivityResult(requestCode, resultCode, responseIntent);
        if (requestCode == 1 ) {
            // extract the response code and other fields from the received intent
           System.out.println("responseIntent "+responseIntent);
        }
    }*/

    /* Wallet chart Methodes implementations */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarX.getProgress() + 1));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        System.out.println("GraphSize mobileee "+GraphType.size());
//       for (int i = 0; i <GraphType.size(); i++) {
//            yVals1.add(new Entry(Float.parseFloat(GraphType.get(i)), i));
//        }


        for (int i = 0; i < count ; i++) {

            System.out.println("GraphSize namee "+GraphPer.get(i));
            //yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));//pass percent values from service.
            yVals1.add(new Entry(Float.parseFloat(GraphPer.get(i)), i));
            // yVals1.add(new Entry(29.3f,i));
            // yVals1.add(new Entry(39.2f,i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(GraphType.get(i % GraphType.size()));

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2f);

        dataSet.setSelectionShift(10f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Wallet Balance "+WalletBalance);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 7, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 7, s.length() - 7, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    private class LoadGraphData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String resp = insertServiceParameter();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null)
            {
                Log.d(TAG1, "Data from Server"+s);

            }else
            {
               Log.d(TAG1,"No Data from Server");
                prgDialog.dismiss();
            }

            if (s.equals("") || s == null) {
               // Toast.makeText(Wallet.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                Log.d(TAG1,"Unable to reach server");
                prgDialog.dismiss();
                return;
            } else {
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);
                    JSONObject responseParams = mainObj.getJSONObject("set");
                    JSONObject TotalListCount=mainObj.getJSONObject("responseParameterMW");
                    System.out.println("TotalListCount of data " + TotalListCount);
                    String TotalCount=TotalListCount.getString("totalListCount");
                    int TotalCountData=Integer.parseInt(TotalCount);

                    System.out.println("TotalCountDataggg " + TotalCountData);
                    DataSet = responseParams.getJSONArray("records");
                    GraphType = new ArrayList<String>();
                    GraphPer = new ArrayList<String>();

                    for (int i = 0; i < DataSet.length(); i++) {
                        JSONObject DataSetObject = DataSet.getJSONObject(i);
                        graphFieldtype = DataSetObject.getString("GRAPHFIELDSTYPE");
                        System.out.println("my field" + graphFieldtype);
                        GraphType.add(graphFieldtype);
                        System.out.println("graphFieldtype ghgh" + GraphType);
                        graphFieldPer = DataSetObject.getString("GRAPHPERCENT");
                        GraphPer.add(graphFieldPer);
                        System.out.println("graphFieldPer ghgh" + GraphPer);

                    }
                    setData(TotalCountData, 100);

                } catch (Exception e) {

                }
            }
        }
    }

    private String insertServiceParameter() {
        URL url = null;
        String resp = "";

        try {
            json = new JSONObject();
            json.put("entityId", "QRYS");
            json.put("actionId", "ENQUIRY");
            json.put("subActionId", "GETPERCENTAGEFORGRAPH");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");

            jsonInner.put("cbsType", cbsType);

            jsonInner.put("MobileNo", userMobileNo);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);

            System.out.println("resp of wallet " + resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    private class LoadWalletBalance extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String resp = insertWalletMoney();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null)
            {
                Log.d(TAG1, "Data from Server"+s);
            }else
            {
                Log.d(TAG1,"No Data from Server");
                prgDialog.dismiss();
            }

            if (s.equals("") || s == null) {
                // Toast.makeText(Wallet.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                Log.d(TAG1, "Unable to reach server");
                prgDialog.dismiss();
                return;
            }
            else
            {
                JSONObject mainObj = null;
                try{

                    mainObj = new JSONObject(s);
                    JSONObject responseParams = mainObj.getJSONObject("responseParameterMW");
                    WalletBalance=responseParams.getString("WalletAmount");
                    WalletPoint=responseParams.getString("WalletPoints");

                    System.out.println("resp of WalletBalance" + WalletBalance);

                }catch(Exception e)
                {

                }
                mChart.setCenterText(generateCenterSpannableText());
            }
        }
    }

    private String insertWalletMoney() {
        URL url = null;
        String resp = "";

        try {
            json = new JSONObject();
            json.put("entityId", "QRYS");
            json.put("actionId", "ENQUIRY");
            json.put("subActionId", "QRYSWALLETPOINTS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("DeviceId",DeviceId);
            jsonInner.put("cbsType", cbsType);

            jsonInner.put("MobileNo", userMobileNo);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);

            System.out.println("resp of wallet money" + resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    class LoadDeals extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, " Test 1 ");
            String resp = insertServiceParameter();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userMobileNo = appPref.getInstance().getString("user_mobile");
            userAccountNo = appPref.getInstance().getString("user_mobile");
            Log.d("Mobile No ", " " + userMobileNo);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            prgDialog.dismiss();
            if(s!=null)
            {
                Log.d(TAG1, "Data from Server"+s);
            }else
            {
                Log.d(TAG1,"No Data from Server");
                prgDialog.dismiss();
            }

            if (s.equals("") || s == null) {
                // Toast.makeText(Wallet.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                Log.d(TAG1, "Unable to reach server");
                prgDialog.dismiss();
                return;
            } else {
                prgDialog.dismiss();
                Log.d(TAG, " Test 2.0 ");
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);
                    String resp = mainObj.getString("responseParameterMW");
                    JSONObject respObjforstatus = new JSONObject(resp);
                    opStatus = respObjforstatus.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObjforstatus.getString("resstatus");
                    Log.d("result", result);
                    if(result.equals("01") || opStatus.equals("01")){
                        Toast.makeText(getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                        return;
                    }
                    respParams = mainObj.getString("listofDataset");
                    Log.d(TAG, " respParams " + respParams);
                    JSONArray temp = new JSONArray(respParams);
                    if(temp.length() <= 0){
                        Toast.makeText(getActivity(), "No Deals found.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONObject respObj = new JSONObject(temp.getJSONObject(0).toString());
                    String firstNode = respObj.getString("setname");
                    String secondNode = respObj.getString("records");
                    Log.d(TAG, " setName " + firstNode);
                    Log.d(TAG, " records " + secondNode);

                    String totallist = respObjforstatus.getString("totalListCount");
                    Log.d("totallist", totallist);
                    int l = Integer.parseInt(totallist);
                    Log.d("length", " " + l);


                    /*Read All Bank Names and */
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = respObj.optJSONArray("records");
                    Log.d("jsonArray.length()", " jsonArray.length() " + jsonArray.length());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        map = new HashMap<String, String>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        DESCRIPTION = jsonObject.getString("DESCRIPTION");
                        DEALNAME = jsonObject.getString("DEALNAME");
                        txn_amount = jsonObject.getString("txn_amount");
                        imagePath = jsonObject.getString("imagePath");
                        DEALDESCRIPTION = jsonObject.getString("DEALDESCRIPTION");
                        COUPONCODE = jsonObject.getString("COUPONCODE");
                        DEALSTATUS  = jsonObject.getString("DEALSTATUS");
                        DEALDAYS = jsonObject.getString("DEALDAYS");
                        DEALID = jsonObject.getString("DEALID");

                        Log.d("DEALNAME",DEALNAME);

                        map.put(DealDescription, DESCRIPTION);
                        map.put(DealName, DEALNAME);
                        map.put(DealAmount, txn_amount);
                        map.put(DealImage, imagePath);
                        map.put(DealDescStatus, DEALDESCRIPTION);
                        map.put(DealCouponCode, COUPONCODE);
                        map.put(DealStatus, DEALSTATUS);
                        map.put(DealDays, DEALDAYS);
                        map.put(DealID, DEALID);

                        dealDataCollection.add(map);
                    }

                    if ( dealDataCollection.size() > 0 ){
                        dealTitle1.setText(dealDataCollection.get(0).get(DealName));
                        dealAmount1.setText(dealDataCollection.get(0).get(DealAmount));
                        dealDetail1.setText(dealDataCollection.get(0).get(DealDescription));
                    }
                    if (dealDataCollection.size() > 1){
                        dealTitle2.setText(dealDataCollection.get(1).get(DealName));
                        dealAmount2.setText(dealDataCollection.get(1).get(DealAmount));
                        dealDetail2.setText(dealDataCollection.get(1).get(DealDescription));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("dealDataPassing",dealDataCollection.toString());

            }
        }

        private String insertServiceParameter() {
            URL url = null;
            String resp = "";
            Log.d(TAG, " Test 1.0 ");
            try {
                json = new JSONObject();
                json.put("entityId", "QRYS");
                json.put("actionId", " ");
                json.put("subActionId", "QRYSOFFERSDEALS");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("DEVICE_BUILD", "1");
                jsonInner.put("cbsType",cbsType);
                jsonInner.put("accountno", userAccountNo);//userAccountNo);
                jsonInner.put("DEALOFFER", "D");
                jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
                jsonInner.put("city", "Mumbai");
                jsonInner.put("MobileNo", userMobileNo);
                json.put("map", jsonInner);
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }
    }

}
