package com.djunicode.queuingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.activity.StudentQueueActivity;
import com.djunicode.queuingapp.model.RecentEvents;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableQueuesAdapter extends
    RecyclerView.Adapter<AvailableQueuesAdapter.MyViewHolder> {

  private Context context;
  private List<RecentEvents> recentEventsList;
  private ApiInterface apiInterface;
  private SharedPreferences preferences;

  public AvailableQueuesAdapter(Context context, List<RecentEvents> recentEventsList) {
    this.context = context;
    this.recentEventsList = recentEventsList;
    apiInterface = ApiClient.getClient().create(ApiInterface.class);
    preferences = context.getSharedPreferences("Student", Context.MODE_PRIVATE);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView locationTextView, fromTextView, toTextView, subjectTextView, startedTextView;

    public MyViewHolder(View itemView) {
      super(itemView);
      locationTextView = (TextView) itemView.findViewById(R.id.locationTextView);
      fromTextView = (TextView) itemView.findViewById(R.id.fromTextView);
      toTextView = (TextView) itemView.findViewById(R.id.toTextView);
      subjectTextView = (TextView) itemView.findViewById(R.id.subjectTextView);
      startedTextView = (TextView) itemView.findViewById(R.id.startedTextView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      RecentEvents event = recentEventsList.get(getAdapterPosition());
      String sapId = preferences.getString("student_sapid", "60004160035");
      final Intent intent = new Intent(context, StudentQueueActivity.class);
      intent.putExtra("id", event.getServerId());
      intent.putExtra("sapId", sapId);
      Log.e("server id", Integer.toString(event.getServerId()));
      Call<StudentQueue> call = apiInterface
          .studentJoiningTheQueue(event.getServerId(), sapId);
      call.enqueue(new Callback<StudentQueue>() {
        @Override
        public void onResponse(Call<StudentQueue> call, Response<StudentQueue> response) {
          if (response.isSuccessful()) {
            Log.i("studentJoining Res", response.body().toString());
            context.startActivity(intent);
          } else {
            Log.i("studentJoining Res", response.errorBody().toString());
          }
        }

        @Override
        public void onFailure(Call<StudentQueue> call, Throwable t) {
          Log.i("studentJoining ResF", t.getMessage().toString());
        }
      });
    }
  }

  @Override
  public AvailableQueuesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.available_queues_list_row, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(AvailableQueuesAdapter.MyViewHolder holder, int position) {
    RecentEvents event = recentEventsList.get(position);
    holder.locationTextView.setText("Not yet set.");
    holder.fromTextView.setText(event.getStartTime());
    holder.toTextView.setText(event.getEndTime());
    holder.subjectTextView.setText(event.getSubjectName());
    holder.startedTextView.setText("No");
  }

  @Override
  public int getItemCount() {
    return recentEventsList.size();
  }
}
