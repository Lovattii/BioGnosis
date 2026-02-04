package com.example.pincel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public class Drawables {
    public static void trickDrawable(ImageView imagem, int id_drawable, int color, Context context)
    {
        Drawable drawable = ContextCompat.getDrawable((context), id_drawable);
        if (drawable != null)
            drawable.setTint(color);
        imagem.setImageDrawable(drawable);
    }

    public static boolean canClick(long last_click)
    {
        long agora = System.currentTimeMillis();

        if (agora - last_click > 1000)
        {
            last_click = agora;
            return true;
        }

        return false;
    }
}
