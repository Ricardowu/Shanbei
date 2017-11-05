package me.ricardo.shanbei.gallery;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ricardo.shanbei.R;
import me.ricardo.shanbei.Utils;

public class GalleryFragment extends Fragment {
    private ImagesAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, null);
        RecyclerView recyclerView = view.findViewById(R.id.image_list);
        mAdapter = new ImagesAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> list = loadLocalImageFiles();
        if (list == null || list.isEmpty()) {
            list = loadOnlineImages();
        }

        mAdapter.setData(list);
        return view;
    }

    /**
     * 读取本地Images文件夹的内容
     * @return 文件名列表
     */
    private List<String> loadLocalImageFiles() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "SD卡不可用！", Toast.LENGTH_LONG).show();
            return null;
        }
        File dir = getActivity().getExternalFilesDir("Images/");
        if (dir == null || !dir.exists()) {
            dir.mkdirs();
            return null;
        }
        List<String> list = new ArrayList<>(Arrays.asList(dir.list()));
        list.remove("journal");
        return list;
    }

    /**
     * 网络图片链接存放在txt文件中方便模拟，读取所有的链接
     * @return 网络图片的URL列表
     */
    private List<String> loadOnlineImages() {
        String text = Utils.getAssetsTxt(getActivity(), "links.txt");
        if (text == null || text.length() <= 0) {
            return null;
        }
        String[] links = text.split("\n");
        return Arrays.asList(links);
    }
}
