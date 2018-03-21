package com.kakao.gettyimagegallery.ui.main;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khan.moon on 2018. 3. 9..
 */

public class GalleryImagePagerAdapter extends PagerAdapter {
    public static final String TAG = "GalleryPagerAdapter";
    private static int MAX_PAGE_COUNT = 10;

    private List<GalleryImage> galleryImages;
    private SparseIntArray viewingPositions;

    public GalleryImagePagerAdapter(List<GalleryImage> galleryImages) {
        this.galleryImages = galleryImages;
        viewingPositions = new SparseIntArray();
    }

    @Override
    public int getCount() {
        return MAX_PAGE_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem");
        final int pos = position;
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_page_container, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_page);
        TextView pageNumber = view.findViewById(R.id.textview_page_number);

        List<GalleryImage> dividedGalleryImages = divideGalleryImages(pos);

        GalleryImageAdapter adapter = new GalleryImageAdapter(dividedGalleryImages);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int viewingPostion = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    Log.d(TAG, "Put viewingPosition: " + viewingPostion);
                    viewingPositions.put(pos, viewingPostion);
                }
            }
        });

        pageNumber.setText("page " + (position + 1));

        int viewingPosition = viewingPositions.get(position);
        recyclerView.scrollToPosition(viewingPosition);

        Log.d(TAG, "instantiate Position: " + position + ", viewingPosition: " + viewingPosition);

        container.addView(view);

        return view;
    }

    private List<GalleryImage> divideGalleryImages(int position) {
        int size = galleryImages.size();
        Log.d(TAG, "size: " + size);
        boolean hasMod = (size % MAX_PAGE_COUNT) != 0;
        int count = size / MAX_PAGE_COUNT;
        if (hasMod) {
            count += 1;
        }

        int start = count * position;
        int end = count * (position + 1);

        List<GalleryImage> dividedGalleryImages;

        try {
            dividedGalleryImages = galleryImages.subList(start, end);
        } catch (IndexOutOfBoundsException e) {
            end = size - 1;
            dividedGalleryImages = galleryImages.subList(start, end);
        }

        Log.d(TAG, "start: " + start + ", end: " + end);

        return new ArrayList<>(dividedGalleryImages);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem");
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
