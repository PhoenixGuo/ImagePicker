package fightcent.imagepickerdemoapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.fightcent.imagepicker.ImagePicker;
import com.fightcent.imagepicker.OnImagePickedListener;
import com.fightcent.imagepicker.model.ImageBean;
import com.fightcent.imagepicker.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import fightcent.imagepickerdemoapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;

    public ArrayList<ImageBean> mImageList = new ArrayList<>();
    public PickedImageAdapter mPickedImageAdapter;

    public void addAllImageList(ArrayList<ImageBean> imageList) {
        mImageList.clear();
        mImageList.addAll(imageList);
    }

    static class MyOnImagePickedListener implements OnImagePickedListener {

        WeakReference<MainActivity> mainActivityWeakReference;

        public MyOnImagePickedListener(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onImagePicked(ArrayList<ImageBean> imageList) {
            mainActivityWeakReference.get().addAllImageList(imageList);
            mainActivityWeakReference.get().notifyAdapter();
            ToastUtil.getInstance().showToast(
                    mainActivityWeakReference.get().getApplicationContext(),
                    mainActivityWeakReference.get()
                            .getApplicationContext()
                            .getString(R.string.picked_images_count)
                            + imageList.size()
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        );

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        mActivityMainBinding.rv.setLayoutManager(gridLayoutManager);
        mPickedImageAdapter = new PickedImageAdapter(getApplicationContext(), mImageList);
        mActivityMainBinding.rv.setAdapter(mPickedImageAdapter);

        final ImagePicker imagePicker = new ImagePicker.Builder(getApplicationContext())
                .setMaxImagePickCount(7)
                .setOnImagePickedListener(
                        new MyOnImagePickedListener(MainActivity.this)
                )
                .setColumnCount(4)
                .build();

        mActivityMainBinding.btnJumpToImagePicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*new ImagePicker.Builder(MainActivity.this)
                                .setMaxImagePickCount(7)
                                .setOnImagePickedListener(
                                        new MyOnImagePickedListener(MainActivity.this)
                                )
                                .setColumnCount(4)
                                .build()
                                .show();*/
                        imagePicker.show();
                    }
                }
        );
    }

    public void notifyAdapter() {
        mPickedImageAdapter.notifyDataSetChanged();
    }

}
