package com.axovel.ecommerce.sweetschoice.model;

import java.util.ArrayList;

/**
 * Created by Umesh Chauhan on 24-12-2015.
 * Axovel Private Limited
 */
public class RequestResponse {
    public String getFrameworkVersion() {
        return frameworkVersion;
    }

    public void setFrameworkVersion(String frameworkVersion) {
        this.frameworkVersion = frameworkVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBannerImgUrl() {
        return bannerImgUrl;
    }

    public void setBannerImgUrl(String bannerImgUrl) {
        this.bannerImgUrl = bannerImgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // App/Framework Version Check
    private String frameworkVersion;
    private String appVersion;
    // Login
    private String userName;
    private String password;
    // Registration
    private String firstName;
    private String lastName;
    private String email;
    // Banner
    private String bannerImgUrl;
    // Products
    private String category;
    private  String cost;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public ArrayList<RequestResponse> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<RequestResponse> products) {
        this.products = products;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    private ArrayList<RequestResponse> products;
    private String productName;
    private String shortDesc;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    private String imgURL;
}
