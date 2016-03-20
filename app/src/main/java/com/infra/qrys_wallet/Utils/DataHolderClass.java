package com.infra.qrys_wallet.Utils;

import java.util.ArrayList;

/**
 * Created by sandeep.devhare on 22-09-2015.
 */
public class DataHolderClass {
    private static DataHolderClass dataObject = null;
    private String individualPersonfName;
    private String individualPersonLname;
    private String deviceID;
    private String userMobileNo;
    private String UserID;
    private String individualPersonEmail;
    private String nBinNo;
    private String selectedBankName;
    private String cbsType;
    private String bankDiscription;
    private ArrayList<String> allContacts, allId;

    private DataHolderClass() {
        // left blank intentionally
    }

    public static DataHolderClass getInstance() {
        if (dataObject == null)
            dataObject = new DataHolderClass();
        return dataObject;
    }

    public String getCbsType() {
        return cbsType;
    }

    public void setCbsType(String cbsType) {
        this.cbsType = cbsType;
    }

    public String getBankDiscription() {
        return bankDiscription;
    }

    public void setBankDiscription(String bankDiscription) {
        this.bankDiscription = bankDiscription;
    }

    public String getIndividualPersonfName() {
        return individualPersonfName;
    }

    public void setIndividualPersonfName(String individualPersonfName) {
        this.individualPersonfName = individualPersonfName;
    }

    public String getIndividualPersonLname() {
        return individualPersonLname;
    }

    public void setIndividualPersonLname(String individualPersonLname) {
        this.individualPersonLname = individualPersonLname;
    }

    public String getIndividualPersonEmail() {
        return individualPersonEmail;
    }

    public void setIndividualPersonEmail(String individualPersonEmail) {
        this.individualPersonEmail = individualPersonEmail;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getSelectedBankName() {
        return selectedBankName;
    }

    public void setSelectedBankName(String selectedBankName) {
        this.selectedBankName = selectedBankName;
    }

    public String getnBinNo() {
        return nBinNo;
    }

    public void setnBinNo(String nBinNo) {
        this.nBinNo = nBinNo;
    }

    public ArrayList<String> getAllContacts() {
        return allContacts;
    }

    public void setAllContacts(ArrayList<String> allContacts) {
        this.allContacts = allContacts;
    }

    public ArrayList<String> getAllId() {
        return allId;
    }

    public void setAllId(ArrayList<String> allId) {
        this.allId = allId;
    }
}