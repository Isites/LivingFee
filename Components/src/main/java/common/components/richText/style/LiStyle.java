package common.components.richText.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.text.Editable;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;

import java.util.ArrayList;
import java.util.List;

import common.components.richText.TextEditor;
import common.components.richText.TextStyle;

public class LiStyle extends BulletSpan implements IContext {
    private TextEditor target;

    private static final int DEFAULT_COLOR = 0xff0000ff;
    private static final int DEFAULT_RADIUS = 3;
    private static final int DEFAULT_GAP_WIDTH = 2;
    private static Path bulletPath = null;

    private int bulletColor;
    private int bulletRadius;
    private int bulletGapWidth;
    public LiStyle(TextEditor target){
        this.target = target;

        this.bulletColor =  DEFAULT_COLOR;
        this.bulletRadius =  DEFAULT_RADIUS;
        this.bulletGapWidth =  DEFAULT_GAP_WIDTH;
    }
    public LiStyle(TextEditor target, Integer bulletColor, Integer bulletRadius, Integer bulletGapWidth){
        this.target = target;

        this.bulletColor = bulletColor == 0 ? DEFAULT_COLOR : bulletColor;
        this.bulletRadius = bulletRadius == 0 ? DEFAULT_RADIUS : bulletRadius;
        this.bulletGapWidth = bulletGapWidth == 0 ? DEFAULT_GAP_WIDTH : bulletGapWidth;
    }
    public LiStyle(Parcel src) {
        super(src);
        this.bulletColor = src.readInt();
        this.bulletRadius = src.readInt();
        this.bulletGapWidth = src.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(bulletColor);
        dest.writeInt(bulletRadius);
        dest.writeInt(bulletGapWidth);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return 2 * bulletRadius + bulletGapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();

            int oldColor = p.getColor();
            p.setColor(bulletColor);
            p.setStyle(Paint.Style.FILL);

            if (c.isHardwareAccelerated()) {
                if (bulletPath == null) {
                    bulletPath = new Path();
                    // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                    bulletPath.addCircle(0.0f, 0.0f, bulletRadius, Path.Direction.CW);
                }

                c.save();
                c.translate(x + dir * bulletRadius, (top + bottom) / 2.0f);
                c.drawPath(bulletPath, p);
                c.restore();
            } else {
                c.drawCircle(x + dir * bulletRadius, (top + bottom) / 2.0f, bulletRadius, p);
            }

            p.setColor(oldColor);
            p.setStyle(style);
        }
    }
    @Override
    public boolean contain(int start, int end, TextStyle style) {
        String[] lines = TextUtils.split(target.getEditableText().toString(), "\n");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart +=  lines[j].length() + 1;
            }
            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }
            if (lineStart <= start && end <= lineEnd) {
                list.add(i);
            } else if (start <= lineStart && lineEnd <= end) {
                list.add(i);
            }
        }
        for (Integer row : list) {
            if (!containLi(row)) {
                return false;
            }
        }
        return true;
    }
    private boolean containLi(int row){
        String[] lines = TextUtils.split(target.getEditableText().toString(), "\n");
        if (row < 0 || row >= lines.length) {
            return false;
        }

        int start = 0;
        for (int i = 0; i < row; i++) {
            start += lines[i].length() + 1;
        }

        int end = start + lines[row].length();
        if (start >= end) {
            return false;
        }

        BulletSpan[] spans = target.getSpanList(BulletSpan.class, start, end);
        return spans.length > 0;
    }

    @Override
    public void render(TextStyle style) {
        int start = target.getSelectionStart();
        int end = target.getSelectionEnd();
        if(!contain(start,end, style)){
            renderValid(start, end);
        }
        else {
            renderInValid(start, end);
        }
    }

    @Override
    public void toStyle(Editable text, int start, int end) {
        BulletSpan[] bulletSpans = text.getSpans(start, end, BulletSpan.class);
        for (BulletSpan span : bulletSpans) {
            int spanStart = text.getSpanStart(span);
            int spanEnd = text.getSpanEnd(span);
            spanEnd = 0 < spanEnd && spanEnd < text.length() && text.charAt(spanEnd) == '\n' ? spanEnd - 1 : spanEnd;
            text.removeSpan(span);
            text.setSpan(this, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void renderValid(int start, int end){
        String[] lines = TextUtils.split(target.getEditableText().toString(), "\n");

        for (int i = 0; i < lines.length; i++) {
            if (containLi(i)) {
                continue;
            }
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart += lines[j].length() + 1;
            }
            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }
            int quoteStart = 0;
            int quoteEnd = 0;
            if (lineStart <= start && end <= lineEnd) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            } else if (start <= lineStart && lineEnd <= end) {
                quoteStart = lineStart;
                quoteEnd = lineEnd;
            }

            if (quoteStart < quoteEnd) {
                target.getEditableText().setSpan(this, quoteStart, quoteEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
    private void renderInValid(int start, int end){
        String[] lines = TextUtils.split(target.getEditableText().toString(), "\n");
        for (int i = 0; i < lines.length; i++) {
            if (!containLi(i)) {
                continue;
            }
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart += lines[j].length() + 1;
            }
            int lineEnd = lineStart + lines[i].length();
            if (lineStart >= lineEnd) {
                continue;
            }
            int bulletStart = 0;
            int bulletEnd = 0;
            if (lineStart <= start && end <= lineEnd) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            } else if (start <= lineStart && lineEnd <= end) {
                bulletStart = lineStart;
                bulletEnd = lineEnd;
            }

            if (bulletStart < bulletEnd) {
                BulletSpan[] spans = target.getSpanList(BulletSpan.class, bulletStart, bulletEnd);
                for (BulletSpan span : spans) {
                    target.getEditableText().removeSpan(span);
                }
            }
        }
    }
    public int getBulletColor() {
        return bulletColor;
    }

    public void setBulletColor(int bulletColor) {
        this.bulletColor = bulletColor;
    }

    public int getBulletRadius() {
        return bulletRadius;
    }

    public void setBulletRadius(int bulletRadius) {
        this.bulletRadius = bulletRadius;
    }

    public int getBulletGapWidth() {
        return bulletGapWidth;
    }

    public void setBulletGapWidth(int bulletGapWidth) {
        this.bulletGapWidth = bulletGapWidth;
    }
}
