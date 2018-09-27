package com.sharry.libtoolbar;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.sharry.libtoolbar.options.ImageOption;
import com.sharry.libtoolbar.options.Option;
import com.sharry.libtoolbar.options.TextOption;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static androidx.annotation.Dimension.DP;
import static androidx.annotation.Dimension.SP;
import static com.sharry.libtoolbar.SToolbar.DEFAULT_INTERVAL;
import static com.sharry.libtoolbar.Utils.isNotEmpty;
import static com.sharry.libtoolbar.options.Option.INVALIDATE;

/**
 * Build SToolbar more easy.
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
     * Set interval associated with this toolbar sub item.
     */
    public Builder setItemHorizontalInterval(@Dimension(unit = DP) int horizontalInterval) {
        mItemHorizontalInterval = horizontalInterval;
        return this;
    }

    /**
     * Set style associated with bind activity status bar.
     */
    public Builder setStatusBarStyle(Style statusBarStyle) {
        mStyle = statusBarStyle;
        return this;
    }

    /**
     * Set the background color to a given resource. The colorResId should refer to
     * a color int.
     */
    public Builder setBackgroundColorRes(@ColorRes int colorResId) {
        mBgColor = ContextCompat.getColor(mContext, colorResId);
        return this;
    }

    /**
     * Set the background color associated with this toolbar.
     */
    public Builder setBackgroundColor(@ColorInt int color) {
        mBgColor = color;
        return this;
    }

    /**
     * Set the background to a given resource. The resource should refer to
     * a Drawable object or 0 to remove the background.
     */
    public Builder setBackgroundDrawableRes(@DrawableRes int drawableResId) {
        mBgDrawableResId = drawableResId;
        return this;
    }

    /**
     * Set gravity associated with this toolbar title.
     */
    public Builder setTitleGravity(int gravity) {
        mTitleGravity = gravity;
        return this;
    }

    /**
     * Set text associated with this toolbar title.
     */
    public Builder setTitleText(CharSequence text) {
        this.setTitleText(text, TextOption.DEFAULT_TITLE_TEXT_SIZE);
        return this;
    }

    public Builder setTitleText(CharSequence text, @Dimension(unit = SP) int textSize) {
        this.setTitleText(text, textSize, TextOption.DEFAULT_TEXT_COLOR);
        return this;
    }

    public Builder setTitleText(CharSequence text, @Dimension(unit = SP) int textSize, @ColorInt int textColor) {
        this.setTitleText(
                new TextOption.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .build()
        );
        return this;
    }

    public Builder setTitleText(@NonNull Option option) {
        mTitleTextOp = option;
        return this;
    }

    /**
     * Set image associated with this toolbar title.
     */
    public Builder setTitleImage(@DrawableRes int drawableRes) {
        this.setTitleImage(drawableRes, INVALIDATE, INVALIDATE);
        return this;
    }

    public Builder setTitleImage(@DrawableRes int drawableRes, int width, int height) {
        this.setTitleImage(
                new ImageOption.Builder()
                        .setDrawableResId(drawableRes)
                        .setWidth(width)
                        .setHeight(height)
                        .build()
        );
        return this;
    }

    public Builder setTitleImage(@NonNull Option option) {
        mTitleImageOp = option;
        return this;
    }

    /**
     * Set add custom view associated with this toolbar title.
     */
    public Builder addCustomTitle(View titleView) {
        mCustomTitleView = titleView;
        return this;
    }

    /**
     * Add back icon associated with this toolbar left menu.
     */
    public Builder addBackIcon(@DrawableRes int drawableRes) {
        return addLeftMenu(
                new ImageOption.Builder()
                        .setDrawableResId(drawableRes)
                        .setListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((Activity) mContext).finish();
                            }
                        })
                        .build()
        );
    }

    /**
     * Add sub item associated with this toolbar left menu.
     */
    public Builder addLeftMenu(@NonNull Option option) {
        if (null == mMenuLeftOps) {
            mMenuLeftOps = new ArrayList<>();
        }
        mMenuLeftOps.add(option);
        return this;
    }

    /**
     * Add sub item associated with this toolbar right menu.
     */
    public Builder addRightMenu(Option option) {
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

    /**
     * Inject data to toolbar.
     */
    private void completion(SToolbar toolbar) {
        // 1. Set layout params associated with the toolbar.
        toolbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // 2. Set arguments.
        toolbar.setItemHorizontalInterval(mItemHorizontalInterval);
        if (Style.DEFAULT != mStyle) {
            toolbar.setStatusBarStyle(mStyle);
        }
        if (INVALIDATE != mBgColor) {
            toolbar.setBackgroundColor(mBgColor);
        }
        if (INVALIDATE != mBgDrawableResId) {
            toolbar.setBackgroundDrawableRes(mBgDrawableResId);
        }
        // 3. Set title associated with the toolbar.
        toolbar.setTitleGravity(mTitleGravity);
        if (null != mTitleTextOp) {
            toolbar.setTitleText(mTitleTextOp);
        }
        if (null != mTitleImageOp) {
            toolbar.setTitleImage(mTitleImageOp);
        }
        if (null != mCustomTitleView) {
            toolbar.addCustomTitle(mCustomTitleView);
        }
        // 4. Add left menu item associated with the toolbar.
        if (isNotEmpty(mMenuLeftOps)) {
            for (Option leftOp : mMenuLeftOps) {
                toolbar.addLeftMenu(leftOp);
            }
        }
        // 5. Add right menu item associated with the toolbar.
        if (isNotEmpty(mMenuRightOps)) {
            for (Option rightOp : mMenuRightOps) {
                toolbar.addRightMenu(rightOp);
            }
        }
        // 6. Add to container.
        mContentParent.addView(toolbar, 0);
    }

    /**
     * Adjust origin content to comfort position.
     */
    private void adjustLayout(SToolbar toolbar) {
        if (null != mContentParent && !(mContentParent instanceof LinearLayout)) {
            // Move origin content under the SToolbar.
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                    mContentParent.getChildAt(1).getLayoutParams();
            params.topMargin += toolbar.getHeight();
            mContentParent.getChildAt(1).setLayoutParams(params);
        }
    }

}
