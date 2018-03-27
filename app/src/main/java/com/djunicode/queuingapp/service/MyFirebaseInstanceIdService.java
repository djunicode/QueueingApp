package com.djunicode.queuingapp.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ruturaj on 20-02-2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

  public static final String REGISTRATION_COMPLETE = "registrationComplete";

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    String token = FirebaseInstanceId.getInstance().getToken();
    Log.e("Refreshed token", token);

    storeIdInPreferences(token);

    sendRegistrationToServer(token);

    Intent intent = new Intent(REGISTRATION_COMPLETE);
    intent.putExtra("token", token);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    storeIdInPreferences(token);
  }

  private void storeIdInPreferences(String token) {
    SharedPreferences preferences = getApplicationContext()
        .getSharedPreferences("com.djunicode.queuingapp", MODE_PRIVATE);
    preferences.edit().putString("regId", token).apply();
  }

  private void sendRegistrationToServer(final String token){
    Log.e("Token", token);
  }
}
