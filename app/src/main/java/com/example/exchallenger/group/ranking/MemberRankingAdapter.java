package com.example.exchallenger.group.ranking;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseRecyclerViewAdapter;
import com.example.exchallenger.base.BaseViewHolder;
import com.utilityview.customview.CustomTextviewFonts;

import butterknife.BindView;

public class MemberRankingAdapter extends BaseRecyclerViewAdapter<Object> {


    public MemberRankingAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberRankingVH(mInflater.inflate(R.layout.item_member_ranking, parent, false));
    }

    public class MemberRankingVH extends BaseViewHolder {

        @BindView(R.id.tv_pos)
        CustomTextviewFonts tvPos;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_point)
        CustomTextviewFonts tvPoint;

        public MemberRankingVH(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            tvPos.setText("" + (position + 1));
        }
    }
}
