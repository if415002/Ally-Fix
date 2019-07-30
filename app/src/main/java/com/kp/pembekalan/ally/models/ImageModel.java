package com.kp.pembekalan.ally.models;

import com.google.gson.annotations.SerializedName;

public class ImageModel {
    @SerializedName( "id" )
    private int id;
    @SerializedName( "user_id" )
    private int user_id;
    @SerializedName( "filename" )
    private String filename;
    @SerializedName( "created" )
    private int created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }
}
