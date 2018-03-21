package com.fightcent.imagepicker.model;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by andy.guo on 2018/2/1.
 */

public class ImageBeanFactory {

    public static ImageBean createImageBeanByCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        //获取图片的id
        int id = cursor.getInt(
                cursor.getColumnIndex(MediaStore.MediaColumns._ID)
        );
        //获取图片的名称
        String name = cursor.getString(
                cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        );
        //获取图片的详细信息
        String desc = cursor.getString(
                cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION)
        );
        //获取图片的角度
        int orientation = cursor.getInt(
                cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)
        );
        //获取图片的大小
        int size = cursor.getInt(
                cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
        );
        //获取BaseContentUri，即content://media/external/images/media/
        Uri baseContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //将BaseContentUri与图片ID进行组装，组装成Uri
        Uri contentUri = Uri.withAppendedPath(baseContentUri, String.valueOf(id));

        //包装Picture对象
        ImageBean imageBean = new ImageBean();
        imageBean.setId(id);
        imageBean.setName(name);
        imageBean.setDesc(desc);
        imageBean.setOrientation(orientation);
        imageBean.setSize(size);
        imageBean.setContentUriString(contentUri.toString());

        return imageBean;
    }

}
