package com.djunicode.queuingapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ruturaj on 23-02-2018.
 */

public class QueueReceiver extends BroadcastReceiver {

  private ApiInterface apiInterface;
  private ArrayList<String> queueList;

  @Override
  public void onReceive(final Context context, final Intent intent) {
    apiInterface = ApiClient.getClient().create(ApiInterface.class);
    Call<StudentQueue> call = apiInterface.getUpdatedQueue(1);
    call.enqueue(new Callback<StudentQueue>() {
      @Override
      public void onResponse(Call<StudentQueue> call, Response<StudentQueue> response) {
        queueList = response.body().getItems();
        Log.e("QueueItems", queueList.toString());
        intent.putStringArrayListExtra("queueList", queueList);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
      }

      @Override
      public void onFailure(Call<StudentQueue> call, Throwable t) {

      }
    });
    Toast.makeText(context, "Receiver is running", Toast.LENGTH_SHORT).show();
  }
}
