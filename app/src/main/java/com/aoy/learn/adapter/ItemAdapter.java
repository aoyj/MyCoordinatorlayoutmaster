package com.aoy.learn.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aoy.learn.ClassicRefreshActivity;
import com.aoy.learn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/19.
 */

public class ItemAdapter extends RecyclerView.Adapter {

    List<String> mList = new ArrayList<>();

    public ItemAdapter() {
        for(int i = 0 ; i < 20 ;i ++){
            mList.add("暴走的萝卜干" + i);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.text.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.card_view)
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toMyBehavior(v);
                }
            });
        }

        private void toMyBehavior(View v) {
            Intent intent = new Intent(v.getContext(),ClassicRefreshActivity.class);
            v.getContext().startActivity(intent);
        }
    }


}
