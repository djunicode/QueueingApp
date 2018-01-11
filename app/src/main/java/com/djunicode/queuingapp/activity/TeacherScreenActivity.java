package com.djunicode.queuingapp.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.fragment.TeacherLocationFragment;
import com.djunicode.queuingapp.fragment.TeacherProfileFragment;
import com.djunicode.queuingapp.fragment.TeacherSubmissionFragment;

public class TeacherScreenActivity extends AppCompatActivity {

  private final static String TAG = TeacherScreenActivity.class.getSimpleName();
  private BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_teacher_screen);

    bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationViewTeacher);

    if(savedInstanceState == null) loadTeacherSubmissionFragment();

    bottomNavigationView.setOnNavigationItemSelectedListener(
        new OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
              case R.id.action_submission_teacher:
                loadTeacherSubmissionFragment();
                return true;
              case R.id.action_location:
                loadTeacherLocationFragment();
                return true;
              case R.id.action_profile:
                loadTeacherProfileFragment();
                return true;
            }
            return false;
          }
        });
  }

  private void loadTeacherSubmissionFragment(){
    TeacherSubmissionFragment fragment = new TeacherSubmissionFragment();
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.setCustomAnimations(R.anim.appear, R.anim.disappear,
        R.anim.appear, R.anim.disappear);
    transaction.replace(R.id.containerLayoutTeacher, fragment);
    transaction.addToBackStack(TAG);
    transaction.commit();
  }

  private void loadTeacherLocationFragment(){
    TeacherLocationFragment fragment = new TeacherLocationFragment();
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.setCustomAnimations(R.anim.appear, R.anim.disappear,
        R.anim.appear, R.anim.disappear);
    transaction.replace(R.id.containerLayoutTeacher, fragment);
    transaction.addToBackStack(TAG);
    transaction.commit();
  }

  private void loadTeacherProfileFragment(){
    TeacherProfileFragment fragment = new TeacherProfileFragment();
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.setCustomAnimations(R.anim.appear, R.anim.disappear,
        R.anim.appear, R.anim.disappear);
    transaction.replace(R.id.containerLayoutTeacher, fragment);
    transaction.addToBackStack(TAG);
    transaction.commit();
  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Exit the app.")
        .setMessage("Are you sure you want to exit?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            finish();
          }
        }).setNegativeButton("No", null).show();
  }
}
