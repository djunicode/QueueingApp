package com.djunicode.queuingapp.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.model.UserEmailVerify;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailActivity extends AppCompatActivity {

  private TextInputLayout signUpEmailTIL, verifyTIL;
  private EditText signUpEmailEditText, verifyEditText;
  private Button sendCodeButton, verifyEmailButton;
  private ApiInterface apiInterface;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_email);

    apiInterface = ApiClient.getClient().create(ApiInterface.class);

    Intent intent = getIntent();
    final String user = intent.getStringExtra("user");

    signUpEmailTIL = (TextInputLayout) findViewById(R.id.signUpEmailTIL);
    verifyTIL = (TextInputLayout) findViewById(R.id.verifyTIL);
    signUpEmailEditText = (EditText) findViewById(R.id.signUpEmailEditText);
    verifyEditText = (EditText) findViewById(R.id.verifyEditText);
    sendCodeButton = (Button) findViewById(R.id.sendCodeButton);
    verifyEmailButton = (Button) findViewById(R.id.verifyEmailButton);

    sendCodeButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Call<UserEmailVerify> call = apiInterface
            .sendEmail(null, signUpEmailEditText.getText().toString(), null);

        call.enqueue(new Callback<UserEmailVerify>() {
          @Override
          public void onResponse(Call<UserEmailVerify> call, Response<UserEmailVerify> response) {
            Integer id = response.body().getId();
            Log.i("ID:", Integer.toString(id));
          }

          @Override
          public void onFailure(Call<UserEmailVerify> call, Throwable t) {

          }
        });
      }
    });

    verifyEmailButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(EmailActivity.this, LogInActivity.class);

        if (user.equals("teacher")) {
          intent.putExtra("user", user);
        } else {
          intent.putExtra("user", "student");
        }
        startActivity(intent);
      }
    });
  }
}
