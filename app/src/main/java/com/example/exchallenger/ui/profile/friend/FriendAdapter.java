package com.example.exchallenger.ui.profile.friend;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.exchallenger.R;

import butterknife.BindView;

import com.example.exchallenger.base.BaseRecyclerViewAdapter;
import com.example.exchallenger.base.BaseViewHolder;
import com.utilityview.customview.CustomTextviewFonts;

import java.util.Random;

public class FriendAdapter extends BaseRecyclerViewAdapter<Object> {


    public FriendAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendVH(mInflater.inflate(R.layout.item_friend, parent, false));
    }

    public class FriendVH extends BaseViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        CustomTextviewFonts tvName;
        @BindView(R.id.iv_rank)
        ImageView ivRank;
        @BindView(R.id.tv_rank)
        CustomTextviewFonts tvRank;
        @BindView(R.id.tv_rank_point)
        CustomTextviewFonts tvRankPoint;
        int random = new Random().nextInt(4000);

        public FriendVH(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            tvName.setText("Joey");

            tvRankPoint.setText("Elo: " + random);

            switch (random / 250) {
                case 0:
                    Glide.with(itemView)
                            .load(R.drawable.rank_rat_1)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_rat_1);
                    break;
                case 1:
                    Glide.with(itemView)
                            .load(R.drawable.rank_rat_1)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_rat_2);
                    break;
                case 2:
                    Glide.with(itemView)
                            .load(R.drawable.rank_rat_1)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_rat_3);
                    break;
                case 3:
                    Glide.with(itemView)
                            .load(R.drawable.rank_rat_1)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_rat_4);
                    break;
                case 4:
                    Glide.with(itemView)
                            .load(R.drawable.rank_buffalo)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_buffalo_1);
                    break;
                case 5:
                    Glide.with(itemView)
                            .load(R.drawable.rank_buffalo)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_buffalo_2);
                    break;
                case 6:
                    Glide.with(itemView)
                            .load(R.drawable.rank_buffalo)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_buffalo_3);
                    break;
                case 7:
                    Glide.with(itemView)
                            .load(R.drawable.rank_buffalo)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_buffalo_4);
                    break;
                case 8:
                    Glide.with(itemView)
                            .load(R.drawable.rank_tiger)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_tiger_1);
                    break;
                case 9:
                    Glide.with(itemView)
                            .load(R.drawable.rank_tiger)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_tiger_2);
                    break;
                case 10:
                    Glide.with(itemView)
                            .load(R.drawable.rank_tiger)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_tiger_3);
                    break;
                case 11:
                    Glide.with(itemView)
                            .load(R.drawable.rank_tiger)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_tiger_4);
                    break;
                case 12:
                    Glide.with(itemView)
                            .load(R.drawable.rank_dragon)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_dragon_1);
                    break;
                case 13:
                    Glide.with(itemView)
                            .load(R.drawable.rank_dragon)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_dragon_2);
                    break;
                case 14:
                    Glide.with(itemView)
                            .load(R.drawable.rank_dragon)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_dragon_3);
                    break;
                case 15:
                    Glide.with(itemView)
                            .load(R.drawable.rank_dragon)
                            .into(ivRank);
                    tvRank.setText(R.string.rank_dragon_4);
                    break;
            }
        }
    }
}
