package fightcent.imagepickerdemoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.util.CollectionUtil;

import java.util.ArrayList;

/**
 * Created by andy.guo on 2018/3/20.
 */

public class PickedImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private Context mContext;
    private ArrayList<ImageBean> mImageList;

    public PickedImageAdapter(Context context, ArrayList<ImageBean> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.item_image,
                parent,
                false
        );
        return new ImageViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.updateViewHolder(mImageList.get(position));
    }

    @Override
    public int getItemCount() {
        return CollectionUtil.size(mImageList);
    }

}
