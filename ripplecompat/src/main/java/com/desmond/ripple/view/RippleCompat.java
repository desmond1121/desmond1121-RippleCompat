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
package com.desmond.ripple.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.desmond.ripple.config.RippleConfig;
import com.desmond.ripple.drawable.RippleCompatDrawable;

import static com.desmond.ripple.util.RippleUtil.*;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class RippleCompat {
    private static final String TAG = RippleCompat.class.getSimpleName();
    private static InputMethodManager manager = null;
    private static Context context = null;

    public static void init(@NonNull Context ctx) {
        manager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        context = ctx;
    }

    public static void apply(View v) {
        apply(v, RippleConfig.getDefaultConfig(), null);
    }

    /**
     * set ripple with ripple color.
     *
     * @param v           view to set
     * @param rippleColor ripple color
     */
    public static void apply(View v, int rippleColor) {
        final RippleConfig config = new RippleConfig()
                .setRippleColor(rippleColor);

        apply(v, config, null);
    }

    /**
     * Set ripple with background
     *
     * @param rippleColor ripple color.
     * @param v           view to set.
     * @param drawable    background drawable.
     * @param scaleType   scaleType.
     */
    public static void apply(View v, int rippleColor, Drawable drawable, ImageView.ScaleType scaleType) {
        final RippleConfig config = new RippleConfig()
                .setRippleColor(rippleColor)
                .setBackgroundDrawable(drawable)
                .setScaleType(scaleType);

        apply(v, config, null);
    }

    /**
     * Set ripple with background
     *
     * @param rippleColor ripple color.
     * @param v           view to set.
     * @param resId       resourceId of background.
     * @param scaleType   scaleType, {@link android.widget.ImageView.ScaleType}.
     */
    public static void apply(View v, int rippleColor, int resId, ImageView.ScaleType scaleType) {
        final RippleConfig config = new RippleConfig()
                .setRippleColor(rippleColor);

        if (context != null) {
            try {
                config.setBackgroundDrawable(ContextCompat.getDrawable(context, resId))
                      .setScaleType(scaleType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "RippleCompat->apply --err log-- not init context!");
        }

        apply(v, config, null);
    }

    public static void apply(View v, RippleConfig config) {
        apply(v, config, null);
    }

    public static void apply(View v, RippleConfig config, RippleCompatDrawable.OnFinishListener onFinishListener) {
        v.setFocusableInTouchMode(true);
        final RippleCompatDrawable drawable = new RippleCompatDrawable(config);
        if (onFinishListener != null) {
            drawable.addOnFinishListener(onFinishListener);
        }
        handleAttach(v, drawable);
        measure(drawable, v);
        adaptBackground(drawable, v, config);
    }

    @TargetApi(12)
    private static void handleAttach(final View v, final RippleCompatDrawable drawable) {
        if (Build.VERSION.SDK_INT >= 12) {
            v.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    drawable.finishRipple();
                }
            });
        }
    }

    private static void adaptBackground(RippleCompatDrawable rippleDrawable, View v, RippleConfig config) {
        Drawable background;

        if (v instanceof ImageView) {
            ImageView.ScaleType scaleType = ((ImageView) v).getScaleType();
            background = ((ImageView) v).getDrawable();
            rippleDrawable.setBackgroundDrawable(background);
            rippleDrawable.setScaleType(scaleType);
            rippleDrawable.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    v.getPaddingBottom());
            ((ImageView) v).setImageDrawable(null);
            setBackground(v, rippleDrawable);
        } else {
            if (config.getBackgroundDrawable() != null) {
                rippleDrawable.setBackgroundDrawable(config.getBackgroundDrawable());
                rippleDrawable.setScaleType(config.getScaleType());
            }

            background = v.getBackground();

            if (background != null) {
                setBackground(v, new LayerDrawable(new Drawable[] {
                        background,
                        rippleDrawable
                }));
            } else {
                setBackground(v, rippleDrawable);
            }
        }
    }

    /**
     * Set palette mode of the ripple.
     *
     * @param v view
     * @param paletteMode palette mode. {@link PaletteMode}
     */
    public static void setPaletteMode(View v, PaletteMode paletteMode) {
        Drawable drawable = v.getBackground();
        if (drawable instanceof RippleCompatDrawable) {
            ((RippleCompatDrawable) drawable).setPaletteMode(paletteMode);
        } else if (drawable instanceof LayerDrawable) {
            int layer = ((LayerDrawable) drawable).getNumberOfLayers();
            if(((LayerDrawable) drawable).getDrawable(layer - 1) instanceof RippleCompatDrawable) {
                ((RippleCompatDrawable) ((LayerDrawable) drawable).getDrawable(layer - 1)).setPaletteMode(paletteMode);
            }
        }
    }

    /**
     * Set scaleType of the ripple drawable background.
     *
     * @param v         view
     * @param scaleType ScaleType of an image, {@link android.widget.ImageView.ScaleType}
     */
    public static void setScaleType(View v, ImageView.ScaleType scaleType) {
        Drawable drawable = v.getBackground();
        if (drawable instanceof RippleCompatDrawable) {
            ((RippleCompatDrawable) drawable).setScaleType(scaleType);
        } else if (drawable instanceof LayerDrawable
                && ((LayerDrawable) drawable).getDrawable(1) instanceof RippleCompatDrawable) {
            ((RippleCompatDrawable) ((LayerDrawable) drawable).getDrawable(1)).setScaleType(scaleType);
        }
        v.invalidate();
    }

    private static void measure(final RippleCompatDrawable drawable, final View v) {
        if (v instanceof Button) {
            fitButton(drawable, v instanceof AppCompatButton);
        } else if (v instanceof EditText) {
            fitEditText(drawable, v instanceof AppCompatEditText);
        }
        v.setFocusableInTouchMode(true);
        v.setOnTouchListener(new ForwardingTouchListener(drawable, v));
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int radius = Math.max(v.getMeasuredWidth(), v.getMeasuredHeight());
                drawable.setMeasure(v.getMeasuredWidth(), v.getMeasuredHeight());
                if (drawable.isFull()) drawable.setMaxRippleRadius(radius);
            }
        });
    }

    private static void fitButton(final RippleCompatDrawable drawable, boolean isAppCompatStyle) {
        drawable.setPadding(BTN_INSET_HORIZONTAL, isAppCompatStyle ? BTN_INSET_VERTICAL_APPCOMPAT : BTN_INSET_VERTICAL,
                BTN_INSET_HORIZONTAL, isAppCompatStyle ? BTN_INSET_VERTICAL_APPCOMPAT : BTN_INSET_VERTICAL);
    }

    private static void fitEditText(final RippleCompatDrawable drawable, boolean isAppCompatStyle) {
        drawable.setPadding(isAppCompatStyle ? ET_INSET_HORIZONTAL_APPCOMPAT : ET_INSET,
                isAppCompatStyle ? ET_INSET_TOP_APPCOMPAT : ET_INSET,
                isAppCompatStyle ? ET_INSET_HORIZONTAL_APPCOMPAT : ET_INSET,
                isAppCompatStyle ? ET_INSET_BOTTOM_APPCOMPAT : ET_INSET);
    }

    private static class ForwardingTouchListener implements View.OnTouchListener {
        RippleCompatDrawable drawable;

        private ForwardingTouchListener(RippleCompatDrawable drawable, final View v) {
            this.drawable = drawable;
            drawable.addOnFinishListener(new RippleCompatDrawable.OnFinishListener() {
                @Override
                public void onFinish() {
                    if (v instanceof EditText && manager != null) {
                        v.requestFocus();
                        manager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    }
                    v.performClick();
                }
            });
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return isInBound(event.getX(), event.getY()) && drawable.onTouch(v, event);
                default: return drawable.onTouch(v, event);
            }
        }

        private boolean isInBound(float x, float y) {
            Rect bound;
            if (drawable.getBackgroundDrawable() == null || drawable.getBackgroundDrawable() instanceof ColorDrawable) {
                bound = drawable.getClipBound();
            } else {
                bound = drawable.getDrawableBound();
            }
            return x >= bound.left && x <= bound.right && y >= bound.top && y <= bound.bottom;
        }
    }
}
