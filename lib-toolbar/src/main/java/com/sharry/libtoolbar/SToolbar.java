package com.sharry.libtoolbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharry.libtoolbar.options.ImageOption;
import com.sharry.libtoolbar.options.Option;
import com.sharry.libtoolbar.options.TextOption;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import static androidx.annotation.Dimension.DP;
import static androidx.annotation.Dimension.SP;
import static com.sharry.libtoolbar.Utils.dp2px;
import static com.sharry.libtoolbar.Utils.getStatusBarHeight;
import static com.sharry.libtoolbar.Utils.isLollipop;
import static com.sharry.libtoolbar.Utils.px2dp;
import static com.sharry.libtoolbar.options.Option.INVALIDATE;

/**
 * SToolbar 的最小高度为系统 ActionBar 的高度
 * <p>
 * 1. 可以直接在 Xml 文件中直接使用
 * 2. 可以使用 Builder 动态的植入 {@link Builder}
 *
 * @author Sharry <a href="frankchoochina@gmail.com">Contact me.</a>
 * @version 3.2
 * @since 2018/8/27 23:20
 */
public class SToolbar extends Toolbar {

    /**
     * Get Builder instance
     * If U want create CommonToolbar dynamic, U should invoke this method.
     */
    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    /**
     * Get Builder instance
     * If U want create CommonToolbar dynamic, U should invoke this method.
     */
    public static Builder Builder(View contentView) {
        return new Builder(contentView);
    }

    private static final int LOCKED_CHILDREN_COUT = 3;
    @Dimension(unit = DP)
    static final int DEFAULT_INTERVAL = 5;
    private int mTitleTextSize = TextOption.DEFAULT_TITLE_TEXT_SIZE;
    private int mTitleTextColor = TextOption.DEFAULT_TEXT_COLOR;
    private int mMenuTextSize = TextOption.DEFAULT_MENU_TEXT_SIZE;
    private int mMenuTextColor = TextOption.DEFAULT_TEXT_COLOR;
    private int mMinimumHeight = Option.INVALIDATE;                // Minimum Toolbar height.
    private int mItemHorizontalInterval;                    // Default padding will be using when create View.

    // Toolbar support container.
    private LinearLayout mLeftMenuContainer;
    private LinearLayout mCenterContainer;
    private LinearLayout mRightMenuContainer;

    // 提供的标题(文本/图片/自定义)
    private TextView mTitleText;
    private ImageView mTitleImage;

    public SToolbar(Context context) {
        this(context, null);
    }

    public SToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SToolbar);
        // 初始化参数和视图
        initArgs(context, array);
        initViews(context);
        // 设置沉浸式状态栏
        switch (array.getInt(R.styleable.SToolbar_statusBarStyle, Style.DEFAULT.getVal())) {
            case 0:
                setStatusBarStyle(Style.TRANSPARENT);
                break;
            case 1:
                setStatusBarStyle(Style.TRANSLUCENCE);
                break;
            case 2:
                setStatusBarStyle(Style.HIDE);
                break;
            default:
                break;
        }
        // 添加菜单选项
        switch (array.getInt(R.styleable.SToolbar_titleGravity, -1)) {
            case 0:
                setTitleGravity(Gravity.LEFT | Gravity.TOP);
                break;
            case 1:
                setTitleGravity(Gravity.RIGHT | Gravity.TOP);
                break;
            default:
                setTitleGravity(Gravity.CENTER | Gravity.TOP);
                break;
        }
        // 文本标题
        String titleText = array.getString(R.styleable.SToolbar_titleText);
        setTitleText(TextUtils.isEmpty(titleText) ? "" : titleText, mTitleTextSize, mTitleTextColor);
        // 图片标题
        int titleImageResId = array.getResourceId(R.styleable.SToolbar_titleImage, View.NO_ID);
        if (View.NO_ID != titleImageResId) {
            setTitleImage(titleImageResId);
        }
        // 添加左部菜单
        int backIconResId = array.getResourceId(R.styleable.SToolbar_backIcon, View.NO_ID);
        if (View.NO_ID != backIconResId) {
            addBackIcon(backIconResId);
        }
        int leftMenuIconResId = array.getResourceId(R.styleable.SToolbar_menuLeftIcon, View.NO_ID);
        if (View.NO_ID != leftMenuIconResId) {
            addLeftMenu(
                    new ImageOption.Builder()
                            .setDrawableResId(leftMenuIconResId)
                            .setPaddingLeft(mItemHorizontalInterval)
                            .build()
            );
        }
        String leftMenuText = array.getString(R.styleable.SToolbar_menuLeftText);
        if (null != leftMenuText) {
            addLeftMenu(
                    new TextOption.Builder()
                            .setText(leftMenuText)
                            .setTextSize(mMenuTextSize)
                            .setTextColor(mMenuTextColor)
                            .setPaddingLeft(mItemHorizontalInterval)
                            .build()
            );
        }
        // 添加右部菜单
        String rightMenuText = array.getString(R.styleable.SToolbar_menuRightText);
        if (null != rightMenuText) {
            addRightMenu(
                    new TextOption.Builder()
                            .setText(rightMenuText)
                            .setTextSize(mMenuTextSize)
                            .setTextColor(mMenuTextColor)
                            .setPaddingRight(mItemHorizontalInterval)
                            .build()
            );
        }
        int rightMenuIconResId = array.getResourceId(R.styleable.SToolbar_menuRightIcon, View.NO_ID);
        if (View.NO_ID != rightMenuIconResId) {
            addRightMenu(
                    new ImageOption.Builder()
                            .setDrawableResId(rightMenuIconResId)
                            .setPaddingRight(mItemHorizontalInterval)
                            .build()
            );
        }
        array.recycle();
    }

    private void initArgs(Context context, TypedArray array) {
        mMinimumHeight = array.getDimensionPixelSize(R.styleable.SToolbar_minHeight, dp2px(context, 56));
        mItemHorizontalInterval = px2dp(context, array.getDimensionPixelSize(R.styleable.SToolbar_itemHorizontalInterval,
                dp2px(context, DEFAULT_INTERVAL)));
        mTitleTextColor = array.getColor(R.styleable.SToolbar_titleTextColor, mTitleTextColor);
        mTitleTextSize = px2dp(context, array.getDimensionPixelSize(R.styleable.SToolbar_titleTextSize,
                dp2px(context, mTitleTextSize)));
        mMenuTextSize = px2dp(context, array.getDimensionPixelSize(R.styleable.SToolbar_menuTextSize,
                dp2px(context, mMenuTextSize)));
        mMenuTextColor = array.getColor(R.styleable.SToolbar_menuTextColor, mMenuTextColor);
    }

    private void initViews(Context context) {
        // Set initialize layout params.
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        removeAllViews();
        // 1. Add left menu container associated with this toolbar.
        mLeftMenuContainer = new LinearLayout(context);
        LayoutParams leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mMinimumHeight);
        leftParams.gravity = Gravity.START | Gravity.TOP;
        mLeftMenuContainer.setLayoutParams(leftParams);
        mLeftMenuContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mLeftMenuContainer);
        // 2. Add right menu container associated with this toolbar.
        mRightMenuContainer = new LinearLayout(context);
        LayoutParams rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mMinimumHeight);
        rightParams.gravity = Gravity.END | Gravity.TOP;
        mRightMenuContainer.setLayoutParams(rightParams);
        mRightMenuContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mRightMenuContainer);
        // 3. Add center item container associated with this toolbar.
        mCenterContainer = new LinearLayout(context);
        LayoutParams centerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        centerParams.gravity = Gravity.CENTER | Gravity.TOP;
        mCenterContainer.setMinimumHeight(mMinimumHeight);
        mCenterContainer.setPadding(mItemHorizontalInterval, 0, mItemHorizontalInterval, 0);
        mCenterContainer.setLayoutParams(centerParams);
        mCenterContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mCenterContainer);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        // Lock height always is WRAP_CONTENT.
        if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        super.setLayoutParams(params);
    }

    /*=========================================  背景色与沉浸式状态栏 ======================================*/

    /**
     * Set app bar style associated with this Activity.
     */
    public void setStatusBarStyle(final Style style) {
        AppBarHelper.with(getContext()).setStatusBarStyle(style).apply();
        if (isLollipop() && (style == Style.TRANSPARENT || style == Style.TRANSLUCENCE)) {
            // Setup padding.
            setPadding(getPaddingLeft(), getPaddingTop() + getStatusBarHeight(getContext()),
                    getPaddingRight(), getPaddingBottom());
        }
    }

    /**
     * Sets the background color to a given resource. The colorResId should refer to
     * a color int.
     */
    public void setBackgroundColorRes(@ColorRes int colorResId) {
        setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));
    }

    /**
     * Set the background to a given resource. The resource should refer to
     * a Drawable object or 0 to remove the background.
     */
    public void setBackgroundDrawableRes(@DrawableRes int drawableRes) {
        setBackgroundResource(drawableRes);
    }

    /*========================================= 标题部分 ==================================================*/

    /**
     * Set gravity for the title associated with these LayoutParams.
     *
     * @see Gravity
     */
    public void setTitleGravity(int gravity) {
        LayoutParams params = (LayoutParams) mCenterContainer.getLayoutParams();
        params.gravity = gravity;
        mCenterContainer.setLayoutParams(params);
    }

    /**
     * Set text associated with this toolbar title.
     */
    public void setTitleText(@StringRes int stringResId) {
        this.setTitleText(getResources().getText(stringResId));
    }

    public void setTitleText(CharSequence text) {
        this.setTitleText(text, mTitleTextSize);
    }

    public void setTitleText(CharSequence text, @Dimension(unit = SP) int textSize) {
        this.setTitleText(text, textSize, mTitleTextColor);
    }

    public void setTitleText(CharSequence text, @Dimension(unit = SP) int textSize, @ColorInt int textColor) {
        this.setTitleText(
                new TextOption.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setPaddingLeft(mItemHorizontalInterval)
                        .setPaddingRight(mItemHorizontalInterval)
                        .build()
        );
    }

    public void setTitleText(Option option) {
        ensureText(option);
        option = isUserSetOptionPadding(option) ? option : new TextOption.Builder(option)
                .setPaddingLeft(mItemHorizontalInterval)
                .setPaddingRight(mItemHorizontalInterval)
                .build();
        complementTextView(getTitleText(), (TextOption) option);
    }

    /**
     * Set image associated with this toolbar title.
     */
    public void setTitleImage(@DrawableRes int resId) {
        this.setTitleImage(resId, INVALIDATE, INVALIDATE);
    }

    public void setTitleImage(@DrawableRes int resId, @Dimension(unit = DP) int width,
                              @Dimension(unit = DP) int height) {
        this.setTitleImage(
                new ImageOption.Builder()
                        .setDrawableResId(resId)
                        .setWidth(width)
                        .setHeight(height)
                        .build()
        );
    }

    public void setTitleImage(Option option) {
        ensureImage(option);
        option = isUserSetOptionPadding(option) ? option : new ImageOption.Builder(option)
                .setPaddingLeft(mItemHorizontalInterval)
                .setPaddingRight(mItemHorizontalInterval)
                .build();
        complementImageView(getTitleImage(), (ImageOption) option);
    }

    /**
     * Add custom view associated with this toolbar title.
     */
    public void addCustomTitle(@NonNull View titleView) {
        mCenterContainer.addView(titleView);
    }

    /**
     * Get text title associated with this toolbar.
     */
    public TextView getTitleText() {
        if (null == mTitleText) {
            mTitleText = createTextView();
            // Add to center container.
            mCenterContainer.addView(mTitleText);
        }
        return mTitleText;
    }

    /**
     * Get image title associated with this toolbar.
     */
    public ImageView getTitleImage() {
        if (null == mTitleImage) {
            mTitleImage = createImageView();
            mCenterContainer.addView(mTitleImage);
        }
        return mTitleImage;
    }


    /* ========================================== 左部菜单 ====================================================*/

    /**
     * Add back icon associated with this toolbar left menu.
     */
    public void addBackIcon(@DrawableRes int drawableRes) {
        this.addLeftMenu(
                new ImageOption.Builder()
                        .setDrawableResId(drawableRes)
                        .setListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (getContext() instanceof Activity) {
                                    ((Activity) getContext()).onBackPressed();
                                }
                            }
                        })
                        .build()
        );
    }

    /**
     * Add text/image sub item associated with this toolbar left menu.
     */
    public void addLeftMenu(Option option) {
        View leftMenuView = null;
        if (option instanceof TextOption) {
            leftMenuView = createTextView();
            complementTextView((TextView) leftMenuView, isUserSetOptionPadding(option) ?
                    (TextOption) option : new TextOption.Builder(option)
                    .setPaddingLeft(mItemHorizontalInterval)
                    .build()
            );
        } else if (option instanceof ImageOption) {
            leftMenuView = createImageView();
            complementImageView((ImageView) leftMenuView, isUserSetOptionPadding(option) ?
                    (ImageOption) option : new ImageOption.Builder(option)
                    .setPaddingLeft(mItemHorizontalInterval)
                    .build()
            );
        }
        if (null != leftMenuView) {
            addLeftView(leftMenuView);
        }
    }

    /**
     * Add custom sub item associated with this toolbar left menu.
     */
    public void addLeftView(View view) {
        mLeftMenuContainer.addView(view);
    }


    /* ========================================== 右部菜单 ====================================================*/

    /**
     * Add text/image sub item associated with this toolbar right menu.
     */
    public void addRightMenu(Option option) {
        View rightMenuView = null;
        if (option instanceof TextOption) {
            rightMenuView = createTextView();
            complementTextView((TextView) rightMenuView, isUserSetOptionPadding(option) ?
                    (TextOption) option : new TextOption.Builder(option)
                    .setPaddingRight(mItemHorizontalInterval)
                    .build()
            );
        } else if (option instanceof ImageOption) {
            rightMenuView = createImageView();
            complementImageView((ImageView) rightMenuView, isUserSetOptionPadding(option) ?
                    (ImageOption) option : new ImageOption.Builder(option)
                    .setPaddingRight(mItemHorizontalInterval)
                    .build()
            );
        }
        if (null != rightMenuView) {
            addRightView(rightMenuView);
        }
    }

    /**
     * Add custom sub item associated with this toolbar right menu.
     */
    public void addRightView(View view) {
        mRightMenuContainer.addView(view);
    }

    /**
     * Get view index of left menu.
     */
    public <T extends View> T getLeftMenuView(int index) {
        return (T) mLeftMenuContainer.getChildAt(index);
    }

    /**
     * Get view index of right menu.
     */
    public <T extends View> T getRightMenuView(int index) {
        return (T) mRightMenuContainer.getChildAt(index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (LOCKED_CHILDREN_COUT == getChildCount()) {
            return;
        }
        super.addView(child, index, params);
    }

    /**
     * Set item horizontal interval associated with this toolbar.
     */
    void setItemHorizontalInterval(int itemHorizontalInterval) {
        mItemHorizontalInterval = itemHorizontalInterval;
    }

    /**
     * Get TextView instance.
     */
    private TextView createTextView() {
        TextView textView = new TextView(getContext());
        // Set params for the view.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * Get ImageView instance.
     */
    private ImageView createImageView() {
        // Create ImageView instance.
        ImageView imageView = new ImageView(getContext());
        // Set default layout params.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
        // Set scale type associated with this imageView.
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    /**
     * Set fields from option associated with the text view.
     */
    private void complementTextView(TextView textView, TextOption option) {
        // Set the layout parameters associated with this textView.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                INVALIDATE == option.width ? ViewGroup.LayoutParams.WRAP_CONTENT :
                        dp2px(getContext(), option.width),
                INVALIDATE == option.height ? ViewGroup.LayoutParams.MATCH_PARENT :
                        dp2px(getContext(), option.height));
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        // Set the padding associated with this textView.
        textView.setPadding(
                dp2px(getContext(), option.paddingLeft),
                dp2px(getContext(), option.paddingTop),
                dp2px(getContext(), option.paddingRight),
                dp2px(getContext(), option.paddingBottom)
        );
        // Set some fields associated with this textView.
        textView.setText(option.text);
        textView.setTextColor(option.textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, option.textSize);
        textView.setMaxEms(option.maxEms);
        textView.setLines(option.lines);
        textView.setEllipsize(option.ellipsize);
        // Set OnClickListener.
        if (null != option.listener) {
            textView.setOnClickListener(option.listener);
        }
    }

    /**
     * Set fields from option associated with the image view.
     */
    private void complementImageView(ImageView imageView, ImageOption option) {
        // Set the layout params associated with this imageView.
        int destWidth = INVALIDATE == option.width ? ViewGroup.LayoutParams.WRAP_CONTENT :
                dp2px(getContext(), option.width + option.paddingLeft + option.paddingRight);
        int destHeight = INVALIDATE == option.height ? ViewGroup.LayoutParams.WRAP_CONTENT :
                dp2px(getContext(), option.height + option.paddingTop + option.paddingTop);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        if (null == params) {
            params = new LinearLayout.LayoutParams(destWidth, destHeight);
        } else {
            params.width = destWidth;
            params.height = destHeight;
        }
        imageView.setLayoutParams(params);
        // Set the padding associated with this imageView.
        imageView.setPadding(
                dp2px(getContext(), option.paddingLeft),
                dp2px(getContext(), option.paddingTop),
                dp2px(getContext(), option.paddingRight),
                dp2px(getContext(), option.paddingBottom)
        );
        // Set some fields associated with this imageView.
        imageView.setImageResource(option.drawableResId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // Set OnClickListener
        if (null != option.listener) {
            imageView.setOnClickListener(option.listener);
        }
    }

    private void ensureText(Option option) {
        if (null == option) {
            throw new NullPointerException("Please ensureText parameter option nonnull.");
        }
        if (option instanceof TextOption) {
            if (null == ((TextOption) option).text) {
                throw new NullPointerException("Please ensure TextOption.text nonnull.");
            }
        } else {
            throw new IllegalArgumentException("Please option instance of TextOption, now is: "
                    + option.getClass().getName());
        }
    }

    private void ensureImage(Option option) {
        if (null == option) {
            throw new NullPointerException("Please ensureText parameter option nonnull.");
        }
        if (option instanceof ImageOption) {
            if (INVALIDATE == ((ImageOption) option).drawableResId) {
                throw new IllegalArgumentException("Please ensure ImageOption.drawableResId is valuable.");
            }
        } else {
            throw new IllegalArgumentException("Please option instance of ImageOption, now is: "
                    + option.getClass().getName());
        }
    }

    /**
     * @return if true is use user custom padding values, false is use default padding values
     */
    private boolean isUserSetOptionPadding(Option option) {
        return 0 != option.paddingLeft || 0 != option.paddingRight;
    }

}
