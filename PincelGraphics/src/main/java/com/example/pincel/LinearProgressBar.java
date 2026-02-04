package com.example.pincel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.animation.ArgbEvaluator;
import java.util.LinkedList;
import java.util.Queue;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.Toast;


import androidx.annotation.NonNull;

public class LinearProgressBar extends View{
    private float dist;
    private float cornerRadius;
    private RectF rectF;
    private RectF progressRect;
    private static int max = 100;
    private PaintPallet paintPallet;
    private float progressNow;
    private AkiraAnimation akira;

    public LinearProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs)
    {
        this.akira = new AkiraAnimation(1500, new AkiraAnimation.CallbackInvalidate() {
            @Override
            public void ativaInvalidate(float progresNow) {
                progressNow = progresNow;
                invalidate();
            }
        });

        this.paintPallet = new PaintPallet(Paint.Style.FILL, Paint.Style.FILL);
        this.dist = 20f;
        this.progressNow = 0f;
        float progressGarbage = 0f;

        if (attrs != null)
        {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LinearProgressBar, 0, 0);

            try {
                progressGarbage = typedArray.getFloat(R.styleable.LinearProgressBar_pgl_progress, progressGarbage);
                dist = typedArray.getDimension(R.styleable.LinearProgressBar_pgl_distance, dist);
                cornerRadius = typedArray.getDimension(R.styleable.LinearProgressBar_pgl_cornerRadius, cornerRadius);
            }
            finally {
                typedArray.recycle();
            }
        }

        this.rectF = new RectF();
        this.progressRect = new RectF();

        akira.setProgressAnimation(0);
        akira.setProgressAnimation(100);
        akira.setProgressAnimation(progressGarbage);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paintPallet.updateColor(progressNow/max);

        int width = getWidth();
        int height = getHeight();
        float progressWidthMax = width - 2*dist;

        rectF.set(0, 0, width, height);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paintPallet.getBackgroudPaint());

        float progressWidthNow = progressNow * progressWidthMax/max + dist;
        progressRect.set(dist, dist, progressWidthNow, height - dist);

        if (progressWidthNow - dist < 2*cornerRadius)
        {
            progressRect.set(dist, dist, 2*cornerRadius + dist, height - dist);
            canvas.drawRoundRect(progressRect, cornerRadius, cornerRadius, paintPallet.getProgressPaint());
        }
        else
            canvas.drawRoundRect(progressRect, cornerRadius, cornerRadius, paintPallet.getProgressPaint());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (akira != null)
            akira.destroy();
    }

    public void setProgressAnimation(float progress)
    {
        akira.setProgressAnimation(progress);
    }

    private int calculaDegrade(int color1, int color2, float pctInv)
    {
        float pct = 1f - pctInv;
        float a = (Color.alpha(color1) * pct + Color.alpha(color2) * pctInv);
        float r = (Color.red(color1) * pct + Color.red(color2) * pctInv);
        float g = (Color.green(color1) * pct + Color.green(color2) * pctInv);
        float b = (Color.blue(color1) * pct + Color.blue(color2) * pctInv);

        return (Color.argb(a, r, g, b));
    }

    public void setColorsProgress(int...colorsProgress) {
        paintPallet.setColorsInProgress(colorsProgress);
    }

    public void setColorsBackground(int...colorsBackground) {
        paintPallet.setColorsInBackground(colorsBackground);
    }

    public void setBackIsNotAlpha(boolean Alpha)
    {
        paintPallet.setBackIsNotAlpha(Alpha);
    }
    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    public void setDist(float dist) {
        this.dist = dist;
        invalidate();
    }

    public float getProgressNow() {
        return progressNow;
    }
}
