package common.components.richText.html;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;


public class UriImageGetter implements Html.ImageGetter{

    private int maxWidth;
    private Context context;

    public UriImageGetter(int width, Context context){
        this.maxWidth = width;
        this.context = context;
    }



    @Override
    public Drawable getDrawable(String source) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(source);
        Drawable drawable = null;
        try {
            Bitmap bit = MediaStore.Images.Media.getBitmap(cr, uri);
            drawable = Drawable.createFromStream(cr.openInputStream(uri), null);
            maxWidth = maxWidth == 0 ? drawable.getIntrinsicWidth() : maxWidth;
            drawable.setBounds(0, 0, maxWidth, maxWidth/drawable.getIntrinsicWidth()*drawable.getIntrinsicHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }


    private String getPath(Uri uri){
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri,null, null, null, null);
        cursor.moveToFirst();
//        while (cursor.)
        return cursor.getCount() + "";
    }
}
