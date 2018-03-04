package com.djunicode.queuingapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.receiver.QueueReceiver;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListActivity extends AppCompatActivity {

  private ListView studentsList;
  private FloatingActionButton cancelFab;
  private Handler handler;
  private ArrayList<String> queueList;
  private ArrayAdapter<String> adapter;
  private ApiInterface apiInterface;
  private ProgressBar loadingIndicator;
  private TextView emptyQueueTextView;
  private Intent intent;
  private int queueId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_list);

    studentsList = (ListView) findViewById(R.id.studentsList);
    cancelFab = (FloatingActionButton) findViewById(R.id.cancelSubFab);
    loadingIndicator = (ProgressBar) findViewById(R.id.loadingIndicator);
    emptyQueueTextView = (TextView) findViewById(R.id.emptyQueueTextView);
    queueList = new ArrayList<>();
    handler = new Handler();
    intent = getIntent();
    queueId = intent.getIntExtra("id", 1);

    loadingIndicator.setVisibility(View.VISIBLE);
    startRepeatingTask();

    cancelFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(StudentListActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Stop the submission.")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    stopRepeatingTask();
                    finish();
                  }
                }).setNegativeButton("No", null).show();
      }
    });
  }

  private Runnable statusChecker = new Runnable() {
    @Override
    public void run() {
      updateStudentList();
      handler.postDelayed(statusChecker, 10000);
      Toast.makeText(StudentListActivity.this, "Queue Updated", Toast.LENGTH_SHORT).show();
    }
  };

  void startRepeatingTask() {
    statusChecker.run();
  }

  void stopRepeatingTask() {
    handler.removeCallbacks(statusChecker);
  }

  void updateStudentList() {
    apiInterface = ApiClient.getClient().create(ApiInterface.class);
    Call<StudentQueue> call = apiInterface.getUpdatedQueue(queueId);
    call.enqueue(new Callback<StudentQueue>() {
      @Override
      public void onResponse(Call<StudentQueue> call, Response<StudentQueue> response) {
        queueList = response.body().getItems();
        if(queueList.size() == 0){
          emptyQueueTextView.setVisibility(View.VISIBLE);
        } else {
          emptyQueueTextView.setVisibility(View.GONE);
        }
        Log.e("QueueItems", queueList.toString());
        adapter = new ArrayAdapter<String>(StudentListActivity.this, R.layout.list_row, queueList);
        studentsList.setAdapter(adapter);
        loadingIndicator.setVisibility(View.GONE);
        studentsList.setVisibility(View.VISIBLE);
      }

      @Override
      public void onFailure(Call<StudentQueue> call, Throwable t) {

      }
    });
  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Stop the submission.")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                stopRepeatingTask();
                finish();
              }
            }).setNegativeButton("No", null).show();
  }
}
