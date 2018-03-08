package com.kakao.gettyimagegallery.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.kakao.gettyimagegallery.Environment;
import com.kakao.gettyimagegallery.R;
import com.kakao.gettyimagegallery.model.GalleryImage;
import com.kakao.gettyimagegallery.net.Network;

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

    private Network network;
    private List<GalleryImage> galleryImages;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("galleryImages", Parcels.wrap(galleryImages));
        outState.putInt("count", ((GalleryImageAdapter)recyclerView.getAdapter()).getCount());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        galleryImages = new ArrayList<>();

        bindView();

        if (savedInstanceState != null) {
            galleryImages = Parcels.unwrap(savedInstanceState.getParcelable("galleryImages"));
            switchViewVisibility(recyclerView);
            switchViewVisibility(progressBar);

            initRecyclerView();

            int count = savedInstanceState.getInt("count");

            GalleryImageAdapter adapter = (GalleryImageAdapter) recyclerView.getAdapter();
            adapter.setCount(count);

            return;
        }

        network = Network.getInstance();
        network.getHtml(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse");

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

                for (Element element:
                        elements) {
//                    Log.d(TAG, element.getElementsByTag("img").get(0).attr("src"));
//                    Log.d(TAG, element.getElementsByClass("gallery-item-caption").get(0).getElementsByTag("a").get(0).text());

                    galleryImage = new GalleryImage();
                    galleryImage.setNumber(i++);
                    galleryImage.setName(element.getElementsByClass("gallery-item-caption").get(0).getElementsByTag("a").get(0).text());
                    galleryImage.setUrl(Environment.baseUrl + element.getElementsByTag("img").get(0).attr("src"));
                    galleryImages.add(galleryImage);
                }

                switchViewVisibility(recyclerView);
                switchViewVisibility(progressBar);

                initRecyclerView();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
    }

    private void switchViewVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        GalleryImageAdapter adapter = new GalleryImageAdapter(galleryImages);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    GalleryImageAdapter galleryImageAdapter = (GalleryImageAdapter) recyclerView.getAdapter();

                    int lastPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    lastPosition += 1; // position이 0번부터 시작.

//                    Log.d(TAG, "LastPosition: " + lastPosition);
//                    Log.d(TAG, "ItemCount: " + galleryImageAdapter.getItemCount());

                    if (lastPosition >= galleryImageAdapter.getItemCount()) {
                        Log.d(TAG, "Pagination");
                        galleryImageAdapter.addItemCount(10);
                        galleryImageAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}
