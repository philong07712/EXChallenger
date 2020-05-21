package com.example.exchallenger.ui.profile.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.exchallenger.models.Group;
import com.example.exchallenger.R;

import butterknife.BindView;

import com.example.exchallenger.base.BaseRecyclerViewAdapter;
import com.example.exchallenger.base.BaseViewHolder;
import com.utilityview.customview.CustomTextviewFonts;

public class GroupAdapter extends BaseRecyclerViewAdapter<Group> {


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
            Group group = mDataList.get(position);
            tvGroup.setText(group.getName());
            tvCount.setText(group.getMembers() != null ? "" + group.getMembers().size() : "0");
            Glide.with(itemView)
                    .load(group.getPhoto())
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.ic_group_default)
                                    .transforms(new CenterCrop(),
                                            new RoundedCorners(getContext().getResources()
                                                    .getDimensionPixelSize(R.dimen.height_ava_group) / 2))
                    )
                    .into(ivGroup);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {
                        getOnItemClickListener().onItemClick(position, mDataList.get(position));
                    }
                }
            });

        }
    }
}
