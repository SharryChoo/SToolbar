package com.sharry.libtoolbar.options;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import static androidx.annotation.Dimension.DP;
import static androidx.annotation.Dimension.SP;

/**
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/27 13:15
 */
public class TextOption extends Option {

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
      Constants
     */
    @ColorInt
    public static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    @Dimension(unit = SP)
    public static final int DEFAULT_TITLE_TEXT_SIZE = 18;
    @Dimension(unit = SP)
    public static final int DEFAULT_MENU_TEXT_SIZE = 13;

    /*
      Fields associated with text menu.
     */
    public CharSequence text;
    @Dimension(unit = SP)
    public int textSize = DEFAULT_MENU_TEXT_SIZE;
    @ColorInt
    public int textColor = DEFAULT_TEXT_COLOR;
    public int maxEms = 8;
    public int lines = 1;
    public TextUtils.TruncateAt ellipsize = TextUtils.TruncateAt.END;

    /**
     * U can get TextOption instance from {@link Builder#build()}
     */
    private TextOption() {

    }

    @Override
    protected void apply(Option other) {
        super.apply(other);
        if (other instanceof TextOption) {
            TextOption op = (TextOption) other;
            this.text = op.text;
            this.textSize = op.textSize;
            this.textColor = op.textColor;
            this.maxEms = op.maxEms;
            this.lines = op.lines;
            this.ellipsize = op.ellipsize;
        }
    }

    /**
     * Builder TextOption instance more easier.
     */
    public static class Builder {

        private TextOption op;

        private Builder() {
            op = new TextOption();
        }

        private Builder(@NonNull Option other) {
            this();
            op.apply(other);
        }

        public Builder setText(CharSequence text) {
            op.text = text;
            return this;
        }

        public Builder setTextSize(@Dimension(unit = SP) int textSize) {
            op.textSize = textSize;
            return this;
        }

        public Builder setTextColor(@ColorInt int textColor) {
            op.textColor = textColor;
            return this;
        }

        public Builder setMaxEms(int maxEms) {
            op.maxEms = maxEms;
            return this;
        }

        public Builder setLines(int lines) {
            op.lines = lines;
            return this;
        }

        public Builder setEllipsize(TextUtils.TruncateAt ellipsize) {
            op.ellipsize = ellipsize;
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

        public TextOption build() {
            return op;
        }

    }

}
