package com.sharry.libtoolbar;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import static com.sharry.libtoolbar.Option.DEFAULT_MENU_TEXT_SIZE;
import static com.sharry.libtoolbar.Option.DEFAULT_TEXT_COLOR;
import static com.sharry.libtoolbar.Option.DEFAULT_TITLE_TEXT_SIZE;
import static com.sharry.libtoolbar.Option.INVALIDATE;
import static com.sharry.libtoolbar.SToolbar.DEFAULT_INTERVAL;
import static com.sharry.libtoolbar.Utils.isNotEmpty;

/**
 * Build common toolbar more easy.
 *
 * @author Sharry <a href="frankchoochina@gmail.com">Contact me.</a>
 * @version 2.0
 * @since 2018/8/27 23:36
 */
public class Builder {

    private Context mContext;
    private ViewGroup mContentParent;
    private Style mStyle = Style.DEFAULT;

    private int mBgColor = INVALIDATE;
    private int mBgDrawableResId = INVALIDATE;
    private int mItemHorizontalInterval = DEFAULT_INTERVAL;

    private int mTitleGravity = Gravity.CENTER | Gravity.TOP;
    private Option mTitleTextOp;
    private Option mTitleImageOp;
    private View mCustomTitleView;

    private List<Option> mMenuLeftOps;
    private List<Option> mMenuRightOps;

    /**
     * 给 Activity 添加 Toolbar
     */
    Builder(Context context) {
        if (context instanceof Activity) {
            mContext = context;
            mContentParent = ((Activity) mContext).findViewById(Window.ID_ANDROID_CONTENT);
        } else {
            throw new IllegalArgumentException("Please ensure context instanceof Activity.");
        }
    }

    /**
     * 给 View 添加 Toolbar, 确保传入的 View 为 LinearLayout
     */
    Builder(View contentView) {
        if (contentView instanceof LinearLayout) {
            mContentParent = (ViewGroup) contentView;
            mContext = contentView.getContext();
        } else {
            throw new IllegalArgumentException("Please ensure parameter contentView instanceof " +
                    "LinearLayout, now is: " + contentView);
        }
    }

    /**
     * 设置状态栏的样式
     */
    public Builder setStatusBarStyle(Style statusBarStyle) {
        mStyle = statusBarStyle;
        return this;
    }

    /**
     * 背景色
     */
    public Builder setBackgroundColor(@ColorInt int color) {
        mBgColor = color;
        return this;
    }

    public Builder setBackgroundColorRes(@ColorRes int colorResId) {
        mBgColor = ContextCompat.getColor(mContext, colorResId);
        return this;
    }

    public Builder setBackgroundDrawableRes(@DrawableRes int drawableResId) {
        mBgDrawableResId = drawableResId;
        return this;
    }

    /* ======================================== 标题相关 =========================================*/

    /**
     * 标题位置
     */
    public Builder setTitleGravity(int gravity) {
        mTitleGravity = gravity;
        return this;
    }

    /**
     * 文本标题
     */
    public Builder setTitleText(CharSequence text) {
        this.setTitleText(text, DEFAULT_TITLE_TEXT_SIZE);
        return this;
    }

    public Builder setTitleText(CharSequence text, int textSize) {
        this.setTitleText(text, textSize, DEFAULT_TEXT_COLOR);
        return this;
    }

    public Builder setTitleText(CharSequence text, int textSize, @ColorInt int textColor) {
        this.setTitleText(
                new Option.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .build()
        );
        return this;
    }

    public Builder setTitleText(Option option) {
        mTitleTextOp = option;
        return this;
    }

    /**
     * 图片标题
     */
    public Builder setTitleImage(@DrawableRes int drawableRes) {
        this.setTitleImage(drawableRes, INVALIDATE, INVALIDATE);
        return this;
    }

    public Builder setTitleImage(@DrawableRes int drawableRes, int width, int height) {
        this.setTitleImage(
                new Option.Builder()
                        .setDrawableResId(drawableRes)
                        .setWidth(width)
                        .setHeight(height)
                        .build()
        );
        return this;
    }

    public Builder setTitleImage(Option option) {
        mTitleImageOp = option;
        return this;
    }

    /**
     * 自定义标题
     */
    public Builder setCustomTitle(View titleView) {
        mCustomTitleView = titleView;
        return this;
    }

    /* ======================================== 菜单相关 =========================================*/
    public Builder setItemHorizontalInterval(/*dp*/int horizontalInterval) {
        mItemHorizontalInterval = horizontalInterval;
        return this;
    }

    /* ======================================== 左部菜单相关 =========================================*/

    public Builder addBackIcon(@DrawableRes int resId) {
        addLeftIcon(resId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
        return this;
    }

    /**
     * 左部文本
     */
    public Builder addLeftText(CharSequence text, final View.OnClickListener listener) {
        this.addLeftText(text, DEFAULT_MENU_TEXT_SIZE, listener);
        return this;
    }

    public Builder addLeftText(CharSequence text, /*sp*/int textSize, View.OnClickListener listener) {
        this.addLeftText(text, textSize, DEFAULT_TEXT_COLOR, listener);
        return this;
    }

    public Builder addLeftText(CharSequence text,/*sp*/int textSize, @ColorInt int textColor, View.OnClickListener listener) {
        this.addLeftText(
                new Option.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setListener(listener)
                        .build()
        );
        return this;
    }

    public Builder addLeftText(Option option) {
        if (null == mMenuLeftOps) {
            mMenuLeftOps = new ArrayList<>();
        }
        mMenuLeftOps.add(option);
        return this;
    }

    /**
     * 左部图标
     */
    public Builder addLeftIcon(@DrawableRes int drawableRes, final View.OnClickListener listener) {
        this.addLeftIcon(drawableRes, INVALIDATE, INVALIDATE, listener);
        return this;
    }

    public Builder addLeftIcon(@DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, View.OnClickListener listener) {
        this.addLeftIcon(
                new Option.Builder()
                        .setDrawableResId(drawableRes)
                        .setWidth(width)
                        .setHeight(height)
                        .setListener(listener)
                        .build()
        );
        return this;
    }

    public Builder addLeftIcon(Option option) {
        if (null == mMenuLeftOps) {
            mMenuLeftOps = new ArrayList<>();
        }
        mMenuLeftOps.add(option);
        return this;
    }

    /* ======================================== 右部菜单相关 =========================================*/

    /**
     * 右部文本
     */
    public Builder addRightText(CharSequence text, final View.OnClickListener listener) {
        this.addRightText(text, DEFAULT_MENU_TEXT_SIZE, listener);
        return this;
    }

    public Builder addRightText(CharSequence text, /*sp*/int textSize, View.OnClickListener listener) {
        this.addRightText(text, textSize, DEFAULT_TEXT_COLOR, listener);
        return this;
    }

    public Builder addRightText(CharSequence text,/*sp*/int textSize, @ColorInt int textColor, View.OnClickListener listener) {
        this.addRightText(
                new Option.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setListener(listener)
                        .build()
        );
        return this;
    }

    public Builder addRightText(Option option) {
        if (null == mMenuRightOps) {
            mMenuRightOps = new ArrayList<>();
        }
        mMenuRightOps.add(option);
        return this;
    }

    /**
     * 右部图标
     */
    public Builder addRightIcon(@DrawableRes int drawableRes, final View.OnClickListener listener) {
        this.addRightIcon(drawableRes, INVALIDATE, INVALIDATE, listener);
        return this;
    }

    public Builder addRightIcon(@DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, View.OnClickListener listener) {
        this.addRightIcon(
                new Option.Builder()
                        .setDrawableResId(drawableRes)
                        .setWidth(width)
                        .setHeight(height)
                        .setListener(listener)
                        .build()
        );
        return this;
    }

    public Builder addRightIcon(Option option) {
        if (null == mMenuRightOps) {
            mMenuRightOps = new ArrayList<>();
        }
        mMenuRightOps.add(option);
        return this;
    }

    /**
     * Inject U set field, and return a instance of SToolbar.
     */
    public SToolbar apply() {
        final SToolbar toolbar = new SToolbar(mContext);
        completion(toolbar);
        // 等待 View 的 performTraversal 完成
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                adjustLayout(toolbar);
            }
        });
        return toolbar;
    }

    private void completion(SToolbar toolbar) {
        // 1. Set layout params associated with the toolbar.
        toolbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // 2. Set background associated with the toolbar.
        if (INVALIDATE != mBgColor) {
            toolbar.setBackgroundColor(mBgColor);
        }
        if (INVALIDATE != mBgDrawableResId) {
            toolbar.setBackgroundDrawableRes(mBgDrawableResId);
        }
        // 3. Set title associated with the toolbar.
        toolbar.setTitleGravity(mTitleGravity);
        if (null != mTitleTextOp) {
            toolbar.setTitleText(
                    isCustomOption(mTitleTextOp) ? mTitleTextOp : new Option.Builder(mTitleTextOp)
                            .setPaddingLeft(mItemHorizontalInterval)
                            .setPaddingRight(mItemHorizontalInterval)
                            .build()
            );
        }
        if (null != mTitleImageOp) {
            toolbar.setTitleImage(
                    isCustomOption(mTitleImageOp) ? mTitleImageOp : new Option.Builder(mTitleImageOp)
                            .setPaddingLeft(mItemHorizontalInterval)
                            .setPaddingRight(mItemHorizontalInterval)
                            .build()
            );
        }
        if (null != mCustomTitleView) {
            toolbar.setCustomTitle(mCustomTitleView);
        }
        // 4. Add left menu item associated with the toolbar.
        if (isNotEmpty(mMenuLeftOps)) {
            for (Option leftOp : mMenuLeftOps) {
                if (isTextOption(leftOp)) {
                    toolbar.addLeftText(
                            isCustomOption(leftOp) ? leftOp : new Option.Builder(leftOp)
                                    .setPaddingLeft(mItemHorizontalInterval)
                                    .build()
                    );
                } else {
                    toolbar.addLeftIcon(
                            isCustomOption(leftOp) ? leftOp : new Option.Builder(leftOp)
                                    .setPaddingLeft(mItemHorizontalInterval)
                                    .build()
                    );
                }
            }
        }
        // 5. Add right menu item associated with the toolbar.
        if (isNotEmpty(mMenuRightOps)) {
            for (Option rightOp : mMenuRightOps) {
                if (isTextOption(rightOp)) {
                    toolbar.addRightText(
                            isCustomOption(rightOp) ? rightOp : new Option.Builder(rightOp)
                                    .setPaddingRight(mItemHorizontalInterval)
                                    .build()
                    );
                } else {
                    toolbar.addRightIcon(
                            isCustomOption(rightOp) ? rightOp : new Option.Builder(rightOp)
                                    .setPaddingRight(mItemHorizontalInterval)
                                    .build()
                    );
                }
            }
        }
        // 6. Adjust Transparent status bar.
        AppBarHelper.with(mContext).setStatusBarStyle(mStyle).apply();
        if (Style.TRANSLUCENCE == mStyle || Style.TRANSPARENT == mStyle) {
            toolbar.setAdjustToTransparentStatusBar(true);
        }
        // 7. Add to container.
        mContentParent.addView(toolbar, 0);
    }

    private void adjustLayout(SToolbar toolbar) {
        if (null != mContentParent && !(mContentParent instanceof LinearLayout)) {
            // 将我们的主体布局移动到 Toolbar 的下方
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                    mContentParent.getChildAt(1).getLayoutParams();
            params.topMargin += toolbar.getHeight();
            mContentParent.getChildAt(1).setLayoutParams(params);
        }
    }

    /**
     * 判断是否是用户传入的 Option
     */
    private boolean isCustomOption(Option option) {
        return 0 != option.paddingLeft || 0 != option.paddingRight;
    }

    /**
     * 是否为文本类型的 Option
     */
    private boolean isTextOption(Option option) {
        return INVALIDATE == option.drawableResId;
    }
}
