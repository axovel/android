package com.axovel.ecommerce.model;

import java.util.ArrayList;

/**
 * Created by Umesh Chauhan on 29-12-2015.
 * Axovel Private Limited
 */
public class NavDrawer {
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

    public String getIconUrl(){
        return this.iconUrl;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
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
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public ArrayList<NavDrawer> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(ArrayList<NavDrawer> subCategory) {
        this.subCategory = subCategory;
    }

    private ArrayList<NavDrawer> subCategory;
    private String subCategoryName;
}
