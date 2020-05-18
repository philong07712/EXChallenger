package com.example.exchallenger.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected LayoutInflater mInflater;
    protected List<T> mDataList;
    private RecyclerView mRecyclerView;
    public Context getContext() {
        return mContext;
    }

    protected Context mContext;

    private OnItemClickListener<T> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public BaseRecyclerViewAdapter(@NonNull Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataList = new ArrayList<>();
    }

    public BaseRecyclerViewAdapter(@NonNull Context context, List<T> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataList = data;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    public void add(List<T> itemList) {
        mDataList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void add(int position, List<T> itemList) {
        mDataList.addAll(position, itemList);
        notifyDataSetChanged();
    }

    public void addItem(int position, T item) {
        mDataList.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(T item) {
        mDataList.add(item);
        notifyDataSetChanged();
    }

    public void addFirst(T item) {
        addItem(0, item);
        restoreScrollPositionAfterAdAdded();
    }

    public T getDataItem(int position) {
        if (position >= mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    public void set(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    private void restoreScrollPositionAfterAdAdded() {
        if (mRecyclerView != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (layoutManager != null) {

                layoutManager.scrollToPosition(0);
            }
        }
    }
}
