package com.sharry.libtoolbar;

import android.view.View;

import androidx.annotation.Dimension;

import static androidx.annotation.Dimension.DP;

/**
 * 用于封装 View 相关属性
 *
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/25 22:20
 */
public class Options {

    // Widget padding
    @Dimension(unit = DP)
    int paddingLeft;
    @Dimension(unit = DP)
    int paddingTop;
    @Dimension(unit = DP)
    int paddingRight;
    @Dimension(unit = DP)
    int paddingBottom;
    // Layout params
    @Dimension(unit = DP)
    int width;
    @Dimension(unit = DP)
    int height;
    // listener callback.
    View.OnClickListener listener;

    Options() {

    }

    /**
     * Inject value from other options
     */
    protected void apply(Options other) {
        if (null == other) {
            throw new NullPointerException("Please ensure parameter other nonnull.");
        }
        this.paddingLeft = other.paddingLeft;
        this.paddingRight = other.paddingRight;
        this.paddingTop = other.paddingTop;
        this.paddingBottom = other.paddingBottom;
        this.width = other.width;
        this.height = other.height;
        this.listener = other.listener;
    }

}
