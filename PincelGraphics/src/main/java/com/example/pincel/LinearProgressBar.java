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
    private ValueAnimator akira;
    private static int max = 100;
    private PaintPallet paintPallet;
    private float progressNow;
    private float progressAlvo;
    private Queue<Float> animationQueue = new LinkedList<>();
    private boolean isAnimating = false;

    public LinearProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs)
    {
        this.paintPallet = new PaintPallet();
        paintPallet.setStyleProgress(Paint.Style.FILL);
        paintPallet.setStyleBackground(Paint.Style.FILL);
        this.dist = 20f;
        this.progressAlvo = 0f;
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

        setProgressAnimation(0);
        setProgressAnimation(100);
        setProgressAnimation(progressGarbage);
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

    public void setProgressAnimation(float newProgress)
    {
        animationQueue.add(newProgress);
        processQueque();
    }

    private void processQueque()
    {
        if (isAnimating || animationQueue.isEmpty())
            return;

        Float nextProgress = animationQueue.poll();
        if (nextProgress == null) return;

        onAnimation(nextProgress);
    }

    private void onAnimation(float newProgress)
    {
        isAnimating = true;
        newProgress = Math.max(0, Math.min(newProgress, max));

        akira = ValueAnimator.ofFloat(progressNow, newProgress);
        akira.setDuration(1500);

        akira.setInterpolator(new DecelerateInterpolator());

        akira.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressNow = (float)animation.getAnimatedValue();
                invalidate();
            }
        });

        akira.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animator)
            {
                isAnimating = false;
                processQueque();
            }
        });

        this.progressAlvo = newProgress;
        akira.start();
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
