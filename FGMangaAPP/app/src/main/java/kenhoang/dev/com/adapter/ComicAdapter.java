package kenhoang.dev.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kenhoang.dev.com.ChapterActivity;
import kenhoang.dev.com.R;
import kenhoang.dev.com.common.Common;
import kenhoang.dev.com.helpers.IRecyclerOnClick;
import kenhoang.dev.com.model.Comic;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {

    Context context;
    List<Comic> comics;

    public ComicAdapter(Context context, List<Comic> comics) {
        this.context = context;
        this.comics = comics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(comics.get(position).getImage()).into(holder.imgComic);
        holder.tvNameComic.setText(comics.get(position).getName());

        // Remember implement it, if you don't do that, your app when crash when you click to comic
        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(View view, int position) {
                Common.selected_comic = comics.get(position);
                // Start new activity
                context.startActivity(new Intent(context, ChapterActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgComic;
        TextView tvNameComic;
        IRecyclerOnClick iRecyclerOnClick;

        public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgComic = itemView.findViewById(R.id.imageComic);
            tvNameComic = itemView.findViewById(R.id.tvComicName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerOnClick.onClick(view, getAdapterPosition());
        }
    }
}
