package com.kakao.gettyimagegallery.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.gettyimagegallery.App;
import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

/**
 * Created by khan.moon on 2018. 3. 8..
 */

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> {
    private List<GalleryImage> galleryImages;

    public GalleryImageAdapter(List<GalleryImage> galleryImages) {
        this.galleryImages = galleryImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        holder.changeViewContents(galleryImages.get(pos));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ImageClickEvent(galleryImages, pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryImages != null ? galleryImages.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageview_gallery_image);
            name = itemView.findViewById(R.id.textview_gallery_image_name);
            number = itemView.findViewById(R.id.textview_gallery_number);
        }

        public void changeViewContents(GalleryImage galleryImage) {
            Glide.with(App.getInstance().getApplicationContext())
                    .load(galleryImage.getUrl())
                    .error(R.drawable.img_loading_failed)
                    .into(image);

            name.setText(galleryImage.getName());
            number.setText(String.format(Locale.getDefault(), "%d.", galleryImage.getNumber()));
        }
    }

    public static class ImageClickEvent {
        private List<GalleryImage> galleryImages;
        private int position;

        public ImageClickEvent(List<GalleryImage> galleryImages, int position) {
            this.galleryImages = galleryImages;
            this.position = position;
        }

        public List<GalleryImage> getGalleryImages() {
            return galleryImages;
        }

        public int getPosition() {
            return position;
        }
    }
}
