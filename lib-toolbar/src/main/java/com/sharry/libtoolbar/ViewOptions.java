package com.sharry.libtoolbar;

import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.Dimension;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import static androidx.annotation.Dimension.PX;

/**
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/28 8:48
 */
public class ViewOptions implements Options<View> {

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {
    }

    private int visibility;
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
    int widthExcludePadding = 0;
    @Dimension(unit = PX)
    int heightExcludePadding = 0;
    // listener callback.
    View.OnClickListener listener;

    private ViewOptions() {
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public void completion(View view) {
        view.setVisibility(visibility);
        // Set padding.
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
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
    }

    public static class Builder {

        private ViewOptions op;

        public Builder() {
            op = new ViewOptions();
            op.widthExcludePadding = ViewGroup.LayoutParams.WRAP_CONTENT;
            op.heightExcludePadding = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        private Builder(@NonNull ViewOptions other) {
            op = other;
        }

        public Builder setVisibility(@Visibility int visibility) {
            op.visibility = visibility;
            return this;
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

        public Builder setWidthExcludePadding(@Dimension(unit = PX) int widthExcludePadding) {
            op.widthExcludePadding = widthExcludePadding;
            return this;
        }

        public Builder setHeightExcludePadding(@Dimension(unit = PX) int widthExcludePadding) {
            op.heightExcludePadding = widthExcludePadding;
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
