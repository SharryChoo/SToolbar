package com.sharry.libtoolbar;

import android.view.View;
import android.view.ViewGroup;
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

    /*
      Fields associated with image menu.
    */
    @DrawableRes
    int drawableResId = View.NO_ID;
    ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

    /**
     * U can get ImageOptions instance from {@link Builder#build()}
     */
    private ImageOptions() {

    }

    /**
     * U can rebuild this instance from here.
     */
    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    void from(Options other) {
        super.from(other);
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

        public Builder() {
            op = new ImageOptions();
            op.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            op.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        private Builder(@NonNull ImageOptions other) {
            op = other;
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
