package kenhoang.dev.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import kenhoang.dev.com.R;
import kenhoang.dev.com.model.Link;

public class CustomViewPagerAdapter extends PagerAdapter {

    Context context;
    List<Link> links;
    LayoutInflater inflater;

    public CustomViewPagerAdapter(Context context, List<Link> links) {
        this.context = context;
        this.links = links;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.item_viewpager, container, false);

        PhotoView pageImage = imageLayout.findViewById(R.id.page_image);
        Picasso.get().load(links.get(position).getLink()).into(pageImage);

        container.addView(imageLayout);
        return imageLayout;
    }
}
