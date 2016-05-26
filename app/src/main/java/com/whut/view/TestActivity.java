package com.whut.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


import com.whut.components.activity.CommonActivity;

import cn.edu.whut.lib.blog.dao.BlogDao;
import cn.edu.whut.lib.common.Dao;
import common.components.richText.TextEditor;
import common.components.richText.TextStyle;
import common.components.richText.html.UriImageGetter;
import common.components.richText.style.ImageStyle;

public class TestActivity extends CommonActivity {
    private  TextEditor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        editor = (TextEditor) findViewById(R.id.editor);
        ImageButton bold = (ImageButton) findViewById(R.id.bold);
        ImageButton quote = (ImageButton) findViewById(R.id.quote);
        ImageButton bullet = (ImageButton) findViewById(R.id.bullet);
        ImageButton image = (ImageButton) findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                if(Build.VERSION.SDK_INT < 19){
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                }
                else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                startActivityForResult(intent, 3);
            }
        });
        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setStyle(TextStyle.LI);
                editor.render();
            }
        });
        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setStyle(TextStyle.QUOTE);
                editor.render();
            }
        });
        bold.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editor.setStyle(TextStyle.BOLD);
                editor.render();
//                editor.showHtml("<ul><li><blockquote>5&#21517;&#21734;LOL&#21734;&#23490;&#23518;</blockquote></li></ul>&#21351;&#40857;&#22696;&#36857;<br><img src=\"content://com.android.providers.media.documents/document/image%3A298922\"><br>&#24656;&#40857;&#21679;&#21704;&#21644;<br><img src=\"content://com.android.providers.media.documents/document/image%3A298922\">");
//                Log.v("imageSpan", editor.toHtml());
//                Log.v("imageSpan", editor.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            UriImageGetter getter = new UriImageGetter(editor.getWidth(), this);
            ImageStyle imageStyle = new ImageStyle(editor, getter.getDrawable(uri.toString()), uri.toString());
            editor.setStyleInstance(imageStyle);
            editor.render();
            Log.v("imageSpan", editor.toHtml());
            BlogDao blogDao = new BlogDao();
            long result = blogDao.insert(editor.toHtml(), Dao.INT_NULL);
            Log.v("imageSpan", "insert into db " + result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
