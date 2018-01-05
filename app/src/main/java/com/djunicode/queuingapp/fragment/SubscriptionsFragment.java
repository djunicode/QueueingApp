package com.djunicode.queuingapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.lib.MultiSelectionSpinner;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionsFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{


  public SubscriptionsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);

    String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
    MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) view.findViewById(R.id.mySpinner);
    multiSelectionSpinner.setItems(array);
    multiSelectionSpinner.setSelection(new int[]{2, 6});
    multiSelectionSpinner.setListener(this);
    return view;
  }

  @Override
  public void selectedIndices(List<Integer> indices) {

  }

  @Override
  public void selectedStrings(List<String> strings) {
    Toast.makeText(getContext(), strings.toString(), Toast.LENGTH_LONG).show();
  }
}
