package com.sharry.libtoolbar;

import android.view.View;

import androidx.annotation.ColorInt;

/**
 * 用于封装 TextView/ImageView 相关属性的类
 *
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/25 22:20
 */
public class Options {

    /*
      Constant associated with options.
     */
    static final int INVALIDATE_VALUE = -1;

    // text
    CharSequence text;
    float textSize;
    int textColor;
    // Widget padding
    int paddingLeft;
    int paddingTop;
    int paddingRight;
    int paddingBottom;
    // Widget size
    int width;
    int height;
    // Widget resource
    int resId;
    // listener callback.
    View.OnClickListener listener;

    /**
     * U can get options instance of {@link Options.Builder#build()}
     */
    private Options() {

    }

    public static class Builder {

        // text
        CharSequence text;
        float textSize;
        int textColor;
        // Widget padding
        int paddingLeft;
        int paddingTop;
        int paddingRight;
        int paddingBottom;
        // Widget size
        int width = INVALIDATE_VALUE;
        int height = INVALIDATE_VALUE;
        // Widget resource
        int resId = INVALIDATE_VALUE;
        // listener callback.
        View.OnClickListener listener;

        public Builder() {

        }

        public Builder(Options options) {
            this.text = options.text;
            this.textSize = options.textSize;
            this.listener = options.listener;
            this.textColor = options.textColor;
            this.paddingLeft = options.paddingLeft;
            this.paddingRight = options.paddingRight;
        }

        public Builder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setPaddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        public Builder setPaddingTop(int paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }

        public Builder setPaddingRight(int paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }

        public Builder setPaddingBottom(int paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setResId(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder setListener(View.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Options build() {
            Options options = new Options();
            options.text = this.text;
            options.textSize = this.textSize;
            options.textColor = this.textColor;
            options.paddingLeft = this.paddingLeft;
            options.paddingTop = this.paddingTop;
            options.paddingRight = this.paddingRight;
            options.paddingBottom = this.paddingBottom;
            options.width = this.width;
            options.height = this.height;
            options.resId = this.resId;
            options.listener = this.listener;
            return options;
        }

    }

}
