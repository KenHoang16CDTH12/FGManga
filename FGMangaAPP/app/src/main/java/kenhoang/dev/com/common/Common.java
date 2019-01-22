package kenhoang.dev.com.common;

import kenhoang.dev.com.retrofit.IComicAPI;
import kenhoang.dev.com.retrofit.RetrofitClient;
import retrofit2.Retrofit;

public class Common  {
    public static IComicAPI getAPI() {
        return RetrofitClient.getInstance().create(IComicAPI.class);
    }
}
