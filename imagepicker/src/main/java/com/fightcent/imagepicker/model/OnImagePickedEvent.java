package com.fightcent.imagepicker.model;

import java.util.ArrayList;

/**
 * Created by andy.guo on 2018/3/20.
 */

public class OnImagePickedEvent {

    private ArrayList<ImageBean> mImageBeanList;

    public OnImagePickedEvent(ArrayList<ImageBean> imageList) {
        mImageBeanList = imageList;
    }

    public ArrayList<ImageBean> getImageList() {
        return mImageBeanList;
    }

}
