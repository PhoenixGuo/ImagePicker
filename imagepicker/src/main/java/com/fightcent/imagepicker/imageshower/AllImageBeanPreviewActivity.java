package com.fightcent.imagepicker.imageshower;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fightcent.imagepicker.ImagePicker;

/**
 * Created by andy.guo on 2018/3/21.
 */

public class AllImageBeanPreviewActivity extends BaseImagePreviewActivity {

/*    public static final String ALL_IMAGE_BEAN_LIST = "ALL_IMAGE_BEAN_LIST";
    private ArrayList<ImageBean> mAllImageBeanList = new ArrayList<>();*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mImageBeanList.addAll(ImagePicker.sAllImageBeanList);
        super.onCreate(savedInstanceState);
    }

}
