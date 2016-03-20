package com.infra.qrys_wallet.Utils;

public class Constants {
    public static String TYPE_OF_READ;
    //  public static String URL = "http://172.25.1.203:8080/MobilityMiddleWare/middleware?inputData=";//For Internal Testing
//   public static String URL = "http://172.25.1.203:3535/MobilityMiddleWare/middleware?inputData=";//For Internal Testing
 //  public static String URL = "http://172.25.1.55:7070/MobilityMiddleWare/middleware?inputData=";//For Public Testing
 //   public static String URL = "http://172.25.1.55:9797/MobilityMiddleWare/middleware?inputData=";//Local on Mahendars System
    //  public static String URL = "http://180.92.165.205:4040/MobilityMiddleWare/middleware?inputData=";//UAT
    //public static String URL = "http://172.25.2.116:9797/MobilityMiddleWare/middleware?inputData=";
 //public static String URL_UPI = "http://115.248.230.180:8080/upi/Middleware/Services/Request";
    public static String URL_UPI = "http://172.25.1.19:8080/upi/Middleware/Services/Request";
   // public static String URL_UPI = "http://172.25.1.203:7070/upi-middleware/Middleware/Services/Request";
    public static String URL = "http://172.25.1.203:4040/MobilityMiddleWare/middleware?inputData=";
    public static String URL_UPI_CHEQUE = "http://172.25.1.203:7070/upi-middleware/Middleware/Services/Request";
    public static String URL_Notification = "http://172.25.1.203:4545/sendnotification?message=";
    public static String URL_UPI_TESTING = "http://103.14.161.149:12001/UpiService/ReqListAccount";
     // public static String URL_UPI = "http://14.141.164.226:7070/upi-middleware/Middleware/Services/Request";
     // public static String URL = "http://14.141.164.226:4040/MobilityMiddleWare/middleware?inputData=";
    public static boolean checkImage = false;
    public static boolean checkSQLiteInserted = false;
    public static boolean changeURL = false;
    public static String entityID_UPI="infrapsp";
    public static String pspName_UPI="mahb";
}