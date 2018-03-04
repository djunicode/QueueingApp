package com.djunicode.queuingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.activity.StudentListActivity;
import com.djunicode.queuingapp.model.RecentEvents;
import com.djunicode.queuingapp.model.StudentQueue;
import com.djunicode.queuingapp.model.TeacherCreateNew;
import com.djunicode.queuingapp.rest.ApiClient;
import com.djunicode.queuingapp.rest.ApiInterface;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.MyViewHolder> {

  private Context context;
  private List<RecentEvents> recentEventsList;
  private RecentEvents recentEvent;
  private ApiInterface apiInterface;

  public RecentsAdapter(Context context, List<RecentEvents> recentEventsList){
    this.context = context;
    this.recentEventsList = recentEventsList;
    apiInterface = ApiClient.getClient().create(ApiInterface.class);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView subjectName, batchName, startTime, endTime, location;
    public RelativeLayout viewBackground, viewForeground;

    public MyViewHolder(View itemView) {
      super(itemView);
      subjectName = (TextView) itemView.findViewById(R.id.subjectName);
      batchName = (TextView) itemView.findViewById(R.id.batchName);
      startTime = (TextView) itemView.findViewById(R.id.startTime);
      endTime = (TextView) itemView.findViewById(R.id.endTime);
      location = (TextView) itemView.findViewById(R.id.location);
      viewBackground = (RelativeLayout) itemView.findViewById(R.id.backgroundView);
      viewForeground = (RelativeLayout) itemView.findViewById(R.id.foregroundView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int position = getAdapterPosition();
      RecentEvents event = recentEventsList.get(position);
      final Integer queueId = event.getServerId();
      Call<TeacherCreateNew> call = apiInterface.startTheQueue(queueId, "Aruna Gawde");
      call.enqueue(new Callback<TeacherCreateNew>() {
        @Override
        public void onResponse(Call<TeacherCreateNew> call, Response<TeacherCreateNew> response) {
          Intent intent = new Intent(context, StudentListActivity.class);
          intent.putExtra("id", queueId);
          context.startActivity(intent);
        }

        @Override
        public void onFailure(Call<TeacherCreateNew> call, Throwable t) {

        }
      });
    }
  }

  @Override
  public RecentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recents_row_layout,
            parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecentsAdapter.MyViewHolder holder, int position) {
    recentEvent = recentEventsList.get(position);
    holder.subjectName.setText(recentEvent.getSubjectName());
    holder.batchName.setText(recentEvent.getBatchName());
    holder.startTime.setText(recentEvent.getStartTime());
    holder.endTime.setText(recentEvent.getEndTime());
    holder.location.setText(recentEvent.getLocation());
  }

  @Override
  public int getItemCount() {
    return recentEventsList.size();
  }

  public void removeItem(int position){
    recentEventsList.remove(position);
    notifyItemRemoved(position);
  }

  public void restoreItem(RecentEvents event, int position){
    recentEventsList.add(position, event);
    notifyItemInserted(position);
  }
}
