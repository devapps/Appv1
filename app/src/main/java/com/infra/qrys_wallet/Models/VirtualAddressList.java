package com.infra.qrys_wallet.Models;

/**
 * Created by vishvendu.palawat on 23-02-2016.
 */
public class VirtualAddressList {

    String virtualAddress;

    public VirtualAddressList(String virtualAddress){
        super();
        this.virtualAddress = virtualAddress;
    }

    public String getVirtualAddressList() {
        return virtualAddress;
    }

    public void setVirtualAddressList(String virtualAddress) {
        this.virtualAddress = virtualAddress;
    }
}
