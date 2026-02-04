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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import android.animation.AnimatorListenerAdapter;

public class PaintPallet {
    private int []ColorsBackground = new int[6];
    private int numCBack = 0;
    private int []ColorsProgress = new int [6];
    private int numCProg = 0;
    private Paint backgroudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private boolean gradient = true;
    private boolean backIsNotAlpha = false;
    private ArgbEvaluator evaluator = new ArgbEvaluator();

    public PaintPallet(Paint.Style styleProgress, Paint.Style styleBackground)
    {
        if (styleProgress != null)
            setStyleProgress(styleProgress);

        if (styleBackground != null)
            setStyleBackground(styleBackground);
    }

    public void setGradient(boolean gradient)
    {
        this.gradient = gradient;
    }

    public void setBackIsNotAlpha(boolean backIsNotAlpha)
    {
        this.backIsNotAlpha = backIsNotAlpha;
    }

    private int getNewColor(int num, int[] colors, float porcentagem)
    {
        int progressColor = 0;

        if (num == 1)
            progressColor = colors[0];

        else if (num > 1)
        {
            int qtd = num - 1;
            float divisoria = 1f/qtd;
            int indice = (int)(porcentagem/divisoria);
            if (indice == qtd)
                indice = qtd - 1;

            if (gradient)
                progressColor = (int)evaluator.evaluate(porcentagem, colors[indice], colors[indice+1]);
            else
            {
                divisoria = 1f/num;
                indice = (int)(porcentagem/divisoria);
                if (indice == qtd)
                    indice = qtd - 1;
                progressColor = colors[indice];
            }
        }

        return progressColor;
    }
    public void updateColor(float porcentagem)
    {
        backgroudPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        int progressColor = getNewColor(numCProg, ColorsProgress, porcentagem);

        progressPaint.setColor(progressColor);

        if (backIsNotAlpha)
            backgroudPaint.setColor(getNewColor(numCBack, ColorsBackground, porcentagem));

        else
            backgroudPaint.setColor(ColorUtils.setAlphaComponent(progressColor, 120));
    }

    public void setStrokeWidthBackground(float width)
    {
        backgroudPaint.setStrokeWidth(width);
    }

    public void setStrokeWidthProgress(float width)
    {
        progressPaint.setStrokeWidth(width);
    }

    public void setColorsInBackground(int...colors)
    {
        ColorsBackground[0] = Color.parseColor("red");
        int i = 0;
        for (int cor : colors)
        {
            ColorsBackground[i] = cor;
            i+= 1;
            if (i >= 6) {
                break;
            }
        }

        numCBack = colors.length;
    }

    public void setColorsInProgress(int...colors)
    {
        ColorsProgress[0] = Color.parseColor("red");

        int i = 0;
        for (int cor : colors)
        {
            ColorsProgress[i] = cor;
            i+= 1;
            if (i >= 6) {
                break;
            }
        }

        numCProg = colors.length;
    }

    public void setStyleProgress(Paint.Style style)
    {
        progressPaint.setStyle(style);
    }

    public void setStyleBackground(Paint.Style style)
    {
        backgroudPaint.setStyle(style);
    }
    public Paint getBackgroudPaint() {
        return backgroudPaint;
    }

    public Paint getProgressPaint() {
        return progressPaint;
    }
}
