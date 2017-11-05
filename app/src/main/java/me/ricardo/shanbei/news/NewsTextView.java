package me.ricardo.shanbei.news;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.PopupWindow;

import me.ricardo.shanbei.MainActivity;
import me.ricardo.shanbei.Utils;
import me.ricardo.shanbei.widget.DictionaryWindow;


/**
 * Created by ricardo on 11/3/17.
 */

public class NewsTextView extends android.support.v7.widget.AppCompatTextView {
    private int mCharWidth;
    private SpannableStringBuilder mSpanText;
    private DictionaryWindow mWindow;
    private int currentOffset = -1;
    private ForegroundColorSpan currentSpan = new ForegroundColorSpan(Color.RED);

    public NewsTextView(Context context) {
        super(context);
    }

    public NewsTextView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            selectWord(x, y);
        }
        return true;
    }

    /**
     * 从文本文件中加载内容
     */
    public void loadTxt() {
        String text = Utils.getAssetsTxt(MainActivity.instance, "news.txt");
        mSpanText = new SpannableStringBuilder(text);
        this.setText(mSpanText, BufferType.SPANNABLE);

        // 初始化文字时同时计算一行中每个单独文字的宽度
        Rect rect = new Rect();
        getPaint().getTextBounds("a", 0, 1, rect);
        mCharWidth = rect.width();
    }

    /**
     * 响应点击事件，从TextView中计算选择单词
     * @param x x坐标
     * @param y y坐标
     */
    private void selectWord(float x, float y) {
        Layout layout = getLayout();
        int lineNumber = layout.getLineForVertical((int) y);
        // 只有一个 '\n'
        if (layout.getLineStart(lineNumber) == layout.getLineEnd(lineNumber) - 1) {
            if (mWindow != null  && mWindow.isShowing()) {
                mWindow.dismiss();
            }
            return;
        }
        int offset = layout.getOffsetForHorizontal(lineNumber, x);
        CharSequence text = getText();
        // 左移至最近一个空格之后
        while (offset > 0 && isValidCharacter(text.charAt(offset))) {
            offset--;
        }
        // 防止点击的是一个空格或无效字符，默认右移至下一单词
        while (offset < text.length() && !isValidCharacter(text.charAt(offset))) {
            offset++;
        }
        int begin = offset;
        StringBuilder sb = new StringBuilder();
        // 右移至单词末尾
        while (offset < text.length() && isValidCharacter(text.charAt(offset))) {
            sb.append(text.charAt(offset));
            offset++;
        }
        sb.append(text.charAt(offset));
        String word = sb.toString().trim();
        if (word.length() > 0) {
            highlightWord(word, begin);
            popUpDictionaryView(word);
        } else {
            mWindow.dismiss();
        }
    }

    private void popUpDictionaryView(String word) {
        mWindow = new DictionaryWindow(getContext());
        mWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
        mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                unHighlightWord();
            }
        });
        mWindow.searchWord(word);
    }

    /**
     * 高亮显示选中单词
     * @param word 需要显示的单词
     * @param offset 在Spanable的Text中从首字符算起的位移
     */
    private void highlightWord(String word, int offset) {
        if (offset != currentOffset) {
            mSpanText.setSpan(currentSpan, offset,
                    offset + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            currentOffset = offset;
            setText(mSpanText);
        }
    }

    /**
     * 取消高亮显示的单词
     */
    private void unHighlightWord() {
        mSpanText.removeSpan(currentSpan);
        setText(mSpanText);
    }

    /**
     * 是否是有效字符，这里设置允许单词包括：字母，数字和'-'连接符
     * @param c 待判断的字符
     * @return
     */
    private static boolean isValidCharacter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '-');
    }
}
