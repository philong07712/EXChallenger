package com.example.exchallenger.group.ranking;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.exchallenger.Models.GroupMember;
import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseRecyclerViewAdapter;
import com.example.exchallenger.base.BaseViewHolder;
import com.utilityview.customview.CustomTextviewFonts;

import butterknife.BindView;

public class MemberRankingAdapter extends BaseRecyclerViewAdapter<GroupMember> {


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
        @BindView(R.id.tv_name)
        CustomTextviewFonts tvName;
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
            GroupMember groupMember = getDataItem(position);
            tvPos.setText("" + (position + 1));
            tvPoint.setText(groupMember.getPoint() + "");
            tvName.setText(groupMember.getName());
            Glide.with(itemView)
                    .load(groupMember.getPhoto())
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.ic_group_default)
                                    .transforms(new CenterCrop(),
                                            new RoundedCorners(getContext().getResources()
                                                    .getDimensionPixelSize(R.dimen.height_ava_group) / 2))
                    )
                    .into(ivAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getOnItemClickListener()!=null){
                        getOnItemClickListener().onItemClick(position, groupMember);
                    }
                }
            });
        }
    }
}
