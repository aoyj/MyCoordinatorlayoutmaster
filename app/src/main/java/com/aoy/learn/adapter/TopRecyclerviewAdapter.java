package com.aoy.learn.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aoy.learn.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/21.
 */

public class TopRecyclerviewAdapter extends RecyclerView.Adapter {
    List<Integer> mList;

    public TopRecyclerviewAdapter(List mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_viewpager_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.img.setImageResource(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img)
        ImageView img;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
