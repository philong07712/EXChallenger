package com.example.exchallenger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.exchallenger.Activities.WorkoutDetailActivity;
import com.example.exchallenger.Helpers.LocalSaveHelper;
import com.example.exchallenger.Helpers.MainHelper;
import com.example.exchallenger.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    Context mContext;
    List<Map<String, Object>> list;

    private int radius;

    public WorkoutAdapter(Context mContext, List<Map<String, Object>> list) {
        this.mContext = mContext;
        this.list = list;
        radius = mContext.getResources().getDimensionPixelSize(R.dimen.radius_workout);
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_workout_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {

        Glide.with(mContext).asBitmap()
                .load(list.get(position).get("photo").toString())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(radius)))
                .into(holder.img);
        holder.tv_name.setText(list.get(position).get("name").toString());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView tv_name;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.row_workout_image);
            tv_name = itemView.findViewById(R.id.row_workout_name);

            // set for the user click the image will turn to activity detail
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WorkoutDetailActivity.class);
                    int position = getAdapterPosition();

                    // we will throw the name, image, and the workout ID
                    intent.putExtra("name", list.get(position).get("name").toString());
                    intent.putExtra("workoutID", list.get(position).get("workoutID").toString());
                    intent.putExtra("photo", list.get(position).get("photo").toString());

                    // save String
                    LocalSaveHelper helper = new LocalSaveHelper(mContext);
                    // save groupID
                    helper.saveMap("currentWorkout", list.get(position));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
