package kenhoang.dev.com.retrofit;

import java.util.List;

import io.reactivex.Observable;
import kenhoang.dev.com.model.Banner;
import kenhoang.dev.com.model.Comic;
import retrofit2.http.GET;

public interface IComicAPI {
    @GET("banner")
    Observable<List<Banner>> getBannerList();

    @GET("comic")
    Observable<List<Comic>> getComicList();
}
