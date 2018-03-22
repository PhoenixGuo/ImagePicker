package com.fightcent.imagepicker.imagepickerview;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fightcent.imagepicker.R;
import com.fightcent.imagepicker.adapter.ImageAdapter;
import com.fightcent.imagepicker.controller.ConfigConstantController;
import com.fightcent.imagepicker.databinding.ActivityImagePickerBinding;
import com.fightcent.imagepicker.imageshower.ImageShowerActivity;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.model.ImageBeanFactory;
import com.fightcent.imagepicker.model.OnImagePickedEvent;
import com.fightcent.imagepicker.model.OnImagePickerActivityDestroyEvent;
import com.fightcent.imagepicker.util.CollectionUtil;
import com.fightcent.imagepicker.util.ToastUtil;
import com.fightcent.imagepicker.util.ViewUtil;
import com.fightcent.imagepicker.widget.ItemThumbnailDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by andy.guo on 2018/1/19.
 */

public class ImagePickerActivity extends AppCompatActivity implements ImagePickerView {

    private ActivityImagePickerBinding mActivityImagePickerBinding;

    private GridLayoutManager mGridLayoutManager;
    private ImageAdapter mImageAdapter;
    public static final String ORDER_BY = MediaStore.Images.Media._ID + " DESC";

    //TODO 待优化
    public static ArrayList<ImageBean> mAllImageBeanList = new ArrayList<>();

    private ArrayList<ImageBean> mPickedImageList = new ArrayList<>();
    private static final String SAVE_INSTANCE_SELECTED_IMAGE_LIST
            = "SAVE_INSTANCE_SELECTED_IMAGE_LIST";

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

        if (savedInstanceState != null) {
            Object obj = savedInstanceState.get(SAVE_INSTANCE_SELECTED_IMAGE_LIST);
            if (obj instanceof ArrayList) {
                ArrayList<ImageBean> saveInstanceSelectedPictureList = (ArrayList<ImageBean>) obj;
                mPickedImageList.addAll(saveInstanceSelectedPictureList);
            }
        }

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

        setConformButtonText();

        //查询图片的Uri
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, ORDER_BY);
        Observable.just(
                cursor
        ).subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).map(
                new Func1<Cursor, List<ImageBean>>() {
                    @Override
                    public List<ImageBean> call(Cursor cursor) {
                        mAllImageBeanList.clear();
                        while (cursor.moveToNext()) {
                            ImageBean imageBean = ImageBeanFactory.createImageBeanByCursor(cursor);
                            if (!CollectionUtil.isEmpty(mPickedImageList)
                                    && mPickedImageList.contains(imageBean)) {
                                imageBean.setIsPicked(true);
                            }
                            mAllImageBeanList.add(imageBean);
                        }
                        return mAllImageBeanList;
                    }
                }
        ).subscribe(
                new Action1<List<ImageBean>>() {
                    @Override
                    public void call(List<ImageBean> imageList) {
                        mImageAdapter.setImageList(imageList);
                        mImageAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void initListeners() {
        mActivityImagePickerBinding.ivBackArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagePickerActivity.this.finish();
                    }
                }
        );

        mActivityImagePickerBinding.rvPictures.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            int firstVisibleItemPosition
                                    = mGridLayoutManager.findFirstVisibleItemPosition();
                            if (firstVisibleItemPosition > 20) {
                                ViewUtil.showView(mActivityImagePickerBinding.tvDoubleClickToTop);
                            } else {
                                ViewUtil.hideView(mActivityImagePickerBinding.tvDoubleClickToTop);
                            }
                        }
                    }
                }
        );

        mActivityImagePickerBinding.tvConform.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(
                                new OnImagePickedEvent(mPickedImageList)
                        );
                        ImagePickerActivity.this.finish();
                    }
                }
        );

    }

    @Override
    public boolean imageCancelPick(ImageBean imageBean) {
        imageBean.setIsPicked(false);
        mPickedImageList.remove(imageBean);
        setConformButtonText();
        return true;
    }

    @Override
    public boolean imagePick(ImageBean imageBean) {
        if (CollectionUtil.size(mPickedImageList) < mMaxImagePickCount) {
            imageBean.setIsPicked(true);
            mPickedImageList.add(imageBean);
            setConformButtonText();
            return true;
        }
        ToastUtil.getInstance().showToast(
                getApplicationContext(),
                R.string.can_not_pick_more_image
        );
        return false;
    }

    private void setConformButtonText() {
        mActivityImagePickerBinding.tvConform.setText(
                String.format(
                        getResources().getString(R.string.conform_count),
                        CollectionUtil.size(mPickedImageList),
                        mMaxImagePickCount
                )
        );
    }

    @Override
    public int getColumnCount() {
        return mColumnCount;
    }

    @Override
    public void startImageShowerView(int clickItem) {
        Intent intent = new Intent(this, ImageShowerActivity.class);
        intent.putExtra(ImageShowerActivity.INIT_SHOW_POSITION, clickItem);
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
        outState.putSerializable(SAVE_INSTANCE_SELECTED_IMAGE_LIST, mPickedImageList);
    }

}
