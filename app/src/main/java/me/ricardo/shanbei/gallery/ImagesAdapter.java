package me.ricardo.shanbei.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

import me.ricardo.shanbei.GlideApp;
import me.ricardo.shanbei.R;

/**
 * Created by ricardo on 11/4/17.
 */

public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> mData;

    public ImagesAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<String> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String path = mData.get(position);
        if (path == null || path.length() == 0) {
            return;
        }
        System.out.println("-----path: " + path);
        System.out.println("-----Count: " + getItemCount());
        if (path.startsWith("http")) {
            System.out.println("-----Loading remote");
            GlideApp.with(mContext).load(path).placeholder(R.mipmap.default_holder).
                    error(GlideApp.with(mContext).load(path).error(R.mipmap.default_holder)).
                    diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                    into(((ImageViewHolder) holder).imageView);
        } else {
            System.out.println("-----Loading local");
            GlideApp.with(mContext).load("file://" +
                    mContext.getExternalFilesDir("Images").getAbsolutePath() + File.separator + path).
                    error(R.mipmap.default_holder).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    into(((ImageViewHolder) holder).imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String getItem(int index) {
        return mData.get(index);
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_item);
        }
    }
}
