package com.djunicode.queuingapp.fragment;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.djunicode.queuingapp.activity.SightedTeacherActivity;
import com.djunicode.queuingapp.model.LocationTeacher;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindTeacherFragment extends Fragment {

  private Spinner subjectSpinner, teacherSpinner;
  private CardView findTeacherButton, sightedTeacherButton;
  ApiInterface apiInterface;
  String location2 = "Dhruv";
  String dept;
  String teacherLocation, floor, room;

  public FindTeacherFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_find_teacher, container, false);
    apiInterface = ApiClient.getClient().create(ApiInterface.class);


    String[] array = {"Select", "one", "two", "three", "four", "five", "six", "seven", "eight",
        "nine", "ten"};

    subjectSpinner = (Spinner) view.findViewById(R.id.subjectSpinner);
    teacherSpinner = (Spinner) view.findViewById(R.id.teacherSpinner);
    findTeacherButton = (CardView) view.findViewById(R.id.findTeacherButton);
    sightedTeacherButton = (CardView) view.findViewById(R.id.sightedTeacherButton);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_spinner_dropdown_item, array);

    subjectSpinner.setAdapter(adapter);
    teacherSpinner.setAdapter(adapter);

    teacherSpinner.setEnabled(false);
    teacherSpinner.setAlpha(0.4f);

    subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
          teacherSpinner.setEnabled(true);
          teacherSpinner.setAlpha(1.0f);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    findTeacherButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if(teacherSpinner.getSelectedItemPosition() != 0){
          Call<LocationTeacher> call = apiInterface.getTeacherLocation(12);
          call.enqueue(new Callback<LocationTeacher>() {
            @Override
            public void onResponse(Call<LocationTeacher> call, Response<LocationTeacher> response) {
              Log.e("Dhruv", "inside onResponse");
              if (response.isSuccessful()) {
                Log.e("Dhruv", "Successfull");
                Log.e("Dhruv", response.body().getDepartment() );
                dept = response.body().getDepartment();
                floor = response.body().getFloor().toString();
                room = response.body().getRoom();
                teacherLocation = "Prof. " + teacherSpinner.getSelectedItem().toString() +
                        " is in Department: " + dept + " at Floor: " + floor + " in Room: "
                        + room;
                AlertDialog.Builder builder = new Builder(getActivity());
                builder.setMessage(teacherLocation)
                        .setPositiveButton("OK", null)
                        .show();


                //+ " Floor: "
                        //+ response.body().getFloor().toString() + " Room: " + response.body().getRoom();
              }
            }

            @Override
            public void onFailure(Call<LocationTeacher> call, Throwable t) {
              Log.e("Dhruv", "failure");
            }
          });
           teacherLocation = "Prof. " + teacherSpinner.getSelectedItem().toString() +
              " " +  "Department: " + "Waiting for Teacher, please try again";
//          AlertDialog.Builder builder = new Builder(getActivity());
//          builder.setMessage(teacherLocation)
//              .setPositiveButton("OK", null)
//              .show();
        }
        else
          Toast.makeText(getContext(), "Please Select a teacher!", Toast.LENGTH_SHORT).show();
      }
    });

    sightedTeacherButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SightedTeacherActivity.class);
        getContext().startActivity(intent);
      }
    });

    return view;
  }


}
