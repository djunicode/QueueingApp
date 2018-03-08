package com.djunicode.queuingapp.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.customClasses.MultiSelectionSpinner;
import com.djunicode.queuingapp.customClasses.MultiSelectionSpinner.OnMultipleItemsSelectedListener;
import com.djunicode.queuingapp.model.StudentSubscriptions;
import com.djunicode.queuingapp.model.TeachersList;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionsFragment extends Fragment {

  private Spinner semesterSpinner;
  private MultiSelectionSpinner subjectSpinner, teacherSpinner;
  private FloatingActionButton subscriptionsFab;
  private ApiInterface apiInterface;
  private Integer id;
  private List<String> subscriptionList;
  private List<String> teachers;
  private SharedPreferences preferences;

  public SubscriptionsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);

    String[] array1 = {"Select Semester", "one", "two", "three", "four", "five", "six", "seven",
        "eight"};
    String[] array2 = {"AOA", "COA"};
    String[] array3 = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
        "ten"};

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
        android.R.layout.simple_spinner_dropdown_item, array1);

    semesterSpinner = (Spinner) view.findViewById(R.id.semesterSpinner);
    subjectSpinner = (MultiSelectionSpinner) view.findViewById(R.id.subjectSpinner);
    teacherSpinner = (MultiSelectionSpinner) view.findViewById(R.id.teacherSpinner);
    subscriptionsFab = (FloatingActionButton) view.findViewById(R.id.subscriptionsFab);
    preferences = getActivity()
        .getSharedPreferences("com.djunicode.queuingapp", Context.MODE_PRIVATE);
    apiInterface = ApiClient.getClient().create(ApiInterface.class);

    subjectSpinner.setEnabled(false);
    subjectSpinner.setAlpha(0.4f);
    subjectSpinner.setItems(array2);

    teacherSpinner.setEnabled(false);
    teacherSpinner.setAlpha(0.4f);
    teacherSpinner.setItems(array3);

    semesterSpinner.setAdapter(adapter);
    semesterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String semester = parent.getItemAtPosition(position).toString();
        if (position != 0) {
          subjectSpinner.setEnabled(true);
          subjectSpinner.setAlpha(1.0f);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getContext(), "Please select the semester!", Toast.LENGTH_SHORT).show();
      }
    });

    subjectSpinner.setListener(new OnMultipleItemsSelectedListener() {
      @Override
      public void selectedIndices(List<Integer> indices) {

      }

      @Override
      public void selectedStrings(List<String> selectedSubjects) {
        teacherSpinner.setEnabled(true);
        teacherSpinner.setAlpha(1.0f);
        Toast.makeText(getContext(), "Subjects:" + selectedSubjects.toString(), Toast.LENGTH_LONG)
            .show();
        /*Call<List<TeachersList>> call = apiInterface.getTeacherNames();
        call.enqueue(new Callback<List<TeachersList>>() {
          @Override
          public void onResponse(Call<List<TeachersList>> call, Response<List<TeachersList>> response) {
//            teachersList = response.body();
            Log.i("Teacher Names", response.body().toString());
            teachers = new ArrayList<>();
            List<TeachersList> teachersList = response.body();
            for(int i = 0; i < teachersList.size(); i++){
              teachers.add(teachersList.get(i).getName());
            }
            teacherSpinner.setItems(teachers);
          }

          @Override
          public void onFailure(Call<List<TeachersList>> call, Throwable t) {
            Log.e("Teacher error", t.getMessage());
          }
        });*/
        teachers = new ArrayList<>();
        for (int i = 0; i < selectedSubjects.size(); i++) {
          Call<TeachersList> call = apiInterface.getTeachersList(selectedSubjects.get(i));
          call.enqueue(new Callback<TeachersList>() {
            @Override
            public void onResponse(Call<TeachersList> call, Response<TeachersList> response) {
              Log.e("queues/subject/", response.body().getTeachers().toString());
              List<String> teacherList = response.body().getTeachers();
              for (int i = 0; i < teacherList.size(); i++) {
                teachers.add(teacherList.get(i));
              }
              teacherSpinner.setItems(teachers);
            }

            @Override
            public void onFailure(Call<TeachersList> call, Throwable t) {

            }
          });
        }
      }
    });

    teacherSpinner.setListener(new OnMultipleItemsSelectedListener() {
      @Override
      public void selectedIndices(List<Integer> indices) {

      }

      @Override
      public void selectedStrings(List<String> strings) {
        subscriptionList = strings;
        Toast.makeText(getContext(), "Teachers:" + strings.toString(), Toast.LENGTH_LONG).show();
      }
    });

    subscriptionsFab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("RegId", token);

        JSONArray jsonArray = new JSONArray(subscriptionList);
        Log.e("JSONArray", jsonArray.toString());
        Call<StudentSubscriptions> call = apiInterface
            .setStudentSubscriptions(preferences.getInt("studentID", 0), jsonArray.toString());
        call.enqueue(new Callback<StudentSubscriptions>() {
          @Override
          public void onResponse(Call<StudentSubscriptions> call,
              Response<StudentSubscriptions> response) {
            Log.e("Subs", response.body().getSubscription().toString());
          }

          @Override
          public void onFailure(Call<StudentSubscriptions> call, Throwable t) {

          }
        });
      }
    });

    return view;
  }
}
