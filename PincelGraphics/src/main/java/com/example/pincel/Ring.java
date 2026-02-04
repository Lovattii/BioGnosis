package com.example.pincel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.LinkedList;
import java.util.Queue;
import android.animation.Animator;
import androidx.core.content.ContextCompat;
import android.animation.AnimatorListenerAdapter;

public class Ring extends View{
    private RectF rectF;
    private float progress = 0f;
    private static int max = 100;
    private float deg_total = 360f;
    private PaintPallet paintPallet = new PaintPallet(Paint.Style.STROKE, Paint.Style.STROKE);
    private float deg_initial = calcula_deg_initial();
    private float progressWidth = 0f;
    private float backgroundWidth = 0f;

    private AkiraAnimation akira;

    private float calcula_deg_initial()
    {
        return 360f - deg_total + 90f - (360f - deg_total)/2f;
    }

    public Ring(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs)
    {
        akira = new AkiraAnimation(2000, new AkiraAnimation.CallbackInvalidate() {
            @Override
            public void ativaInvalidate(float progresNow) {
                progress = progresNow;
                invalidate();
            }
        });

        float garb = 0f;

        if (attrs != null)
        {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ring, 0, 0);

            try {
                this.deg_total = typedArray.getFloat(R.styleable.ring_bfm_deg_max, this.deg_total);
                backgroundWidth = typedArray.getDimension(R.styleable.ring_bfm_backgroundWidth, backgroundWidth);
                progressWidth = typedArray.getDimension(R.styleable.ring_bfm_progressWidth, progressWidth);
                garb = typedArray.getFloat(R.styleable.ring_bfm_progress, garb);
            } finally {
                typedArray.recycle();
            }
        }

        this.deg_initial = calcula_deg_initial();
        paintPallet.setStrokeWidthBackground(backgroundWidth);
        paintPallet.setStrokeWidthProgress(progressWidth);
        this.rectF = new RectF();

        akira.setProgressAnimation(0);
        akira.setProgressAnimation(100);
        akira.setProgressAnimation(garb);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paintPallet.updateColor(progress/max);

        float padding = backgroundWidth / 2;
        rectF.set(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawArc(rectF, deg_initial, deg_total, false, paintPallet.getBackgroudPaint());

        rectF.set(padding, padding, getWidth() - padding, getHeight() - padding);
        float Deg = (deg_total * progress)/max;
        canvas.drawArc(rectF, deg_initial, Deg, false, paintPallet.getProgressPaint());
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (akira != null)
            akira.destroy();
    }

    public void setProgressAnimation(float newProgress)
    {
        akira.setProgressAnimation(newProgress);
    }


    public void setBackIsNotAlpha(boolean Alpha)
    {
        paintPallet.setBackIsNotAlpha(Alpha);
    }

    public void setColorsProgress(int...colorsProgress) {
        paintPallet.setColorsInProgress(colorsProgress);
    }

    public void setColorsBackground(int...colorsBackground) {
        paintPallet.setColorsInBackground(colorsBackground);
    }

    public int getColorBackground()
    {
        return paintPallet.getBackgroudPaint().getColor();
    }

    public int getColorProgress()
    {
        return paintPallet.getProgressPaint().getColor();
    }

    public float getProgress() {
        return progress;
    }
}
