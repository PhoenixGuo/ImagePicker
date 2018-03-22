package com.fightcent.imagepicker.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fightcent.imagepicker.R;

/**
 * Created by andy.guo on 2018/1/19.
 */

public class ItemThumbnailDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    public ItemThumbnailDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void getItemOffsets(
            Rect outRect,
            View view,
            RecyclerView parent,
            RecyclerView.State state
    ) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemOffset = (int) mContext.getResources().getDimension(R.dimen.super_very_small_spacing);
        outRect.right = itemOffset;
        outRect.left = itemOffset;
        outRect.top = itemOffset;
        outRect.bottom = itemOffset;
    }

}
