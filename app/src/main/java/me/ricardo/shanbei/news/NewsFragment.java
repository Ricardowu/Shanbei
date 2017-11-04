package me.ricardo.shanbei.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.ricardo.shanbei.R;

public class NewsFragment extends Fragment {
    private NewsTextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        mTextView = (NewsTextView) view.findViewById(R.id.news_text_view);
        mTextView.loadTxt();
        return view;
    }
}
