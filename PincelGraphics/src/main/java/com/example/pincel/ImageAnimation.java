package com.example.pincel;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public class ImageAnimation {
    private PaintPallet paint;
    private AkiraAnimation akira;
    private final float max = 100f;
    ImageView imageView;
    Drawable drawable;

    public ImageAnimation(ImageView imageView, int id_drawable, Context context, int[]colors)
    {
        this.paint = new PaintPallet(null, null);
        this.imageView = imageView;
        drawable = ContextCompat.getDrawable(context, id_drawable);

        paint.setColorsInBackground(colors);
        paint.setBackIsNotAlpha(true);

        this.akira = new AkiraAnimation(2000, new AkiraAnimation.CallbackInvalidate() {
            @Override
            public void ativaInvalidate(float progresNow) {
                paint.updateColor(progresNow / max);
                Drawables.trickDrawable(imageView, id_drawable, paint.getBackgroudPaint().getColor(), context);
            }
        });

        akira.setProgressAnimation(0);
        akira.setProgressAnimation(100);

    }

    public void setProgressAnimation(float newProgress)
    {
        akira.setProgressAnimation(newProgress);
    }


}
