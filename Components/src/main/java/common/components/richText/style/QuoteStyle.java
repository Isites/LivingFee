package common.components.richText.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Editable;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.QuoteSpan;

import java.util.ArrayList;
import java.util.List;

import common.components.richText.TextEditor;
import common.components.richText.TextStyle;

public class QuoteStyle extends QuoteSpan implements IContext{
    private TextEditor target;


    private static final int DEFAULT_STRIPE_WIDTH = 8;
    private static final int DEFAULT_GAP_WIDTH = 2;
    private static final int DEFAULT_COLOR = 0xff0000ff;

    private int quoteColor;
    private int quoteStripeWidth;
    private int quoteGapWidth;


    public QuoteStyle(TextEditor target){
        this.target = target;

        this.quoteColor = DEFAULT_COLOR;
        this.quoteGapWidth = DEFAULT_GAP_WIDTH;
        this.quoteStripeWidth = DEFAULT_STRIPE_WIDTH;
    }
    public QuoteStyle(TextEditor target, Integer quoteColor, Integer quoteGapWidth, Integer quoteStripeWidth){
        this.target = target;
        this.quoteColor = quoteColor == 0 ?  DEFAULT_COLOR : quoteColor;
        this.quoteStripeWidth =  quoteStripeWidth == 0 ? DEFAULT_STRIPE_WIDTH : quoteStripeWidth;
        this.quoteGapWidth =  quoteGapWidth == 0 ?  DEFAULT_GAP_WIDTH : quoteGapWidth;
    }
    public QuoteStyle(TextEditor target, Parcel src){
        super(src);
        this.quoteColor = src.readInt();
        this.quoteStripeWidth = src.readInt();
        this.quoteGapWidth = src.readInt();
    }
    // TODO: 2016/5/4 里面很多部分可以作为函数提取出来
    @Override
    public boolean contain(int start, int end, TextStyle style) {
        String[] lines = TextUtils.split(target.getEditableText().toString(), "\n");
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < lines.length; i++){
            int lineStart = 0;
            for(int j = 0; j < i; j++){
                lineStart +=  lines[j].length() + 1;
            }
            int lineEnd  = lineStart + lines[i].length();
            if(lineEnd <= lineStart){
                continue;
            }
            if(lineStart <= start && lineEnd >= end){
                list.add(i);
            }
            else if(start <= lineStart && lineEnd <= end){
                list.add(i);
            }
        }
        for(Integer row : list){
            if(!containQuote(row)){
                return false;
            }
        }
        return true;
    }
    private boolean containQuote(int row){
        String[] lines = TextUtils.split(target.getEditableText().toString(), "\n");
        if( row < 0 || row >= lines.length){
            return false;
        }
        int start = 0;
        for(int i = 0; i < row; i++){
            start += lines[i].length() + 1;
        }
        int end = start + lines[row].length();
        if(start >= end){
            return false;
        }
        QuoteSpan[] spans = target.getSpanList(QuoteSpan.class, start, end);
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
        QuoteSpan[] quoteSpans = text.getSpans(start, end, QuoteSpan.class);
        for (QuoteSpan span : quoteSpans) {
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
            if (containQuote(i)) {
                continue;
            }
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart +=  lines[j].length() + 1;
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
        for(int i = 0; i < lines.length; i++){
            if(!containQuote(i)){
                continue;
            }
            int lineStart = 0;
            for(int j = 0; j < i; j++){
                lineStart +=  lines[j].length() + 1;
            }
            int lineEnd  = lineStart + lines[i].length();
            if(lineEnd <= lineStart){
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
            if(quoteStart < quoteEnd){
                QuoteSpan[] spans = target.getSpanList(QuoteSpan.class, quoteStart, quoteEnd);
                for(QuoteSpan span : spans){
                    target.getEditableText().removeSpan(span);
                }
            }
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(quoteColor);
        dest.writeInt(quoteStripeWidth);
        dest.writeInt(quoteGapWidth);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return quoteStripeWidth + quoteGapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout layout) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(quoteColor);
        c.drawRect(x, top, x + dir * quoteGapWidth, bottom, p);

        p.setStyle(style);
        p.setColor(color);
    }

    public int getQuoteColor() {
        return quoteColor;
    }

    public void setQuoteColor(int quoteColor) {
        this.quoteColor = quoteColor;
    }

    public int getQuoteStripeWidth() {
        return quoteStripeWidth;
    }

    public void setQuoteStripeWidth(int quoteStripeWidth) {
        this.quoteStripeWidth = quoteStripeWidth;
    }

    public int getQuoteGapWidth() {
        return quoteGapWidth;
    }

    public void setQuoteGapWidth(int quoteGapWidth) {
        this.quoteGapWidth = quoteGapWidth;
    }

}
