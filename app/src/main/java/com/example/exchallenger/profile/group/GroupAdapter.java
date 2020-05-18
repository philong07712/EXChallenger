package com.example.exchallenger.profile.group;

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

public class GroupAdapter extends BaseRecyclerViewAdapter<Object> {


    public GroupAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupVH(mInflater.inflate(R.layout.item_group, parent, false));
    }

    public class GroupVH extends BaseViewHolder {

        @BindView(R.id.iv_group)
        ImageView ivGroup;
        @BindView(R.id.tv_group)
        CustomTextviewFonts tvGroup;
        @BindView(R.id.iv_count)
        ImageView ivCount;
        @BindView(R.id.tv_count)
        CustomTextviewFonts tvCount;
        @BindView(R.id.tv_your_rank)
        CustomTextviewFonts tvYourRank;

        public GroupVH(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            tvGroup.setText("Six packs boys");
            tvCount.setText("8");


        }
    }
}
