package kenhoang.dev.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kenhoang.dev.com.adapter.ComicAdapter;
import kenhoang.dev.com.adapter.CustomSliderAdapter;
import kenhoang.dev.com.common.Common;
import kenhoang.dev.com.model.Banner;
import kenhoang.dev.com.model.Comic;
import kenhoang.dev.com.retrofit.IComicAPI;
import kenhoang.dev.com.service.PicassoImageLoadingService;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity {

    Slider slider;
    IComicAPI iComicAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recycler_comic;
    TextView tvNumberComic;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init API
        iComicAPI = Common.getAPI();

        // View
        slider = findViewById(R.id.banner_slider);
        Slider.init(new PicassoImageLoadingService());

        recycler_comic = findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this, 2));

        tvNumberComic = findViewById(R.id.tvNumComic);

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    // Fetch data
                    fetchBanner();
                    fetchComic();
                } else {
                    Toast.makeText(MainActivity.this, "Cannot connected to INTERNET!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Default load first time
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    // Fetch data
                    fetchBanner();
                    fetchComic();
                } else {
                    Toast.makeText(MainActivity.this, "Cannot connected to INTERNET!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FilterCategoryActivity.class));
            }
        });
    }

    private void fetchComic() {
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait ...")
                .setCancelable(false)
                .build();
        if (!swipeRefreshLayout.isRefreshing())
            dialog.show();

        compositeDisposable.add(iComicAPI.getComicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {
                        recycler_comic.setAdapter(new ComicAdapter(getBaseContext(), comics));
                        tvNumberComic.setText(new StringBuilder("NEW COMIC (")
                                .append(comics.size())
                                .append(")"));
                        if (!swipeRefreshLayout.isRefreshing())
                            dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!swipeRefreshLayout.isRefreshing())
                            dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Error while loading comic", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void fetchBanner() {
        compositeDisposable.add(iComicAPI.getBannerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        slider.setAdapter(new CustomSliderAdapter(banners));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "Error while loading banner", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
