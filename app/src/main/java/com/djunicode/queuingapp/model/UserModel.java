package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhruv on 2/3/18.
 */

public class UserModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("username")
    private String name;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;

    public UserModel(String name, String email, String password, Integer id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return id;
    }


}
