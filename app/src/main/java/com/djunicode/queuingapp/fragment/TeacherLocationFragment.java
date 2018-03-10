package com.djunicode.queuingapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.customClasses.TeacherTimerTask;
import com.djunicode.queuingapp.data.QueuesDbHelper;
import com.djunicode.queuingapp.model.LocationTeacher;
import com.djunicode.queuingapp.model.RecentEvents;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.model.TeacherCreateNew;
import com.djunicode.queuingapp.model.TeacherModel;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;

import java.util.Timer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherLocationFragment extends Fragment {

  private Spinner floorSpinner, departmentSpinner, roomSpinner;
  private FloatingActionButton locationUpdateFab;
  public static boolean locationUpdated = false;
  public static String locationString;
  int glo_id = 9;
  private ApiInterface apiInterface;
  private String room, dept, name, toTime, fromTime, avgTime, subject2;
  private Integer floor;
  private Integer t_id, user, tempId, noOfStudents2, size;
  private SQLiteDatabase db;
  private Integer queueId, teacherId, teacherUserID;
  private SharedPreferences sp, preferences;
  private ProgressDialog progressDialog;

  public TeacherLocationFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_teacher_location, container, false);
    apiInterface = ApiClient.getClient().create(ApiInterface.class);

   /* TeacherTimerTask timerTask = new TeacherTimerTask();
    Timer t = new Timer();
    t.schedule(timerTask, 6000,360000);*/

    String[] array = {"Select", "1", "2", "3", "4", "5", "6"};
    final String[] array2 = {"Computer", "IT"};
    final String[] array3 = {"C1", "C2", "C3", "L1", "L2", "L3", "L4", "L5", "L6", "Staff Lounge"};

    final Bundle extras = getArguments();

    floorSpinner = (Spinner) view.findViewById(R.id.floorSpinner);
    departmentSpinner = (Spinner) view.findViewById(R.id.departmentSpinner);
    roomSpinner = (Spinner) view.findViewById(R.id.roomSpinner);
    locationUpdateFab = (FloatingActionButton) view.findViewById(R.id.locationUpdateFab);

    final QueuesDbHelper dbHelper = new QueuesDbHelper(getContext());
    db = dbHelper.getWritableDatabase();
    sp = getContext().getSharedPreferences("Teacher", MODE_PRIVATE);
    name = sp.getString("teacher_name", "no name");
    teacherId = sp.getInt("teacherId", 0);

    preferences = getActivity()
        .getSharedPreferences("com.djunicode.queuingapp", Context.MODE_PRIVATE);
    teacherUserID = preferences.getInt("teacherUserID", 0);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_spinner_dropdown_item, array);

    floorSpinner.setAdapter(adapter);
    roomSpinner.setAdapter(adapter);

    departmentSpinner.setEnabled(false);
    departmentSpinner.setAlpha(0.4f);

    roomSpinner.setEnabled(false);
    roomSpinner.setAlpha(0.4f);

    locationUpdateFab.setEnabled(false);

    floorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
          floor = position;
          if (position == 6) {
            departmentSpinner.setAdapter(
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                    array2));
          }
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
        dept = departmentSpinner.getItemAtPosition(position).toString();
        if (dept.equals("Computer")) {
          roomSpinner.setAdapter(
              new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                  array3));
        }
        roomSpinner.setEnabled(true);
        roomSpinner.setAlpha(1.0f);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    roomSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        room = roomSpinner.getItemAtPosition(position).toString();

        locationUpdateFab.setEnabled(true);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    locationUpdateFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        progressDialog = ProgressDialog.show(getContext(), "Updating location", "Please wait...");
        locationUpdated = true;
        locationString = "Floor-" + floorSpinner.getSelectedItem().toString() +
            " Dept-" + departmentSpinner.getSelectedItem().toString() + " Room-" +
            roomSpinner.getSelectedItem().toString();

        boolean flag = false;

        if (extras != null) {
          Toast.makeText(getContext(), "Set the location of submission.", Toast.LENGTH_SHORT)
              .show();
          flag = extras.getBoolean("Flag");
          final String subject = extras.getString("Subject");
          final String batch = extras.getString("Batch");
          final String from = extras.getString("From");
          final String to = extras.getString("To", "00:00:00");
          final int noOfStudents = extras.getInt("noOfStudents", 50);

          if (flag) {
            tempId = extras.getInt("tempId", -1);
            dbHelper.updateQueue(new RecentEvents(subject, batch, from, to, noOfStudents,
                locationString, tempId));
            Toast.makeText(getContext(), "Data updated!", Toast.LENGTH_SHORT).show();
                  /*extras.putInt("noOfStudents", noOfStudents);
                  extras.putString("fromTime", fromTime);
                  extras.putString("toTime", toTime);
                  extras.putString("subject", subjectSpinner.getSelectedItem().toString());
                  extras.putString("avgTime", "");
                  extras.putInt("size", 0);*/
            /*noOfStudents2 = extras.getInt("noOfStudents", -1);
            fromTime = extras.getString("fromTime", "");
            toTime = extras.getString("toTime", "");
            subject2 = extras.getString("subject", "");
            avgTime = extras.getString("avgTime", "");
            size = extras.getInt("size", -1);*/
            Log.e("Up_loca", Integer.toString(tempId));
            Log.e("Up_loca", Integer.toString(floor));
            Log.e("Up_loca", dept);
            Log.e("Up_loca", room);
            Call<LocationTeacher> call1 = apiInterface
                .sendQueueLocation(Integer.parseInt(floorSpinner.getSelectedItem().toString()),
                    departmentSpinner.getSelectedItem().toString(),
                    roomSpinner.getSelectedItem().toString());
            call1.enqueue(new Callback<LocationTeacher>() {
              @Override
              public void onResponse(Call<LocationTeacher> call,
                  Response<LocationTeacher> response) {
                Log.e("Location", response.body().getId().toString());
                int id = response.body().getId();
                if (response.isSuccessful()) {
                  Call<StudentQueue> call2 = apiInterface.editQueueLocation(tempId, noOfStudents,
                      from, to, subject, "", 0, id);
                  call2.enqueue(new Callback<StudentQueue>() {
                    @Override
                    public void onResponse(Call<StudentQueue> call,
                        Response<StudentQueue> response) {
                      try {
                        Log.e("Up_loca", response.body().getId().toString());
                      } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Up_location", e.getMessage());
                      }
                    }

                    @Override
                    public void onFailure(Call<StudentQueue> call, Throwable t) {

                    }
                  });
                }
              }

              @Override
              public void onFailure(Call<LocationTeacher> call, Throwable t) {

              }
            });

          } else {
            Call<LocationTeacher> call1 = apiInterface
                .sendQueueLocation(Integer.parseInt(floorSpinner.getSelectedItem().toString()),
                    departmentSpinner.getSelectedItem().toString(),
                    roomSpinner.getSelectedItem().toString());
            try {
              call1.enqueue(new Callback<LocationTeacher>() {
                @Override
                public void onResponse(Call<LocationTeacher> call3,
                    Response<LocationTeacher> response) {
                  int queueLocationId = response.body().getId();
                  Call<TeacherCreateNew> call = apiInterface
                      .sendSubmissionData(subject, from + ":00", to + ":00", noOfStudents, "",
                          queueLocationId);
                  call.enqueue(new Callback<TeacherCreateNew>() {

                    @Override
                    public void onResponse(Call<TeacherCreateNew> call,
                        Response<TeacherCreateNew> response) {
                      Bundle ids = new Bundle();
                      queueId = response.body().getId();
                      ids.putString("ids", Integer.toString(queueId));
                      dbHelper.addQueue(new RecentEvents(subject, batch, from, to, noOfStudents,
                          locationString, queueId));
                      Toast.makeText(getContext(), "Created new event!", Toast.LENGTH_SHORT).show();
                      Log.e("teacherId queueId", teacherId.toString() + " " + queueId.toString());
                      Call<TeacherCreateNew> call2 = apiInterface
                          .linkQueueToTeacher(teacherId, queueId);
                      call2.enqueue(new Callback<TeacherCreateNew>() {
                        @Override
                        public void onResponse(Call<TeacherCreateNew> call,
                            Response<TeacherCreateNew> response) {

                        }

                        @Override
                        public void onFailure(Call<TeacherCreateNew> call, Throwable t) {

                        }
                      });
                      if (response.isSuccessful()) {
                        Log.d("Response succ", response.body().toString());
                      } else {
                        Log.d("Response fail", response.errorBody().toString());
                      }
                    }

                    @Override
                    public void onFailure(Call<TeacherCreateNew> call, Throwable t) {
                      Toast.makeText(getContext(), "Submission failed", Toast.LENGTH_SHORT).show();
                    }
                  });
                }

                @Override
                public void onFailure(Call<LocationTeacher> call3, Throwable t) {

                }
              });
            } catch (NullPointerException e) {
              Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
              Log.e("Up_location", e.getMessage());
            }
          }
        } else {
          Log.e("Floor", floor.toString());
          Call<LocationTeacher> call = apiInterface.sendTeacherLocation(floor, dept, room);
          call.enqueue(new Callback<LocationTeacher>() {
            @Override
            public void onResponse(Call<LocationTeacher> call, Response<LocationTeacher> response) {
              try {
                glo_id = response.body().getId();
                Log.i("Id", response.body().getId().toString());
                Log.i("Floor", response.body().getFloor().toString());
                Log.i("Department", response.body().getDepartment().toString());
                Log.i("Room", response.body().getRoom().toString());
                Log.i("Updated At", response.body().getUpdated_at().toString());
              } catch (NullPointerException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Up_location", e.getMessage());
              }
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
      }
    });

    return view;
  }

  private void getTeacherId(String name) {

    /*Call<TeacherModel> call = apiInterface.getIdForTeacherFromName(name);
    call.enqueue(new Callback<TeacherModel>() {
      @Override
      public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
        t_id = response.body().getId();
        user = response.body().getUser();
        Log.e("T_Id", Integer.toString(t_id));
        Log.e("GId", Integer.toString(glo_id));
        Call<TeacherModel> call1 = apiInterface
            .updateTeachersLocation(teacherId, glo_id, teacherUserID);
        call1.enqueue(new Callback<TeacherModel>() {
          @Override
          public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
            Log.e("Dhruv", "Response successfull");
            Toast.makeText(getContext(), "Location Updated!", Toast.LENGTH_LONG).show();
          }

          @Override
          public void onFailure(Call<TeacherModel> call, Throwable t) {
            Log.e("Dhruv", "Response unsuccessfull");
          }
        });
      }

      @Override
      public void onFailure(Call<TeacherModel> call, Throwable t) {
        t_id = 0;
      }
    });*/
    Log.e("tId, glo_id, tUId",
        teacherId.toString() + " " + Integer.toString(glo_id) + " " + teacherUserID.toString());
    Call<TeacherModel> call1 = apiInterface
        .updateTeachersLocation(teacherId, glo_id, teacherUserID);
    call1.enqueue(new Callback<TeacherModel>() {
      @Override
      public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
        Log.e("Dhruv", "Response successfull");
        Toast.makeText(getContext(), "Location Updated!", Toast.LENGTH_LONG).show();
      }

      @Override
      public void onFailure(Call<TeacherModel> call, Throwable t) {
        Log.e("Dhruv", "Response unsuccessfull");
      }
    });
  }
}