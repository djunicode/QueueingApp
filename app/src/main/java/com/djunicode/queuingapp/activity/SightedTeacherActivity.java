package com.djunicode.queuingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.model.LocationTeacher;
import com.djunicode.queuingapp.model.TeacherModel;
import com.djunicode.queuingapp.model.TeachersList;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SightedTeacherActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private Spinner subjectSpinner, teacherSpinner, floorSpinner, departmentSpinner, roomSpinner;
  private CardView updateTeacherLocationButton;
  private ArrayAdapter<String> subjectAdapter, teacherAdapter, adapter;
  private List<String> teachers;
  private ApiInterface apiInterface;
  private String name;
  int glo_id = 9, t_id, user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sighted_teacher);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
    teacherSpinner = (Spinner) findViewById(R.id.teacherSpinner);
    floorSpinner = (Spinner) findViewById(R.id.floorSpinner);
    departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
    roomSpinner = (Spinner) findViewById(R.id.roomSpinner);
    updateTeacherLocationButton = (CardView) findViewById(R.id.updateTeacherLocationButton);

    apiInterface = ApiClient.getClient().create(ApiInterface.class);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    String[] array1 = {"Select", "AOA", "COA", "DS"};
    String[] array2 = {"Select", "1", "2", "3", "4", "5", "6"};

    subjectAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_dropdown_item, array1);

    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array2);

    subjectSpinner.setAdapter(subjectAdapter);
    floorSpinner.setAdapter(adapter);
    roomSpinner.setAdapter(adapter);

    teacherSpinner.setEnabled(false);
    teacherSpinner.setAlpha(0.4f);
    floorSpinner.setEnabled(false);
    floorSpinner.setAlpha(0.4f);
    departmentSpinner.setEnabled(false);
    departmentSpinner.setAlpha(0.4f);
    roomSpinner.setEnabled(false);
    roomSpinner.setAlpha(0.4f);

    subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
          teachers = new ArrayList<>();
          Call<TeachersList> call = apiInterface
              .getTeachersList(subjectSpinner.getItemAtPosition(position).toString());
          call.enqueue(new Callback<TeachersList>() {
            @Override
            public void onResponse(Call<TeachersList> call, Response<TeachersList> response) {
              Log.e("queues/subject/", response.body().getTeachers().toString());
              teachers = response.body().getTeachers();
              teacherAdapter = new ArrayAdapter<String>(SightedTeacherActivity.this,
                  android.R.layout.simple_spinner_dropdown_item, teachers);
              teacherSpinner.setAdapter(teacherAdapter);
              teacherSpinner.setEnabled(true);
              teacherSpinner.setAlpha(1.0f);
            }

            @Override
            public void onFailure(Call<TeachersList> call, Throwable t) {

            }
          });
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    teacherSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        floorSpinner.setEnabled(true);
        floorSpinner.setAlpha(1.0f);
        name = teacherSpinner.getItemAtPosition(position).toString();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    floorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
          departmentSpinner.setEnabled(true);
          departmentSpinner.setAlpha(1.0f);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    departmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
          roomSpinner.setEnabled(true);
          roomSpinner.setAlpha(1.0f);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    updateTeacherLocationButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Call<LocationTeacher> call = apiInterface
            .sendTeacherLocation(Integer.parseInt(floorSpinner.getSelectedItem().toString()),
                departmentSpinner.getSelectedItem().toString(),
                roomSpinner.getSelectedItem().toString());
        call.enqueue(new Callback<LocationTeacher>() {
          @Override
          public void onResponse(Call<LocationTeacher> call, Response<LocationTeacher> response) {
            glo_id = response.body().getId();
            Log.i("Id", response.body().getId().toString());
            Log.i("Floor", response.body().getFloor().toString());
            Log.i("Department", response.body().getDepartment().toString());
            Log.i("Room", response.body().getRoom().toString());
            Log.i("Updated At", response.body().getUpdated_at().toString());
            updateLocation();
          }

          private void updateLocation() {
            if (name.equals("no name")) {
              Log.e("no name", "no name");
            } else {
              getTeacherId(name);
            }
          }

          @Override
          public void onFailure(Call<LocationTeacher> call, Throwable t) {
            if (t != null) {/* TeacherTimerTask timerTask = new TeacherTimerTask();
    Timer t = new Timer();
    t.schedule(timerTask, 6000,360000);*/
              int i = Log.i("info: ", t.getMessage());
            } else {
              Log.i("info: ", "failed");
            }
          }
        });
      }
    });
  }

  private void getTeacherId(String name) {

    Call<TeacherModel> call = apiInterface.getIdForTeacherFromName(name);
    call.enqueue(new Callback<TeacherModel>() {
      @Override
      public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
        t_id = response.body().getId();
        user = response.body().getUser();
        Log.e("T_Id", Integer.toString(t_id));
        Log.e("GId", Integer.toString(glo_id));
        Call<TeacherModel> call1 = apiInterface.updateTeachersLocation(t_id, glo_id, user);
        call1.enqueue(new Callback<TeacherModel>() {
          @Override
          public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {

          }

          @Override
          public void onFailure(Call<TeacherModel> call, Throwable t) {

          }
        });
      }

      @Override
      public void onFailure(Call<TeacherModel> call, Throwable t) {
        t_id = 0;
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return super.onOptionsItemSelected(item);
  }
}
