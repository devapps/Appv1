package com.infra.qrys_wallet.Utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Created by vishvendu.palawat on 22-02-2016.
 */
public class MWRequest_json {


    HttpClient httpClient;
    HttpPost request;
    HttpResponse response;
    StringEntity jsonEntity;
    String responseData;
    Logger logger;

    public String serviceReq(JSONObject jObj){
        httpClient = new DefaultHttpClient();
        //logger = new Logger();
        try {

            //logger.addRecordToLog("url" + Constants.URL);



                Log.d("url for mahender", Constants.URL_UPI_TESTING);
                request = new HttpPost(Constants.URL_UPI_TESTING);


           // logger.addRecordToLog("jObj" + jObj.toString());


            jsonEntity = new StringEntity(jObj.toString());



           // logger.addRecordToLog("jsonEntity" + "afer jsonEntity");
            request.setHeader("content-type", "application/json");
            request.setHeader("Authorization", "Basic bWdzdXBpOmFkbWluQDEyMw==");
            request.setHeader("Accept", "application/json");

            request.setEntity(jsonEntity);
            Log.d("setEntity", "afer setEntity");
            System.out.println( "Request from android  "+request);
           // logger.addRecordToLog("setEntity"+ "afer setEntity");
            response = httpClient.execute(request);
            Log.d("response" , "afer response");
           // logger.addRecordToLog("response" + response);
            responseData = EntityUtils.toString(response.getEntity(), "utf-8");
            Log.d("Response" , responseData);

        } catch (Exception ex) {

            // handle exception here
            Log.d("error msg",ex.getMessage());
            //responseData="Could not reach server, Please check the connection.";
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseData;
    }


}
