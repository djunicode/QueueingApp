package com.djunicode.queuingapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Spinner;
import com.djunicode.queuingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpTeacherFragment extends Fragment {

  private EditText usernameTeacherEditText, sapIDTeacherEditText, passwordTeacherEditText;
  private Spinner departmentTeacherSpinner;
  private CardView signUpTeacherButton;

  public SignUpTeacherFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sign_up_teacher, container, false);

    usernameTeacherEditText = (EditText) view.findViewById(R.id.usernameTeacherEditText);
    passwordTeacherEditText = (EditText) view.findViewById(R.id.passwordTeacherEditText);
    sapIDTeacherEditText = (EditText) view.findViewById(R.id.sapIDTeacherEditText);
    departmentTeacherSpinner = (Spinner) view.findViewById(R.id.departmentTeacherSpinner);
    signUpTeacherButton = (CardView) view.findViewById(R.id.signUpTeacherButton);

    return view;
  }

}
