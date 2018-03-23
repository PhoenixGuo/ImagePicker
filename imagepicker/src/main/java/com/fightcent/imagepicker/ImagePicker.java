package com.fightcent.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.fightcent.imagepicker.imagepickerview.ImagePickerActivity;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.model.ImageBeanFactory;
import com.fightcent.imagepicker.model.event.AllImageBeanGotEvent;
import com.fightcent.imagepicker.util.CollectionUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    private static final String ORDER_BY = MediaStore.Images.Media._ID + " DESC";

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

        //开始从本机获取图片列表
        getAllImageBean();

        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.COLUMN_COUNT, mColumnCount);
        intent.putExtra(ImagePickerActivity.MAX_IMAGE_PICK_COUNT, mMaxImagePickCount);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
        );
        mContext.startActivity(intent);
    }

    private void getAllImageBean() {
        //查询图片的Uri
        Observable.just(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).map(
                new Func1<Uri, List<ImageBean>>() {
                    @Override
                    public List<ImageBean> call(Uri uri) {
                        Cursor cursor = mContext.getContentResolver().query(
                                uri, null, null, null, ORDER_BY
                        );
                        sAllImageBeanList.clear();
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                ImageBean imageBean = ImageBeanFactory.createImageBeanByCursor(cursor);
                                if (!CollectionUtil.isEmpty(ImagePicker.sPickedImageBeanList)
                                        && ImagePicker.sPickedImageBeanList.contains(imageBean)) {
                                    imageBean.setIsPicked(true);
                                }
                                sAllImageBeanList.add(imageBean);
                            }
                        }
                        return sAllImageBeanList;
                    }
                }
        ).subscribe(
                new Action1<List<ImageBean>>() {
                    @Override
                    public void call(List<ImageBean> imageList) {
                        EventBus.getDefault().post(new AllImageBeanGotEvent());
                    }
                }
        );

    }

}
