package com.example.codyadmin.Model;

public class Statement {
    private String description;
    private String category;
    private String imageUrl;
    private boolean isActive;
    private String pictureName;
    private int intensityLevel;

    public Statement() {
        // Default constructor required for calls to DataSnapshot.getValue(Statement.class)
    }

    public Statement(String description, String category, String imageUrl, boolean isActive, String pictureName, int intensityLevel) {
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.pictureName = pictureName;
        this.intensityLevel = intensityLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName){
        this.pictureName = pictureName;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getIntensityLevel() {
        return intensityLevel;
    }

    public void setIntensityLevel(int intensityLevel) {
        this.intensityLevel = intensityLevel;
    }
}
