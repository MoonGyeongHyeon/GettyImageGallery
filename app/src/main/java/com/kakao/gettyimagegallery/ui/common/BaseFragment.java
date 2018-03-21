package com.kakao.gettyimagegallery.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by khan.moon on 2018. 3. 20..
 */

public abstract class BaseFragment extends Fragment {
    private boolean isNew;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNew = true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onViewCreatedImpl(view, savedInstanceState, isNew);
        isNew = false;
    }

    protected void onViewCreatedImpl(View view, @Nullable Bundle saveInstanceState, boolean isNew) {
    }
}
