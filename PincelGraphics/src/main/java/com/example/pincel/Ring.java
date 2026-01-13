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
    private PaintPallet paintPallet = new PaintPallet();
    private float deg_initial = calcula_deg_initial();

    private float progressWidth = 20f;
    private float backgroundWidth = 20f;
    private boolean isAnimating = false;
    private Queue<Float> queue = new LinkedList<>();
    private ValueAnimator chainsaw;
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

        this.rectF = new RectF();

        setProgressAnimation(0);
        setProgressAnimation(100);
        setProgressAnimation(garb);
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

    public void setProgressAnimation(float progress)
    {
        queue.add(progress);
        queuecessa();
    }

    private void queuecessa()
    {
        if (isAnimating || queue.isEmpty()) return;

        Float newProcess = queue.poll();

        if (newProcess == null) return;

        onAnimation(newProcess);
    }

    private void onAnimation(float newProgress)
    {
        isAnimating = true;
        newProgress = Math.max(0, Math.min(newProgress, max));

        chainsaw = ValueAnimator.ofFloat(progress, newProgress);
        chainsaw.setDuration(1500);

        chainsaw.setInterpolator(new DecelerateInterpolator());

        chainsaw.addUpdateListener(a -> {
            progress = (float)a.getAnimatedValue();
            invalidate();
        });

        chainsaw.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator a)
            {
                isAnimating = false;
                queuecessa();
            }
        });

        this.progress = newProgress;
        chainsaw.start();
    }

    public void setBackIsNotAlpha(boolean Alpha)
    {
        paintPallet.setBackIsNotAlpha(Alpha);
    }

    public void setColorsProgress(int...colorsProgress) {
        paintPallet.setColorsInProgress(Paint.Style.STROKE, progressWidth, colorsProgress);
    }

    public void setColorsBackground(int...colorsBackground) {
        paintPallet.setColorsInBackground(Paint.Style.STROKE, backgroundWidth, colorsBackground);
    }
}
