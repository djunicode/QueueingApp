package com.djunicode.queuingapp.customClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.activity.StudentQueueActivity;
import com.djunicode.queuingapp.adapter.AvailableQueuesAdapter;
import com.djunicode.queuingapp.adapter.RecentsAdapter;
import com.djunicode.queuingapp.model.RecentEvents;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.model.TeacherCreateNew;
import com.djunicode.queuingapp.model.TeachersList;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Ruturaj on 09-01-2018.
 */

public class QueueDialogClass extends Dialog {

  private Activity activity;
  ApiInterface apiInterface;
  private SharedPreferences sp_student;
  private List<TeacherCreateNew> teachers;
  String sapid;
  public static int flag = 0;
  private RecyclerView recyclerView;
  private AvailableQueuesAdapter adapter;
  private ProgressBar loadingIndicator;
  private TextView noQueuesTextView;

  public QueueDialogClass(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.queue_dialog_layout);

    loadingIndicator = (ProgressBar) findViewById(R.id.loadingIndicator);
    recyclerView = (RecyclerView) findViewById(R.id.available_queues_recycler_view);
    noQueuesTextView = (TextView) findViewById(R.id.noQueuesTextView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
        DividerItemDecoration.VERTICAL));
    sp_student = getContext().getSharedPreferences("Student", MODE_PRIVATE);
    sapid = sp_student.getString("student_sapid", "missing");
    if (sapid.equals("missing")) {
      sapid = "90";
    }

    teachers = new ArrayList<>();

    apiInterface = ApiClient.getClient().create(ApiInterface.class);
    loadingIndicator.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.GONE);
    noQueuesTextView.setVisibility(View.GONE);
    final String teacherName = sp_student.getString("teacherName", "");
    Log.e("teacherName", teacherName);
    Call<List<TeacherCreateNew>> call = apiInterface.getParticularTeacherQueues(teacherName);
    call.enqueue(new Callback<List<TeacherCreateNew>>() {
      @Override
      public void onResponse(Call<List<TeacherCreateNew>> call, Response<List<TeacherCreateNew>> response) {
        try {
          teachers = response.body();
          if (teachers.size() == 0) {
            loadingIndicator.setVisibility(View.GONE);
            noQueuesTextView.setVisibility(View.VISIBLE);
          } else {
            adapter = new AvailableQueuesAdapter(getContext(), teachers);
            recyclerView.setAdapter(adapter);
            loadingIndicator.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<List<TeacherCreateNew>> call, Throwable t) {

      }
    });
  }
}
