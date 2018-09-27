package com.sharry.libtoolbar.options;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import static androidx.annotation.Dimension.DP;

/**
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/27 13:15
 */
public class ImageOption extends Option {

    /**
     * U can get ImageOption.Builder from this factory method.
     */
    public static Builder Builder() {
        return new Builder();
    }

    public static Builder Builder(@NonNull Option other) {
        return new Builder(other);
    }

    /*
      Fields associated with image menu.
    */
    @DrawableRes
    public int drawableResId = INVALIDATE;
    public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

    /**
     * U can get ImageOption instance from {@link Builder#build()}
     */
    private ImageOption() {

    }

    @Override
    protected void apply(Option other) {
        super.apply(other);
        if (other instanceof ImageOption) {
            ImageOption op = (ImageOption) other;
            this.drawableResId = op.drawableResId;
            this.scaleType = op.scaleType;
        }
    }

    /**
     * Builder Option instance more easier.
     */
    public static class Builder {

        private ImageOption op;

        private Builder() {
            op = new ImageOption();
        }

        private Builder(@NonNull Option other) {
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

        public ImageOption build() {
            return op;
        }

    }

}
