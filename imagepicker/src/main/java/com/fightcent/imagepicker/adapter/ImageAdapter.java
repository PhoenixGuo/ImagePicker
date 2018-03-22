package com.fightcent.imagepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fightcent.imagepicker.imagepickerview.ImagePickerView;
import com.fightcent.imagepicker.R;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.util.CollectionUtil;
import com.fightcent.imagepicker.viewholder.ImageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy.guo on 2018/1/19.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private Context mContext;
    private ImagePickerView mImagePickerView;
    private List<ImageBean> mImageList = new ArrayList<>();

    public ImageAdapter(Context context, ImagePickerView imagePickerView) {
        mContext = context;
        mImagePickerView = imagePickerView;
    }

    public void setImageList(List<ImageBean> imageList) {
        mImageList.clear();
        mImageList.addAll(imageList);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItemImageThumbnail = LayoutInflater.from(
                mContext
        ).inflate(
                R.layout.item_image_thumbnail,
                parent,
                false
        );
        return new ImageViewHolder(mContext, viewItemImageThumbnail, mImagePickerView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageBean imageBean = mImageList.get(position);
        holder.setModel(imageBean);
        holder.updateViewHolder();
    }

    @Override
    public int getItemCount() {
        return CollectionUtil.size(mImageList);
    }

}
