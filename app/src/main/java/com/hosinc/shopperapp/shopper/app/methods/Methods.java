package com.hosinc.shopperapp.shopper.app.methods;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.hosinc.shopperapp.shopper.app.UsGetStarted;


public class Methods {

    private Context mContext;

    public Methods(UsGetStarted usGetStarted) {

    }

    public boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result != PackageManager.PERMISSION_GRANTED;
    }

}
