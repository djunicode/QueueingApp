package com.djunicode.queuingapp.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.SessionManagement.SessionManager;
import com.djunicode.queuingapp.activity.LogInActivity;
import com.djunicode.queuingapp.activity.StudentScreenActivity;
import com.djunicode.queuingapp.model.Student;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInStudentFragment extends Fragment {

  private EditText sapIdLogInEditText, passwordLogInEditText;
  private CardView logInStudentButton;
  private TextInputLayout studentLogIninputLayoutSapId, studentLogIninputLayoutPassword;
  // Session Manager Class
  SessionManager session;
  SharedPreferences spDemo;
  private ProgressDialog pd;

  public Boolean trueLogin = false;
  final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

  public LogInStudentFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_log_in_student, container, false);

    studentLogIninputLayoutSapId = (TextInputLayout) view.findViewById(R.id.logInStudentSapId);
    studentLogIninputLayoutPassword = (TextInputLayout) view.findViewById(R.id.logIn_student_password);
    sapIdLogInEditText = (EditText) view.findViewById(R.id.sapIdLogInEditText);
    passwordLogInEditText = (EditText) view.findViewById(R.id.passwordLogInEditText);
    logInStudentButton = (CardView) view.findViewById(R.id.logInStudentButton);

    String username = sapIdLogInEditText.getText().toString();
    String password = passwordLogInEditText.getText().toString();

    // Session Manager
    session = new SessionManager(getContext(), "Student");

    logInStudentButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.e("Clicked", "cl");
        pd = ProgressDialog.show(getContext(), "Logging In!", "Please wait...");
        /*boolean valid = validLogIn();
        if (valid && trueLogin) {
          *//*session.createLoginSession(sapIdLogInEditText.getText().toString(),
              passwordLogInEditText.getText().toString(), "demo_username");
          Intent intent = new Intent(getContext(), StudentScreenActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
          Toast.makeText(getContext(), sapIdLogInEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
          Toast.makeText(getContext(), passwordLogInEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();*//*
        }*/
        validLogIn();
      }
    });
    return view;
  }

  private Boolean validLogIn () {

    if (!validateUsername()) {
      return false;
    }

    if (!validatePassword()) {
      return false;
    }

    Call<Student> call = apiService.getValidIdStudent(sapIdLogInEditText.getText().toString(),passwordLogInEditText.getText().toString());
    call.enqueue(new Callback<Student>() {
      @Override
      public void onResponse(Call<Student> call, Response<Student> response) {
        try{
          if(response.isSuccessful()) {
            trueLogin = true;
            session.createLoginSession(sapIdLogInEditText.getText().toString(),
                passwordLogInEditText.getText().toString(), "demo_username");
            pd.dismiss();
            Intent intent = new Intent(getContext(), StudentScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(getContext(), sapIdLogInEditText.getText().toString(),
                Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), passwordLogInEditText.getText().toString(),
                Toast.LENGTH_SHORT).show();
          }
          else {
            trueLogin = false;
            pd.dismiss();
            Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
          }
        } catch (Exception e){
          Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<Student> call, Throwable t) {

      }
    });
    return true;
  }

  private boolean validateUsername() {
    if (sapIdLogInEditText.getText().toString().trim().isEmpty()) {
      studentLogIninputLayoutSapId.setError(getString(R.string.err_msg_username));
      requestFocus(sapIdLogInEditText);
      return false;
    } else if ((sapIdLogInEditText.getText().toString().length() < 5)) {
      studentLogIninputLayoutSapId.setError(getString(R.string.err_msg_username_inappropriate_length));
      requestFocus(sapIdLogInEditText);
      return false;
    }else {
      studentLogIninputLayoutSapId.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validatePassword() {
    if (passwordLogInEditText.getText().toString().trim().isEmpty()) {
      studentLogIninputLayoutPassword.setError(getString(R.string.err_msg_password));
      requestFocus(passwordLogInEditText);
      return false;
    } else if ((passwordLogInEditText.getText().toString().length() < 5)) {
      studentLogIninputLayoutPassword.setError(getString(R.string.err_msg_password_inappropriate_length));
      requestFocus(passwordLogInEditText);
      return false;
    }else {
      studentLogIninputLayoutPassword.setErrorEnabled(false);
    }

    return true;
  }


  private void requestFocus (View view) {

    if(view.requestFocus()){
      getActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }
}
