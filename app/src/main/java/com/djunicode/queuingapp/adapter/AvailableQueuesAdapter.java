package com.djunicode.queuingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.djunicode.queuingapp.R;
import com.djunicode.queuingapp.activity.StudentQueueActivity;
import com.djunicode.queuingapp.model.RecentEvents;
import java.util.List;

public class AvailableQueuesAdapter extends
        RecyclerView.Adapter<AvailableQueuesAdapter.MyViewHolder> {

    private Context context;
    private List<RecentEvents> recentEventsList;

    public AvailableQueuesAdapter(Context context, List<RecentEvents> recentEventsList) {
        this.context = context;
        this.recentEventsList = recentEventsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView locationTextView, fromTextView, toTextView, batchTextView, startedTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            locationTextView = (TextView) itemView.findViewById(R.id.locationTextView);
            fromTextView = (TextView) itemView.findViewById(R.id.fromTextView);
            toTextView = (TextView) itemView.findViewById(R.id.toTextView);
            batchTextView = (TextView) itemView.findViewById(R.id.batchTextView);
            startedTextView = (TextView) itemView.findViewById(R.id.startedTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RecentEvents event = recentEventsList.get(getAdapterPosition());
            Intent intent = new Intent(context, StudentQueueActivity.class);
            intent.putExtra("id", event.getServerId());
            context.startActivity(intent);
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
        holder.locationTextView.setText(event.getLocation());
        holder.fromTextView.setText(event.getStartTime());
        holder.toTextView.setText(event.getEndTime());
        holder.batchTextView.setText(event.getBatchName());
        holder.startedTextView.setText("No");
    }

    @Override
    public int getItemCount() {
        return recentEventsList.size();
    }
}
