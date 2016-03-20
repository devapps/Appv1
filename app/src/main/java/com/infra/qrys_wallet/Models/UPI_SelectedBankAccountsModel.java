package com.infra.qrys_wallet.Models;

/**
 * Created by vishvendu.palawat on 25-02-2016.
 */
public class UPI_SelectedBankAccountsModel {
    String AccountType;
    String IFSCCode;
    String AccountNo;


    public UPI_SelectedBankAccountsModel(String AccountType,String IFSCCode,String AccountNo){

        super();
        this.AccountType=AccountType;
        this.IFSCCode=IFSCCode;
        this.AccountNo=AccountNo;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String IFSCCode) {
        this.IFSCCode = IFSCCode;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }


}
