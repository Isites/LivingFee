package common.components.richText.style;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.ImageSpan;

import common.components.richText.TextEditor;
import common.components.richText.TextStyle;
import common.components.richText.html.UriImageGetter;

public class ImageStyle extends ImageSpan implements IContext{
    private TextEditor target;
    //图片路径
    private String path;
    public ImageStyle(TextEditor target, Drawable d, String path) {
        super(d);
        this.target = target;
        this.path = path;
    }

    @Override
    public boolean contain(int start, int end, TextStyle style) {
        if(start >= end){
            return false;
        }
        else {
            //只需要判断是否有图片即可
            ImageSpan[] spans = target.getSpanList(ImageSpan.class, start, end);
            return spans.length > 0;
        }
    }

    @Override
    public void render(TextStyle style) {
        int start = target.getSelectionStart();
        int end = target.getSelectionEnd();
        if(start > end) return;
        if(start == end){
            //插入图片
            Editable text = target.getEditableText();
            text.insert(start, getPath());
            text.setSpan(this, start, start + getPath().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else {
            //替换字符串
            Editable text = target.getEditableText();
            text.replace(start, end, getPath());
            text.setSpan(this, start, start + getPath().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public void toStyle(Editable text, int start, int end) {
        ImageSpan[] spans = text.getSpans(start, end, ImageSpan.class);
        for (ImageSpan span : spans) {
            int spanStart = text.getSpanStart(span);
            int spanEnd = text.getSpanEnd(span);
            //spanEnd = 0 < spanEnd && spanEnd < text.length() && text.charAt(spanEnd) == '\n' ? spanEnd - 1 : spanEnd;
            ImageStyle imgStyle = new ImageStyle(
                    target,
                    new UriImageGetter(target.getWidth(), target.getContext()).getDrawable(span.getSource()),
                    span.getSource()
            );
            text.removeSpan(span);
            text.replace(spanStart, spanEnd, imgStyle.getPath());
            text.setSpan(imgStyle, spanStart, spanStart + imgStyle.getPath().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
