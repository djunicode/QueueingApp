package com.djunicode.queuingapp.rest;


import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL_PC on 09-01-2018.
 */

public class ApiClient {

  private static Retrofit retrofit = null;

  public static Retrofit getClient () {
    retrofit = new Retrofit.Builder()
        .baseUrl("http://sahiljajodia01.pythonanywhere.com/")
        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
        .build();
    return retrofit;
  }
}
