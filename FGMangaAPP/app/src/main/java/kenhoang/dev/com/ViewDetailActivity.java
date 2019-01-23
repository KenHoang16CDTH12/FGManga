package kenhoang.dev.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kenhoang.dev.com.adapter.ChapterAdapter;
import kenhoang.dev.com.adapter.CustomViewPagerAdapter;
import kenhoang.dev.com.common.Common;
import kenhoang.dev.com.model.Chapter;
import kenhoang.dev.com.model.Link;
import kenhoang.dev.com.retrofit.IComicAPI;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.List;

public class ViewDetailActivity extends AppCompatActivity {
    IComicAPI iComicAPI;
    ViewPager viewPager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView tvChapterName;
    View back, next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);
        // Init API
        iComicAPI = Common.getAPI();
        // Init View
        viewPager = findViewById(R.id.viewPager);
        tvChapterName = findViewById(R.id.tvChapterName);
        back = findViewById(R.id.chapterBack);
        next = findViewById(R.id.chapterNext);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.chapter_index == 0) // If user in first chapter but press back
                {
                    Toast.makeText(ViewDetailActivity.this, "You are reading first chapter", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Common.chapter_index--;
                    fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.chapter_index == Common.chapterList.size() - 1) // If user in last chapter but press next
                {
                    Toast.makeText(ViewDetailActivity.this, "You are reading last chapter", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Common.chapter_index++;
                    fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                }
            }
        });

        fetchLinks(Common.selected_chapter.getID());
    }

    private void fetchLinks(int id) {
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait ...")
                .setCancelable(false)
                .build();
        dialog.show();

        compositeDisposable.add(iComicAPI.getImageList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Link>>() {
                    @Override
                    public void accept(List<Link> links) throws Exception {
                        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getBaseContext(), links);
                        viewPager.setAdapter(adapter);
                        tvChapterName.setText(Common.formatString(Common.selected_chapter.getName()));
                        // Create Book Flip Page
                        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
                        bookFlipPageTransformer.setScaleAmountPercent(10f);
                        viewPager.setPageTransformer(true, bookFlipPageTransformer);
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        Toast.makeText(ViewDetailActivity.this, "This chappter is being translating", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
