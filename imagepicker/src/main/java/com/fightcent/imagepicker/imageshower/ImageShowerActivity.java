package com.fightcent.imagepicker.imageshower;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.fightcent.imagepicker.R;
import com.fightcent.imagepicker.databinding.ActivityImageShowerBinding;
import com.fightcent.imagepicker.imagepickerview.ImagePickerActivity;
import com.fightcent.imagepicker.util.CollectionUtil;
import com.fightcent.imagepicker.util.ViewUtil;

/**
 * Created by andy.guo on 2018/3/21.
 */

public class ImageShowerActivity extends AppCompatActivity {

    private ActivityImageShowerBinding mActivityImageShowerBinding;
/*    public static final String ALL_IMAGE_BEAN_LIST = "ALL_IMAGE_BEAN_LIST";
    private ArrayList<ImageBean> mAllImageBeanList = new ArrayList<>();*/

    public static final String CURRENT_SHOW_POSITION = "CURRENT_SHOW_POSITION";
    private int mCurrentShowPosition;

    public static final String MAX_IMAGE_PICK_COUNT = "MAX_IMAGE_PICK_COUNT";
    private int mMaxImagePickCount;

    private boolean mIsShowToolBar = true;
    private ImageShowerAdapter mImageShowerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                CollectionUtil.size(ImagePickerActivity.mPickedImageBeanList),
                mMaxImagePickCount
        );
        mImageShowerAdapter = new ImageShowerAdapter(getSupportFragmentManager());
        mActivityImageShowerBinding.vp.setAdapter(
                mImageShowerAdapter
        );
        mActivityImageShowerBinding.vp.setCurrentItem(mCurrentShowPosition);
        setPositionSizeText(mCurrentShowPosition + 1);

        if (ImagePickerActivity.mAllImageBeanList.get(mCurrentShowPosition).isIsPicked()) {
            mActivityImageShowerBinding.ivPick.setSelected(true);
        }
    }

    private void initListeners() {
        mActivityImageShowerBinding.ivBackArrow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageShowerActivity.this.finish();
                    }
                }
        );

        mActivityImageShowerBinding.vp.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        mCurrentShowPosition = position;
                        setPositionSizeText(position + 1);
                        if (ImagePickerActivity.mAllImageBeanList.get(position).isIsPicked()) {
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
                        if (mActivityImageShowerBinding.ivPick.isSelected()) {
                            mActivityImageShowerBinding.ivPick.setSelected(false);
                            ImagePickerActivity.mAllImageBeanList
                                    .get(mCurrentShowPosition)
                                    .setIsPicked(
                                            false
                                    );
                            ImagePickerActivity.mPickedImageBeanList.remove(
                                    ImagePickerActivity.mAllImageBeanList.get(mCurrentShowPosition)
                            );
                            setConformButtonText(
                                    CollectionUtil.size(ImagePickerActivity.mPickedImageBeanList),
                                    mMaxImagePickCount
                            );
                        } else {
                            mActivityImageShowerBinding.ivPick.setSelected(true);
                            ImagePickerActivity.mAllImageBeanList
                                    .get(mCurrentShowPosition)
                                    .setIsPicked(
                                            true
                                    );
                            ImagePickerActivity.mPickedImageBeanList.add(
                                    ImagePickerActivity.mAllImageBeanList.get(mCurrentShowPosition)
                            );
                            setConformButtonText(
                                    CollectionUtil.size(ImagePickerActivity.mPickedImageBeanList),
                                    mMaxImagePickCount
                            );
                        }
                    }
                }
        );
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

    private void setPositionSizeText(int position) {
        String positionSizeText = String.format(
                getResources().getString(R.string.position_size),
                position,
                CollectionUtil.size(ImagePickerActivity.mAllImageBeanList)
        );
        mActivityImageShowerBinding.tvPositionSize.setText(positionSizeText);
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

    private void hideToolbarTop() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                mActivityImageShowerBinding.tbTop,
                "translationY",
                -mActivityImageShowerBinding.tbTop.getHeight()
        );
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    private void showToolbarTop() {
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

    private void hideToolbarBottom() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                mActivityImageShowerBinding.tbBottom,
                "translationY",
                mActivityImageShowerBinding.tbBottom.getHeight()
        );
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    private void showToolbarBottom() {
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

    private class ImageShowerAdapter extends FragmentStatePagerAdapter {

        public ImageShowerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageShowerFragment.makeFragment(
                    ImagePickerActivity.mAllImageBeanList.get(position)
            );
        }

        @Override
        public int getCount() {
            return CollectionUtil.size(ImagePickerActivity.mAllImageBeanList);
        }
    }

    private void setConformButtonText(int pickedImageCount, int maxImageCount) {
        mActivityImageShowerBinding.tvConform.setText(
                String.format(
                        getResources().getString(R.string.conform_count),
                        pickedImageCount,
                        maxImageCount
                )
        );
    }

}
