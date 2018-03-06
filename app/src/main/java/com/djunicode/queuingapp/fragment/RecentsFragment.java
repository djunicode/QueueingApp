package com.djunicode.queuingapp.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.adapter.RecentsAdapter;
import com.djunicode.queuingapp.customClasses.RecyclerItemTouchHelper;
import com.djunicode.queuingapp.customClasses.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import com.djunicode.queuingapp.data.QueuesContract.QueuesEntry;
import com.djunicode.queuingapp.data.QueuesDbHelper;
import com.djunicode.queuingapp.model.RecentEvents;
import com.djunicode.queuingapp.model.TeacherCreateNew;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentsFragment extends Fragment implements
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

  private RecyclerView recentsRecyclerView;
  private RecentsAdapter adapter;
  private RelativeLayout relativeLayout;
  private SQLiteDatabase db;
  private List<RecentEvents> recentEventsList;
  private ApiInterface apiInterface;
  private QueuesDbHelper dbHelper;

  public RecentsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_recents, container, false);

    recentsRecyclerView = (RecyclerView) view.findViewById(R.id.recentsRecyclerView);
    recentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    relativeLayout = (RelativeLayout) view.findViewById(R.id.recentsRelativeLayout);
    apiInterface = ApiClient.getClient().create(ApiInterface.class);

    dbHelper = new QueuesDbHelper(getContext());
    db = dbHelper.getWritableDatabase();

    recentEventsList = dbHelper.getAllQueues();
    Log.i("event size", Integer.toString(recentEventsList.size()));

    adapter = new RecentsAdapter(getContext(), recentEventsList);

    recentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    recentsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
        DividerItemDecoration.VERTICAL));
    recentsRecyclerView.setAdapter(adapter);

    ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,
        ItemTouchHelper.LEFT, this);
    new ItemTouchHelper(simpleCallback).attachToRecyclerView(recentsRecyclerView);

    return view;
  }

  @Override
  public void onSwiped(ViewHolder viewHolder, int direction, int position) {
    if (viewHolder instanceof RecentsAdapter.MyViewHolder) {
      final RecentEvents event = recentEventsList.get(viewHolder.getAdapterPosition());

      final int deletedIndex = viewHolder.getAdapterPosition();
      adapter.removeItem(viewHolder.getAdapterPosition());

      final CountDownTimer timer = new CountDownTimer(5000, 1000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {
          Call<TeacherCreateNew> call1 = apiInterface
              .deleteQueueLinkFromTeacher(37, event.getServerId());
          call1.enqueue(new Callback<TeacherCreateNew>() {
            @Override
            public void onResponse(Call<TeacherCreateNew> call,
                Response<TeacherCreateNew> response) {
              Call<TeacherCreateNew> call2 = apiInterface.deleteQueue(event.getServerId());
              call2.enqueue(new Callback<TeacherCreateNew>() {
                @Override
                public void onResponse(Call<TeacherCreateNew> call,
                    Response<TeacherCreateNew> response) {
                  dbHelper.deleteQueue(event);
                }

                @Override
                public void onFailure(Call<TeacherCreateNew> call, Throwable t) {

                }
              });
            }

            @Override
            public void onFailure(Call<TeacherCreateNew> call, Throwable t) {

            }
          });
        }
      }.start();

      Snackbar snackbar = Snackbar
          .make(relativeLayout, "Removed from recents!", Snackbar.LENGTH_LONG);
      snackbar.setAction("UNDO", new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          timer.cancel();
          adapter.restoreItem(event, deletedIndex);
        }
      });
      snackbar.setActionTextColor(Color.YELLOW);
      snackbar.show();
    }
  }
}


