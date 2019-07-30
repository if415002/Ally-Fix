package com.kp.pembekalan.ally.models;

import com.google.gson.annotations.SerializedName;

public class ImageResponses {
    @SerializedName( "images" )
    private ImageModel image;

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }
}
