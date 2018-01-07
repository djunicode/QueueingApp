package com.djunicode.queuingapp.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.SessionManagement.SessionManager;
import com.djunicode.queuingapp.activity.StudentScreenActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInTeacherFragment extends Fragment {

  private EditText usernameLogInTeacherEditText, passwordLogInTeacherEditText;
  private CardView logInTeacherButton;
  private TextInputLayout teacherLogIninputLayoutUsername, teacherLogIninputLayoutPassword;
  // Session Manager Class
  SessionManager session;
  SharedPreferences spDemo;
  public LogInTeacherFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_log_in_teacher, container, false);

    teacherLogIninputLayoutUsername = (TextInputLayout) view.findViewById(R.id.logIn_teacher_username);
    teacherLogIninputLayoutPassword = (TextInputLayout) view.findViewById(R.id.logIn_teacher_password);
    usernameLogInTeacherEditText = (EditText) view.findViewById(R.id.usernameLogInTeacherEditText);
    passwordLogInTeacherEditText = (EditText) view.findViewById(R.id.passwordLogInTeacherEditText);
    logInTeacherButton = (CardView) view.findViewById(R.id.logInTeacherButton);

    String username = usernameLogInTeacherEditText.getText().toString();
    String password = passwordLogInTeacherEditText.getText().toString();

    // Session Manager
    session = new SessionManager(getContext(), "Teacher");

    logInTeacherButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(validTeacherLogIn()) {
          session.createLoginSession(usernameLogInTeacherEditText.getText().toString(),
              passwordLogInTeacherEditText.getText().toString());
          Intent intent = new Intent(getContext(), StudentScreenActivity.class);
          // StudentScreenActivity just for demo till the time teacher fragments are not ready
          startActivity(intent);
          Toast.makeText(getContext(), usernameLogInTeacherEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
          Toast.makeText(getContext(), passwordLogInTeacherEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
        }
      }
    });

    return view;
  }
  private Boolean validTeacherLogIn () {

    if (!validateUsername()) {
      return false;
    }

    if (!validatePassword()) {
      return false;
    }

    if(!validMatch()){
      return false;
    }
    return true;
  }

  private boolean validateUsername() {
    if (usernameLogInTeacherEditText.getText().toString().trim().isEmpty()) {
      teacherLogIninputLayoutUsername.setError(getString(R.string.err_msg_username));
      requestFocus(usernameLogInTeacherEditText);
      return false;
    } else if ((usernameLogInTeacherEditText.getText().toString().length() < 5)) {
      teacherLogIninputLayoutUsername.setError(getString(R.string.err_msg_username_inappropriate_length));
      requestFocus(usernameLogInTeacherEditText);
      return false;
    }else {
      teacherLogIninputLayoutUsername.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validatePassword() {
    if (passwordLogInTeacherEditText.getText().toString().trim().isEmpty()) {
      teacherLogIninputLayoutPassword.setError(getString(R.string.err_msg_password));
      requestFocus(passwordLogInTeacherEditText);
      return false;
    } else if ((passwordLogInTeacherEditText.getText().toString().length() < 5)) {
      teacherLogIninputLayoutPassword.setError(getString(R.string.err_msg_password_inappropriate_length));
      requestFocus(passwordLogInTeacherEditText);
      return false;
    }else {
      teacherLogIninputLayoutPassword.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validMatch() {
    if(usernameLogInTeacherEditText.getText().toString().equals("dhruv") &&
        passwordLogInTeacherEditText.getText().toString().equals("demopass")) {

      return true;
    }
    else {
      Toast.makeText(getContext(), "Doesn't match!",
          Toast.LENGTH_SHORT).show();
      return false;
    }
  }

  private void requestFocus (View view) {

    if(view.requestFocus()){
      getActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }
}
