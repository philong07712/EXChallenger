package com.example.exchallenger.challenge;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.exchallenger.R;
import com.example.exchallenger.base.BaseRecyclerViewAdapter;
import com.example.exchallenger.base.BaseViewHolder;

public class ChallengeAdapter extends BaseRecyclerViewAdapter<Object> {
    public ChallengeAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChallengeVH(mInflater.inflate(R.layout.item_challenge, parent, false));
    }

    public class ChallengeVH extends BaseViewHolder {
        public ChallengeVH(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
        }
    }
}
