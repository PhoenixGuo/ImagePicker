package com.fightcent.imagepicker.imagepickerview;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.fightcent.imagepicker.BaseActivity;
import com.fightcent.imagepicker.ImagePicker;
import com.fightcent.imagepicker.R;
import com.fightcent.imagepicker.adapter.ImageAdapter;
import com.fightcent.imagepicker.controller.CodeConstantController;
import com.fightcent.imagepicker.controller.ConfigConstantController;
import com.fightcent.imagepicker.databinding.ActivityImagePickerBinding;
import com.fightcent.imagepicker.imageshower.AllImageBeanPreviewActivity;
import com.fightcent.imagepicker.imageshower.BaseImagePreviewActivity;
import com.fightcent.imagepicker.imageshower.PickedImageBeanPreviewActivity;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.model.ImageBeanFactory;
import com.fightcent.imagepicker.model.event.OnImagePickedEvent;
import com.fightcent.imagepicker.model.event.OnImagePickerActivityDestroyEvent;
import com.fightcent.imagepicker.util.CollectionUtil;
import com.fightcent.imagepicker.util.ToastUtil;
import com.fightcent.imagepicker.widget.ItemThumbnailDecoration;
import com.fightcent.imagepicker.widget.RequestPermissionDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.fightcent.imagepicker.ImagePicker.sAllImageBeanList;

/**
 * Created by andy.guo on 2018/1/19.
 */

public class ImagePickerMainActivity extends BaseActivity implements ImagePickerView {

    private ActivityImagePickerBinding mActivityImagePickerBinding;

    private GridLayoutManager mGridLayoutManager;
    private ImageAdapter mImageAdapter;
    private AlertDialog mAlertDialog;

    private static final String SAVE_INSTANCE_PICKED_IMAGE_BEAN_LIST
            = "SAVE_INSTANCE_PICKED_IMAGE_BEAN_LIST";

    public static final String COLUMN_COUNT = "COLUMN_COUNT";
    private int mColumnCount;

    public static final String MAX_IMAGE_PICK_COUNT = "MAX_IMAGE_PICK_COUNT";
    private int mMaxImagePickCount;

    private static final String ORDER_BY = MediaStore.Images.Media._ID + " DESC";

    private String[] mPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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

    @Override
    protected void onStart() {
        super.onStart();
        if (CollectionUtil.size(ImagePicker.sPickedImageBeanList) <= 0) {
            //判断Android版本进行不同的权限处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //请求权限
                startRequestPermission();
            } else {
                //开始从本机获取图片列表
                getAllImageBean();
            }
        }
    }

    private void startRequestPermission() {
        ActivityCompat.requestPermissions(
                this,
                mPermissions,
                CodeConstantController.CODE_REQUEST_PERMISSION_EXTERNAL_STORAGE
        );
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
                        Cursor cursor = getContentResolver().query(
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
                            cursor.close();
                        }
                        return sAllImageBeanList;
                    }
                }
        ).subscribe(
                new Action1<List<ImageBean>>() {
                    @Override
                    public void call(List<ImageBean> imageList) {
                        if (mImageAdapter != null) {
                            mImageAdapter.setImageList(sAllImageBeanList);
                            mImageAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

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

        mImageAdapter.setImageList(sAllImageBeanList);
        mImageAdapter.notifyDataSetChanged();

    }

    private void initListeners() {
        mActivityImagePickerBinding.ivBackArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //清空两个静态集合
                        sAllImageBeanList.clear();
                        ImagePicker.sPickedImageBeanList.clear();

                        ImagePickerMainActivity.this.finish();
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

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CodeConstantController.CODE_REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        showMyPermissionRequestDialog();
                    } else
                        finish();
                } else {
                    getAllImageBean();
                }
            }
        }
    }

    private void showMyPermissionRequestDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = RequestPermissionDialog.makeDialog(
                    this,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    },
                    R.string.request_permission,
                    R.string.request_permission_read_external_storage
            );
        } else if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

}
