package com.infra.qrys_wallet.Utils;

import com.infra.qrys_wallet.Models.PhoneBook_SelectUser;

import java.util.ArrayList;

/**
 * Created by sandeep.devhare on 04-12-2015.
 */
public class ContactHolderDesignPattern {
    private static ContactHolderDesignPattern dataObject = null;
    private String contactID;
    private String ContactName;
    private String ContactNumber;
    private byte[] contactImage;
   private ArrayList<PhoneBook_SelectUser> selectUsers;
    public ContactHolderDesignPattern() {

    }

    public static ContactHolderDesignPattern getInstance() {
        if (dataObject == null)
            dataObject = new ContactHolderDesignPattern();
        return dataObject;
    }

    public ArrayList<PhoneBook_SelectUser> getSelectUsers() {
        return selectUsers;
    }

    public void setSelectUsers(ArrayList<PhoneBook_SelectUser> selectUsers) {
        this.selectUsers = selectUsers;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public byte[] getContactImage() {
        return contactImage;
    }

    public void setContactImage(byte[] contactImage) {
        this.contactImage = contactImage;
    }
}
