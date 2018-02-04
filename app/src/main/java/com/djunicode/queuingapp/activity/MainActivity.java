package com.djunicode.queuingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import com.djunicode.queuingapp.R;

/**
 * Main Activity for app.
 */
public class MainActivity extends AppCompatActivity {

  private CardView signUpStudentCardView, logInStudentCardView, signUpTeacherCardView, logInTeacherCardView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    signUpStudentCardView = (CardView) findViewById(R.id.signUpStudentCardView);
    logInStudentCardView = (CardView) findViewById(R.id.logInStudentCardView);
    signUpTeacherCardView = (CardView) findViewById(R.id.signUpTeacherCardView);
    logInTeacherCardView = (CardView) findViewById(R.id.logInTeacherCardView);


  }
}
