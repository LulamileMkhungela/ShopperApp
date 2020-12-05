package com.hosinc.shopperapp.shopper.app.sendFeedback;

import javax.mail.Address;

public class InternetAddress extends Address {
    public InternetAddress(String email) {
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object address) {
        return false;
    }
}
