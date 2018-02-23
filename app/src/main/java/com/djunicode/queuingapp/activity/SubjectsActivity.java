package com.djunicode.queuingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.customClasses.MultiSelectionSpinner;
import com.djunicode.queuingapp.customClasses.MultiSelectionSpinner.OnMultipleItemsSelectedListener;
import com.djunicode.queuingapp.customClasses.ObjectSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {

  private MultiSelectionSpinner oddSemSpinner, evenSemSpinner;
  private FloatingActionButton fab;
  private ArrayList<String> subjects;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subjects);

    oddSemSpinner = (MultiSelectionSpinner) findViewById(R.id.oddSemSubjectSpinner);
    evenSemSpinner = (MultiSelectionSpinner) findViewById(R.id.evenSemSubjectSpinner);
    fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
    subjects = new ArrayList<>();

    final SharedPreferences preferences = this
        .getSharedPreferences("com.djunicode.queuingapp", MODE_PRIVATE);

    String[] array = {"Select", "one", "two", "three", "four", "five", "six", "seven", "eight",
        "nine", "ten"};
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_dropdown_item, array);

    oddSemSpinner.setItems(array);
    evenSemSpinner.setItems(array);

    fab.setEnabled(false);

    oddSemSpinner.setListener(new OnMultipleItemsSelectedListener() {
      @Override
      public void selectedIndices(List<Integer> indices) {

      }

      @Override
      public void selectedStrings(List<String> strings) {
        subjects.addAll(strings);
        try {
          preferences.edit().putString("subjects", ObjectSerializer.serialize(subjects)).apply();
        } catch (IOException e) {
          e.printStackTrace();
        }
        evenSemSpinner.setEnabled(false);
        fab.setEnabled(true);
        Toast.makeText(SubjectsActivity.this, "Subjects:" + strings.toString(), Toast.LENGTH_LONG)
            .show();
      }
    });

    evenSemSpinner.setListener(new OnMultipleItemsSelectedListener() {
      @Override
      public void selectedIndices(List<Integer> indices) {

      }

      @Override
      public void selectedStrings(List<String> strings) {
        subjects.addAll(strings);
        try {
          preferences.edit().putString("subjects", ObjectSerializer.serialize(subjects)).apply();
        } catch (IOException e) {
          e.printStackTrace();
        }
        oddSemSpinner.setEnabled(false);
        fab.setEnabled(true);
        Toast.makeText(SubjectsActivity.this, "Subjects:" + strings.toString(), Toast.LENGTH_LONG)
            .show();
      }
    });

    fab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(SubjectsActivity.this, TeacherScreenActivity.class);
        startActivity(intent);
      }
    });
  }
}
