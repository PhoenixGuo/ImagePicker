package com.fightcent.imagepicker;

import com.fightcent.imagepicker.model.ImageBean;

import java.util.ArrayList;

/**
 * Created by andy.guo on 2018/3/19.
 */

public interface OnImagePickedListener {

    void onImagePicked(ArrayList<ImageBean> pictureList);

}
