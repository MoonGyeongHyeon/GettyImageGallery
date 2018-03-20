package com.kakao.gettyimagegallery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by khan.moon on 2018. 3. 19..
 */

public class MainFragment extends Fragment {
    public static final String TAG = "MainFragment";

    private ViewPager viewPager;

    private List<GalleryImage> galleryImages;

    public static Fragment newInstance(List<GalleryImage> galleryImages) {
        Fragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("galleryImages", Parcels.wrap(galleryImages));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = layout.findViewById(R.id.viewpager);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        Bundle bundle = getArguments();

        if (bundle == null) {
            Log.d(TAG, "Failed to get data");
            return;
        }

        galleryImages = Parcels.unwrap(bundle.getParcelable("galleryImages"));
        viewPager.setAdapter(new GalleryImagePagerAdapter(galleryImages));
    }
}
