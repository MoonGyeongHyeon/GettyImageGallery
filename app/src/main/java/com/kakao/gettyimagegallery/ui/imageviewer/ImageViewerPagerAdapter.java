package com.kakao.gettyimagegallery.ui.imageviewer;

import android.support.v4.view.PagerAdapter;
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
 * Created by khan.moon on 2018. 3. 16..
 */

public class ImageViewerPagerAdapter extends PagerAdapter {
    private List<GalleryImage> galleryImages;

    public ImageViewerPagerAdapter(List<GalleryImage> galleryImages) {
        this.galleryImages = galleryImages;
    }

    @Override
    public int getCount() {
        return galleryImages != null ? galleryImages.size() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_image_viewer, container, false);

        TextView title = view.findViewById(R.id.textview_viewer_title);
        ImageView image = view.findViewById(R.id.imageview_viewer);

        GalleryImage galleryImage = galleryImages.get(position);

        title.setText(String.format(Locale.getDefault(),
                "%d. %s", galleryImage.getNumber(), galleryImage.getName()));
        Glide.with(view.getContext())
                .load(galleryImage.getUrl())
                .error(R.drawable.img_loading_failed)
                .into(image);

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
