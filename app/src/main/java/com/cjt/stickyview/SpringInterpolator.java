package com.cjt.stickyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.BaseInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Administrator on 2016/3/15.
 */
public class SpringInterpolator implements Interpolator {
    public SpringInterpolator() {
    }

    public SpringInterpolator(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float t) {
        double factor = 0.2;
        return (float)(Math.pow(2, -10 * t) * Math.sin((t - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
