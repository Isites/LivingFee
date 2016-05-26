package common.components.richText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.EditText;

import common.components.R;
import common.components.richText.html.TextParser;
import common.components.richText.style.IContext;
import common.components.richText.style.ImageStyle;

public class TextEditor extends EditText{

    //当前style
    private TextStyle currStyle;
    private IContext currContext;

    public TextEditor(Context context) {
        super(context);
        init(null, context);
    }
    public TextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }
    public TextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }
    @SuppressLint("NewApi")
    public TextEditor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, context);
    }

    private void init(AttributeSet atts, Context context){
        StyleConfig.init();
        TypedArray array = getContext().obtainStyledAttributes(atts, R.styleable.TextEditor);

        int quoteColor = array.getColor(R.styleable.TextEditor_quoteColor, 0);
        int quoteGapWidth = array.getDimensionPixelSize(R.styleable.TextEditor_quoteCapWidth, 0);
        int quoteStripeWidth = array.getDimensionPixelSize(R.styleable.TextEditor_quoteStripeWidth, 0);
        StyleConfig.setAttrs(TextStyle.QUOTE, quoteColor, quoteGapWidth, quoteStripeWidth);

        int bulletColor = array.getDimensionPixelSize(R.styleable.TextEditor_bulletColor, 0);
        int bulletRadius = array.getDimensionPixelSize(R.styleable.TextEditor_bulletRadius, 0);
        int bulletGapWidth = array.getDimensionPixelSize(R.styleable.TextEditor_bulletGapWidth, 0);
        StyleConfig.setAttrs(TextStyle.LI, bulletColor, bulletRadius, bulletGapWidth);

        array.recycle();
    }
    public <T> T setStyle(TextStyle style){
        if(!TextStyle.isSurpport(style.getStyle())){
            return null;
        }
        this.currStyle = style;
        this.currContext = TextUtil.getInstance(this, style);
        return (T) this.currContext;
    }
    public void setStyleInstance(IContext context){
        Class clazz = context.getClass();
        currStyle = TextStyle.getStyleByClass(clazz);
        if(currStyle != null){
            this.currContext = context;
        }
    }
    public void render(){
        if(currContext != null){
            currContext.render(currStyle);
        }
    }
    public boolean contans(TextStyle style){
//        if(!TextStyle.isSurpport(style.getStyle())) return false;
        if(currContext == null){
            currContext = TextUtil.getInstance(this, style);
        }
        return currContext.contain(getSelectionStart(), getSelectionEnd(), style);
    }
    public void showHtml(String source){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(TextParser.fromHtml(source,getWidth(), getContext()));
        toSelfStyle(builder, 0, builder.length());
        setText(builder);
    }
    protected void toSelfStyle(Editable text, int start, int end){
        IContext liStyle = TextUtil.getInstance(this, TextStyle.LI);
        liStyle.toStyle(text, start, end);
        IContext quoteStyle = TextUtil.getInstance(this, TextStyle.QUOTE);
        quoteStyle.toStyle(text, start, end);
        IContext imgStyle = new ImageStyle(this, null, null);
        imgStyle.toStyle(text, start, end);
    }
    public String toHtml(){
        return TextParser.toHtml(getEditableText());
    }
    public <T> T[] getSpanList(Class<T> clazz, int start, int end){
        T[] styles = getEditableText().getSpans(start, end, clazz);
        return styles;
    }
}
