package com.djunicode.queuingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
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
import com.djunicode.queuingapp.model.TeacherModel;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {

  private MultiSelectionSpinner oddSemSpinner, evenSemSpinner;
  private FloatingActionButton fab;
  private ArrayList<String> subjects;
  private ApiInterface apiInterface;
  private Integer id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subjects);

    apiInterface = ApiClient.getClient().create(ApiInterface.class);

    Intent intent = getIntent();
    id = intent.getIntExtra("teacherId", 0);

    oddSemSpinner = (MultiSelectionSpinner) findViewById(R.id.oddSemSubjectSpinner);
    evenSemSpinner = (MultiSelectionSpinner) findViewById(R.id.evenSemSubjectSpinner);
    fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
    subjects = new ArrayList<>();

    final SharedPreferences preferences = this
        .getSharedPreferences("com.djunicode.queuingapp", MODE_PRIVATE);
    Resources res = getResources();

    Log.e("teacherId", id.toString());

    oddSemSpinner.setItems(res.getStringArray(R.array.comps_odd));
    evenSemSpinner.setItems(res.getStringArray(R.array.comps_even));

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
        addingSubjects();
        Intent intent = new Intent(SubjectsActivity.this, TeacherScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
    });
  }


  void addingSubjects() {
    for(int i=0; i< subjects.size(); i++){
      Log.i("subject", subjects.get(i).toString());
      Call<TeacherModel> call = apiInterface.addTeacherSubjects(id, subjects.get(i).toString());
      call.enqueue(new Callback<TeacherModel>() {
        @Override
        public void onResponse(Call<TeacherModel> call, Response<TeacherModel> response) {
//          Log.i("Subjects", response.body().getSubject().toString());
        }

        @Override
        public void onFailure(Call<TeacherModel> call, Throwable t) {
          Log.i("Error", t.getMessage());
        }
      });
    }
  }
}
