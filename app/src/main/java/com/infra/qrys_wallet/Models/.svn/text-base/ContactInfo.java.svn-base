package com.infra.qrys_wallet.Models;
/**
 * Created by sandeep.devhare on 02-12-2015.
 */
import java.io.Serializable;

public class ContactInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public String phone, name, email;
    public int contactssid;
    // public Bitmap thumb;
   public byte[] thumb;

    public ContactInfo(int contactssid,String name, String phone, byte[] thumb) {
        this.contactssid = contactssid;
        this.phone = phone;
        this.name = name;
        this.thumb = thumb;

    }
  /* public ContactInfo(int contactssid,String phone, String name, String email) {
       this.phone = phone;
       this.name = name;
       this.email = email;
       this.contactssid = contactssid;
   }*/

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContactssid() {
        return contactssid;
    }

    public void setContactssid(int contactssid) {
        this.contactssid = contactssid;
    }

    public byte[] getThumb() {
        return thumb;
    }

    public void setThumb(byte[] thumb) {
        this.thumb = thumb;
    }
}

