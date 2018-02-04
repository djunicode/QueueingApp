package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ruturaj on 04-02-2018.
 */

public class UserEmailVerify {

  @SerializedName("name")
  private String name;
  @SerializedName("email")
  private String email;
  @SerializedName("password")
  private String password;
  @SerializedName("id")
  private Integer id;

  public UserEmailVerify(String name, String email, String password, Integer id){
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
