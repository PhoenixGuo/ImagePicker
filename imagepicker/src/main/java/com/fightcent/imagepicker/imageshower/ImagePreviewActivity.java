package com.fightcent.imagepicker.imageshower;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fightcent.imagepicker.ImagePicker;

/**
 * Created by andy.guo on 2018/3/23.
 */

public class ImagePreviewActivity extends BaseImageShowerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mImageBeanList.addAll(ImagePicker.sPickedImageBeanList);
        super.onCreate(savedInstanceState);
    }

}
