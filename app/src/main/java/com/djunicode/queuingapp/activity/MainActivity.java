package com.djunicode.queuingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.djunicode.queuingapp.R;

/**
 * Main Activity for app.
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*Intent intent = new Intent(this, LogInActivity.class);
    startActivity(intent);*/
  }
}
