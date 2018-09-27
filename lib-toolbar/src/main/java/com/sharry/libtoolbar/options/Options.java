package com.sharry.libtoolbar.options;

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

    /*
      Constant associated with options.
     */
    public static final int INVALIDATE = -1;

    // Widget padding
    @Dimension(unit = DP)
    public int paddingLeft;
    @Dimension(unit = DP)
    public int paddingTop;
    @Dimension(unit = DP)
    public int paddingRight;
    @Dimension(unit = DP)
    public int paddingBottom;
    // Widget size
    public int width = INVALIDATE;
    public int height = INVALIDATE;
    // listener callback.
    public View.OnClickListener listener;

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
