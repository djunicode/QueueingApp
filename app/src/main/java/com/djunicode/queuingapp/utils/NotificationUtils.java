package com.djunicode.queuingapp.utils;


import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.text.TextUtils;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.activity.StudentScreenActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ruturaj on 20-02-2018.
 */

public class NotificationUtils {

  private Context context;
  private final static int NOTIFICATION_ID = 1000;

  public NotificationUtils(Context context) {
    this.context = context;
  }

  public void showNotification(String title, String message, String timestamp, Intent intent) {
    if (TextUtils.isEmpty(message)) {
      return;
    }

    final int icon = R.mipmap.ic_launcher_round;
    intent = new Intent(context, StudentScreenActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    final PendingIntent pendingIntent = PendingIntent
        .getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    final NotificationCompat.Builder builder = new Builder(context);
    showSmallNotification(builder, icon, title, message, timestamp, pendingIntent);
  }

  private void showSmallNotification(Builder builder, int icon, String title, String message,
      String timestamp, PendingIntent pendingIntent) {
    NotificationCompat.InboxStyle inboxStyle = new InboxStyle();
    inboxStyle.addLine(message);

    Notification notification = builder
        .setSmallIcon(icon)
        .setTicker(title)
        .setWhen(System.currentTimeMillis())
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentIntent(pendingIntent)
        .setStyle(inboxStyle)
        .setWhen(getTimeMilliSec(timestamp))
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
        .setContentText(message)
        .setPriority(Notification.PRIORITY_MAX)
        .setDefaults(DEFAULT_VIBRATE)
        .build();

    NotificationManager manager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(NOTIFICATION_ID, notification);
  }

  public static boolean isAppInBackground(Context context) {
    boolean isInBackground = true;
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (VERSION.SDK_INT > VERSION_CODES.KITKAT_WATCH) {
      List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager
          .getRunningAppProcesses();
      for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
        if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          for (String activeProcess : processInfo.pkgList) {
            if (activeProcess.equals(context.getPackageName())) {
              isInBackground = false;
            }
          }
        }
      }
    } else {
      List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
      ComponentName componentName = runningTaskInfos.get(0).topActivity;
      if (componentName.getPackageName().equals(context.getPackageName())) {
        isInBackground = false;
      }
    }

    return isInBackground;
  }

  public static void clearNotifications(Context context) {
    NotificationManager manager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    manager.cancelAll();
  }

  public static long getTimeMilliSec(String timestamp) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = format.parse(timestamp);
      return date.getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return 0;
  }
}
