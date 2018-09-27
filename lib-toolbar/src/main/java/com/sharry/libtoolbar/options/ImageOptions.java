package com.sharry.libtoolbar.options;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import static androidx.annotation.Dimension.DP;

/**
 * Options for ImageView
 *
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/27 13:15
 */
public class ImageOptions extends Options {

    /**
     * U can get ImageOptions.Builder from this factory method.
     */
    public static Builder Builder() {
        return new Builder();
    }

    public static Builder Builder(@NonNull Options other) {
        return new Builder(other);
    }

    /*
      Fields associated with image menu.
    */
    @DrawableRes
    public int drawableResId = INVALIDATE;
    public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

    /**
     * U can get ImageOptions instance from {@link Builder#build()}
     */
    private ImageOptions() {

    }

    @Override
    protected void apply(Options other) {
        super.apply(other);
        if (other instanceof ImageOptions) {
            ImageOptions op = (ImageOptions) other;
            this.drawableResId = op.drawableResId;
            this.scaleType = op.scaleType;
        }
    }

    /**
     * Builder Options instance more easier.
     */
    public static class Builder {

        private ImageOptions op;

        private Builder() {
            op = new ImageOptions();
        }

        private Builder(@NonNull Options other) {
            this();
            op.apply(other);
        }

        public Builder setDrawableResId(@DrawableRes int drawableResId) {
            op.drawableResId = drawableResId;
            return this;
        }

        public Builder setScaleType(ImageView.ScaleType scaleType) {
            op.scaleType = scaleType;
            return this;
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

        public ImageOptions build() {
            return op;
        }

    }

}
