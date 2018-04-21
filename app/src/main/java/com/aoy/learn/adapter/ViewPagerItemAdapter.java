package com.aoy.learn.adapter;

import android.support.v4.view.PagerAdapter;
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

public class ViewPagerItemAdapter extends PagerAdapter {
    List<Integer> mList;

    public ViewPagerItemAdapter(List<Integer> mList) {
        this.mList = mList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_viewpager_item, null, false);
        container.addView(view);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.img.setImageResource(mList.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    static class ViewHolder {
        @BindView(R.id.img)
        ImageView img;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
