package com.djunicode.queuingapp.fragment;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.activity.StudentListActivity;
import com.djunicode.queuingapp.activity.StudentQueueActivity;
import com.djunicode.queuingapp.activity.TeacherScreenActivity;
import com.djunicode.queuingapp.customClasses.ObjectSerializer;
import com.djunicode.queuingapp.data.QueuesDbHelper;
import com.djunicode.queuingapp.model.RecentEvents;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.model.TeacherCreateNew;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherSubmissionFragment extends Fragment {

  public static Spinner subjectSpinner, batchSpinner;
  private CardView fromTimePickerButton;
  private ImageButton studentsButton, timerButton;
  private FloatingActionButton createFab, startFab, cancelFab, createNewFab, editFab;
  private LinearLayout fabLL1, fabLL2, fabLL3, fabLL4;
  private CoordinatorLayout coordinatorLayout;
  private RelativeLayout relativeLayout;
  private Animation fabOpen, fabClose, rotateForward, rotateBackward;
  private Boolean isFabOpen, fromSelected, toSelected, studentsSelected;
  private String fromTime;
  private String toTime;
  private int noOfStudents;
  private int numberOfStudents;
  public static List<RecentEvents> recentEventsList = new ArrayList<>();
  public static BottomSheetFragment bottomSheetFragment;
  private Bundle globalArgs;
  private static final int SUBMISSION_NOTIFICATION_ID = 1000;
  private NotificationCompat.Builder notificationBuilder;
  private NotificationManager notificationManager;
  private ArrayList<String> subjects;
  private ApiInterface apiInterface;
  private Integer queueId, tempId;

  public TeacherSubmissionFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_teacher_submission, container, false);

    String[] array = {"Select", "A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4"};

    SharedPreferences preferences = getActivity()
        .getSharedPreferences("com.djunicode.queuingapp", Context.MODE_PRIVATE);

    final Bundle args = getArguments();

    isFabOpen = false;
    fromSelected = false;
    toSelected = false;
    studentsSelected = false;
    subjectSpinner = (Spinner) view.findViewById(R.id.subjectSpinner);
    batchSpinner = (Spinner) view.findViewById(R.id.batchSpinner);
    fromTimePickerButton = (CardView) view.findViewById(R.id.fromTimePickerButton);
    studentsButton = (ImageButton) view.findViewById(R.id.studentsButton);
    timerButton = (ImageButton) view.findViewById(R.id.timerButton);
    createFab = (FloatingActionButton) view.findViewById(R.id.createFab);
    createNewFab = (FloatingActionButton) view.findViewById(R.id.createNewFab);
    editFab = (FloatingActionButton) view.findViewById(R.id.editFab);
    coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.teacherCoordinatorLayout);
    relativeLayout = (RelativeLayout) view.findViewById(R.id.submissionRelativeLayout);
    fabLL3 = (LinearLayout) view.findViewById(R.id.fabLL3);
    fabLL4 = (LinearLayout) view.findViewById(R.id.fabLL4);
    fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
    fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
    rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
    rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
    apiInterface = ApiClient.getClient().create(ApiInterface.class);
    final QueuesDbHelper dbHelper = new QueuesDbHelper(getContext());

    if (args != null) {
      globalArgs = args;
      createFab.setImageResource(R.drawable.ic_upload);
    }

    try {
      subjects = (ArrayList<String>) ObjectSerializer.deserialize(
          preferences.getString("subjects", ObjectSerializer.serialize(new ArrayList<String>())));
    } catch (IOException e) {
      e.printStackTrace();
    }

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_spinner_dropdown_item, array);

    ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_list_item_1, subjects);

    subjectSpinner.setAdapter(subjectsAdapter);
    batchSpinner.setAdapter(adapter);

    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_submission);

    notificationBuilder = new NotificationCompat.Builder(getContext())
        .setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
        .setSmallIcon(R.drawable.ic_submission)
        .setLargeIcon(largeIcon)
        .setContentTitle("Submission started.")
        .setContentText("The submission for DLDA is started.")
        .setAutoCancel(true);
    Intent queueActivityIntent = new Intent(getContext(), StudentQueueActivity.class);
    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getContext());
    taskStackBuilder.addNextIntentWithParentStack(queueActivityIntent);
    PendingIntent resultPendingIntent = taskStackBuilder
        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    notificationBuilder.setContentIntent(resultPendingIntent);

    notificationManager = (NotificationManager)
        getContext().getSystemService(Context.NOTIFICATION_SERVICE);

    subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
          batchSpinner.setEnabled(true);
          batchSpinner.setAlpha(1.0f);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    fromTimePickerButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (batchSpinner.getSelectedItemPosition() != 0) {
          TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),

              new OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                  String hour, min;
                  if (hourOfDay <= 11 && minute <= 59) {
                    Snackbar.make(coordinatorLayout,
                        "Submission is from " + hourOfDay + ":" + minute + "am",
                        Snackbar.LENGTH_SHORT).show();
                    if (hourOfDay < 10) {
                      hour = "0" + Integer.toString(hourOfDay);
                    } else {
                      hour = Integer.toString(hourOfDay);
                    }
                    if (minute < 10) {
                      min = "0" + Integer.toString(minute);
                    } else {
                      min = Integer.toString(minute);
                    }
                    fromTime = hour + ":" + min;
                  } else {
                    if (hourOfDay == 12 && minute == 0) {
                      hour = Integer.toString(hourOfDay);
                      min = "0" + Integer.toString(minute);
                      Snackbar.make(coordinatorLayout,
                          "Submission is from " + hourOfDay + ":" + minute + "pm",
                          Snackbar.LENGTH_SHORT).show();
                      Snackbar.make(coordinatorLayout,
                          "Submission is from " + hourOfDay + ":" + minute + "pm",
                          Snackbar.LENGTH_SHORT).show();
                      fromTime =
                          hour + ":" + min;
                    } else {
                      hourOfDay -= 12;
                      if (hourOfDay < 10) {
                        hour = "0" + Integer.toString(hourOfDay);
                      } else {
                        hour = Integer.toString(hourOfDay);
                      }
                      if (minute < 10) {
                        min = "0" + Integer.toString(minute);
                      } else {
                        min = Integer.toString(minute);
                      }
                      Snackbar.make(coordinatorLayout,
                          "Submission is from " + hourOfDay + ":" + minute + "pm",
                          Snackbar.LENGTH_SHORT).show();
                      fromTime =
                          hour + ":" + min;
                    }
                  }
                  fromSelected = true;
                }
              },
              0, 0, false);
          timePickerDialog.show();
        } else {
          Toast.makeText(getContext(), "Please select the batch!", Toast.LENGTH_SHORT).show();
        }
      }
    });

    timerButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fromSelected) {
          TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
              new OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                  String hour, min;
                  if (hourOfDay <= 11 && minute <= 59) {
                    if (hourOfDay < 10) {
                      hour = "0" + Integer.toString(hourOfDay);
                    } else {
                      hour = Integer.toString(hourOfDay);
                    }
                    if (minute < 10) {
                      min = "0" + Integer.toString(minute);
                    } else {
                      min = Integer.toString(minute);
                    }
                    Snackbar.make(coordinatorLayout,
                        "Submission is till " + hourOfDay + ":" + minute + "am",
                        Snackbar.LENGTH_SHORT).show();
                    toTime = hour + ":" + min;
                  } else {
                    if (hourOfDay == 12 && minute == 0) {
                      hour = Integer.toString(hourOfDay);
                      min = "0" + Integer.toString(minute);
                      Snackbar.make(coordinatorLayout,
                          "Submission is till " + hourOfDay + ":" + minute + "pm",
                          Snackbar.LENGTH_SHORT).show();
                      toTime = hour + ":" + min;
                    } else {
                      hourOfDay -= 12;
                      if (hourOfDay < 10) {
                        hour = "0" + Integer.toString(hourOfDay);
                      } else {
                        hour = Integer.toString(hourOfDay);
                      }
                      if (minute < 10) {
                        min = "0" + Integer.toString(minute);
                      } else {
                        min = Integer.toString(minute);
                      }
                      Snackbar.make(coordinatorLayout,
                          "Submission is till " + hourOfDay + ":" + minute + "pm",
                          Snackbar.LENGTH_SHORT).show();
                      toTime = hour + ":" + min;
                    }
                  }
                  toSelected = true;
                }
              },
              0, 0, false);
          timePickerDialog.show();
        } else {
          Toast.makeText(getContext(), "Please select from time.", Toast.LENGTH_SHORT).show();
        }
      }
    });

    studentsButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fromSelected) {
          final EditText input = new EditText(getContext());
          input.setInputType(InputType.TYPE_CLASS_NUMBER);
          AlertDialog.Builder builder = new Builder(getContext());
          builder.setTitle("Enter the number of students");
          if (input.getParent() != null) {
            ((ViewGroup) input.getParent()).removeView(input);
            input.setText("");
          }
          builder.setView(input);
          builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              Toast.makeText(getContext(), input.getText().toString() + " students selected",
                  Toast.LENGTH_SHORT).show();
              noOfStudents = Integer.parseInt(input.getText().toString());
              studentsSelected = true;
            }
          });
          builder.setNegativeButton("CANCEL", null);
          builder.show();
        } else {
          Toast.makeText(getContext(), "Please select from time.", Toast.LENGTH_SHORT).show();
        }
      }
    });

    createFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (args != null) {
          Log.i("Position", Integer.toString(args.getInt("Position")));
          recentEventsList = dbHelper.getAllQueues();
          final RecentEvents event = recentEventsList.get(args.getInt("Position"));
          if (toSelected || studentsSelected) {
            /*AlertDialog.Builder builder = new Builder(getContext());
            builder.setMessage("Do you want to use the last location or set new location?")
                .setPositiveButton("LAST", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    if (!TeacherLocationFragment.locationUpdated) {
                      updateLocation(true);
                      String message = "No last location set! Please update your location.";
                      Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                      *//*recentEventsList
                          .set(args.getInt("Position"),
                              new RecentEvents(subjectSpinner.getSelectedItem().toString(),
                                  batchSpinner.getSelectedItem().toString(), fromTime, toTime,
                                  TeacherLocationFragment.locationString));*//*
                      dbHelper
                          .updateQueue(new RecentEvents(subjectSpinner.getSelectedItem().toString(),
                              batchSpinner.getSelectedItem().toString(), fromTime, toTime,
                              noOfStudents, TeacherLocationFragment.locationString,
                              event.getServerId()));
                      Toast.makeText(getContext(), "Data updated!", Toast.LENGTH_SHORT).show();
                      tempId = event.getServerId();
                      Log.e("Editing", Integer.toString(noOfStudents));
                      Log.e("Editing", fromTime);
                      Log.e("Editing", toTime);
                      Log.e("Editing", subjectSpinner.getSelectedItem().toString());
                      //Log.e("queueId", Integer.toString(queueId));
                      Call<StudentQueue> call = apiInterface
                          .editingQueue(tempId, noOfStudents, fromTime, toTime,
                              subjectSpinner.getSelectedItem().toString(),
                              "", noOfStudents);
                      call.enqueue(new Callback<StudentQueue>() {
                        @Override
                        public void onResponse(Call<StudentQueue> call,
                            Response<StudentQueue> response) {
                          Log.i("Subject returned is", response.body().getSubject().toString());
                        }

                        @Override
                        public void onFailure(Call<StudentQueue> call, Throwable t) {
                          Log.i("error", t.getMessage());
                        }
                      });
                    }
                    createFab.setImageResource(R.drawable.ic_add);
                  }
                })
                .setNegativeButton("NEW", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    tempId = event.getServerId();
                    updateLocation(true);
                    createFab.setImageResource(R.drawable.ic_add);
                  }
                });
            builder.show();*/
            tempId = event.getServerId();
            updateLocation(true);
            createFab.setImageResource(R.drawable.ic_add);
          } else {
            Toast.makeText(getContext(), "Please select all fields.", Toast.LENGTH_SHORT).show();
          }
        } else {
          animateFab();
        }
      }
    });

    createNewFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        animateFab();

        if (toSelected || studentsSelected) {
          /*AlertDialog.Builder builder = new Builder(getContext());
          builder.setMessage("Do you want to use the last location or set new location?")
              .setPositiveButton("LAST", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  if (!TeacherLocationFragment.locationUpdated) {
                    updateLocation(false);

                    String message = "No last location set! Please update your location.";
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                  } else {
                    *//*recentEventsList
                        .add(new RecentEvents(subjectSpinner.getSelectedItem().toString(),
                            batchSpinner.getSelectedItem().toString(), fromTime, toTime,
                            TeacherLocationFragment.locationString));*//*
                    Call<TeacherCreateNew> call = apiInterface
                        .sendSubmissionData(subjectSpinner.getSelectedItem().toString(),
                            fromTime + ":00", toTime + ":00", noOfStudents, "", 23);
                    call.enqueue(new Callback<TeacherCreateNew>() {

                      @Override
                      public void onResponse(Call<TeacherCreateNew> call,
                          Response<TeacherCreateNew> response) {
                        //Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                        Bundle ids = new Bundle();
                        queueId = response.body().getId();
                        ids.putString("ids", Integer.toString(queueId));
                        dbHelper
                            .addQueue(new RecentEvents(subjectSpinner.getSelectedItem().toString(),
                                batchSpinner.getSelectedItem().toString(), fromTime, toTime,
                                noOfStudents,
                                TeacherLocationFragment.locationString, queueId));
                        Toast.makeText(getContext(), "Created new event!", Toast.LENGTH_SHORT)
                            .show();
                        if (response.isSuccessful()) {
                          Log.d("Response succ", response.body().toString());
                        } else {
                          Log.d("Response fail", response.errorBody().toString());
                        }
                      }

                      @Override
                      public void onFailure(Call<TeacherCreateNew> call, Throwable t) {
                        Toast.makeText(getContext(), "Submission failed", Toast.LENGTH_SHORT)
                            .show();
                      }
                    });
                  }
                  createFab.setImageResource(R.drawable.ic_add);
                }
              })
              .setNegativeButton("NEW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  updateLocation(false);
                  createFab.setImageResource(R.drawable.ic_add);
                }
              });

          builder.show();*/
          updateLocation(false);
          createFab.setImageResource(R.drawable.ic_add);
        } else {
          Toast.makeText(getContext(), "Please select all fields.", Toast.LENGTH_SHORT).show();
        }
      }
    });

    editFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment
            .show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
      }
    });

    return view;
  }

  private void updateLocation(boolean flag) {
    Bundle extras = new Bundle();
    extras.putBoolean("Flag", flag);
    extras.putString("Subject", subjectSpinner.getSelectedItem().toString());
    extras.putString("Batch", batchSpinner.getSelectedItem().toString());
    extras.putString("From", fromTime);
    extras.putString("To", toTime);
    extras.putInt("noOfStudents", noOfStudents);
    if (flag) {
      extras.putInt("Position", globalArgs.getInt("Position"));
      extras.putInt("tempId", tempId);
    }

    FragmentTransaction transaction = getActivity().getSupportFragmentManager()
        .beginTransaction();

    TeacherLocationFragment locationFragment = new TeacherLocationFragment();
    locationFragment.setArguments(extras);

    TeacherScreenActivity.bottomNavigationView
        .setSelectedItemId(R.id.action_location);

    transaction.replace(R.id.containerLayoutTeacher, locationFragment);
    transaction.commit();
  }

  private void animateFab() {
    if (isFabOpen) {
      fabLL3.setVisibility(View.INVISIBLE);
      fabLL4.setVisibility(View.INVISIBLE);
      isFabOpen = false;
      createFab.setAnimation(rotateBackward);
      fabLL3.animate().translationY(0).alpha(0.0f);
      fabLL4.animate().translationY(0).alpha(0.0f);
      if (VERSION.SDK_INT >= 23) {
        relativeLayout.setForeground(new ColorDrawable(getResources().
            getColor(android.R.color.transparent)));
      }
    } else {
      fabLL3.setVisibility(View.VISIBLE);
      fabLL4.setVisibility(View.VISIBLE);
      isFabOpen = true;
      createFab.setAnimation(rotateForward);
      fabLL3.animate().translationY(-getResources().getDimension(R.dimen.standard_65)).alpha(1.0f);
      fabLL4.animate().translationY(-getResources().getDimension(R.dimen.standard_115)).alpha(1.0f);
      if (VERSION.SDK_INT >= 23) {
        relativeLayout.setForeground(new ColorDrawable(ContextCompat.
            getColor(getContext(), R.color.transparent_white)));
      }
    }
  }

}
