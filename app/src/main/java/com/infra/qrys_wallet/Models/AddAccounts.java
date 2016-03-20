package com.infra.qrys_wallet.Models;

/**
 * Created by vishvendu.palawat on 04-11-2015.
 */
public class AddAccounts {
    String AddAccount_Name;
    String AddAccount_BankName;
    String AddAccount_AccountNo;

    public AddAccounts(String addAccount_Name, String addAccount_BankName, String addAccount_AccountNo) {
        super();
        this.AddAccount_Name = addAccount_Name;
        this.AddAccount_BankName = addAccount_BankName;
        this.AddAccount_AccountNo = addAccount_AccountNo;
    }

    public String getAddAccount_Name() {
        return AddAccount_Name;
    }

    public void setAddAccount_Name(String addAccount_Name) {
        AddAccount_Name = addAccount_Name;
    }

    public String getAddAccount_BankName() {
        return AddAccount_BankName;
    }

    public void setAddAccount_BankName(String addAccount_BankName) {
        AddAccount_BankName = addAccount_BankName;
    }

    public String getAddAccount_AccountNo() {
        return AddAccount_AccountNo;
    }

    public void setAddAccount_AccountNo(String addAccount_AccountNo) {
        AddAccount_AccountNo = addAccount_AccountNo;
    }


}
