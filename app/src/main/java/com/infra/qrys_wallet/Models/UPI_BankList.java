package com.infra.qrys_wallet.Models;

/**
 * Created by vishvendu.palawat on 23-02-2016.
 */
public class UPI_BankList {

    String banklistupi;

    public UPI_BankList(String banklistupi){
        super();
        this.banklistupi=banklistupi;
    }

    public String getBanklistupi() {
        return banklistupi;
    }

    public void setBanklistupi(String banklistupi) {
        this.banklistupi = banklistupi;
    }


}
