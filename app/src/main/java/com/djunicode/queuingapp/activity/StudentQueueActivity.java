package com.djunicode.queuingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.TextView;
import com.djunicode.queuingapp.R;

public class StudentQueueActivity extends AppCompatActivity {

  private TextView queuePositionTextView;
  private CardView doneButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_queue);

    queuePositionTextView = (TextView) findViewById(R.id.queuePositionTextView);
    doneButton = (CardView) findViewById(R.id.doneButton);

    queuePositionTextView.setText("18");
  }
}
