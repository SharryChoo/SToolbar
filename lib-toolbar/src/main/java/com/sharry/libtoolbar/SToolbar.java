package com.sharry.libtoolbar;

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

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import static com.sharry.libtoolbar.Option.DEFAULT_MENU_TEXT_SIZE;
import static com.sharry.libtoolbar.Option.DEFAULT_TEXT_COLOR;
import static com.sharry.libtoolbar.Option.DEFAULT_TITLE_TEXT_SIZE;
import static com.sharry.libtoolbar.Option.INVALIDATE;
import static com.sharry.libtoolbar.Utils.dp2px;
import static com.sharry.libtoolbar.Utils.getActionBarHeight;
import static com.sharry.libtoolbar.Utils.getStatusBarHeight;
import static com.sharry.libtoolbar.Utils.isLollipop;
import static com.sharry.libtoolbar.Utils.px2dp;

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

    static final int DEFAULT_INTERVAL = 5;
    private int mTitleTextSize = DEFAULT_TITLE_TEXT_SIZE;
    private int mTitleTextColor = DEFAULT_TEXT_COLOR;
    private int mMenuTextSize = DEFAULT_MENU_TEXT_SIZE;
    private int mMenuTextColor = DEFAULT_TEXT_COLOR;
    private int mItemHorizontalInterval;                    // Default padding will be using when create View.
    private int mMinimumHeight;                             // Minimum Toolbar height.

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
        if (null != array.getString(R.styleable.SToolbar_titleText)) {
            setTitleText(array.getString(R.styleable.SToolbar_titleText), mTitleTextSize, mTitleTextColor);
        }
        if (View.NO_ID != array.getResourceId(R.styleable.SToolbar_titleImage, View.NO_ID)) {
            setTitleImage(array.getResourceId(R.styleable.SToolbar_titleImage, View.NO_ID));
        }
        // 添加左部菜单
        if (View.NO_ID != array.getResourceId(R.styleable.SToolbar_menuLeftImage, View.NO_ID)) {
            addLeftIcon(array.getResourceId(R.styleable.SToolbar_menuLeftImage, View.NO_ID), null);
        }
        if (null != array.getString(R.styleable.SToolbar_menuLeftText)) {
            addLeftText(array.getString(R.styleable.SToolbar_menuLeftText), mMenuTextSize, mMenuTextColor, null);
        }
        // 添加右部菜单
        if (null != array.getString(R.styleable.SToolbar_menuRightText)) {
            addRightText(array.getString(R.styleable.SToolbar_menuRightText), mMenuTextSize, mMenuTextColor, null);
        }
        if (View.NO_ID != array.getResourceId(R.styleable.SToolbar_menuRightImage, View.NO_ID)) {
            addRightIcon(array.getResourceId(R.styleable.SToolbar_menuRightImage, View.NO_ID), null);
        }
        array.recycle();
    }

    private void initArgs(Context context, TypedArray array) {
        mMinimumHeight = array.getDimensionPixelSize(R.styleable.SToolbar_minHeight, getActionBarHeight(context));
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

    /*=========================================  背景色与沉浸式状态栏 ======================================*/

    /**
     * Set the view adjust to fit status bar.
     *
     * @param adjust if true is adjust transparent status bar.
     */
    public void setAdjustToTransparentStatusBar(boolean adjust) {
        if (adjust && isLollipop()) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                // Set the layout parameters associated with this view.
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setLayoutParams(params);
            }
            // Setup padding.
            setPadding(getPaddingLeft(), getPaddingTop() + getStatusBarHeight(getContext()),
                    getPaddingRight(), getPaddingBottom());
        }
    }

    /**
     * Sets the background color to a given resource. The colorResID should refer to
     * a color int.
     */
    public void setBackgroundColorRes(@ColorRes int colorRes) {
        setBackgroundColor(ContextCompat.getColor(getContext(), colorRes));
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
     * Gravity for the title associated with these LayoutParams.
     *
     * @see android.view.Gravity
     */
    public void setTitleGravity(int gravity) {
        LayoutParams params = (LayoutParams) mCenterContainer.getLayoutParams();
        params.gravity = gravity;
        mCenterContainer.setLayoutParams(params);
    }

    /**
     * Set text title
     */
    public void setTitleText(@StringRes int stringResId) {
        this.setTitleText(getResources().getText(stringResId));
    }

    public void setTitleText(CharSequence text) {
        this.setTitleText(text, mTitleTextSize);
    }

    public void setTitleText(CharSequence text, int textSize) {
        this.setTitleText(text, textSize, mTitleTextColor);
    }

    public void setTitleText(CharSequence text, int textSize, @ColorInt int textColor) {
        this.setTitleText(
                new Option.Builder()
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
        complementTextView(getTitleText(), option);
    }

    /**
     * Set image title
     */
    public void setTitleImage(@DrawableRes int resId) {
        this.setTitleImage(resId, INVALIDATE, INVALIDATE);
    }

    public void setTitleImage(@DrawableRes int resId, int width, int height) {
        this.setTitleImage(
                new Option.Builder()
                        .setDrawableResId(resId)
                        .setWidth(width)
                        .setHeight(height)
                        .setPaddingLeft(mItemHorizontalInterval)
                        .setPaddingRight(mItemHorizontalInterval)
                        .build()
        );
    }

    public void setTitleImage(Option option) {
        ensureImage(option);
        complementImageView(getTitleImage(), option);
    }

    /**
     * Add custom title
     */
    public void setCustomTitle(@NonNull View titleView) {
        mCenterContainer.addView(titleView);
    }

    /**
     * Get text title.
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
     * Get image title.
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
     * Add left menu text item.
     */
    public void addLeftText(CharSequence text, OnClickListener listener) {
        this.addLeftText(text, mMenuTextSize, listener);
    }

    public void addLeftText(CharSequence text, /*sp*/int textSize, OnClickListener listener) {
        this.addLeftText(text, textSize, mMenuTextColor, listener);
    }

    public void addLeftText(CharSequence text, /*sp*/int textSize, @ColorInt int textColor, OnClickListener listener) {
        this.addLeftText(
                new Option.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setPaddingLeft(mItemHorizontalInterval)
                        .setListener(listener)
                        .build()
        );
    }

    public void addLeftText(Option option) {
        ensureText(option);
        TextView textView = createTextView();
        complementTextView(textView, option);
        addLeftView(textView);
    }

    /**
     * Add left menu image item.
     */
    public void addLeftIcon(@DrawableRes int drawableRes, OnClickListener listener) {
        this.addLeftIcon(drawableRes, INVALIDATE, INVALIDATE, listener);
    }

    public void addLeftIcon(@DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        addLeftIcon(
                new Option.Builder()
                        .setWidth(width)
                        .setHeight(height)
                        .setDrawableResId(drawableRes)
                        .setPaddingLeft(mItemHorizontalInterval)
                        .setListener(listener)
                        .build()
        );
    }

    public void addLeftIcon(Option option) {
        ensureImage(option);
        ImageView imageView = createImageView();
        complementImageView(imageView, option);
        addLeftView(imageView);
    }

    /**
     * Add left menu custom item.
     */
    public void addLeftView(View view) {
        mLeftMenuContainer.addView(view);
    }

    /* ========================================== 右部菜单 ====================================================*/

    /**
     * Add right menu text item.
     */
    public void addRightText(CharSequence text, @Nullable OnClickListener listener) {
        this.addRightText(text, mMenuTextSize, listener);
    }

    public void addRightText(CharSequence text, /*sp*/int textSize, @Nullable OnClickListener listener) {
        this.addRightText(text, textSize, mMenuTextColor, listener);
    }

    public void addRightText(CharSequence text, /*sp*/int textSize, @ColorInt int textColor, @Nullable OnClickListener listener) {
        this.addRightText(
                new Option.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setPaddingRight(mItemHorizontalInterval)
                        .setListener(listener)
                        .build()
        );
    }

    public void addRightText(Option option) {
        ensureText(option);
        TextView textView = createTextView();
        complementTextView(textView, option);
        addRightView(textView);
    }

    /**
     * Add right menu image item.
     */
    public void addRightIcon(@DrawableRes int drawableRes, OnClickListener listener) {
        this.addRightIcon(drawableRes, INVALIDATE, INVALIDATE, listener);
    }

    public void addRightIcon(@DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        addRightIcon(
                new Option.Builder()
                        .setWidth(width)
                        .setHeight(height)
                        .setPaddingRight(mItemHorizontalInterval)
                        .setDrawableResId(drawableRes)
                        .setListener(listener)
                        .build()
        );
    }

    public void addRightIcon(Option option) {
        ensureImage(option);
        ImageView imageView = createImageView();
        complementImageView(imageView, option);
        addRightView(imageView);
    }

    /**
     * Add right menu custom item.
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
        if (getChildCount() == 3) {
            return;
        }
        super.addView(child, index, params);
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
        // Config some fields.
        textView.setMaxEms(8);
        textView.setLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
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
    private void complementTextView(TextView textView, Option option) {
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
        // Set OnClickListener.
        if (null != option.listener) {
            textView.setOnClickListener(option.listener);
        }
    }

    /**
     * Set fields from option associated with the image view.
     */
    private void complementImageView(ImageView imageView, Option option) {
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
        if (TextUtils.isEmpty(option.text)) {
            throw new IllegalArgumentException("Please ensureText option.text not empty.");
        }
    }

    private void ensureImage(Option option) {
        if (null == option) {
            throw new NullPointerException("Please ensureText parameter option nonnull.");
        }
        if (INVALIDATE == option.drawableResId) {
            throw new IllegalArgumentException("Please ensureText option.text not empty.");
        }
    }

}
