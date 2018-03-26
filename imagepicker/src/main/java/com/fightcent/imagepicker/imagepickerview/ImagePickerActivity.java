package com.fightcent.imagepicker.imagepickerview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.fightcent.imagepicker.BaseActivity;
import com.fightcent.imagepicker.ImagePicker;
import com.fightcent.imagepicker.R;
import com.fightcent.imagepicker.adapter.ImageAdapter;
import com.fightcent.imagepicker.controller.ConfigConstantController;
import com.fightcent.imagepicker.databinding.ActivityImagePickerBinding;
import com.fightcent.imagepicker.imageshower.AllImageBeanPreviewActivity;
import com.fightcent.imagepicker.imageshower.BaseImagePreviewActivity;
import com.fightcent.imagepicker.imageshower.PickedImageBeanPreviewActivity;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.model.event.AllImageBeanGotEvent;
import com.fightcent.imagepicker.model.event.OnImagePickedEvent;
import com.fightcent.imagepicker.model.event.OnImagePickerActivityDestroyEvent;
import com.fightcent.imagepicker.util.CollectionUtil;
import com.fightcent.imagepicker.util.ToastUtil;
import com.fightcent.imagepicker.widget.ItemThumbnailDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by andy.guo on 2018/1/19.
 */

public class ImagePickerActivity extends BaseActivity implements ImagePickerView {

    private ActivityImagePickerBinding mActivityImagePickerBinding;

    private GridLayoutManager mGridLayoutManager;
    private ImageAdapter mImageAdapter;

    private static final String SAVE_INSTANCE_PICKED_IMAGE_BEAN_LIST
            = "SAVE_INSTANCE_PICKED_IMAGE_BEAN_LIST";

    public static final String COLUMN_COUNT = "COLUMN_COUNT";
    private int mColumnCount;

    public static final String MAX_IMAGE_PICK_COUNT = "MAX_IMAGE_PICK_COUNT";
    private int mMaxImagePickCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mActivityImagePickerBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_image_picker
        );

        Intent intent = getIntent();
        if (intent != null) {
            mColumnCount = intent.getIntExtra(
                    COLUMN_COUNT,
                    ConfigConstantController.DEFAULT_IMAGE_GRID_SPAN_COUNT
            );
            mMaxImagePickCount = intent.getIntExtra(
                    MAX_IMAGE_PICK_COUNT,
                    0
            );
        }

        //TODO
/*        if (savedInstanceState != null) {
            Object obj = savedInstanceState.get(SAVE_INSTANCE_PICKED_IMAGE_BEAN_LIST);
            if (obj instanceof ArrayList) {
                ArrayList<ImageBean> saveInstanceSelectedPictureList = (ArrayList<ImageBean>) obj;
                mPickedImageBeanList.addAll(saveInstanceSelectedPictureList);
            }
        }*/

        initViews();
        initListeners();
    }

    private void initViews() {
        //设置GridView
        mGridLayoutManager = new GridLayoutManager(this, mColumnCount);
        mActivityImagePickerBinding.rvPictures.setLayoutManager(mGridLayoutManager);
        mImageAdapter = new ImageAdapter(this, this);
        mActivityImagePickerBinding.rvPictures.setAdapter(mImageAdapter);
        mActivityImagePickerBinding.rvPictures.addItemDecoration(new ItemThumbnailDecoration(
                getApplicationContext()
        ));

        setConformButtonText(
                CollectionUtil.size(ImagePicker.sPickedImageBeanList),
                mMaxImagePickCount
        );
        setPreviewButtonText(CollectionUtil.size(ImagePicker.sPickedImageBeanList));

        mImageAdapter.setImageList(ImagePicker.sAllImageBeanList);
        mImageAdapter.notifyDataSetChanged();

    }

    private void initListeners() {
        mActivityImagePickerBinding.ivBackArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //清空两个静态集合
                        ImagePicker.sAllImageBeanList.clear();
                        ImagePicker.sPickedImageBeanList.clear();

                        ImagePickerActivity.this.finish();
                    }
                }
        );

        mActivityImagePickerBinding.tvConform.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(
                                new OnImagePickedEvent()
                        );
                    }
                }
        );

        mActivityImagePickerBinding.tvPreview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPickedImageBeanView();
                    }
                }
        );

    }

    @Subscribe
    public void onReceivedAllImageBeanGotEvent(AllImageBeanGotEvent allImageBeanGotEvent) {
        if (mImageAdapter != null) {
            mImageAdapter.setImageList(ImagePicker.sAllImageBeanList);
            mImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean imageCancelPick(ImageBean imageBean) {
        imageBean.setIsPicked(false);
        ImagePicker.sPickedImageBeanList.remove(imageBean);
        setConformButtonText(
                CollectionUtil.size(ImagePicker.sPickedImageBeanList),
                mMaxImagePickCount
        );
        setPreviewButtonText(CollectionUtil.size(ImagePicker.sPickedImageBeanList));
        return true;
    }

    @Override
    public boolean imagePick(ImageBean imageBean) {
        if (CollectionUtil.size(ImagePicker.sPickedImageBeanList) < mMaxImagePickCount) {
            imageBean.setIsPicked(true);
            ImagePicker.sPickedImageBeanList.add(imageBean);
            setConformButtonText(
                    CollectionUtil.size(ImagePicker.sPickedImageBeanList),
                    mMaxImagePickCount
            );
            setPreviewButtonText(CollectionUtil.size(ImagePicker.sPickedImageBeanList));
            return true;
        }
        ToastUtil.getInstance().showToast(
                getApplicationContext(),
                R.string.can_not_pick_more_image
        );
        return false;
    }

    private void setConformButtonText(int pickedImageCount, int maxImageCount) {
        if (pickedImageCount > 0) {
            mActivityImagePickerBinding.tvConform.setEnabled(true);
        } else {
            mActivityImagePickerBinding.tvConform.setEnabled(false);
        }
        mActivityImagePickerBinding.tvConform.setText(
                String.format(
                        getResources().getString(R.string.conform_count),
                        pickedImageCount,
                        maxImageCount
                )
        );
    }

    private void setPreviewButtonText(int pickedImageBeanSize) {
        if (pickedImageBeanSize > 0) {
            mActivityImagePickerBinding.tvPreview.setEnabled(true);
            mActivityImagePickerBinding.tvPreview.setText(
                    String.format(
                            getResources().getString(R.string.preview_size),
                            pickedImageBeanSize
                    )
            );
        } else {
            mActivityImagePickerBinding.tvPreview.setEnabled(false);
            mActivityImagePickerBinding.tvPreview.setText(
                    R.string.preview
            );
        }
    }

    @Override
    public int getColumnCount() {
        return mColumnCount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO 待优化，不需要每次都刷
        if (mImageAdapter != null) {
            mImageAdapter.notifyDataSetChanged();
        }
        setConformButtonText(
                CollectionUtil.size(ImagePicker.sPickedImageBeanList),
                mMaxImagePickCount
        );
        setPreviewButtonText(CollectionUtil.size(ImagePicker.sPickedImageBeanList));
    }

    @Override
    public void startAllImageBeanView(int clickItemPosition) {
        Intent intent = new Intent(this, AllImageBeanPreviewActivity.class);
        intent.putExtra(BaseImagePreviewActivity.CURRENT_SHOW_POSITION, clickItemPosition);
        intent.putExtra(BaseImagePreviewActivity.MAX_IMAGE_PICK_COUNT, mMaxImagePickCount);
        startActivity(intent);
    }

    private void startPickedImageBeanView() {
        Intent intent = new Intent(this, PickedImageBeanPreviewActivity.class);
        intent.putExtra(BaseImagePreviewActivity.CURRENT_SHOW_POSITION, 0);
        intent.putExtra(BaseImagePreviewActivity.MAX_IMAGE_PICK_COUNT, mMaxImagePickCount);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new OnImagePickerActivityDestroyEvent());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO
//        outState.putSerializable(SAVE_INSTANCE_SELECTED_IMAGE_LIST, ImagePicker.mPickedImageBeanList);
    }

}
