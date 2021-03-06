package com.kakao.gettyimagegallery.ui.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kakao.gettyimagegallery.Environment;
import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;
import com.kakao.gettyimagegallery.net.Network;
import com.kakao.gettyimagegallery.ui.common.OnBackPressedListener;
import com.kakao.gettyimagegallery.ui.imageviewer.ImageViewerFragment;
import com.kakao.gettyimagegallery.ui.utils.FragmentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ProgressBar progressBar;
    private Button networkRetryButton;
    private FrameLayout container;

    private Network network;
    private List<GalleryImage> galleryImages;
    private boolean isFetched;
    private boolean isConfigChanged;

    private ViewInitializer viewInitializer;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "orientation: " + newConfig.orientation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        isConfigChanged = false;
        isFetched = false;

        init();
        fetchGettyImageData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        Log.d(TAG, "init()");
        bindView();

        viewInitializer = new ViewInitializer() {
            @Override
            public void init() {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.framelayout_main_container, MainFragment.newInstance(galleryImages), MainFragment.TAG)
                        .commit();

                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    private void bindView() {
        container = findViewById(R.id.framelayout_main_container);
        progressBar = findViewById(R.id.progressbar);
        networkRetryButton = findViewById(R.id.button_retry_connection);
    }

    private void fetchGettyImageData() {
        network = Network.getInstance();
        network.getHtml(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse");

                if (isFinishing()) {
                    return;
                }

                if (!isConfigChanged && !isFetched) {
                    Log.d(TAG, "get Data");
                    String html;
                    try {
                        html = response.body().string();
                        response.body().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    Document document = Jsoup.parse(html);
                    Elements elements = document.getElementsByClass("gallery-item-group");

                    GalleryImage galleryImage = null;

                    int i = 1;
                    galleryImages = new ArrayList<>();

                    for (Element element :
                            elements) {

                        galleryImage = new GalleryImage();
                        galleryImage.setNumber(i++);
                        galleryImage.setName(element.getElementsByClass("gallery-item-caption").get(0).getElementsByTag("a").get(0).text());
                        galleryImage.setUrl(Environment.baseUrl + element.getElementsByTag("img").get(0).attr("src"));
                        galleryImages.add(galleryImage);
                    }

                    if (viewInitializer != null) {
                        viewInitializer.init();
                    }

                    isFetched = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure, message: " + t.getMessage());

                initNetworkNotConnectedView();
            }
        });
    }

    private void initNetworkNotConnectedView() {
        Log.d(TAG, "NetworkConnection is failed");
        Toast.makeText(this, "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.GONE);
        networkRetryButton.setVisibility(View.VISIBLE);

        networkRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                networkRetryButton.setVisibility(View.GONE);

                fetchGettyImageData();
            }
        });
    }

    @Subscribe
    public void onEvent(GalleryImageAdapter.ImageClickEvent e) {
        Fragment fragment = ImageViewerFragment.newInstance(e.getGalleryImages(), e.getPosition());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_main_container, fragment, ImageViewerFragment.TAG)
                .addToBackStack(ImageViewerFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = FragmentUtils.getLastFragment(this);

        if (fragment != null) {
            if (fragment instanceof OnBackPressedListener) {
                ((OnBackPressedListener) fragment).onBackPressed();
                return;
            }
        }

        super.onBackPressed();
    }
}
