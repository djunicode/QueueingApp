package com.djunicode.queuingapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.SessionManagement.SessionManager;
import com.djunicode.queuingapp.activity.StudentScreenActivity;

import com.djunicode.queuingapp.activity.SubjectsActivity;
import com.djunicode.queuingapp.activity.TeacherScreenActivity;
import com.djunicode.queuingapp.model.TeacherModel;
import com.djunicode.queuingapp.model.UserModel;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.djunicode.queuingapp.activity.EmailActivity.id;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpTeacherFragment extends Fragment {

  private EditText usernameTeacherEditText, sapIDTeacherEditText, passwordTeacherEditText;
  private Spinner departmentTeacherSpinner;
  private CardView signUpTeacherButton;
  private TextInputLayout teacherSignUpinputLayoutUsername, teacherSignUpinputLayoutPassword,
      teacherSignUpinputLayoutDepartment, teacherSignUpinputLayoutSAPId;
  SessionManager session;
  private String username, dept, sapId, pass;
  ApiInterface apiInterface;

  public SignUpTeacherFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_sign_up_teacher, container, false);

    teacherSignUpinputLayoutUsername = (TextInputLayout) view.findViewById(R.id.signUp_teacher_username);
    teacherSignUpinputLayoutPassword = (TextInputLayout) view.findViewById(R.id.signUp_teacher_password);
    teacherSignUpinputLayoutDepartment = (TextInputLayout) view.findViewById(R.id.signUp_teacher_Department);
    teacherSignUpinputLayoutSAPId = (TextInputLayout) view.findViewById(R.id.signUp_teacher_SAPId);
    usernameTeacherEditText = (EditText) view.findViewById(R.id.usernameTeacherEditText);
    passwordTeacherEditText = (EditText) view.findViewById(R.id.passwordTeacherEditText);
    sapIDTeacherEditText = (EditText) view.findViewById(R.id.sapIDTeacherEditText);
    departmentTeacherSpinner = (Spinner) view.findViewById(R.id.departmentTeacherSpinner);
    signUpTeacherButton = (CardView) view.findViewById(R.id.signUpTeacherButton);

    apiInterface = ApiClient.getClient().create(ApiInterface.class);

    // Session Manager
    session = new SessionManager(getContext(), "Teacher");

    signUpTeacherButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(validSubmission()) {
          username = usernameTeacherEditText.getText().toString();
          dept = departmentTeacherSpinner.getSelectedItem().toString();
          sapId = sapIDTeacherEditText.getText().toString();
          pass = passwordTeacherEditText.getText().toString();
          Call<TeacherModel> call = apiInterface.createTeacherAccount(username, 11, 25,
                  "AOA", sapId);
          call.enqueue(new Callback<TeacherModel>() {
            @Override
            public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
              if (response.isSuccessful()) {
                Log.e("teacherSignUp", "successful");
                updateDataOnUserUrl();
              }
              else
                Log.e("teacherSignUp", response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<TeacherModel> call, Throwable t) {
              Log.e("teacherSignUp", "unsuccessful");
            }
          });
          session.createLoginSession(usernameTeacherEditText.getText().toString(),
              passwordTeacherEditText.getText().toString(), usernameTeacherEditText.getText().
                          toString());
          Intent intent = new Intent(getContext(), SubjectsActivity.class);
          // StudentScreenActivity just for demo till the time teacher fragments are not ready
          startActivity(intent);
          Toast.makeText(getContext(), usernameTeacherEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
          Toast.makeText(getContext(), passwordTeacherEditText.getText().toString(),
              Toast.LENGTH_SHORT).show();
        }
      }

      private void updateDataOnUserUrl() {

        Call<UserModel> call1 = apiInterface.updateUserData(3, username, pass);
        call1.enqueue(new Callback<UserModel>() {
          @Override
          public void onResponse(Call<UserModel> call, Response<UserModel> response) {
            Log.e("teacherSignUp", "User data updation successful");
          }

          @Override
          public void onFailure(Call<UserModel> call, Throwable t) {
            Log.e("teacherSignUp", "User data updation unsuccessful");
          }
        });

      }
    });

    return view;
  }

  private Boolean validSubmission () {

    if (!validateUsername()) {
      return false;
    }

    if (!validateDepartment()) {
      return false;
    }

    if (!validateSAPId()) {
      return false;
    }

    if (!validatePassword()) {
      return false;
    }

    return true;
  }

  private boolean validateUsername() {
    if (usernameTeacherEditText.getText().toString().trim().isEmpty()) {
      teacherSignUpinputLayoutUsername.setError(getString(R.string.err_msg_username));
      requestFocus(usernameTeacherEditText);
      return false;
    } else if ((usernameTeacherEditText.getText().toString().length() < 5)) {
      teacherSignUpinputLayoutUsername.setError(getString(R.string.err_msg_username_inappropriate_length));
      requestFocus(usernameTeacherEditText);
      return false;
    }else {
      teacherSignUpinputLayoutUsername.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validateSAPId() {
    if (sapIDTeacherEditText.getText().toString().trim().isEmpty()) {
      teacherSignUpinputLayoutSAPId.setError(getString(R.string.err_msg_SAPId));
      requestFocus(sapIDTeacherEditText);
      return false;
    } else if ((sapIDTeacherEditText.getText().toString().length() != 11)) {
      teacherSignUpinputLayoutSAPId.setError(getString(R.string.err_msg_SAPId_inappropriate_length));
      requestFocus(sapIDTeacherEditText);
      return false;
    }else {
      teacherSignUpinputLayoutSAPId.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validatePassword() {
    if (passwordTeacherEditText.getText().toString().trim().isEmpty()) {
      teacherSignUpinputLayoutPassword.setError(getString(R.string.err_msg_password));
      requestFocus(passwordTeacherEditText);
      return false;
    } else if ((passwordTeacherEditText.getText().toString().length() < 5)) {
      teacherSignUpinputLayoutPassword.setError(getString(R.string.err_msg_password_inappropriate_length));
      requestFocus(passwordTeacherEditText);
      return false;
    }else {
      teacherSignUpinputLayoutPassword.setErrorEnabled(false);
    }

    return true;
  }

  private boolean validateDepartment() {
    if(departmentTeacherSpinner.getSelectedItemPosition() == 0) {
      teacherSignUpinputLayoutDepartment.setError(getString(R.string.err_msg_department));
      requestFocusSpinner(departmentTeacherSpinner);
      return false;
    }
    else {
      teacherSignUpinputLayoutDepartment.setError("");
      return true;
    }
  }

  private void requestFocus (View view) {

    if(view.requestFocus()){
      getActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
  }

  private void requestFocusSpinner(View view) {
    if (view.requestFocus()) {
      getActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
  }
}
