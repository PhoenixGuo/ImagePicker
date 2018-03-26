package com.fightcent.imagepicker.imageshower;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fightcent.imagepicker.R;
import com.fightcent.imagepicker.databinding.FragmentImageShowerBinding;
import com.fightcent.imagepicker.model.ImageBean;

/**
 * Created by andy.guo on 2018/3/22.
 */

public class ImageShowerFragment extends Fragment {

    private static final String IMAGE_BEAN = "IMAGE_BEAN";
    private ImageBean mImageBean;
    private FragmentImageShowerBinding mFragmentImageShowerBinding;
    private BaseImagePreviewActivity mBaseImageShowerActivity;

    public static ImageShowerFragment makeFragment(ImageBean imageBean) {
        ImageShowerFragment imageShowerFragment = new ImageShowerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IMAGE_BEAN, imageBean);
        imageShowerFragment.setArguments(bundle);
        return imageShowerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragmentImageShowerBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_image_shower,
                container,
                false
        );
        Bundle bundle = getArguments();
        if (bundle != null) {
            mImageBean = (ImageBean) bundle.getSerializable(IMAGE_BEAN);
        }

        initViews();
        initListeners();
        return mFragmentImageShowerBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity instanceof BaseImagePreviewActivity) {
            mBaseImageShowerActivity = (BaseImagePreviewActivity) fragmentActivity;
        }
    }

    private void initViews() {
        if (mImageBean != null) {
            Glide.with(getContext())
                    .load(mImageBean.getContentUriString())
                    .asBitmap()
                    .thumbnail(0.1f)
                    .into(mFragmentImageShowerBinding.iv);
        }
    }

    private void initListeners() {
        mFragmentImageShowerBinding.iv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mBaseImageShowerActivity != null) {
                            mBaseImageShowerActivity.showToolBar();
                        }
                    }
                }
        );
    }

}
