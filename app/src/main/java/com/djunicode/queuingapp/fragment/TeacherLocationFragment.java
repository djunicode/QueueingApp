package com.djunicode.queuingapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.djunicode.queuingapp.model.RecentEvents;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherLocationFragment extends Fragment {

  private Spinner floorSpinner, departmentSpinner, roomSpinner;
  private FloatingActionButton locationUpdateFab;
  public static boolean locationUpdated = false;
  public static String locationString;

  public TeacherLocationFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_teacher_location, container, false);

    String[] array = {"Select", "one", "two", "three", "four", "five", "six", "seven", "eight",
        "nine", "ten"};

    final Bundle extras = getArguments();

    floorSpinner = (Spinner) view.findViewById(R.id.floorSpinner);
    departmentSpinner = (Spinner) view.findViewById(R.id.departmentSpinner);
    roomSpinner = (Spinner) view.findViewById(R.id.roomSpinner);
    locationUpdateFab = (FloatingActionButton) view.findViewById(R.id.locationUpdateFab);

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
        if(position != 0){
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
        if(position != 0){
          roomSpinner.setEnabled(true);
          roomSpinner.setAlpha(1.0f);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    roomSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        locationUpdateFab.setEnabled(true);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    locationUpdateFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        locationUpdated = true;
        locationString = "Floor-" + floorSpinner.getSelectedItem().toString() +
            " Dept-" + departmentSpinner.getSelectedItem().toString() + " Room-" +
            roomSpinner.getSelectedItem().toString();

        boolean flag = false;

        if(extras != null){
          flag = extras.getBoolean("Flag");
          String subject = extras.getString("Subject");
          String batch = extras.getString("Batch");
          String from = extras.getString("From");
          String to = extras.getString("To");
          TeacherSubmissionFragment.recentEventsList.add(new RecentEvents(subject, batch, from, to,
              locationString));
          Toast.makeText(getContext(), "Created new event!", Toast.LENGTH_SHORT).show();
        }
      }
    });

    return view;
  }

}
