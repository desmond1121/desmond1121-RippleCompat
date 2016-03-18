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
package com.desmond.ripple.config;

import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.desmond.ripple.drawable.RippleCompatDrawable;
import com.desmond.ripple.factory.RipplePathFactory;

import static com.desmond.ripple.util.RippleUtil.*;

/**
 * @author Desmond Yao
 * @author Jonatan Salas
 */
public class RippleConfig {
    private static final RippleConfig defaultCfg = new RippleConfig();

    /* ripple animation duration*/
    private int rippleDuration = RIPPLE_DURATION;

    /* ripple fade duration, default equal to rippleDuration */
    private int fadeDuration = RIPPLE_DURATION;

    /* ripple color*/
    private int rippleColor = 0x7000ff00;

    /* palette mode*/
    private PaletteMode paletteMode = PaletteMode.VIBRANT;

    /* background image*/
    private Drawable backgroundDrawable = null;

    /* scale type of background image*/
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_CENTER;

    /* max ripple radius. */
    private int maxRippleRadius = MAX_RIPPLE_RADIUS;

    /* ripple animation interpolator*/
    private Interpolator interpolator = new AccelerateInterpolator();

    /* ripple shape type */
    private RippleCompatDrawable.Type type = RippleCompatDrawable.Type.CIRCLE;

    /* true if ripple color with palette of background image.*/
    private boolean isEnablePalette = false;

    /* true if ripple full of the view, and maxRippleRadius would be ignore. */
    private boolean isFull = false;

    /* true if ripple spinning*/
    private boolean isSpin = false;

    public static RippleConfig getDefaultConfig(){
        return defaultCfg;
    }

    public int getRippleDuration() {
        return rippleDuration;
    }

    public RippleConfig setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
        return this;
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public RippleConfig setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        return this;
    }

    public boolean isFull() {
        return isFull;
    }

    public RippleConfig setIsFull(boolean isFull) {
        this.isFull = isFull;
        return this;
    }

    public int getMaxRippleRadius() {
        return maxRippleRadius;
    }

    public RippleConfig setMaxRippleRadius(int maxRippleRadius) {
        if (isFull) {
            return this;
        }

        this.maxRippleRadius = maxRippleRadius;
        return this;
    }

    public int getFadeDuration() {
        return fadeDuration;
    }

    public RippleConfig setFadeDuration(int fadeDuration) {
        this.fadeDuration = fadeDuration;
        return this;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public RippleConfig setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public RippleConfig setType(RippleCompatDrawable.Type type) {
        this.type = type;
        return this;
    }

    public Path getPath(){
        switch (type){

            case CIRCLE:
                return RipplePathFactory.produceCirclePath();

            case HEART:
                return RipplePathFactory.produceHeartPath();

            case TRIANGLE:
                return RipplePathFactory.produceTrianglePath();

            default:
                return RipplePathFactory.produceCirclePath();
        }
    }

    public boolean isSpin() {
        return isSpin;
    }

    public RippleConfig setIsSpin(boolean isSpin) {
        this.isSpin = isSpin;
        return this;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public RippleConfig setBackgroundDrawable(Drawable backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
        return this;
    }

    public RippleConfig setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public PaletteMode getPaletteMode() {
        if(isEnablePalette){
            return paletteMode;
        }else{
            return PaletteMode.DISABLED;
        }
    }

    public RippleConfig setPaletteMode(PaletteMode paletteMode) {
        if(isEnablePalette) {
            this.paletteMode = paletteMode;
        }
        return this;
    }

    public RippleConfig setIsEnablePalette(boolean isEnablePalette) {
        this.isEnablePalette = isEnablePalette;
        return this;
    }
}
