package kenhoang.dev.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kenhoang.dev.com.adapter.ComicAdapter;
import kenhoang.dev.com.adapter.MultipleChooseAdapter;
import kenhoang.dev.com.common.Common;
import kenhoang.dev.com.model.Category;
import kenhoang.dev.com.model.Comic;
import kenhoang.dev.com.retrofit.IComicAPI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class FilterCategoryActivity extends AppCompatActivity {
    Button btnFilter;
    Button btnSearch;
    IComicAPI iComicAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recycler_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_category);
        // Init API
        iComicAPI = Common.getAPI();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fetchCategory(); // Load all category from server
            }
        });
        // Init View
        recycler_filter = findViewById(R.id.recycler_filter);
        recycler_filter.setHasFixedSize(true);
        recycler_filter.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));

        btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create Dialog
                showOptionsDialog();
            }
        });
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create Dialog
                showSearchDialog();
            }
        });
    }

    private void showSearchDialog() {
        AlertDialog.Builder searchDialog = new AlertDialog.Builder(new ContextThemeWrapper(FilterCategoryActivity.this, R.style.myDialog));
        searchDialog.setTitle("Search Comic");

        LayoutInflater inflater = this.getLayoutInflater();
        View searchLayout = inflater.inflate(R.layout.dialog_search, null);
        final EditText edSearch = searchLayout.findViewById(R.id.edSearch);

        searchDialog.setView(searchLayout);
        searchDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        searchDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fetchSearchComic(edSearch.getText().toString());
                dialogInterface.dismiss();
            }
        });
        searchDialog.show();
    }

    private void fetchSearchComic(String search) {
        compositeDisposable.add(iComicAPI.getSearchComic(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {
                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new ComicAdapter(getBaseContext(), comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(FilterCategoryActivity.this, "No comic found", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void showOptionsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(FilterCategoryActivity.this, R.style.myDialog));
        alertDialog.setTitle("Select Category");

        LayoutInflater inflater = this.getLayoutInflater();
        View filterLayout = inflater.inflate(R.layout.dialog_option, null);

        RecyclerView recyclerOptions = filterLayout.findViewById(R.id.recycler_options);
        recyclerOptions.setHasFixedSize(true);
        recyclerOptions.setLayoutManager(new LinearLayoutManager(this));
        final MultipleChooseAdapter multipleChooseAdapter = new MultipleChooseAdapter(getBaseContext(), Common.categoryList);
        recyclerOptions.setAdapter(multipleChooseAdapter);

        alertDialog.setView(filterLayout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fetchFilterCategory(multipleChooseAdapter.getFilterArray());
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void fetchFilterCategory(String data) {
        compositeDisposable.add(iComicAPI.getFilterComic(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {
                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new ComicAdapter(getBaseContext(), comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(FilterCategoryActivity.this, "No comic found", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void fetchCategory() {
        compositeDisposable.add(iComicAPI.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        Common.categoryList = categories;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(FilterCategoryActivity.this, "Error while loading category", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
