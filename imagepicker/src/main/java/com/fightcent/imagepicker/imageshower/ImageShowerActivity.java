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
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.util.CollectionUtil;
import com.fightcent.imagepicker.util.ViewUtil;

import java.util.ArrayList;

/**
 * Created by andy.guo on 2018/3/21.
 */

public class ImageShowerActivity extends AppCompatActivity {

    private ActivityImageShowerBinding mActivityImageShowerBinding;
    public static final String ALL_IMAGE_BEAN_LIST = "ALL_IMAGE_BEAN_LIST";
    private ArrayList<ImageBean> mAllImageBeanList = new ArrayList<>();

    public static final String INIT_SHOW_POSITION = "INIT_SHOW_POSITION";
    private int mInitShowPosition;

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
            mInitShowPosition = intent.getIntExtra(INIT_SHOW_POSITION, 0);
        }

        initViews();
        initListeners();
    }

    private void initViews() {
        mImageShowerAdapter = new ImageShowerAdapter(getSupportFragmentManager());
        mActivityImageShowerBinding.vp.setAdapter(
                mImageShowerAdapter
        );
        mActivityImageShowerBinding.vp.setCurrentItem(mInitShowPosition);
        setPositionSizeText(mInitShowPosition + 1);

        if (ImagePickerActivity.mAllImageBeanList.get(mInitShowPosition).isIsPicked()) {
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
                        mInitShowPosition = position;
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
                            //TODO 图片取消选中，怎么把选中的值返回过去


                        } else {
                            mActivityImageShowerBinding.ivPick.setSelected(true);
                            //TODO 图片选中之后的处理


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

}
