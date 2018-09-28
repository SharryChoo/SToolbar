package com.sharry.libtoolbar;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import static androidx.annotation.Dimension.PX;

/**
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/28 8:48
 */
public class ViewOptions implements Options<View> {

    // Widget padding
    @Dimension(unit = PX)
    int paddingLeft = 0;
    @Dimension(unit = PX)
    int paddingTop = 0;
    @Dimension(unit = PX)
    int paddingRight = 0;
    @Dimension(unit = PX)
    int paddingBottom = 0;
    // Layout params
    @Dimension(unit = PX)
    int width = 0;
    @Dimension(unit = PX)
    int height = 0;
    // listener callback.
    View.OnClickListener listener;

    private ViewOptions() {
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public void completion(View view) {
        // Set padding.
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        // Set the layout parameters associated with this textView.
        int validWidth = Utils.isLayoutParamsSpecialValue(width) ? width :
                width + view.getPaddingLeft() + view.getPaddingRight();
        int validHeight = Utils.isLayoutParamsSpecialValue(height) ? height :
                height + view.getPaddingTop() + view.getPaddingBottom();
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
    }

    public static class Builder {

        private ViewOptions op;

        public Builder() {
            op = new ViewOptions();
            op.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            op.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        private Builder(@NonNull ViewOptions other) {
            op = other;
        }

        public Builder setPaddingLeft(@Dimension(unit = PX) int paddingLeft) {
            op.paddingLeft = paddingLeft;
            return this;
        }

        public Builder setPaddingTop(@Dimension(unit = PX) int paddingTop) {
            op.paddingTop = paddingTop;
            return this;
        }

        public Builder setPaddingRight(@Dimension(unit = PX) int paddingRight) {
            op.paddingRight = paddingRight;
            return this;
        }

        public Builder setPaddingBottom(@Dimension(unit = PX) int paddingBottom) {
            op.paddingBottom = paddingBottom;
            return this;
        }

        public Builder setWidth(@Dimension(unit = PX) int width) {
            op.width = width;
            return this;
        }

        public Builder setHeight(@Dimension(unit = PX) int height) {
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
