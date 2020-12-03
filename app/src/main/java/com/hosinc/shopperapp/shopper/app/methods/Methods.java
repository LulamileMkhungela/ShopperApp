package com.hosinc.shopperapp.shopper.app.methods;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;


public class Methods {

    private Context mContext;

    public boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result != PackageManager.PERMISSION_GRANTED;
    }

}
