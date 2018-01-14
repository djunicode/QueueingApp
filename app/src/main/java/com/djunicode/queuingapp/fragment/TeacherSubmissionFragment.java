package com.djunicode.queuingapp.fragment;


import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.CardView;
import android.text.InputType;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherSubmissionFragment extends Fragment {

  private Spinner subjectSpinner, batchSpinner;
  private CardView fromTimePickerButton;
  private ImageButton studentsButton, timerButton;
  private FloatingActionButton createFab, startFab, cancelFab;
  private ScrollView scrollView;
  private LinearLayout fabLL1, fabLL2;
  private CoordinatorLayout coordinatorLayout;
  private Animation fabOpen, fabClose, rotateForward, rotateBackward;
  private Boolean isFabOpen = false;
  private Boolean isCreated = false;

  public TeacherSubmissionFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_teacher_submission, container, false);

    String[] array = {"Select", "one", "two", "three", "four", "five", "six", "seven", "eight",
        "nine", "ten"};

    subjectSpinner = (Spinner) view.findViewById(R.id.subjectSpinner);
    batchSpinner = (Spinner) view.findViewById(R.id.batchSpinner);
    fromTimePickerButton = (CardView) view.findViewById(R.id.fromTimePickerButton);
    studentsButton = (ImageButton) view.findViewById(R.id.studentsButton);
    timerButton = (ImageButton) view.findViewById(R.id.timerButton);
    createFab = (FloatingActionButton) view.findViewById(R.id.createFab);
    startFab = (FloatingActionButton) view.findViewById(R.id.startFab);
    cancelFab = (FloatingActionButton) view.findViewById(R.id.cancelFab);
//    scrollView = (ScrollView) view.findViewById(R.id.teacherScrollView);
    coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.teacherCoordinatorLayout);
    fabLL1 = (LinearLayout) view.findViewById(R.id.fabLL1);
    fabLL2 = (LinearLayout) view.findViewById(R.id.fabLL2);
    fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
    fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
    rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
    rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_spinner_dropdown_item, array);

    subjectSpinner.setAdapter(adapter);
    batchSpinner.setAdapter(adapter);

    batchSpinner.setEnabled(false);
    batchSpinner.setAlpha(0.4f);

    subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
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
        if(batchSpinner.getSelectedItemPosition() != 0){
          TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
              if(hourOfDay <= 11 && minute <=59)
                Snackbar.make(coordinatorLayout, "Submission is from " + hourOfDay + ":" + minute + "am",
                    Snackbar.LENGTH_LONG).show();
              else {
                if(hourOfDay == 12 && minute == 0)
                  Snackbar.make(coordinatorLayout, "Submission is from " + hourOfDay + ":" + minute + "pm",
                      Snackbar.LENGTH_LONG).show();
                else {
                  hourOfDay -= 12;
                  Snackbar.make(coordinatorLayout, "Submission is from " + hourOfDay + ":" + minute + "pm",
                      Snackbar.LENGTH_LONG).show();
                }
              }
            }
          },
              0, 0, false);
          timePickerDialog.show();
        }
        else
          Toast.makeText(getContext(), "Please select the batch!", Toast.LENGTH_SHORT).show();
      }
    });

    timerButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new OnTimeSetListener(){
          @Override
          public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(hourOfDay <= 11 && minute <=59)
              Snackbar.make(coordinatorLayout, "Submission is till " + hourOfDay + ":" + minute + "am",
                  Snackbar.LENGTH_LONG).show();
            else {
              if(hourOfDay == 12 && minute == 0)
                Snackbar.make(coordinatorLayout, "Submission is till " + hourOfDay + ":" + minute + "pm",
                    Snackbar.LENGTH_LONG).show();
              else {
                hourOfDay -= 12;
                Snackbar.make(coordinatorLayout, "Submission is till " + hourOfDay + ":" + minute + "pm",
                    Snackbar.LENGTH_LONG).show();
              }
            }
          }
        },
            0, 0, false);
        timePickerDialog.show();
      }
    });

    studentsButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new Builder(getContext());
        builder.setTitle("Enter the number of students");
        if(input.getParent() != null){
          ((ViewGroup) input.getParent()).removeView(input);
          input.setText("");
        }
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getContext(), input.getText().toString() + " students selected",
                Toast.LENGTH_SHORT).show();
          }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
      }
    });

    createFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!isCreated){
          createFab.setImageResource(R.drawable.ic_add);
          isCreated = true;
        }
        else {
          animateFab();
        }
      }
    });

    cancelFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        animateFab();
        createFab.setImageResource(R.drawable.book_open_page);
        isCreated = false;
      }
    });

    startFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        animateFab();
        createFab.setImageResource(R.drawable.book_open_page);
        Intent intent = new Intent(getContext(), StudentListActivity.class);
        startActivity(intent);
        isCreated = false;
      }
    });
    return view;
  }

  private void animateFab(){
    if(isFabOpen){
      fabLL1.setVisibility(View.INVISIBLE);
      fabLL2.setVisibility(View.INVISIBLE);
      isFabOpen=false;
      createFab.setAnimation(rotateBackward);
      fabLL1.animate().translationY(0).alpha(0.0f);
      fabLL2.animate().translationY(0).alpha(0.0f);
    } else {
      fabLL1.setVisibility(View.VISIBLE);
      fabLL2.setVisibility(View.VISIBLE);
      isFabOpen=true;
      createFab.setAnimation(rotateForward);
      fabLL1.animate().translationY(-getResources().getDimension(R.dimen.standard_55)).alpha(1.0f);
      fabLL2.animate().translationY(-getResources().getDimension(R.dimen.standard_105)).alpha(1.0f);
    }
  }

}
