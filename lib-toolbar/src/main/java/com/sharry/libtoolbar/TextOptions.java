package com.sharry.libtoolbar;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

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

    /*
      Constants
     */
    static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    static final int DEFAULT_TITLE_TEXT_SIZE = 18;
    static final int DEFAULT_MENU_TEXT_SIZE = 13;
    static final int DEFAULT_MAX_EMS = 8;
    static final int DEFAULT_LINES = 1;
    static final TextUtils.TruncateAt DEFAULT_ELLIPSIZE = TextUtils.TruncateAt.END;

    /**
     * U can get ImageOptions.Builder from this factory method.
     */
    public static Builder Builder() {
        return new Builder();
    }

    static Builder Builder(@NonNull TextOptions other) {
        return new Builder(other);
    }

    /*
      Fields associated with text menu.
     */
    CharSequence text;
    @Dimension(unit = SP)
    int textSize;
    @ColorInt
    int textColor = DEFAULT_TEXT_COLOR;
    int maxEms = DEFAULT_MAX_EMS;
    int lines = DEFAULT_LINES;
    TextUtils.TruncateAt ellipsize = DEFAULT_ELLIPSIZE;

    /**
     * U can get TextOptions instance from {@link Builder#build()}
     */
    private TextOptions() {

    }

    @Override
    void from(Options other) {
        super.from(other);
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
            op.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            op.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        private Builder(@NonNull TextOptions other) {
            this();
            op.from(other);
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
