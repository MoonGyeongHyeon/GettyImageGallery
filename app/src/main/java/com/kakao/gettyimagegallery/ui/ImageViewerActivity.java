package com.kakao.gettyimagegallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by khan.moon on 2018. 3. 16..
 */

public class ImageViewerActivity extends AppCompatActivity {
    private static final String TAG = "ImageViewerActivity";

    private ViewPager viewPager;

    private List<GalleryImage> galleryImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        Intent intent = getIntent();

        if (intent == null) {
            Log.d(TAG, "Failed to get image data");
            finish();
            return;
        }

        viewPager = findViewById(R.id.viewpager_viewer);

        galleryImages = Parcels.unwrap(intent.getParcelableExtra("galleryImages"));
        int viewingPosition = intent.getIntExtra("viewingPosition", 0);

        ImageViewerPagerAdapter adapter = new ImageViewerPagerAdapter(galleryImages);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewingPosition);

        ImageView close = findViewById(R.id.imageview_viewer_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) v.getContext()).finish();
            }
        });
    }
}
