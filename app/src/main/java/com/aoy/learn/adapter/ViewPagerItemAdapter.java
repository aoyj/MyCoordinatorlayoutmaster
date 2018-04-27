package com.aoy.learn.adapter;

import android.graphics.PointF;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aoy.learn.R;
import com.aoy.learn.uitls.Tools;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/21.
 */

public class ViewPagerItemAdapter extends PagerAdapter {
    List<Uri> mList;

    public ViewPagerItemAdapter(List<Uri> mList) {
        this.mList = mList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_viewpager_item, null, false);
        container.addView(view);
        ViewHolder viewHolder = new ViewHolder(view);
        setImageUrl(mList.get(position),  viewHolder.img);
        return view;
    }

    void setImageUrl(Uri uri, SimpleDraweeView img){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .build();
        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(img.getController())
                .setImageRequest(request)
                .build();
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(
                img.getResources());
        builder.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
        builder.setActualImageFocusPoint(new PointF(0.5f,0f));
        img.setHierarchy(builder.build());
        img.setController(controller);
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
        SimpleDraweeView img;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
