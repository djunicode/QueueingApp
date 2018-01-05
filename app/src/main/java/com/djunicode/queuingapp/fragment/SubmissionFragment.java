package com.djunicode.queuingapp.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.djunicode.queuingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmissionFragment extends Fragment {

  private Spinner subjectSpinner, teacherNameSpinner;
  private FloatingActionButton fab;

  public SubmissionFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_submission, container, false);

    String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
    subjectSpinner = (Spinner) view.findViewById(R.id.subjectSpinner);
    teacherNameSpinner = (Spinner) view.findViewById(R.id.teacherNameSpinner);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_spinner_dropdown_item, array);
    subjectSpinner.setAdapter(adapter);
    teacherNameSpinner.setAdapter(adapter);
    return view;
  }

}
