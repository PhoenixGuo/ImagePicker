package com.fightcent.imagepicker.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by andy.guo on 2018/3/29.
 */

public class GestureImageView extends ImageView
        implements
        ScaleGestureDetector.OnScaleGestureListener {

    private ScaleGestureDetector mScaleGestureDetector;
    private Matrix mMatrix = new Matrix();
    private static final float THUMBNAIL_SIZE_MULTIPLIER = 0.1f;

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleGestureDetector = new ScaleGestureDetector(
                context,
                this
        );
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mMatrix.postScale(
                detector.getScaleFactor(),
                detector.getScaleFactor(),
                detector.getFocusX(),
                detector.getFocusY()
        );
        setImageMatrix(mMatrix);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void setImageByUri(String uri) {
        Glide.with(getContext())
                .load(uri)
                .asBitmap()
                .thumbnail(THUMBNAIL_SIZE_MULTIPLIER)
                .listener(
                        new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(
                                    Exception e,
                                    String model,
                                    Target<Bitmap> target,
                                    boolean isFirstResource
                            ) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(
                                    Bitmap resource,
                                    String model,
                                    Target<Bitmap> target,
                                    boolean isFromMemoryCache,
                                    boolean isFirstResource
                            ) {
                                //获取图片显示后宽高
                                if (!isFirstResource) {
                                    //加载完图片后改为MATRIX缩放模式
                                    setScaleType(ScaleType.MATRIX);
                                    int resourceWidth = resource.getWidth();
                                    int resourceHeight = resource.getHeight();
                                    //将图片移动到中央
                                    mMatrix.postTranslate(
                                            getWidth() / 2 - resourceWidth / 2,
                                            getHeight() / 2 - resourceHeight / 2
                                    );
                                    setImageMatrix(mMatrix);
                                }
                                return false;
                            }
                        }
                )
                .into(this);
    }

}
