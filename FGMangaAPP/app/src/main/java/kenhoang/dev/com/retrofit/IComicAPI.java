package kenhoang.dev.com.retrofit;

import java.util.List;

import io.reactivex.Observable;
import kenhoang.dev.com.model.Banner;
import kenhoang.dev.com.model.Chapter;
import kenhoang.dev.com.model.Comic;
import kenhoang.dev.com.model.Link;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IComicAPI {
    @GET("banner")
    Observable<List<Banner>> getBannerList();

    @GET("comic")
    Observable<List<Comic>> getComicList();

    @GET("chapter/{comicid}")
    Observable<List<Chapter>> getChapterList(@Path("comicid") int comicid);

    @GET("links/{chapterid}")
    Observable<List<Link>> getImageList(@Path("chapterid") int comicid);
}
