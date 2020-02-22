package com.laioffer.usedbook.Entity;

public class SellerInfo {
    private Double longitude;
    private Double latitude;
    private String SellerId;
    private String userName;
    private String imageUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }
}
