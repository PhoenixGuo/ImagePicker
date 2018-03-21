package com.fightcent.imagepicker;

import com.fightcent.imagepicker.model.OnImagePickedEvent;
import com.fightcent.imagepicker.model.OnImagePickerActivityDestroyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
            mOnImagePickedListener.onImagePicked(onImagePickedEvent.getImageList());
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
