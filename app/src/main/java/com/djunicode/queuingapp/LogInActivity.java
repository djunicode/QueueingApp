package com.djunicode.queuingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

public class LogInActivity extends AppCompatActivity {

  private EditText usernameEditText, sapIDEditText, passwordEditText;
  private Spinner departmentSpinner, yearSpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_log_in);

    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    sapIDEditText = (EditText) findViewById(R.id.sapIDEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
    yearSpinner = (Spinner) findViewById(R.id.yearSpinner);

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
  }
}
