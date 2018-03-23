package com.fightcent.imagepicker.imageshower;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.fightcent.imagepicker.BaseActivity;
import com.fightcent.imagepicker.ImagePicker;
import com.fightcent.imagepicker.R;
import com.fightcent.imagepicker.databinding.ActivityImageShowerBinding;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.model.event.OnImagePickedEvent;
import com.fightcent.imagepicker.util.CollectionUtil;
import com.fightcent.imagepicker.util.ToastUtil;
import com.fightcent.imagepicker.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by andy.guo on 2018/3/23.
 */

public abstract class BaseImagePreviewActivity extends BaseActivity {

    protected ActivityImageShowerBinding mActivityImageShowerBinding;

    public static final String MAX_IMAGE_PICK_COUNT = "MAX_IMAGE_PICK_COUNT";
    protected int mMaxImagePickCount;

    public static final String CURRENT_SHOW_POSITION = "CURRENT_SHOW_POSITION";
    protected int mCurrentShowPosition;

    private boolean mIsShowToolBar = true;

    protected ArrayList<ImageBean> mImageBeanList = new ArrayList<>();

    protected ImageShowerAdapter mImageShowerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityImageShowerBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_image_shower
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        hideStatusBar();

        Intent intent = getIntent();
        if (intent != null) {
            mCurrentShowPosition = intent.getIntExtra(CURRENT_SHOW_POSITION, 0);
            mMaxImagePickCount = intent.getIntExtra(
                    MAX_IMAGE_PICK_COUNT,
                    0
            );
        }

        initViews();
        initListeners();
    }

    private void initViews() {
        setConformButtonText(
                CollectionUtil.size(ImagePicker.sPickedImageBeanList),
                mMaxImagePickCount
        );
        mImageShowerAdapter = new ImageShowerAdapter(
                getSupportFragmentManager(),
                mImageBeanList
        );
        mActivityImageShowerBinding.vp.setAdapter(
                mImageShowerAdapter
        );
        mActivityImageShowerBinding.vp.setCurrentItem(mCurrentShowPosition);
        setPositionSizeText(mCurrentShowPosition + 1);

        if (CollectionUtil.size(mImageBeanList) > mCurrentShowPosition
                && mImageBeanList.get(mCurrentShowPosition).isIsPicked()) {
            mActivityImageShowerBinding.ivPick.setSelected(true);
        }
    }

    private void initListeners() {
        mActivityImageShowerBinding.ivBackArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseImagePreviewActivity.this.finish();
                    }
                }
        );

        mActivityImageShowerBinding.vp.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(
                            int position,
                            float positionOffset,
                            int positionOffsetPixels
                    ) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        mCurrentShowPosition = position;
                        setPositionSizeText(position + 1);
                        if (mImageBeanList.get(position).isIsPicked()) {
                            mActivityImageShowerBinding.ivPick.setSelected(true);
                        } else {
                            mActivityImageShowerBinding.ivPick.setSelected(false);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );

        mActivityImageShowerBinding.ivPick.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageBean imageBean = null;
                        if (CollectionUtil.size(mImageBeanList) > mCurrentShowPosition) {
                            imageBean = mImageBeanList.get(mCurrentShowPosition);
                        }
                        if (mActivityImageShowerBinding.ivPick.isSelected()) {
                            imageCancelPick(imageBean);
                        } else {
                            imagePick(imageBean);
                        }
                    }
                }
        );

        mActivityImageShowerBinding.tvConform.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(
                                new OnImagePickedEvent()
                        );
                    }
                }
        );
    }

    protected class ImageShowerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<ImageBean> mImageBeanList;

        public ImageShowerAdapter(FragmentManager fm, ArrayList<ImageBean> imageBeanList) {
            super(fm);
            mImageBeanList = imageBeanList;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageShowerFragment.makeFragment(
                    mImageBeanList.get(position)
            );
        }

        @Override
        public int getCount() {
            return CollectionUtil.size(mImageBeanList);
        }
    }

    public void hideStatusBar() {
        //隐藏状态栏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
    }

    public void showStatusBar() {
        //显示状态栏
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attr);
    }

    public void showToolBar() {
        if (mIsShowToolBar) {
            hideToolbarTop();
            hideToolbarBottom();
            mIsShowToolBar = false;
        } else {
            showToolbarTop();
            showToolbarBottom();
            mIsShowToolBar = true;
        }
    }

    protected void hideToolbarTop() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                mActivityImageShowerBinding.tbTop,
                "translationY",
                -mActivityImageShowerBinding.tbTop.getHeight()
        );
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    protected void showToolbarTop() {
        ViewUtil.showView(mActivityImageShowerBinding.tbTop);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                mActivityImageShowerBinding.tbTop,
                "translationY",
                0
        );
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    protected void hideToolbarBottom() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                mActivityImageShowerBinding.tbBottom,
                "translationY",
                mActivityImageShowerBinding.tbBottom.getHeight()
        );
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    protected void showToolbarBottom() {
        ViewUtil.showView(mActivityImageShowerBinding.tbBottom);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                mActivityImageShowerBinding.tbBottom,
                "translationY",
                0
        );
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    protected void setPositionSizeText(int position) {
        String positionSizeText = String.format(
                getResources().getString(R.string.position_size),
                position,
                CollectionUtil.size(mImageBeanList)
        );
        mActivityImageShowerBinding.tvPositionSize.setText(positionSizeText);
    }

    protected void setConformButtonText(int pickedImageCount, int maxImageCount) {
        if (pickedImageCount > 0) {
            mActivityImageShowerBinding.tvConform.setEnabled(true);
        } else {
            mActivityImageShowerBinding.tvConform.setEnabled(false);
        }
        mActivityImageShowerBinding.tvConform.setText(
                String.format(
                        getResources().getString(R.string.conform_count),
                        pickedImageCount,
                        maxImageCount
                )
        );
    }

    protected boolean imageCancelPick(ImageBean imageBean) {
        imageBean.setIsPicked(false);
        ImagePicker.sPickedImageBeanList.remove(imageBean);
        mActivityImageShowerBinding.ivPick.setSelected(false);
        setConformButtonText(
                CollectionUtil.size(ImagePicker.sPickedImageBeanList),
                mMaxImagePickCount
        );
        return true;
    }

    protected boolean imagePick(ImageBean imageBean) {
        if (CollectionUtil.size(ImagePicker.sPickedImageBeanList) < mMaxImagePickCount) {
            imageBean.setIsPicked(true);
            ImagePicker.sPickedImageBeanList.add(imageBean);
            mActivityImageShowerBinding.ivPick.setSelected(true);
            setConformButtonText(
                    CollectionUtil.size(ImagePicker.sPickedImageBeanList),
                    mMaxImagePickCount
            );
            return true;
        }
        ToastUtil.getInstance().showToast(
                getApplicationContext(),
                R.string.can_not_pick_more_image
        );
        return false;
    }

}
