package com.example.exchallenger.challenge;

import android.content.Context;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.exchallenger.Models.ChallengeItem;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseRecyclerViewAdapter;
import com.example.exchallenger.base.BaseViewHolder;
import com.example.exchallenger.utils.AppUtils;
import com.utilityview.customview.CustomTextviewFonts;

import butterknife.BindView;

public class ChallengeAdapter extends BaseRecyclerViewAdapter<ChallengeItem> {

    private boolean isAdmin;

    public ChallengeAdapter(@NonNull Context context, boolean isAdmin) {
        super(context);
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChallengeVH(mInflater.inflate(R.layout.item_challenge, parent, false));
    }

    public class ChallengeVH extends BaseViewHolder {
        @BindView(R.id.iv_challenge)
        ImageView ivChallenge;
        @BindView(R.id.iv_done)
        ImageView ivDone;
        @BindView(R.id.tv_challenge)
        CustomTextviewFonts tvChallenge;
        @BindView(R.id.tv_schedule_type)
        CustomTextviewFonts tvScheduleType;
        @BindView(R.id.tv_count)
        CustomTextviewFonts tvCount;
        @BindView(R.id.tv_time)
        CustomTextviewFonts tvTime;
        @BindView(R.id.tv_point)
        CustomTextviewFonts tvPoint;

        public ChallengeVH(View itemView) {
            super(itemView);

        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChallengeItem challengeItem = mDataList.get(position);
            if (TextUtils.isEmpty(challengeItem.getType())) {
                return;
            }
            if (!TextUtils.isEmpty(challengeItem.getId())) {
                ivDone.setVisibility(challengeItem.isDone() ? View.VISIBLE : View.GONE);
            } else {
                ivDone.setVisibility(View.GONE);
            }
            tvChallenge.setText(challengeItem.getType());
            if (challengeItem.getType().toLowerCase().equals(ChallengeItem.PLANK)) {
                tvCount.setText(challengeItem.getNumber() + "m");
            } else {
                tvCount.setText("x" + challengeItem.getNumber());
            }
            tvPoint.setText(challengeItem.getPoint() + " points");
            tvTime.setText(AppUtils.getTimeFromHourAndUnit(challengeItem.getHour(), challengeItem.getMinute()));
            tvScheduleType.setText(AppUtils.getScheduleText(challengeItem.getRepeat()));
            if (TextUtils.isEmpty(challengeItem.getPhoto())) {
                Glide.with(itemView)
                        .load(AppUtils.getPhotoOfExercise(challengeItem.getType()))
                        .into(ivChallenge);
            } else {
                Glide.with(itemView)
                        .load(challengeItem.getPhoto())
                        .into(ivChallenge);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {
                        getOnItemClickListener().onItemClick(position, challengeItem);
                    }
                }
            });
            if(isAdmin){
                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(position, 0, 0, "Edit");
                        menu.add(position, 1, 1, "Delete");
                    }
                });
            }

        }



    }

}
