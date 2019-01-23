package kenhoang.dev.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
        // Fetch data
        fetchBanner();
        fetchComic();
    }

    private void fetchComic() {
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait ...")
                .setCancelable(false)
                .build();
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
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
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
