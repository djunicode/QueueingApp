package com.djunicode.queuingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.djunicode.queuingapp.R;

/**
 * Main Activity for app.
 */
public class  MainActivity extends AppCompatActivity {

  private CardView signUpStudentCardView, logInStudentCardView, signUpTeacherCardView, logInTeacherCardView;
  private SharedPreferences sp_student, sp_teacher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*sp_student = getSharedPreferences("Student", MODE_PRIVATE);
    sp_teacher = getSharedPreferences("Teacher", MODE_PRIVATE);
//dlmsmkfnkfls
    if (sp_student.contains("student_sapid") && sp_student.contains("student_password")) {
      Intent in = new Intent(this, StudentScreenActivity.class);
      startActivity(in);
    } else {
      if (sp_teacher.contains("teacher_sapid") && sp_teacher.contains("teacher_password")) {
        Toast.makeText(getApplicationContext(), "I got it!", Toast.LENGTH_SHORT).show();
        Intent in = new Intent(this, TeacherScreenActivity.class);
        // StudentScreenActivity just for demo till the time teacher fragments are not ready
        startActivity(in);
      }
    }*/


      signUpStudentCardView = (CardView) findViewById(R.id.signUpStudentCardView);
      logInStudentCardView = (CardView) findViewById(R.id.logInStudentCardView);
      signUpTeacherCardView = (CardView) findViewById(R.id.signUpTeacherCardView);
      logInTeacherCardView = (CardView) findViewById(R.id.logInTeacherCardView);

      final Intent intent1 = new Intent(this, EmailActivity.class);
      final Intent intent2 = new Intent(this, LogInActivity.class);

      signUpStudentCardView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          intent1.putExtra("user", "student");
          startActivity(intent1);
        }
      });

      signUpTeacherCardView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          intent1.putExtra("user", "teacher");
          startActivity(intent1);
        }
      });

      logInStudentCardView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          intent2.putExtra("userLogin", "student");
          startActivity(intent2);
        }
      });

      logInTeacherCardView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          intent2.putExtra("userLogin", "teacher");
          startActivity(intent2);
        }
      });
    }
  }