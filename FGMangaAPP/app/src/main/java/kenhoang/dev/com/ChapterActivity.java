package kenhoang.dev.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kenhoang.dev.com.adapter.ChapterAdapter;
import kenhoang.dev.com.adapter.ComicAdapter;
import kenhoang.dev.com.common.Common;
import kenhoang.dev.com.model.Chapter;
import kenhoang.dev.com.model.Comic;
import kenhoang.dev.com.retrofit.IComicAPI;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    IComicAPI iComicAPI;
    RecyclerView recycler_chapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView tvChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        // Init API
        iComicAPI = Common.getAPI();
        // View
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.selected_comic.getName());
        // Set icon for toolbar
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recycler_chapter = findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        tvChapter = findViewById(R.id.tvChapter);

        // Fetch data
        fetchChapter();
    }

    private void fetchChapter() {
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait ...")
                .setCancelable(false)
                .build();
        dialog.show();

        compositeDisposable.add(iComicAPI.getChapterList(Common.selected_comic.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Chapter>>() {
                    @Override
                    public void accept(List<Chapter> chapters) throws Exception {
                        Common.chapterList = chapters; // Save chapter to back, next
                        recycler_chapter.setAdapter(new ChapterAdapter(getBaseContext(), chapters));
                        tvChapter.setText(new StringBuilder("CHAPTER (")
                                .append(chapters.size())
                                .append(")"));
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        Toast.makeText(ChapterActivity.this, "Error while loading chapter", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
