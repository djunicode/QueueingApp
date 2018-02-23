package com.djunicode.queuingapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.fragment.SubmissionFragment;

import static com.djunicode.queuingapp.customClasses.QueueDialogClass.flag;

public class StudentQueueActivity extends AppCompatActivity {

  private TextView queuePositionTextView, positionTV;
  private CardView doneButton, queueNumber;
  private Animation upToDown, downToUp, scale;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_student_queue);

    queuePositionTextView = (TextView) findViewById(R.id.queuePositionTextView);
    positionTV = (TextView) findViewById(R.id.positionTV);
    doneButton = (CardView) findViewById(R.id.doneButton);
    queueNumber = (CardView) findViewById(R.id.queueNumberCardView);

    upToDown = AnimationUtils.loadAnimation(this, R.anim.up_to_down);
    downToUp = AnimationUtils.loadAnimation(this, R.anim.down_to_up);
    scale = AnimationUtils.loadAnimation(this, R.anim.scale);

    positionTV.setAnimation(upToDown);
    doneButton.setAnimation(downToUp);
    queueNumber.setAnimation(scale);

    queuePositionTextView.setText("18");
    doneButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        flag = 0;
        finish();
      }
    });
  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("You will be removed from the Queue")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

                finish();
              }
            }).setNegativeButton("No", null).show();
  }
}
