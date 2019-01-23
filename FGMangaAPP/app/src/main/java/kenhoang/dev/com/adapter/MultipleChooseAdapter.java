package kenhoang.dev.com.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kenhoang.dev.com.R;
import kenhoang.dev.com.model.Category;

public class MultipleChooseAdapter extends RecyclerView.Adapter<MultipleChooseAdapter.ViewHolder> {

    Context context;
    List<Category> categories;
    SparseBooleanArray itemSparseBooleanArray = new SparseBooleanArray();
    List<Category> selected_category = new ArrayList<>();

    public MultipleChooseAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_check, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chkOption.setText(categories.get(position).getName());
        holder.chkOption.setChecked(itemSparseBooleanArray.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public String getFilterArray() {
        List<Integer> id_selected = new ArrayList<>();
        for (Category category: selected_category)
            id_selected.add(category.getID());
        return new Gson().toJson(id_selected);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox chkOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chkOption = itemView.findViewById(R.id.check_options);
            chkOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = getAdapterPosition();
                    itemSparseBooleanArray.put(adapterPosition, b);
                    if (b)
                        selected_category.add(categories.get(adapterPosition));
                    else
                        selected_category.remove(categories.get(adapterPosition));
                }
            });
        }
    }
}
