package com.djunicode.queuingapp.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.fragment.LogInStudentFragment;
import com.djunicode.queuingapp.fragment.LogInTeacherFragment;
import com.djunicode.queuingapp.fragment.SignUpTeacherFragment;

public class LogInActivity extends AppCompatActivity {

  private final static String TAG = LogInActivity.class.getSimpleName();

  private EditText usernameEditText, sapIDEditText, passwordEditText;
  private Spinner departmentSpinner, yearSpinner;
  private TextView logInStudentTextView, logInTeacherTextView, signUpTeacherTextView;
  private CardView signUpStudentButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_log_in);

    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    sapIDEditText = (EditText) findViewById(R.id.sapIDEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
    yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
    logInStudentTextView = (TextView) findViewById(R.id.logInStudentTextView);
    logInTeacherTextView = (TextView) findViewById(R.id.logInTeacherTextView);
    signUpTeacherTextView = (TextView) findViewById(R.id.signUpTeacherTextView);
    signUpStudentButton = (CardView) findViewById(R.id.signUpStudentButton);

    String username = usernameEditText.getText().toString();
    String sapID = sapIDEditText.getText().toString();
    String password = passwordEditText.getText().toString();

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
  }
}
