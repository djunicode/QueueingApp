package com.djunicode.queuingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.djunicode.queuingapp.R;
import java.io.IOException;

public class timeTableActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_time_table);

    Intent intent = getIntent();
    String timeTableUri = intent.getStringExtra("timeTableUri");

    ImageView imageView = (ImageView) findViewById(R.id.timeTableImageView);
    if(timeTableUri != null){
      try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(timeTableUri));
        imageView.setImageBitmap(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
