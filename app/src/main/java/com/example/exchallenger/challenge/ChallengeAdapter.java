package com.example.exchallenger.challenge;

import android.content.Context;
import android.text.TextUtils;
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

    public ChallengeAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChallengeVH(mInflater.inflate(R.layout.item_challenge, parent, false));
    }

    public class ChallengeVH extends BaseViewHolder {
        @BindView(R.id.iv_challenge)
        ImageView ivChallenge;
        @BindView(R.id.tv_challenge)
        CustomTextviewFonts tvChallenge;
        @BindView(R.id.tv_schedule_type)
        CustomTextviewFonts tvScheduleType;
        @BindView(R.id.tv_count)
        CustomTextviewFonts tvCount;
        @BindView(R.id.tv_time)
        CustomTextviewFonts tvTime;

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
            if(TextUtils.isEmpty(challengeItem.getType())){
                return;
            }
            tvChallenge.setText(challengeItem.getType());
            tvCount.setText("x" + challengeItem.getNumber());
            tvTime.setText(AppUtils.getTimeFromHourAndUnit(challengeItem.getHour(), challengeItem.getMinute()));
            tvScheduleType.setText(AppUtils.getScheduleText(challengeItem.getRepeat()));
            switch (challengeItem.getType().toLowerCase()) {
                case ChallengeItem.SQUAT:
                    Glide.with(itemView)
                            .load(R.drawable.ic_squat)
                            .into(ivChallenge);
                    break;
                case ChallengeItem.PUSH_UP:
                    Glide.with(itemView)
                            .load(R.drawable.ic_push_up)
                            .into(ivChallenge);
                    break;
                case ChallengeItem.PLANK:
                    Glide.with(itemView)
                            .load(R.drawable.ic_plank)
                            .into(ivChallenge);
                    break;
            }
        }

    }

}
