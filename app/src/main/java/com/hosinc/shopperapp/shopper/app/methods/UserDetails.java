package com.hosinc.shopperapp.shopper.app.methods;

public class UserDetails {

    private String fullName;
    private String located;
    private String gender;
    private String dateOfBirth;
    private String imageUrl;
    private String cellphone;
    private String regToken;

    public UserDetails() {
    }

    public UserDetails(String userName, String fullName, String email, String password, String imageUrl, String registerAs, String regToken) {
        this.fullName = userName;
        this.located = fullName;
        this.gender = email;
        this.dateOfBirth = password;
        this.imageUrl = imageUrl;
        this.cellphone = registerAs;
        this.regToken = regToken;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLocated() {
        return located;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getRegToken() {
        return regToken;
    }


}
