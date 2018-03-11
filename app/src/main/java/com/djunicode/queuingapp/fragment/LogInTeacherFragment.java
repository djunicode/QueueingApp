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
import com.djunicode.queuingapp.activity.TeacherScreenActivity;
import com.djunicode.queuingapp.model.TeacherModel;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInTeacherFragment extends Fragment {

  private EditText sapIdLogInTeacherEditText, passwordLogInTeacherEditText;
  private CardView logInTeacherButton;
  private TextInputLayout teacherLogIninputLayoutSapID, teacherLogIninputLayoutPassword;
  // Session Manager Class
  SessionManager session;
  SharedPreferences spDemo;
  public Boolean trueLogin;
  final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
  public LogInTeacherFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_log_in_teacher, container, false);

    teacherLogIninputLayoutSapID = (TextInputLayout) view.findViewById(R.id.logInTeacherSapID);
    teacherLogIninputLayoutPassword = (TextInputLayout) view.findViewById(R.id.logIn_teacher_password);
    sapIdLogInTeacherEditText = (EditText) view.findViewById(R.id.sapIdLogInTeacherEditText);
    passwordLogInTeacherEditText = (EditText) view.findViewById(R.id.passwordLogInTeacherEditText);
    logInTeacherButton = (CardView) view.findViewById(R.id.logInTeacherButton);

    String username = sapIdLogInTeacherEditText.getText().toString();
    String password = passwordLogInTeacherEditText.getText().toString();

    // Session Manager
    session = new SessionManager(getContext(), "Teacher");

    logInTeacherButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(validTeacherLogIn()) {
          session.createLoginSession(sapIdLogInTeacherEditText.getText().toString(),
              passwordLogInTeacherEditText.getText().toString(), "demo_username");
          Intent intent = new Intent(getContext(), TeacherScreenActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
          /*Toast.makeText(getContext(), sapIdLogInTeacherEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
          Toast.makeText(getContext(), passwordLogInTeacherEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();*/
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

    validMatch();
    return true;
  }

  private boolean validateUsername() {
    if (sapIdLogInTeacherEditText.getText().toString().trim().isEmpty()) {
      teacherLogIninputLayoutSapID.setError(getString(R.string.err_msg_username));
      requestFocus(sapIdLogInTeacherEditText);
      return false;
    } else if ((sapIdLogInTeacherEditText.getText().toString().length() < 5)) {
      teacherLogIninputLayoutSapID.setError(getString(R.string.err_msg_username_inappropriate_length));
      requestFocus(sapIdLogInTeacherEditText);
      return false;
    }else {
      teacherLogIninputLayoutSapID.setErrorEnabled(false);
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

  private void validMatch() {
    Call<TeacherModel> call = apiService.getValidId(sapIdLogInTeacherEditText.getText().toString(),passwordLogInTeacherEditText.getText().toString());
    call.enqueue(new Callback<TeacherModel>() {
      @Override
      public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
        if(response.isSuccessful())
          trueLogin = true;

        else {
          trueLogin = false;
          Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
        }
      }
      @Override
      public void onFailure(Call<TeacherModel> call, Throwable t) {


      }
    });
  }

  private void requestFocus (View view) {

    if(view.requestFocus()){
      getActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }
}
