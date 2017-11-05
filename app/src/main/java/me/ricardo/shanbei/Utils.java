package me.ricardo.shanbei;

import android.content.Context;
import android.text.Layout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    /**
     * 本地Assets读取工具
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetsTxt(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            reader.close();
            inputStreamReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
