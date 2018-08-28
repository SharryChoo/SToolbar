package com.frank.libtoolbar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import static com.frank.libtoolbar.Utils.getActionBarHeight;
import static com.frank.libtoolbar.Utils.getStatusBarHeight;

/**
 * Build common toolbar more easy.
 *
 * @author Frank <a href="frankchoochina@gmail.com">Contact me.</a>
 * @version 1.0
 * @since 2018/8/27 23:36
 */
public class Builder {

    private Context mContext;
    private CommonToolbar mToolbar;
    private ViewGroup mContentParent;
    private ViewGroup mContentView;
    private Style mStyle = Style.DEFAULT;

    /**
     * 给 Activity 添加 Toolbar
     */
    Builder(Context context) {
        if (context instanceof Activity) {
            mContext = context;
            // 通过安卓源码中的id拿到mContentParent, 这个就是我们的setContentView的直接父容器
            mContentParent = ((Activity) mContext).findViewById(Window.ID_ANDROID_CONTENT);
            mToolbar = new CommonToolbar(mContext);
        } else {
            throw new IllegalArgumentException("Please ensure context is instanceof Activity.");
        }
    }

    /**
     * 给 View 添加 Toolbar, 确保传入的 View 为 LinearLayout
     */
    Builder(View contentView) {
        if (contentView instanceof LinearLayout) {
            mContext = new WeakReference<>(contentView.getContext()).get();
            mToolbar = new CommonToolbar(mContext);
            mContentView = (ViewGroup) contentView;
        } else {
            throw new IllegalArgumentException("GenericToolbar.Builder.Constructor --> " +
                    "传入的View不为LinearLayout, 无法将Toolbar放置正确的位置");
        }
    }

    /**
     * 背景色
     */
    public Builder setBackgroundColor(@ColorInt int color) {
        mToolbar.setBackgroundColor(color);
        return this;
    }

    public Builder setBackgroundColorRes(@ColorRes int colorRes) {
        mToolbar.setBackgroundColorRes(colorRes);
        return this;
    }

    public Builder setBackgroundDrawableRes(@DrawableRes int drawableRes) {
        mToolbar.setBackgroundDrawableRes(drawableRes);
        return this;
    }

    /**
     * 标题位置
     */
    public Builder setTitleGravity(int gravity) {
        mToolbar.setTitleGravity(gravity);
        return this;
    }

    /**
     * 文本标题
     */
    public Builder addTitleText(CharSequence text) {
        mToolbar.setTitle(text);
        return this;
    }

    public Builder addTitleText(CharSequence text, float textSize) {
        mToolbar.setTitle(text, textSize);
        return this;
    }

    public Builder addTitleText(CharSequence text, float textSize, @ColorInt int textColor) {
        mToolbar.setTitle(text, textSize, textColor);
        return this;
    }

    /**
     * 图片标题
     */
    public Builder addTitleImage(@DrawableRes int drawableRes) {
        mToolbar.setTitleImage(drawableRes);
        return this;
    }

    public Builder addTitleImage(@DrawableRes int iconRes, int width, int height) {
        mToolbar.setTitleImage(iconRes, width, height);
        return this;
    }

    public Builder addTitleImage(CommonToolbar.TitleImageLoader loader) {
        mToolbar.setTitleImage(loader);
        return this;
    }

    public Builder addTitleImage(int width, int height, CommonToolbar.TitleImageLoader loader) {
        mToolbar.setTitleImage(width, height, loader);
        return this;
    }

    /**
     * 自定义标题
     */
    public Builder addCustomTitle(View titleView) {
        mToolbar.addCustomTitle(titleView);
        return this;
    }

    public Builder addBackIcon(int IconRes) {
        addLeftIcon(0xBBBBBBB, IconRes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
        return this;
    }

    /**
     * 左部图标
     */
    public Builder addLeftIcon(int tag, @DrawableRes int drawableRes, final View.OnClickListener listener) {
        mToolbar.addLeftIcon(tag, drawableRes, listener);
        return this;
    }

    public Builder addLeftIcon(int tag, @DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, View.OnClickListener listener) {
        mToolbar.addLeftIcon(tag, drawableRes, width, height, listener);
        return this;
    }

    /**
     * 左部文本
     */
    public Builder addLeftText(int tag, CharSequence text, final View.OnClickListener listener) {
        mToolbar.addLeftText(tag, text, listener);
        return this;
    }

    public Builder addLeftText(int tag, CharSequence text, /*sp*/float textSize, View.OnClickListener listener) {
        mToolbar.addLeftText(tag, text, textSize, listener);
        return this;
    }

    public Builder addLeftText(int tag, CharSequence text,/*sp*/float textSize, @ColorInt int textColor, View.OnClickListener listener) {
        mToolbar.addLeftText(tag, text, textSize, textColor, listener);
        return this;
    }

    /**
     * 右部图标
     */
    public Builder addRightIcon(int tag, @DrawableRes int drawableRes, final View.OnClickListener listener) {
        mToolbar.addRightIcon(tag, drawableRes, listener);
        return this;
    }

    public Builder addRightIcon(int tag, @DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, View.OnClickListener listener) {
        mToolbar.addRightIcon(tag, drawableRes, width, height, listener);
        return this;
    }

    /**
     * 右部文本
     */
    public Builder addRightText(int tag, CharSequence text, final View.OnClickListener listener) {
        mToolbar.addRightText(tag, text, listener);
        return this;
    }

    public Builder addRightText(int tag, CharSequence text, /*sp*/float textSize, View.OnClickListener listener) {
        mToolbar.addRightText(tag, text, textSize, listener);
        return this;
    }

    public Builder addRightText(int tag, CharSequence text,/*sp*/float textSize, @ColorInt int textColor, View.OnClickListener listener) {
        mToolbar.addRightText(tag, text, textSize, textColor, listener);
        return this;
    }

    public Builder setStatusBarStyle(Style statusBarStyle) {
        if (mContext instanceof Activity) {
            AppBarHelper.with(mContext).setStatusBarStyle(statusBarStyle).apply();
        }
        mStyle = statusBarStyle;
        return this;
    }

    /**
     * 将Toolbar添加到当前Window的DecorView中
     * 调整当前Window中其他View的位置, 以适应Toolbar的插入
     */
    public CommonToolbar apply() {
        // 添加自定义标题的View
        mToolbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        if (mContentParent != null) {
            mContentParent.addView(mToolbar, 0);
        } else {
            mContentView.addView(mToolbar, 0);
        }
        // 防止用户使用 Builder 模式设置沉浸式状态栏无效
        mToolbar.setAdjustToTransparentStatusBar(isAdjustTransparentStatusBar(mStyle));
        // 等待 View 的 performTraversal 完成
        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                adjustLayout();
            }
        });
        return mToolbar;
    }

    private void adjustLayout() {
        if (mContentParent != null
                && !(mContentParent instanceof LinearLayout)) {
            // 将我们的主体布局移动到Toolbar的下方
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                    mContentParent.getChildAt(1).getLayoutParams();
            params.topMargin += getNeedMarginHeight();
            mContentParent.getChildAt(1).setLayoutParams(params);
        }
    }

    private int getNeedMarginHeight() {
        int toolbarCurHeight = mToolbar.getHeight();
        if (isAdjustTransparentStatusBar(mStyle)) {
            // 若设置了沉浸式状态栏
            // toolbar 的高度最小为 getStatusBarHeight() + getActionBarHeight()
            if (toolbarCurHeight < getStatusBarHeight(mContext) + getActionBarHeight(mContext)) {
                toolbarCurHeight = getStatusBarHeight(mContext) + getActionBarHeight(mContext);
            }
        }
        return toolbarCurHeight;
    }

    /**
     * 根据Style判断是否需要适应沉浸式状态栏
     */
    private boolean isAdjustTransparentStatusBar(Style style) {
        if (style == Style.TRANSLUCENCE || style == Style.TRANSPARENT) {
            return true;
        } else {
            return false;
        }
    }

}
