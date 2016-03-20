package com.infra.qrys_wallet.Models;

import android.graphics.Bitmap;

/**
 * Created by sandeep.devhare on 24-11-2015.
 */
public class PendingNotification {
    String message;
    Bitmap thumb;
    public Bitmap getThumb() {
        return thumb;
    }

    public String getName() {
        return message;
    }

    public void setName(String message) {
        this.message = message;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }
}
