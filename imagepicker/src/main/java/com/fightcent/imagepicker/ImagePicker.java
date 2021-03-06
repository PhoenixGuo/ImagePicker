package com.fightcent.imagepicker;

import android.content.Context;
import android.content.Intent;

import com.fightcent.imagepicker.imagepickerview.ImagePickerMainActivity;
import com.fightcent.imagepicker.model.ImageBean;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by andy.guo on 2018/2/2.
 */

public class ImagePicker implements Serializable {

    //建造者
    //缩略图每行个数（默认每行3个，最多允许6个）
    //图片大小限制（默认不限制）
    //最多选择图片张数（默认3张）
    //缩略图gif是否可动（默认不可动）
    //是否开启图片编辑功能（默认开启）
    //选择器回调
    //是否支持相机
    //是否支持视频选择

    //速度快
    //权限适配
    //Android7相机适配
    //拒绝Bitmap对象
    //

    //待做任务：
    //选中照片的回显（放弃实现该功能，微信允许选取多张图片）
    //点击反馈水波纹
    //图片手势缩放
    //图片编辑（旋转，裁剪）
    //相机
    //小图预览
    //加载loading

    private Context mContext;

    private int mColumnCount;
    //byte
    private int mAllowMaxImageSize;

    private int mMaxImagePickCount;

    private boolean mIsAllImageAsBitmap;

    private boolean mIsShowCamera;

    private OnImagePickedListener mOnImagePickedListener;
    private OnImagePickedListenerWrapper mOnImagePickedListenerWrapper;

    //TODO 数据结构替换成LinkedHashSet
    public static ArrayList<ImageBean> sAllImageBeanList = new ArrayList<>();
    public static ArrayList<ImageBean> sPickedImageBeanList = new ArrayList<>();

    private ImagePicker() {

    }

    private ImagePicker(Builder builder) {
        this.mContext = builder.mContext;
        this.mColumnCount = builder.mColumnCount;
        this.mAllowMaxImageSize = builder.mAllowMaxImageByteSize;
        this.mMaxImagePickCount = builder.mMaxImagePickCount;
        this.mIsAllImageAsBitmap = builder.mIsAllImageAsBitmap;
        this.mIsShowCamera = builder.mIsShowCamera;
        this.mOnImagePickedListener = builder.mOnImagePickedListener;
        mOnImagePickedListenerWrapper =
                new OnImagePickedListenerWrapper(mOnImagePickedListener);
    }

    public static class Builder implements Serializable {

        private Context mContext;

        private int mColumnCount = 3;
        //byte
        private int mAllowMaxImageByteSize;

        private int mMaxImagePickCount;

        private boolean mIsAllImageAsBitmap;

        private boolean mIsShowCamera;

        private OnImagePickedListener mOnImagePickedListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setColumnCount(int columnCount) {
            if (columnCount < 2) {
                mColumnCount = 3;
            } else if (columnCount > 6) {
                mColumnCount = 6;
            } else {
                this.mColumnCount = columnCount;
            }
            return this;
        }

        public Builder setAllowMaxImageByteSize(int allowMaxImageByteSize) {
            this.mAllowMaxImageByteSize = allowMaxImageByteSize;
            return this;
        }

        public Builder setMaxImagePickCount(int maxImagePickCount) {
            this.mMaxImagePickCount = maxImagePickCount;
            return this;
        }

        public Builder setIsAllImageAsBitmap(boolean isAllImageAsBitmap) {
            this.mIsAllImageAsBitmap = isAllImageAsBitmap;
            return this;
        }

        public Builder setIsShowCamera(boolean isShowCamera) {
            this.mIsShowCamera = isShowCamera;
            return this;
        }

        public Builder setOnImagePickedListener(OnImagePickedListener onImagePickedListener) {
            this.mOnImagePickedListener = onImagePickedListener;
            return this;
        }

        public ImagePicker build() {
            return new ImagePicker(this);
        }

    }

    public void show() {
        if (mOnImagePickedListenerWrapper != null
                && !EventBus.getDefault().isRegistered(mOnImagePickedListenerWrapper)) {
            EventBus.getDefault().register(mOnImagePickedListenerWrapper);
        }

        Intent intent = new Intent(mContext, ImagePickerMainActivity.class);
        intent.putExtra(ImagePickerMainActivity.COLUMN_COUNT, mColumnCount);
        intent.putExtra(ImagePickerMainActivity.MAX_IMAGE_PICK_COUNT, mMaxImagePickCount);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

}
