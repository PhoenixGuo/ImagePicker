package com.fightcent.imagepicker.viewholder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fightcent.imagepicker.databinding.ItemImageThumbnailBinding;
import com.fightcent.imagepicker.imagepickerview.ImagePickerView;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.util.SystemUtil;


/**
 * Created by andy.guo on 2018/1/19.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private ItemImageThumbnailBinding mItemImageThumbnailBinding;
    private ImagePickerView mImagePickerView;
    private ImageBean mImageBean;
    private Context mContext;
    private int mImageViewThumbnailWidth;
    private int mImageViewThumbnailHeight;

    public ImageViewHolder(Context context, View itemView, ImagePickerView imagePickerView) {
        super(itemView);
        mContext = context;
        mItemImageThumbnailBinding = DataBindingUtil.bind(itemView);
        mImagePickerView = imagePickerView;
        mImageViewThumbnailWidth =
                SystemUtil.getScreenWidth(mContext) / imagePickerView.getColumnCount();
        mImageViewThumbnailHeight =
                SystemUtil.getScreenWidth(mContext) / imagePickerView.getColumnCount();
    }

    public void setModel(ImageBean imageBean) {
        mImageBean = imageBean;
    }

    public void updateViewHolder() {
        if (mContext != null && mImageBean != null) {

            Glide.with(mContext).load(
                    mImageBean.getContentUriString()
            ).asBitmap(

            ).override(
                    mImageViewThumbnailWidth / 2,
                    mImageViewThumbnailHeight / 2
            ).fitCenter(

            ).centerCrop(

            ).into(
                    mItemImageThumbnailBinding.ivImageThumbnail
            );
        }

        mItemImageThumbnailBinding.ivPick.setSelected(mImageBean.isIsPicked());
        mItemImageThumbnailBinding.ivPickImageForeground.setSelected(mImageBean.isIsPicked());

        mItemImageThumbnailBinding.ivPick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemImageThumbnailBinding.ivPick.isSelected()) {
                            if (mImagePickerView.imageCancelPick(mImageBean)) {
                                mItemImageThumbnailBinding.ivPick.setSelected(false);
                                mItemImageThumbnailBinding.ivPickImageForeground.setSelected(false);
                            }
                        } else {
                            if (mImagePickerView.imagePick(mImageBean)) {
                                mItemImageThumbnailBinding.ivPick.setSelected(true);
                                mItemImageThumbnailBinding.ivPickImageForeground.setSelected(true);
                            }
                        }
                    }
                }
        );

        mItemImageThumbnailBinding.ivImageThumbnail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImagePickerView.startImageShowerView(getLayoutPosition());
                    }
                }
        );

    }

}
