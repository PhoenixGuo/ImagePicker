package com.fightcent.imagepicker.model;

import java.io.Serializable;

/**
 * Created by andy.guo on 2018/1/19.
 */

public class ImageBean implements Serializable{

    private int mId;
    private String mName;
    private String mDesc;
    private String mContentUriString;
    private int mOrientation;
    private int mSize;
    private boolean mIsPicked;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        this.mDesc = desc;
    }

    public String getContentUriString() {
        return mContentUriString;
    }

    public void setContentUriString(String contentUriString) {
        this.mContentUriString = contentUriString;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public boolean isIsPicked() {
        return mIsPicked;
    }

    public void setIsPicked(boolean isPicked) {
        this.mIsPicked = isPicked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageBean) {
            ImageBean imageBean = (ImageBean) obj;
            return imageBean.getId() == this.getId();
        } else {
            return super.equals(obj);
        }
    }

}
