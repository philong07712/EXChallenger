package com.example.exchallenger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.exchallenger.Factories.WorkoutFactory;
import com.example.exchallenger.Helpers.WorkoutHelper;
import com.example.exchallenger.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkoutDetailAdapter extends RecyclerView.Adapter<WorkoutDetailAdapter.DetailViewHolder> {

    Context mContext;
    List<Map<String, Object>> list;

    public WorkoutDetailAdapter(Context mContext, List<Map<String, Object>> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_workout_detail_item, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        String point = "+ " + list.get(position).get("point");
        String rep = "x " + list.get(position).get("rep");
        holder.point.setText(point);
        holder.rep.setText(rep);
        holder.name.setText(list.get(position).get("name").toString());
        Glide.with(mContext).load(list.get(position).get("photo").toString())
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder
    {
        ImageView photo;
        TextView name, rep, point;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.row_workout_detail_photo);
            name = itemView.findViewById(R.id.row_workout_detail_name);
            rep = itemView.findViewById(R.id.row_workout_detail_rep);
            point = itemView.findViewById(R.id.row_workout_detail_point);
        }
    }




}
