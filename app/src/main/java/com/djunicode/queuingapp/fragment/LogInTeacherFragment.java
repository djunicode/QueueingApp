package com.djunicode.queuingapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import com.djunicode.queuingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInTeacherFragment extends Fragment {

  private EditText usernameLogInTeacherEditText, passwordLogInTeacherEditText;
  private CardView logInTeacherButton;

  public LogInTeacherFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_log_in_teacher, container, false);

    usernameLogInTeacherEditText = (EditText) view.findViewById(R.id.usernameLogInTeacherEditText);
    passwordLogInTeacherEditText = (EditText) view.findViewById(R.id.passwordLogInTeacherEditText);
    logInTeacherButton = (CardView) view.findViewById(R.id.logInTeacherButton);

    String username = usernameLogInTeacherEditText.getText().toString();
    String password = passwordLogInTeacherEditText.getText().toString();

    return view;
  }

}
