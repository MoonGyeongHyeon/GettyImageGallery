package com.kakao.gettyimagegallery.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kakao.gettyimagegallery.Environment;
import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;
import com.kakao.gettyimagegallery.net.Network;
import com.kakao.gettyimagegallery.net.NetworkConnectivityManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ViewPager viewPager;
    private Button networkRetryButton;

    private Network network;
    private List<GalleryImage> galleryImages;
    private boolean isInitialized;
    private boolean isActive;

    private ViewInitializer viewInitializer;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isInitialized) {
            Log.d(TAG, "onSaveInstanceState");
            outState.putParcelable("galleryImages", Parcels.wrap(galleryImages));

            if (isPortrait()) {
                outState.putInt("currentPosition", ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
            } else {
                Log.d(TAG, "Landscape CurrentPosition: " + viewPager.getCurrentItem());
                outState.putInt("currentPosition", viewPager.getCurrentItem());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        if (isPortrait()) {
            bindPortraitView();

            viewInitializer = new ViewInitializer() {
                @Override
                public void init() {
                    initRecyclerView();

                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            };

            if (savedInstanceState != null && savedInstanceState.containsKey("galleryImages")) {
                Log.d(TAG, "available BackupState");

                galleryImages = Parcels.unwrap(savedInstanceState.getParcelable("galleryImages"));

                viewInitializer.init();

                int currentPosition = savedInstanceState.getInt("currentPosition");
                recyclerView.getLayoutManager().scrollToPosition(currentPosition);

                isInitialized = true;

                return;
            }

            fetchGettyImageData();

        } else {
            bindLandscapeView();

            viewInitializer = new ViewInitializer() {
                @Override
                public void init() {
                    initViewPager();

                    viewPager.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            };

            if (savedInstanceState != null && savedInstanceState.containsKey("galleryImages")) {
                Log.d(TAG, "available BackupState");

                galleryImages = Parcels.unwrap(savedInstanceState.getParcelable("galleryImages"));

                viewInitializer.init();

                int currentPosition = savedInstanceState.getInt("currentPosition");
                viewPager.setCurrentItem(currentPosition, false);

                isInitialized = true;

                return;
            }

            fetchGettyImageData();
        }
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void bindPortraitView() {
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
        networkRetryButton = findViewById(R.id.button_retry_connection);
    }

    private void fetchGettyImageData() {
        if (!NetworkConnectivityManager.getInstance().isConnected()) {
            initNetworkNotConnectedView();

            return;
        }

        network = Network.getInstance();
        network.getHtml(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse");

                if (isActive) {
                    Log.d(TAG, "isActive");
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
//                    Log.d(TAG, element.getElementsByTag("img").get(0).attr("src"));
//                    Log.d(TAG, element.getElementsByClass("gallery-item-caption").get(0).getElementsByTag("a").get(0).text());

                        galleryImage = new GalleryImage();
                        galleryImage.setNumber(i++);
                        galleryImage.setName(element.getElementsByClass("gallery-item-caption").get(0).getElementsByTag("a").get(0).text());
                        galleryImage.setUrl(Environment.baseUrl + element.getElementsByTag("img").get(0).attr("src"));
                        galleryImages.add(galleryImage);
                    }

                    if (viewInitializer != null) {
                        viewInitializer.init();
                    }

                    isInitialized = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure");
                Toast.makeText(MainActivity.this, "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();

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

    private void bindLandscapeView() {
        viewPager = findViewById(R.id.viewpager);
        progressBar = findViewById(R.id.progressbar);
        networkRetryButton = findViewById(R.id.button_retry_connection);
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        GalleryImageAdapter adapter = new GalleryImageAdapter(galleryImages);
        recyclerView.setAdapter(adapter);
    }


    private void initViewPager() {
        GalleryImagePagerAdapter pagerAdapter = new GalleryImagePagerAdapter(galleryImages);

        viewPager.setAdapter(pagerAdapter);
    }

}
