package com.sharry.libtoolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import static androidx.annotation.Dimension.PX;

/**
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/28 8:50
 */
public class ImageViewOptions implements Options<ImageView> {

    /*
      Fields associated with image menu.
    */
    @DrawableRes
    int drawableResId = View.NO_ID;
    ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    // Widget padding
    @Dimension(unit = PX)
    int paddingLeft = 0;
    @Dimension(unit = PX)
    int paddingRight = 0;
    // Layout params
    @Dimension(unit = PX)
    int widthExcludePadding = ViewGroup.LayoutParams.WRAP_CONTENT;
    @Dimension(unit = PX)
    int heightExcludePadding = ViewGroup.LayoutParams.WRAP_CONTENT;
    // listener callback.
    View.OnClickListener listener;

    private ImageViewOptions() {
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public void completion(ImageView view) {
        // Set padding.
        view.setPadding(paddingLeft, 0, paddingRight, 0);
        // Set the layout parameters associated with this textView.
        int validWidth = Utils.isLayoutParamsSpecialValue(widthExcludePadding) ? widthExcludePadding :
                widthExcludePadding + view.getPaddingLeft() + view.getPaddingRight();
        int validHeight = Utils.isLayoutParamsSpecialValue(heightExcludePadding) ? heightExcludePadding :
                heightExcludePadding + view.getPaddingTop() + view.getPaddingBottom();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (null == params) {
            params = new ViewGroup.LayoutParams(validWidth, validHeight);
        } else {
            params.width = validWidth;
            params.height = validHeight;
        }
        view.setLayoutParams(params);
        // Set OnClickListener
        if (null != listener) {
            view.setOnClickListener(listener);
        }
        // Set some fields associated with this imageView.
        view.setImageResource(drawableResId);
        view.setScaleType(scaleType);
    }

    /**
     * Builder Options instance more easier.
     */
    public static class Builder {

        private ImageViewOptions op;

        public Builder() {
            op = new ImageViewOptions();
        }

        private Builder(@NonNull ImageViewOptions other) {
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

        public Builder setPaddingLeft(@Dimension(unit = PX) int paddingLeft) {
            op.paddingLeft = paddingLeft;
            return this;
        }

        public Builder setPaddingRight(@Dimension(unit = PX) int paddingRight) {
            op.paddingRight = paddingRight;
            return this;
        }

        public Builder setWidthWithoutPadding(@Dimension(unit = PX) int widthExcludePadding) {
            op.widthExcludePadding = widthExcludePadding;
            return this;
        }

        public Builder setHeightWithoutPadding(@Dimension(unit = PX) int heightExcludePadding) {
            op.heightExcludePadding = heightExcludePadding;
            return this;
        }

        public Builder setListener(View.OnClickListener listener) {
            op.listener = listener;
            return this;
        }

        public ImageViewOptions build() {
            return op;
        }

    }
}
