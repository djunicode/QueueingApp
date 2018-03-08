package com.djunicode.queuingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
  public static Integer id;
  private SharedPreferences preferences;

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
    preferences = this.getSharedPreferences("com.djunicode.queuingapp", MODE_PRIVATE);

//    verifyEmailButton.setEnabled(false);

    sendCodeButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String email = signUpEmailEditText.getText().toString();
//        email.split("@")[0]
        Call<UserEmailVerify> call = apiInterface.sendEmail("Ruturaj20", email, "demopass");
        if (user.equals("teacher")) {
          if (email.contains("@djsce.ac.in")) {
            call.enqueue(new Callback<UserEmailVerify>() {
              @Override
              public void onResponse(@NonNull Call<UserEmailVerify> call,
                  @NonNull Response<UserEmailVerify> response) {
                id = response.body().getId();
                preferences.edit().putInt("teacherID", id).apply();
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
              preferences.edit().putInt("studentID", id).apply();
              Log.e("ID:", id.toString());
            }

            @Override
            public void onFailure(Call<UserEmailVerify> call, Throwable t) {
              Log.e("Error:", t.getMessage());
            }
          });
        }
        sendCodeButton.setEnabled(false);
        verifyEmailButton.setEnabled(true);
      }
    });

    verifyEmailButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final Intent intent = new Intent(EmailActivity.this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Call<UserEmailVerify> call = apiInterface
            .verifyEmail(id, verifyEditText.getText().toString());
        call.enqueue(new Callback<UserEmailVerify>() {
          @Override
          public void onResponse(Call<UserEmailVerify> call, Response<UserEmailVerify> response) {
            Boolean valid = response.body().getValid();
            Log.e("Valid:", valid.toString());

            if(valid){
              if (user.equals("teacher")) {
                intent.putExtra("user", user);
              } else {
                intent.putExtra("user", "student");
              }
              startActivity(intent);
            }
          }

          @Override
          public void onFailure(Call<UserEmailVerify> call, Throwable t) {
            Log.e("Error:", t.getMessage());
          }
        });

//        if (user.equals("teacher")) {
//          intent.putExtra("user", user);
//        } else {
//          intent.putExtra("user", "student");
//        }
//        startActivity(intent);

      }
    });
  }
}
