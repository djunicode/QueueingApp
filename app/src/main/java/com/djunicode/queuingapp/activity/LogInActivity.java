package com.djunicode.queuingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.SessionManagement.SessionManager;
import com.djunicode.queuingapp.fragment.FindTeacherFragment;
import com.djunicode.queuingapp.fragment.LogInStudentFragment;
import com.djunicode.queuingapp.fragment.LogInTeacherFragment;
import com.djunicode.queuingapp.fragment.SignUpTeacherFragment;

public class LogInActivity extends AppCompatActivity {

  private final static String TAG = LogInActivity.class.getSimpleName();

  private EditText usernameEditText, sapIDEditText, passwordEditText;
  private Spinner departmentSpinner, yearSpinner;
  private TextView logInStudentTextView, logInTeacherTextView, signUpTeacherTextView;
  private CardView signUpStudentButton;
  private TextInputLayout studentSignUpinputLayoutUsername, studentSignUpinputLayoutSAPId,
      studentSignUpinputLayoutPassword, studentSignUpinputLayoutDepartment, studentSignUpinputLayoutYear;
  private SharedPreferences sp_student, sp_teacher;
  // Session Manager Class
  SessionManager session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_log_in);

    studentSignUpinputLayoutUsername = (TextInputLayout) findViewById(R.id.signUp_student_username);
    studentSignUpinputLayoutSAPId = (TextInputLayout) findViewById(R.id.signUp_student_SAPId);
    studentSignUpinputLayoutPassword = (TextInputLayout) findViewById(R.id.signUp_student_password);
    studentSignUpinputLayoutDepartment = (TextInputLayout) findViewById(R.id.signUp_student_department);
    studentSignUpinputLayoutYear = (TextInputLayout) findViewById(R.id.signUp_student_year);
    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    sapIDEditText = (EditText) findViewById(R.id.sapIDEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
    yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
    logInStudentTextView = (TextView) findViewById(R.id.logInStudentTextView);
    logInTeacherTextView = (TextView) findViewById(R.id.logInTeacherTextView);
    signUpTeacherTextView = (TextView) findViewById(R.id.signUpTeacherTextView);
    signUpStudentButton = (CardView) findViewById(R.id.signUpStudentButton);

    //studentSignUpinputLayoutUsername.setError(getString(R.string.err_msg_username));

    String username = usernameEditText.getText().toString();
    String sapID = sapIDEditText.getText().toString();
    String password = passwordEditText.getText().toString();
    sp_student = getSharedPreferences("Student",MODE_PRIVATE);
    sp_teacher = getSharedPreferences("Teacher",MODE_PRIVATE);

    // To remove Locally stored variables remove the below comment

    /*Editor editor_student = sp_student.edit();
    Editor editor_teacher = sp_teacher.edit();

    editor_student.remove("student_username");
    editor_student.remove("student_password");
    editor_student.commit();

    editor_teacher.remove("teacher_username");
    editor_teacher.remove("teacher_password");
    editor_teacher.commit();*/

    //if SharedPreferences contains username and password then redirect to Home activity
    if(sp_student.contains("student_username") && sp_student.contains("student_password")){
      Intent in = new Intent(this, StudentScreenActivity.class);
      startActivity(in);

    }

    else{

      if(sp_teacher.contains("teacher_username") && sp_teacher.contains("teacher_password")){
        Toast.makeText(getApplicationContext(), "I got it!", Toast.LENGTH_SHORT).show();
        Intent in = new Intent(this, StudentScreenActivity.class);
        // StudentScreenActivity just for demo till the time teacher fragments are not ready
        startActivity(in);

      }

    }

    departmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String department = parent.getItemAtPosition(position).toString();
        Log.i("Dept", department);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String year = parent.getItemAtPosition(position).toString();
        Log.i("Year", year);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    logInStudentTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        LogInStudentFragment fragment = new LogInStudentFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.appear, R.anim.disappear,
            R.anim.appear, R.anim.disappear);
        transaction.replace(R.id.containerFrame, fragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
      }
    });

    signUpTeacherTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        SignUpTeacherFragment fragment = new SignUpTeacherFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.appear, R.anim.disappear,
            R.anim.appear, R.anim.disappear);
        transaction.replace(R.id.containerFrame, fragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
      }
    });

    logInTeacherTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        LogInTeacherFragment fragment = new LogInTeacherFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.appear, R.anim.disappear,
            R.anim.appear, R.anim.disappear);
        transaction.replace(R.id.containerFrame, fragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
      }
    });

    /*signUpStudentButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), StudentScreenActivity.class);
        startActivity(intent);
      }
    });*/

    // Session Manager
    session = new SessionManager(this, "Student");

    signUpStudentButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (validSignUp()) {
          session.createLoginSession(usernameEditText.getText().toString(),
              passwordEditText.getText().toString());
          Intent intent = new Intent(LogInActivity.this, StudentScreenActivity.class);
          startActivity(intent);
          finish();
          Toast.makeText(getApplicationContext(), usernameEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
          Toast.makeText(getApplicationContext(), passwordEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
  private Boolean validSignUp () {

    if (!validateUsername()) {
      return false;
    }

    if (!validateDepartment()) {
      return false;
    }

    if (!validateYear()) {
      return false;
    }

    if (!validateSAPId()) {
      return false;
    }

    if (!validatePassword()) {
      return false;
    }

    return true;
  }

  private boolean validateUsername() {
    if (usernameEditText.getText().toString().trim().isEmpty()) {
      studentSignUpinputLayoutUsername.setError(getString(R.string.err_msg_username));
      requestFocus(usernameEditText);
      return false;
    } else if ((usernameEditText.getText().toString().length() < 5)) {
      studentSignUpinputLayoutUsername.setError(getString(R.string.err_msg_username_inappropriate_length));
      requestFocus(usernameEditText);
      return false;
    }else {
      studentSignUpinputLayoutUsername.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validateSAPId() {
    if (sapIDEditText.getText().toString().trim().isEmpty()) {
      studentSignUpinputLayoutSAPId.setError(getString(R.string.err_msg_SAPId));
      requestFocus(sapIDEditText);
      return false;
    }else if ((sapIDEditText.getText().toString().length() != 11)) {
      studentSignUpinputLayoutSAPId.setError(getString(R.string.err_msg_SAPId_inappropriate_length));
      requestFocus(sapIDEditText);
      return false;
    } else {
      studentSignUpinputLayoutSAPId.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validatePassword() {
    if (passwordEditText.getText().toString().trim().isEmpty()) {
      studentSignUpinputLayoutPassword.setError(getString(R.string.err_msg_password));
      requestFocus(passwordEditText);
      return false;
    } else if ((passwordEditText.getText().toString().length() < 5)) {
      studentSignUpinputLayoutPassword.setError(getString(R.string.err_msg_password_inappropriate_length));
      requestFocus(passwordEditText);
      return false;
    }else {
      studentSignUpinputLayoutPassword.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validateDepartment() {
    if(departmentSpinner.getSelectedItemPosition() == 0) {
      studentSignUpinputLayoutDepartment.setError(getString(R.string.err_msg_department));
      //requestFocusSpinner(departmentSpinner);
      return false;
    }
    else {
      studentSignUpinputLayoutDepartment.setError("");
      return true;
    }
  }

  private boolean validateYear() {
    if(yearSpinner.getSelectedItemPosition() == 0) {
      studentSignUpinputLayoutYear.setError(getString(R.string.err_msg_year));
      //requestFocusSpinner(yearSpinner);
      return false;
    }
    else {
      studentSignUpinputLayoutYear.setError("");
      return true;
    }
  }

  private void requestFocus(View view) {
    if (view.requestFocus()) {
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }


}
