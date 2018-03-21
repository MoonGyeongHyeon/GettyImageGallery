package com.kakao.gettyimagegallery.ui.imageviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;
import com.kakao.gettyimagegallery.ui.common.OnBackPressedListener;
import com.kakao.gettyimagegallery.ui.utils.FragmentUtils;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by khan.moon on 2018. 3. 16..
 */

public class ImageViewerFragment extends Fragment implements OnBackPressedListener {
    public static final String TAG = "ImageViewerFragment";

    private ViewPager viewPager;

    private List<GalleryImage> galleryImages;

    public static Fragment newInstance(List<GalleryImage> galleryImages, int position) {
        Fragment fragment = new ImageViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("galleryImages", Parcels.wrap(galleryImages));
        bundle.putInt("viewingPosition", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("RestrictedApi") final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_ImageViewer);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View layout = localInflater.inflate(R.layout.fragment_image_viewer, container, false);

        viewPager = layout.findViewById(R.id.viewpager_viewer);
        ImageView close = layout.findViewById(R.id.imageview_viewer_close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

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

        if (!bundle.containsKey("galleryImages")){
            Log.d(TAG, "Failed to get data");
            return;
        }

        galleryImages = Parcels.unwrap(bundle.getParcelable("galleryImages"));
        int viewingPosition = bundle.getInt("viewingPosition", 0);

        ImageViewerPagerAdapter adapter = new ImageViewerPagerAdapter(galleryImages);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewingPosition);
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void close() {
        FragmentUtils.close(getActivity(), ImageViewerFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
