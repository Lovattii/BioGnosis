package com.example.pincel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.LinkedList;
import java.util.Queue;



public class AkiraAnimation {

    public interface CallbackInvalidate{
        void ativaInvalidate(float progresNow);
    }

    CallbackInvalidate callbackInvalidate;

    private ValueAnimator akira;
    private Queue<Float> animationQueue = new LinkedList<>();
    private boolean isAnimating = false;
    private final int max = 100;
    private float progressNow;

    private long duration;
    public AkiraAnimation(long duration, CallbackInvalidate callbackInvalidate)
    {
        this.duration = duration;

        if (callbackInvalidate != null)
            this.callbackInvalidate = callbackInvalidate;
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

        float diff = Math.abs(progressNow - newProgress);

        long durationDynamics = ((long)(duration * 0.2 + (diff/max) * duration* 0.8));

        akira.setDuration(durationDynamics);

        if (diff < 5)
            akira.setInterpolator(new LinearInterpolator());

        else
            akira.setInterpolator(new DecelerateInterpolator());

        akira.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressNow = (float)animation.getAnimatedValue();

                if (callbackInvalidate != null)
                    callbackInvalidate.ativaInvalidate(progressNow);
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

        akira.start();
    }

    public void destroy() {
        // 1. Para a animação se estiver rodando
        if (akira != null && akira.isRunning())
            akira.cancel();

        animationQueue.clear();
        callbackInvalidate = null;
        akira = null;
    }


}
