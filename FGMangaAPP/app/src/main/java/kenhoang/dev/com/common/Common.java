package kenhoang.dev.com.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import kenhoang.dev.com.model.Chapter;
import kenhoang.dev.com.model.Comic;
import kenhoang.dev.com.retrofit.IComicAPI;
import kenhoang.dev.com.retrofit.RetrofitClient;

public class Common  {

    public static final String URL = "http://192.168.1.5:3000/";

    public static Comic selected_comic;
    public static Chapter selected_chapter;
    public static int chapter_index = -1;
    public static List<Chapter> chapterList = new ArrayList<>();

    public static IComicAPI getAPI() {
        return RetrofitClient.getInstance().create(IComicAPI.class);
    }

    public static String formatString(String name) {
        // if character is too long, just substring
        StringBuilder finalResult = new StringBuilder(name.length() > 15 ? name.substring(0, 15) + "..." : name);
        return finalResult.toString();
    }

    public static boolean isConnectedToInternet(Context baseContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) baseContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for(int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
            }
        }
        return false;
    }
}
