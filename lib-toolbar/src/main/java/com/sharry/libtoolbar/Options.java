package com.sharry.libtoolbar;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

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
    void from(Options other) {
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

    public static class Builder {

        private Options op;

        private Builder() {
            op = new Options();
            op.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            op.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        private Builder(@NonNull Options other) {
            this();
            op.from(other);
        }

        public Builder setPaddingLeft(@Dimension(unit = DP) int paddingLeft) {
            op.paddingLeft = paddingLeft;
            return this;
        }

        public Builder setPaddingTop(@Dimension(unit = DP) int paddingTop) {
            op.paddingTop = paddingTop;
            return this;
        }

        public Builder setPaddingRight(@Dimension(unit = DP) int paddingRight) {
            op.paddingRight = paddingRight;
            return this;
        }

        public Builder setPaddingBottom(@Dimension(unit = DP) int paddingBottom) {
            op.paddingBottom = paddingBottom;
            return this;
        }

        public Builder setWidth(@Dimension(unit = DP) int width) {
            op.width = width;
            return this;
        }

        public Builder setHeight(@Dimension(unit = DP) int height) {
            op.height = height;
            return this;
        }

        public Builder setListener(View.OnClickListener listener) {
            op.listener = listener;
            return this;
        }

        public Options build() {
            return op;
        }

    }

}
