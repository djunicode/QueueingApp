package com.djunicode.queuingapp.fragment;


import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
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

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.djunicode.queuingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherSubmissionFragment extends Fragment {

  private Spinner subjectSpinner, batchSpinner;
  private CardView fromTimePickerButton;
  private Button studentsButton, timerButton;
  private FloatingActionButton createFab;
  private ScrollView scrollView;

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
    studentsButton = (Button) view.findViewById(R.id.studentsButton);
    timerButton = (Button) view.findViewById(R.id.timerButton);
    createFab = (FloatingActionButton) view.findViewById(R.id.createFab);
    scrollView = (ScrollView) view.findViewById(R.id.teacherScrollView);

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
                Snackbar.make(scrollView, "Submission is from " + hourOfDay + ":" + minute + "am",
                    Snackbar.LENGTH_LONG).show();
              else {
                if(hourOfDay == 12 && minute == 0)
                  Snackbar.make(scrollView, "Submission is from " + hourOfDay + ":" + minute + "pm",
                      Snackbar.LENGTH_LONG).show();
                else {
                  hourOfDay -= 12;
                  Snackbar.make(scrollView, "Submission is from " + hourOfDay + ":" + minute + "pm",
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
              Snackbar.make(scrollView, "Submission is till " + hourOfDay + ":" + minute + "am",
                  Snackbar.LENGTH_LONG).show();
            else {
              if(hourOfDay == 12 && minute == 0)
                Snackbar.make(scrollView, "Submission is till " + hourOfDay + ":" + minute + "pm",
                    Snackbar.LENGTH_LONG).show();
              else {
                hourOfDay -= 12;
                Snackbar.make(scrollView, "Submission is till " + hourOfDay + ":" + minute + "pm",
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
    return view;
  }

}
