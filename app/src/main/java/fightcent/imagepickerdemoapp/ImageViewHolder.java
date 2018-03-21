package fightcent.imagepickerdemoapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fightcent.imagepicker.model.ImageBean;

import fightcent.imagepickerdemoapp.databinding.ItemImageBinding;


/**
 * Created by andy.guo on 2018/3/20.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private ItemImageBinding mItemImageBinding;

    public ImageViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mItemImageBinding = DataBindingUtil.bind(itemView);
    }

    public void updateViewHolder(ImageBean image) {
        Glide.with(mContext)
                .load(image.getContentUriString())
                .into(mItemImageBinding.iv);
    }

}
