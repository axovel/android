package com.axovel.ecommerce.model;

/**
 * Created by Umesh Chauhan on 29-12-2015.
 * Axovel Private Limited
 */
public class NavDrawer {
    private String title;
    private String iconUrl;
    private String count = "0";
    // boolean to set visibility of the counter
    private boolean isCounterVisible = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public NavDrawer(){}

    public NavDrawer(String title, String iconUrl){
        this.title = title;
        this.iconUrl = iconUrl;
    }

    public NavDrawer(String title, String iconUrl, boolean isCounterVisible, String count){
        this.title = title;
        this.iconUrl = iconUrl;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public String getIconUrl(){
        return this.iconUrl;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIconUrl(String iconUrl){
        this.iconUrl = iconUrl;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
