package com.djunicode.queuingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ruturaj on 04-02-2018.
 */

public class UserEmailVerify {

  @SerializedName("username")
  private String name;
  @SerializedName("email")
  private String email;
  @SerializedName("password")
  private String password;
  @SerializedName("id")
  private Integer id;
  @SerializedName("token")
  private String token;
  @SerializedName("valid")
  private Boolean valid;

  public UserEmailVerify(String name, String email, String password, Integer id, String token,
      Boolean valid) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.id = id;
    this.token = token;
    this.valid = valid;
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

  public String getToken() {
    return token;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  public Boolean getValid() {
    return valid;
  }
}
