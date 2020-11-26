package com.example.viewimages.model;

import java.io.Serializable;



public class ImageSearchRequest implements Serializable {

    private String query;
    private String imageType;
    private String imageCategory;
    private String imageOrder;
    private String orientation;

    private int minHeight;
    private int minWidth;

    private boolean isSafeSearchEnabled;

    private boolean isNewRequest = false;

    public ImageSearchRequest(String query, String imageType, String imageCategory, String imageOrder, int minHeight, int minWidth, boolean isVerticalOrientationEnabled, boolean isHorizontalOrientationEnabled, boolean isSafeSearchEnabled) {
        this.query = query;
        this.imageType = imageType;
        this.imageOrder = imageOrder;
        this.minHeight = minHeight;
        this.minWidth = minWidth;
        this.isSafeSearchEnabled = isSafeSearchEnabled;

        if (imageCategory.equals("default")) this.imageCategory = "";
        else this.imageCategory = imageCategory;

        if (isHorizontalOrientationEnabled && isVerticalOrientationEnabled) orientation = "all";
        else if (isHorizontalOrientationEnabled) orientation = "horizontal";
        else if (isVerticalOrientationEnabled) orientation = "vertical";

    }

    public ImageSearchRequest(String query) {
        this.query = query;
        imageType = "all";
        imageCategory = "";
        imageOrder = "popular";
        orientation = "all";
        minHeight = 0;
        minHeight = 0;
        isSafeSearchEnabled = false;
    }

    public ImageSearchRequest() {
        this.query = "";
        imageType = "all";
        imageCategory = "";
        imageOrder = "popular";
        orientation = "all";
        minHeight = 0;
        minHeight = 0;
        isSafeSearchEnabled = false;
    }

    @Override
    public String toString() {
        return "ImageSearchRequest{" +
                "query='" + query + '\'' +
                ", imageType='" + imageType + '\'' +
                ", imageCategory='" + imageCategory + '\'' +
                ", imageOrder='" + imageOrder + '\'' +
                ", orientation='" + orientation + '\'' +
                ", minHeight=" + minHeight +
                ", minWidth=" + minWidth +
                ", isSafeSearchEnabled=" + isSafeSearchEnabled +
                '}';
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }

    public String getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(String imageOrder) {
        this.imageOrder = imageOrder;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public boolean isSafeSearchEnabled() {
        return isSafeSearchEnabled;
    }

    public void setSafeSearchEnabled(boolean safeSearchEnabled) {
        isSafeSearchEnabled = safeSearchEnabled;
    }


    public boolean isNewRequest() {
        return isNewRequest;
    }

    public void setNewRequest(boolean newRequest) {
        isNewRequest = newRequest;
    }
}
