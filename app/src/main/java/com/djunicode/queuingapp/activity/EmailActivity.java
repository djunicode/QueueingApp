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
  private Integer id;

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
        String email = signUpEmailEditText.getText().toString();
        Call<UserEmailVerify> call = apiInterface.sendEmail("", email, "");
        if (user.equals("teacher")) {
          if (email.contains("@djsce.ac.in")) {
            call.enqueue(new Callback<UserEmailVerify>() {
              @Override
              public void onResponse(Call<UserEmailVerify> call,
                  Response<UserEmailVerify> response) {
                Integer id = response.body().getId();
                Log.i("ID:", Integer.toString(id));
              }

              @Override
              public void onFailure(Call<UserEmailVerify> call, Throwable t) {
                Log.e("Error:", t.getMessage());
              }
            });
          } else {
            Log.e("Error: ", "Invalid teacher email");
          }
        } else {
          call.enqueue(new Callback<UserEmailVerify>() {
            @Override
            public void onResponse(Call<UserEmailVerify> call, Response<UserEmailVerify> response) {
              id = response.body().getId();
              Log.i("ID:", Integer.toString(id));
            }

            @Override
            public void onFailure(Call<UserEmailVerify> call, Throwable t) {
              Log.e("Error:", t.getMessage());
            }
          });
        }
      }
    });

    verifyEmailButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(EmailActivity.this, LogInActivity.class);

       Call<UserEmailVerify> call = apiInterface
           .verifyEmail(id, verifyEditText.getText().toString());
       call.enqueue(new Callback<UserEmailVerify>() {
         @Override
         public void onResponse(Call<UserEmailVerify> call, Response<UserEmailVerify> response) {
           Boolean valid = response.body().getValid();
           Log.i("Token: ", valid.toString());
         }

         @Override
         public void onFailure(Call<UserEmailVerify> call, Throwable t) {

         }
       });
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
