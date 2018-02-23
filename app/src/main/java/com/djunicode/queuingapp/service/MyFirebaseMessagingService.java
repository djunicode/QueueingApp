package com.djunicode.queuingapp.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.djunicode.queuingapp.activity.MainActivity;
import com.djunicode.queuingapp.activity.StudentScreenActivity;
import com.djunicode.queuingapp.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ruturaj on 20-02-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
  private NotificationUtils notificationUtils;
  public static final String PUSH_NOTIFICATION = "push_notification";

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.e("From: ", remoteMessage.getFrom());

    if(remoteMessage == null){
      return;
    }

    if(remoteMessage.getNotification() != null){
      Log.e("Notification Body", remoteMessage.getNotification().getBody());
      handleNotification(remoteMessage.getNotification().getBody());
    }

    if(remoteMessage.getData().size() > 0){
      Log.e("Data Payload", remoteMessage.getData().toString());

      try{
        JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
        handleDataMessage(jsonObject);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private void handleDataMessage(JSONObject jsonObject) {
    Log.e("Push JSON", jsonObject.toString());

    try{
      JSONObject data = jsonObject.getJSONObject("data");
      String title = data.getString("title");
      String message = data.getString("message");
      boolean isBackground = data.getBoolean("is_background");
      String timestamp = data.getString("timestamp");
      JSONObject payload = data.getJSONObject("payload");


      Log.e("title: ", title);
      Log.e("message: ", message);
      Log.e("isBackground: ", "" + isBackground);
      Log.e("payload: ", payload.toString());
      Log.e("timestamp: ", timestamp);

      if(!NotificationUtils.isAppInBackground(getApplicationContext())){
        Intent intent = new Intent(PUSH_NOTIFICATION);
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      } else {
        Intent intent = new Intent(getApplicationContext(), StudentScreenActivity.class);
        intent.putExtra("message", message);
        showNotification(getApplicationContext(), title, message, timestamp, intent);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void showNotification(Context context, String title, String message,
      String timestamp, Intent intent) {
    notificationUtils = new NotificationUtils(context);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notificationUtils.showNotification(title, message, timestamp, intent);
  }

  private void handleNotification(String body) {
    if(!NotificationUtils.isAppInBackground(getApplicationContext())){
      Intent intent = new Intent(PUSH_NOTIFICATION);
      intent.putExtra("message", body);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
  }
}
