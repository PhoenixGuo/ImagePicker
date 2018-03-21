package com.fightcent.imagepicker;

import com.fightcent.imagepicker.model.ImageBean;

/**
 * Created by andy.guo on 2018/3/16.
 */

public interface ImagePickerView {

    boolean imageCancelPick(ImageBean imageBean);

    boolean imagePick(ImageBean imageBean);

    int getColumnCount();

}
