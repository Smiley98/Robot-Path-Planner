package com.smiley98.robot_path_planner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smiley98.robot_path_planner.Events.Bus;
import com.smiley98.robot_path_planner.Events.GenericEvent;

import java.util.List;

public class PathNameView extends RecyclerView.Adapter<PathNameView.ViewHolder> {
    private final List<String> mPathNames;

    PathNameView(List<String> pathNames) {
        mPathNames = pathNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.path_name_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mPathNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mPathNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.txtPathName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bus.post(new ItemClickEvent(mTextView.getText().toString()));
        }
    }

    public static class ItemClickEvent extends GenericEvent<String> {
        public ItemClickEvent(String data) {
            super(data);
        }
    }
}
