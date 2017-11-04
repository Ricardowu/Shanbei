package me.ricardo.shanbei.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import me.ricardo.shanbei.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricardo on 10/31/17.
 */

public class DictionaryWindow extends PopupWindow {
    private Context mContext;
    private TextView wordTv;
    private TextView soundTv;
    private TextView definitionTv;
    private String pronunciationUrl;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public DictionaryWindow(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.window_dictionary, null);
        ImageView pronIv = view.findViewById(R.id.btn_pronounce);
        pronIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pronunciationUrl != null) {
                    MediaPlayer mp = new MediaPlayer();
                    try {
                        mp.setDataSource(pronunciationUrl);
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ImageView closeIv = view.findViewById(R.id.btn_close);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button addWord = view.findViewById(R.id.dict_add_word);
        addWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "什么也没有", Toast.LENGTH_LONG).show();
            }
        });
        wordTv = view.findViewById(R.id.dict_word);
        soundTv = view.findViewById(R.id.dict_sound);
        definitionTv = view.findViewById(R.id.dict_definition);

        setOutsideTouchable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getY() < getContentView().getHeight() - v.getHeight()) {
                    dismiss();
                }
                return true;
            }
        });
        setContentView(view);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.pop_up_dictionary_anim);
    }

    public void searchWord(final String word) {
        if (!word.isEmpty()) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Response response = client.newCall(
                                new Request.Builder().url("https://api.shanbay.com/bdc/search/?word=" + word).build()).execute();
                        final JSONObject json;
                        if (response.code() == 200) {
                            json = JSON.parseObject(response.body().string()).getJSONObject("data");
                        } else {
                            json = new JSONObject();
                            json.put("content", "Error");
                            json.put("pronunciation", "Error");
                            json.put("definition", "Error");
                            json.put("audio", null);
                            Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
                        }
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayInfo(json);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }.start();
        }
    }

    private void displayInfo(JSONObject data) {
        wordTv.setText(data.getString("content"));
        soundTv.setText(data.getString("pronunciation"));
        definitionTv.setText(data.getString("definition"));
        pronunciationUrl = data.getString("audio");
    }
}
