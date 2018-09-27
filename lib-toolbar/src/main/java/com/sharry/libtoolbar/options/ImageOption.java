package com.sharry.libtoolbar.options;

import android.view.View;

import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;

import static androidx.annotation.Dimension.DP;

/**
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/27 13:15
 */
public class ImageOption extends Option {

    // Widget resource
    @DrawableRes
    public int drawableResId = INVALIDATE;

    /**
     * U can get ImageOption instance from {@link Builder#build()}
     */
    private ImageOption() {

    }

    @Override
    protected void apply(Option other) {
        super.apply(other);
        if (other instanceof ImageOption) {
            this.drawableResId = ((ImageOption) other).drawableResId;
        }
    }

    /**
     * Builder Option instance more easier.
     */
    public static class Builder {

        private ImageOption op;

        public Builder() {
            op = new ImageOption();
        }

        public Builder(Option other) {
            this();
            op.apply(other);
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

        public Builder setDrawableResId(@DrawableRes int drawableResId) {
            op.drawableResId = drawableResId;
            return this;
        }

        public Builder setListener(View.OnClickListener listener) {
            op.listener = listener;
            return this;
        }

        public ImageOption build() {
            return op;
        }

    }

}
