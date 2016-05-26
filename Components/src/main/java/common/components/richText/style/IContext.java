package common.components.richText.style;

import android.text.Editable;

import common.components.richText.TextStyle;

public interface IContext {
    public boolean contain(int start, int end, TextStyle style);
    public void render(TextStyle style);
    public void toStyle(Editable text, int start, int end);
}
