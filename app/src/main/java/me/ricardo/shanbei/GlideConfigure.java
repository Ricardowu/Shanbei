package me.ricardo.shanbei;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;

import okhttp3.internal.cache.DiskLruCache;

/**
 * Created by ricardo on 11/4/17.
 */

@GlideModule
public class GlideConfigure extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            builder.setDiskCache(new DiskCache.Factory() {
                @Nullable
                @Override
                public DiskCache build() {
                    File cacheDir = MainActivity.instance.getExternalFilesDir("Images/");
                    return DiskLruCacheWrapper.get(cacheDir, 200 * 1024 * 1024);
                }
            });
        }
    }
}
