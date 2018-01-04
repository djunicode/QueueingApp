package com.djunicode.queuingapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.EditText;
import com.djunicode.queuingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInStudentFragment extends Fragment {

  private EditText usernameLogInEditText, passwordLogInEditText;
  private CardView logInStudentButton;

  public LogInStudentFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_log_in_student, container, false);

    usernameLogInEditText = (EditText) view.findViewById(R.id.usernameEditText);
    passwordLogInEditText = (EditText) view.findViewById(R.id.passwordEditText);
    logInStudentButton = (CardView) view.findViewById(R.id.logInStudentButton);

    String username = usernameLogInEditText.getText().toString();
    String password = passwordLogInEditText.getText().toString();
    return view;
  }

}
