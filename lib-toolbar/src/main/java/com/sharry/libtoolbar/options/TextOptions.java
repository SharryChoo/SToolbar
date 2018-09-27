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
 * Options for TextView
 *
 * @author Sharry <a href="SharryChooCHN@Gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/9/27 13:15
 */
public class TextOptions extends Options {

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
    public int textSize;
    @ColorInt
    public int textColor = DEFAULT_TEXT_COLOR;
    public int maxEms = 8;
    public int lines = 1;
    public TextUtils.TruncateAt ellipsize = TextUtils.TruncateAt.END;

    /**
     * U can get TextOptions instance from {@link Builder#build()}
     */
    private TextOptions() {

    }

    @Override
    protected void apply(Options other) {
        super.apply(other);
        if (other instanceof TextOptions) {
            TextOptions op = (TextOptions) other;
            this.text = op.text;
            this.textSize = op.textSize;
            this.textColor = op.textColor;
            this.maxEms = op.maxEms;
            this.lines = op.lines;
            this.ellipsize = op.ellipsize;
        }
    }

    /**
     * Builder TextOptions instance more easier.
     */
    public static class Builder {

        private TextOptions op;

        private Builder() {
            op = new TextOptions();
        }

        private Builder(@NonNull Options other) {
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

        public TextOptions build() {
            return op;
        }

    }

}
