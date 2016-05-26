package common.components.richText.style;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.StyleSpan;

import java.util.ArrayList;
import java.util.List;

import common.components.richText.TextEditor;
import common.components.richText.TextPart;
import common.components.richText.TextStyle;

public class SimpleStyle extends StyleSpan implements IContext{
    private TextEditor target;
    public SimpleStyle(TextEditor target, Integer style) {
        super(style);
        this.target = target;
    }

    @Override
    public boolean contain(int start, int end, TextStyle style) {
        if(start > end){
            return false;
        }
        else if(start == end){
            if(start -1 < 0 || start + 1 > target.getEditableText().length()){
                return false;
            }
            else{
                //左右两边是否包含
                StyleSpan[] before = target.getSpanList(StyleSpan.class, start - 1, start);
                StyleSpan[] after = target.getSpanList(StyleSpan.class, start, start + 1);
                return before.length > 0 && after.length > 0
                        && before[0].getStyle() == getStyle()
                        && after[0].getStyle() == getStyle();
            }
        }
        else{
            StringBuilder builder = new StringBuilder();
            // 判断是否全部包含
            for (int i = start; i < end; i++) {
                StyleSpan[] spans = target.getSpanList(StyleSpan.class, i, i + 1);
                for (StyleSpan span : spans) {
                    if (span.getStyle() == getStyle()) {
                        builder.append(target.getEditableText().subSequence(i, i + 1).toString());
                        break;
                    }
                }
            }
            return target.getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    @Override
    public void render(TextStyle style) {
        int start = target.getSelectionStart();
        int end = target.getSelectionEnd();
        if(start >= end) return;
        if(!contain(start,end, style)){
            renderValid(start, end);
        }
        else {
            renderInValid(start, end);
        }
    }

    @Override
    public void toStyle(Editable text, int start, int end) {

    }

    protected void renderValid(int start, int end){
        target.getEditableText().setSpan(this, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    protected void renderInValid(int start, int end){
        StyleSpan[] spans = target.getSpanList(StyleSpan.class, start, end);
        List<TextPart> parts = new ArrayList<TextPart>();
        for(StyleSpan span : spans){
            if(span.getStyle() == getStyle()){
                parts.add(new TextPart(
                        target.getEditableText().getSpanStart(span),
                        target.getEditableText().getSpanEnd(span)
                ));
                target.getEditableText().removeSpan(span);
            }
        }
        for(TextPart part : parts){
            if(part.isValid()){
                if(part.getStart() < start){
                    renderValid(part.getStart(), start);
                }
                if(part.getEnd() > end){
                    renderValid(end, part.getEnd());
                }
            }
        }
    }
}
