package com.sharry.libtoolbar;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.ColorInt;

/**
 * 用于封装 TextView/ImageView 相关属性的类
 *
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/25 22:20
 */
public class Option {

    /*
      Constant associated with options.
     */
    static final int INVALIDATE = -1;
    static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    static final int DEFAULT_TITLE_TEXT_SIZE = 18;
    static final int DEFAULT_MENU_TEXT_SIZE = 13;

    // text
    CharSequence text;
    int textSize;
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
    int drawableResId;
    // listener callback.
    View.OnClickListener listener;

    /**
     * U can get options instance of {@link Option.Builder#build()}
     */
    private Option() {

    }

    /**
     * Builder Option instance more easier.
     */
    public static class Builder {

        // text
        CharSequence text;
        int textSize = DEFAULT_TITLE_TEXT_SIZE;
        int textColor = DEFAULT_TEXT_COLOR;
        // Widget padding
        int paddingLeft;
        int paddingTop;
        int paddingRight;
        int paddingBottom;
        // Widget size
        int width = INVALIDATE;
        int height = INVALIDATE;
        // Widget resource
        int drawableResId = INVALIDATE;
        // listener callback.
        View.OnClickListener listener;

        public Builder() {

        }

        public Builder(Option option) {
            this.text = option.text;
            this.textSize = option.textSize;
            this.textColor = option.textColor;
            this.paddingLeft = option.paddingLeft;
            this.paddingRight = option.paddingRight;
            this.paddingTop = option.paddingTop;
            this.paddingBottom = option.paddingBottom;
            this.width = option.width;
            this.height = option.height;
            this.drawableResId = option.drawableResId;
            this.listener = option.listener;
        }

        public Builder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setPaddingLeft(/*dp*/int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        public Builder setPaddingTop(/*dp*/int paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }

        public Builder setPaddingRight(/*dp*/int paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }

        public Builder setPaddingBottom(/*dp*/int paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }

        public Builder setWidth(/*dp*/int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(/*dp*/int height) {
            this.height = height;
            return this;
        }

        public Builder setDrawableResId(int drawableResId) {
            this.drawableResId = drawableResId;
            return this;
        }

        public Builder setListener(View.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Option build() {
            Option option = new Option();
            option.text = this.text;
            option.textSize = this.textSize;
            option.textColor = this.textColor;
            option.paddingLeft = this.paddingLeft;
            option.paddingTop = this.paddingTop;
            option.paddingRight = this.paddingRight;
            option.paddingBottom = this.paddingBottom;
            option.width = this.width;
            option.height = this.height;
            option.drawableResId = this.drawableResId;
            option.listener = this.listener;
            return option;
        }

    }

}
