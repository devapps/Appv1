package com.infra.qrys_wallet.Utils;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;


public class MWRequest {
    HttpURLConnection httpConnection = null;
    URL url;
    BufferedReader input;
    StringBuilder response;
    String  requestUrl;


    public String middleWareReq(JSONObject json) {
        try {


           if(Constants.changeURL==true)
           {
                 requestUrl = Constants.URL_UPI + URLEncoder.encode(json.toString());
           }
            else
           {
                 requestUrl = Constants.URL + URLEncoder.encode(json.toString());
           }

            System.out.println("requestUrl of MWRequest----------->" + requestUrl);
            url = new URL(requestUrl);

            response = new StringBuilder();


            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                String strLine = null;
                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                input.close();
            }

            System.out.println("response using URL--->" + response.toString());
          //  Toast.makeText(getApplicationContext(), "FIRST_TIME_USER" + Constants.FIRST_TIME_USER, Toast.LENGTH_LONG).show();



        } catch (SocketTimeoutException e) {
        } catch (ConnectTimeoutException e) {
        } catch (IOException e) {
            e.printStackTrace();
            //  prgDialog.dismiss();
            //Toast.makeText(getApplicationContext(),"Invalid Registration"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return response.toString();
    }
}
