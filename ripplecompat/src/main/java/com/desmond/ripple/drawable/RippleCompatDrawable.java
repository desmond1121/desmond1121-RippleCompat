/*
 * Copyright (C) 2016 Jonatan Salas
 * Copyright (C) 2015 Desmond Yao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.desmond.ripple.drawable;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.desmond.ripple.config.RippleConfig;
import com.desmond.ripple.util.RippleUtil;

import java.util.ArrayList;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class RippleCompatDrawable extends Drawable implements View.OnTouchListener {

    private enum Speed {
        PRESSED,
        NORMAL
    }

    public enum Type {
        CIRCLE,
        HEART,
        TRIANGLE
    }

    public interface OnFinishListener {
        void onFinish();
    }

    /* ClipBound for widget inset padding.*/
    private Rect clipBound;
    /* Drawable bound for background image. */
    private Rect drawableBound;

    private ArrayList<OnFinishListener> onFinishListeners;
    private Paint ripplePaint;
    private Speed speed;
    private Path ripplePath;
    private Interpolator interpolator;
    private ValueAnimator fadeAnimator;
    private Drawable backgroundDrawable;
    private RippleUtil.PaletteMode paletteMode;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_CENTER;

    private int width = 0;
    private int height = 0;
    private int rippleColor;
    private int backgroundColor;
    private int backgroundColorAlpha = 0;
    private int rippleDuration;
    private int maxRippleRadius;
    private int fadeDuration;
    private int alpha;
    private int alphaDelta = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private float degree;

    private long startTime;
    private int x;
    private int y;
    private float scale = 0f;
    private int lastX;
    private int lastY;
    private float lastScale = 0f;

    private boolean isFull = false;
    private boolean isSpin = false;
    private boolean isWaving = false;
    private boolean isPressed = false;
    private boolean isFading = false;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable rippleRunnable = new Runnable() {
        @Override
        public void run() {
            updateRipple(speed);
            if (isWaving || isPressed) {
                handler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            } else if (!isFading) {
                startFadeAnimation();
            }
        }
    };

    public RippleCompatDrawable(RippleConfig cfg) {
        this(
                cfg.getRippleColor(),
                cfg.getMaxRippleRadius(),
                cfg.getRippleDuration(),
                cfg.getInterpolator(),
                cfg.getFadeDuration(),
                cfg.isFull(),
                cfg.getPath(),
                cfg.isSpin(),
                cfg.getPaletteMode()
        );
    }

    private RippleCompatDrawable(int rippleColor, int maxRippleRadius,
                                 int rippleDuration, Interpolator interpolator, int fadeDuration,
                                 boolean isFull, Path path, boolean isSpin, RippleUtil.PaletteMode paletteMode) {
        setRippleColor(rippleColor);
        this.maxRippleRadius = maxRippleRadius;
        this.rippleDuration = rippleDuration;
        this.interpolator = interpolator;
        this.fadeDuration = fadeDuration;
        this.paletteMode = paletteMode;

        this.isFull = isFull;
        this.isSpin = isSpin;

        ripplePath = path;

        ripplePaint = new Paint();
        ripplePaint.setAntiAlias(true);
        alpha = 0;
    }

    @Override
    public void draw(Canvas canvas) {

        if (backgroundDrawable == null) {
            canvas.clipRect(clipBound);
        } else if (backgroundDrawable instanceof ColorDrawable) {
            canvas.clipRect(clipBound);
            backgroundDrawable.setBounds(clipBound);
            backgroundDrawable.draw(canvas);
        } else {
            if (drawableBound == null) {
                drawableBound = RippleUtil.getBound(scaleType, new Rect(0, 0, width, height),
                        backgroundDrawable.getIntrinsicWidth(), backgroundDrawable.getIntrinsicHeight());
            }
            canvas.clipRect(drawableBound);
            backgroundDrawable.setBounds(drawableBound);
            backgroundDrawable.draw(canvas);
        }

        canvas.drawColor(RippleUtil.alphaColor(backgroundColor, backgroundColorAlpha));
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        if (isSpin) canvas.rotate(degree);
        ripplePaint.setAlpha(alpha);
        canvas.drawPath(ripplePath, ripplePaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        ripplePaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {
        return 0;
    }

    private long elapsedOffset = 0;

    private void updateRipple(Speed speed) {
        float progress = 0f;
        if (isWaving) {
            long elapsed = SystemClock.uptimeMillis() - startTime;
            if (speed == Speed.PRESSED) {
                elapsed = elapsed / 5;
                elapsedOffset = elapsed * 4;
            } else {
                elapsed = elapsed - elapsedOffset;
            }
            progress = Math.min(1f, (float) elapsed / rippleDuration);
            isWaving = progress <= 0.99f;
            scale = (maxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS * interpolator.getInterpolation(progress) + 1f;
            backgroundColorAlpha = (int) (Color.alpha(backgroundColor) * (progress <= 0.125f ? progress * 8 : 1f));
        } else {
            scale = (maxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS + 1f;
            backgroundColorAlpha = Color.alpha(backgroundColor);
        }
        if (lastX == x && lastY == y && lastScale == scale) return;

        lastX = x;
        lastY = y;
        lastScale = scale;

        ripplePaint.setColor(rippleColor);
        ripplePaint.setStyle(Paint.Style.FILL);

        degree = progress * 480f;
        invalidateSelf();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();

                speed = Speed.PRESSED;

                stopFading();
                handler.removeCallbacks(rippleRunnable);
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(rippleRunnable, RippleUtil.FRAME_INTERVAL);
                isWaving = true;
                isPressed = true;
                elapsedOffset = 0;
                lastX = x;
                lastY = y;
                degree = 0;
                alpha = Color.alpha(rippleColor);

                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                speed = Speed.PRESSED;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                speed = Speed.NORMAL;
                isPressed = false;
                startFadeAnimation();
                break;
        }
        return true;
    }

    public void triggerListener() {
        if (onFinishListeners != null && onFinishListeners.size() != 0) {
            for (OnFinishListener listener : onFinishListeners) {
                listener.onFinish();
            }
        }
    }

    public void finishRipple() {
        handler.removeCallbacks(rippleRunnable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && fadeAnimator != null) {
            fadeAnimator.removeAllUpdateListeners();
            fadeAnimator.removeAllListeners();
            fadeAnimator = null;
        } else {
            handler.removeCallbacks(mFadeRunnable4Froyo);
        }
    }

    private void startFadeAnimation() {
        isFading = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            startFadeAnimation4HoneyComb();
        } else {
            startFadeAnimation4Froyo();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startFadeAnimation4HoneyComb() {
        if (fadeAnimator == null) {
            fadeAnimator = ValueAnimator.ofInt(Color.alpha(rippleColor), 0);
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alpha = (int) animation.getAnimatedValue();
                    if (alpha <= backgroundColorAlpha) backgroundColorAlpha = alpha;
                    invalidateSelf();
                }
            });

            fadeAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isFading = false;
                    triggerListener();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isFading = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            fadeAnimator.setDuration(fadeDuration);
        } else {
            fadeAnimator.cancel();
        }
        fadeAnimator.start();
    }

    private void startFadeAnimation4Froyo() {
        alphaDelta = getAlphaDelta();
        handler.removeCallbacks(mFadeRunnable4Froyo);
        handler.post(mFadeRunnable4Froyo);
    }

    private void stopFading() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (fadeAnimator != null) fadeAnimator.cancel();
        } else {
            handler.removeCallbacks(mFadeRunnable4Froyo);
        }
        isFading = false;
    }

    private Runnable mFadeRunnable4Froyo = new Runnable() {
        @Override
        public void run() {
            if (alpha != 0) {
                int alpha = RippleCompatDrawable.this.alpha - alphaDelta;
                if (alpha <= 0) {
                    alpha = 0;
                    isFading = false;
                    triggerListener();
                }
                RippleCompatDrawable.this.alpha = alpha;
                if (RippleCompatDrawable.this.alpha <= backgroundColorAlpha) backgroundColorAlpha = RippleCompatDrawable.this.alpha;
                invalidateSelf();
                handler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            }
        }
    };

    public void setPadding(float l, float t, float r, float b) {
        paddingLeft = RippleUtil.dip2px(l);
        paddingRight = RippleUtil.dip2px(r);
        paddingTop = RippleUtil.dip2px(t);
        paddingBottom = RippleUtil.dip2px(b);
    }

    public void setMeasure(int width, int height) {
        this.width = width;
        this.height = height;
        setClipBound();
    }

    public void setMaxRippleRadius(int maxRippleRadius) {
        this.maxRippleRadius = maxRippleRadius;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
        RippleUtil.palette(this, backgroundDrawable, paletteMode);
        drawableBound = null;
    }

    public void setPaletteMode(RippleUtil.PaletteMode paletteMode) {
        this.paletteMode = paletteMode;
        RippleUtil.palette(this, backgroundDrawable, this.paletteMode);
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        drawableBound = null;
    }

    public void addOnFinishListener(OnFinishListener onFinishListener) {
        if (onFinishListeners == null) {
            onFinishListeners = new ArrayList<>();
        }
        onFinishListeners.add(onFinishListener);
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        backgroundColor = RippleUtil.produceBackgroundColor(rippleColor);
    }

    private void setClipBound() {
        if (clipBound == null) {
            clipBound = new Rect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        } else {
            clipBound.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        }
    }

    public Rect getDrawableBound() {
        return drawableBound;
    }

    public Rect getClipBound() {
        return clipBound;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    private int getAlphaDelta() {
        int times = fadeDuration / RippleUtil.FRAME_INTERVAL + 1;
        return Color.alpha(rippleColor) / times;
    }
}
