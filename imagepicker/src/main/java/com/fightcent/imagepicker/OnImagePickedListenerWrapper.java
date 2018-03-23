package com.fightcent.imagepicker;

import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.model.event.OnImagePickedEvent;
import com.fightcent.imagepicker.model.event.OnImagePickerActivityDestroyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by andy.guo on 2018/3/20.
 */

public class OnImagePickedListenerWrapper {

    private OnImagePickedListener mOnImagePickedListener;

    public OnImagePickedListenerWrapper(OnImagePickedListener onImagePickedListener) {
        mOnImagePickedListener = onImagePickedListener;
    }

    @Subscribe
    public void onReceivedOnImagePickedEvent(OnImagePickedEvent onImagePickedEvent) {
        if (mOnImagePickedListener != null) {
            ArrayList<ImageBean> imageBeanList = new ArrayList<>();
            imageBeanList.addAll(ImagePicker.sPickedImageBeanList);
            ImagePicker.sAllImageBeanList.clear();
            ImagePicker.sPickedImageBeanList.clear();
            ActivityManager.getAppManager().finishAllActivity();
            mOnImagePickedListener.onImagePicked(imageBeanList);
        }
    }

    @Subscribe
    public void onReceivedOnImagePickerActivityDestroyEvent(
            OnImagePickerActivityDestroyEvent onNeedImagePickedListenerDestroyEvent
    ) {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
