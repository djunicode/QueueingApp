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
import com.djunicode.queuingapp.customClasses.HashingPassword;
import com.djunicode.queuingapp.fragment.FindTeacherFragment;
import com.djunicode.queuingapp.fragment.LogInStudentFragment;
import com.djunicode.queuingapp.fragment.LogInTeacherFragment;
import com.djunicode.queuingapp.fragment.SignUpTeacherFragment;
import com.djunicode.queuingapp.model.Student;
import com.djunicode.queuingapp.model.StudentForId;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

  private final static String TAG = LogInActivity.class.getSimpleName();

  private EditText usernameEditText, sapIDEditText, passwordEditText;
  private Spinner departmentSpinner, yearSpinner, batchSpinner;
  private TextView logInStudentTextView, logInTeacherTextView, signUpTeacherTextView;
  private CardView signUpStudentButton;
  private String username, department, year, batch, SAPId, password; //Student's data to be send to server
  private TextInputLayout studentSignUpinputLayoutUsername, studentSignUpinputLayoutSAPId,
      studentSignUpinputLayoutPassword, studentSignUpinputLayoutDepartment,
      studentSignUpinputLayoutYear, batchSpinnerLayout;
  private SharedPreferences sp_student, sp_teacher;
  // Session Manager Class
  SessionManager session;
  ApiInterface apiInterface;
  private String salt, intermediate_password, hashed_password;
  int idFromServer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_log_in);

    studentSignUpinputLayoutUsername = (TextInputLayout) findViewById(R.id.signUp_student_username);
    studentSignUpinputLayoutSAPId = (TextInputLayout) findViewById(R.id.signUp_student_SAPId);
    studentSignUpinputLayoutPassword = (TextInputLayout) findViewById(R.id.signUp_student_password);
    studentSignUpinputLayoutDepartment = (TextInputLayout) findViewById(R.id.signUp_student_department);
    studentSignUpinputLayoutYear = (TextInputLayout) findViewById(R.id.signUp_student_year);
    batchSpinnerLayout = (TextInputLayout) findViewById(R.id.batchTextInputLayout);
    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    sapIDEditText = (EditText) findViewById(R.id.sapIDEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
    yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
    batchSpinner = (Spinner) findViewById(R.id.batchSpinner);
    logInStudentTextView = (TextView) findViewById(R.id.logInStudentTextView);
    logInTeacherTextView = (TextView) findViewById(R.id.logInTeacherTextView);
    signUpTeacherTextView = (TextView) findViewById(R.id.signUpTeacherTextView);
    signUpStudentButton = (CardView) findViewById(R.id.signUpStudentButton);
    apiInterface = ApiClient.getClient().create(ApiInterface.class);

    //studentSignUpinputLayoutUsername.setError(getString(R.string.err_msg_username));
    sp_student = getSharedPreferences("Student",MODE_PRIVATE);
    sp_teacher = getSharedPreferences("Teacher",MODE_PRIVATE);


    Editor editor_student = sp_student.edit();
    Editor editor_teacher = sp_teacher.edit();

    editor_student.remove("student_username");
    editor_student.remove("student_password");
    editor_student.commit();

    editor_teacher.remove("teacher_username");
    editor_teacher.remove("teacher_password");
    editor_teacher.commit();

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

    batchSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String batch = parent.getItemAtPosition(position).toString();
        Log.i("Batch", batch);
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
          username = usernameEditText.getText().toString();
          department = departmentSpinner.getSelectedItem().toString();
          year = yearSpinner.getSelectedItem().toString();
          batch = batchSpinner.getSelectedItem().toString();
          SAPId = sapIDEditText.getText().toString();
          password = passwordEditText.getText().toString();
          hash_password(password); //Hash the password before sending it to the server
          getIdForStudentFromUser();
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

    if (!validateBatch()) {
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

  private boolean validateBatch() {
    if(batchSpinner.getSelectedItemPosition() == 0) {
      batchSpinnerLayout.setError(getString(R.string.err_msg_batch));
      return false;
    }
    else {
      batchSpinnerLayout.setError("");
      return true;
    }
  }

  private void requestFocus(View view) {
    if (view.requestFocus()) {
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }

  private void sendDataToServer(int id) {
    Log.i("id", Integer.toString(id));
    Call<Student> call = apiInterface.createStudentAccount(id, username, SAPId, department, year, batch);
    call.enqueue(new Callback<Student>() {
      @Override
      public void onResponse(Call<Student> call, Response<Student> response) {
//        Log.i("student", response.body().toString());
      }

      @Override
      public void onFailure(Call<Student> call, Throwable t) {

      }
    });
  }

  private void hash_password (String password) {

    HashingPassword hashingPassword = new HashingPassword();
    salt = hashingPassword.generate_salt();
    intermediate_password = password + salt;
    hashed_password = hashingPassword.hash_the_password(intermediate_password);
  }

  private void getIdForStudentFromUser () {
    Call<StudentForId> call = apiInterface.getId(username, hashed_password);
    call.enqueue(new Callback<StudentForId>() {
      @Override
      public void onResponse(Call<StudentForId> call, Response<StudentForId> response) {
        Log.i("idFromServer", Integer.toString(response.body().getId()));
        sendDataToServer(response.body().getId());

      }

      @Override
      public void onFailure(Call<StudentForId> call, Throwable t) {

      }
    });

  }

}