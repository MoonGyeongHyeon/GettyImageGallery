package com.kakao.gettyimagegallery.ui;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;

import java.util.List;
import java.util.Locale;

/**
 * Created by khan.moon on 2018. 3. 9..
 */

public class GalleryImagePagerAdapter extends PagerAdapter {
    private static final String TAG = "GalleryPagerAdapter";

    private List<GalleryImage> galleryImages;

    public GalleryImagePagerAdapter(List<GalleryImage> galleryImages) {
        this.galleryImages = galleryImages;
    }

    @Override
    public int getCount() {
        return galleryImages != null ? galleryImages.size() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.gallery_image, container, false);

        GalleryImage galleryImage = galleryImages.get(position);

        ImageView imageView = view.findViewById(R.id.imageview_gallery_image);
        TextView nameTextView = view.findViewById(R.id.textview_gallery_image_name);
        TextView numberTextView = view.findViewById(R.id.textview_gallery_number);

        Glide.with(view.getContext())
                .load(galleryImage.getUrl())
                .into(imageView);
        nameTextView.setText(galleryImage.getName());
        numberTextView.setText(String.format(Locale.getDefault(),"%d.",galleryImage.getNumber()));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
