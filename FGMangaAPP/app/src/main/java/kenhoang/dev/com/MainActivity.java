package kenhoang.dev.com;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kenhoang.dev.com.adapter.MySliderAdapter;
import kenhoang.dev.com.common.Common;
import kenhoang.dev.com.model.Banner;
import kenhoang.dev.com.retrofit.IComicAPI;
import kenhoang.dev.com.service.PicassoImageLoadingService;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity {

    Slider slider;
    IComicAPI iComicAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init API
        iComicAPI = Common.getAPI();

        // View
        slider = findViewById(R.id.banner_slider);
        Slider.init(new PicassoImageLoadingService());

        fetchBanner();
    }

    private void fetchBanner() {
        compositeDisposable.add(iComicAPI.getBannerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        slider.setAdapter(new MySliderAdapter(banners));
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
