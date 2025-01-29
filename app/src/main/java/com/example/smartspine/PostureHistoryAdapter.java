package com.example.smartspine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostureHistoryAdapter extends RecyclerView.Adapter<PostureHistoryAdapter.ViewHolder> {

    private List<PostureData> postureDataList;

    public PostureHistoryAdapter(List<PostureData> postureDataList) {
        this.postureDataList = postureDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posture_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostureData postureData = postureDataList.get(position);
        holder.postureTextView.setText(postureData.getPosture());
        holder.timestampTextView.setText(postureData.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return postureDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postureTextView;
        public TextView timestampTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postureTextView = itemView.findViewById(R.id.postureTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }
}
