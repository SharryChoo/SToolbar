package com.sharry.libtoolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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

import static com.sharry.libtoolbar.Utils.dp2px;
import static com.sharry.libtoolbar.Utils.getActionBarHeight;
import static com.sharry.libtoolbar.Utils.getStatusBarHeight;
import static com.sharry.libtoolbar.Utils.isLollipop;
import static com.sharry.libtoolbar.Utils.px2dp;

/**
 * SToolbar 的最小高度为系统 ActionBar 的高度
 * <p>
 * 1. 可以直接在Xml文件中直接使用
 * 2. 可以使用 Builder 动态的植入 {@link Builder}
 *
 * @author Sharry <a href="frankchoochina@gmail.com">Contact me.</a>
 * @version 3.1
 * @since 2018/8/27 23:20
 */
public class SToolbar extends Toolbar {

    /**
     * ImageLoader interface.
     */
    public interface OnImageLoaderListener {
        void onImageLoader(Context context, ImageView titleImage);
    }

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

    /*
      Constants.
     */
    private final static int INVALIDATE_VALUE = -1;
    private final static int DEFAULT_TEXT_COLOR = Color.WHITE;   // Default color will be using when set text color.

    private float mTitleTextSize = 18f;
    private int mTitleTextColor = DEFAULT_TEXT_COLOR;
    private float mMenuTextSize = 15f;
    private int mMenuTextColor = DEFAULT_TEXT_COLOR;
    private int mMenuHorizontalPadding;                    // Default padding will be using when create View.
    private int mMinimumHeight;                            // Minimum Toolbar height.

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
        // 获取相关变量初始化视图
        mMinimumHeight = array.getDimensionPixelSize(R.styleable.SToolbar_minHeight, getActionBarHeight(context));
        mMenuHorizontalPadding = array.getDimensionPixelSize(R.styleable.SToolbar_menuHorizontalPadding,
                (int) dp2px(context, 5));
        mTitleTextColor = array.getColor(R.styleable.SToolbar_titleTextColor, mTitleTextColor);
        mTitleTextSize = px2dp(context, array.getDimensionPixelSize(R.styleable.SToolbar_titleTextSize,
                (int) dp2px(context, mTitleTextSize)));
        mMenuTextSize = px2dp(context, array.getDimensionPixelSize(R.styleable.SToolbar_menuTextSize,
                (int) dp2px(context, mMenuTextSize)));
        mMenuTextColor = array.getColor(R.styleable.SToolbar_menuTextColor, mMenuTextColor);
        initViews();
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

    private void initViews() {
        removeAllViews();
        // 1. Add left menu container associated with this toolbar.
        mLeftMenuContainer = new LinearLayout(getContext());
        LayoutParams leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mMinimumHeight);
        leftParams.gravity = Gravity.START | Gravity.TOP;
        mLeftMenuContainer.setLayoutParams(leftParams);
        mLeftMenuContainer.setGravity(Gravity.CENTER_VERTICAL);
        mLeftMenuContainer.setPadding(mMenuHorizontalPadding, 0, mMenuHorizontalPadding, 0);
        addView(mLeftMenuContainer);
        // 2. Add right menu container associated with this toolbar.
        mRightMenuContainer = new LinearLayout(getContext());
        LayoutParams rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mMinimumHeight);
        rightParams.gravity = Gravity.END | Gravity.TOP;
        mRightMenuContainer.setLayoutParams(rightParams);
        mRightMenuContainer.setGravity(Gravity.CENTER_VERTICAL);
        mRightMenuContainer.setPadding(mMenuHorizontalPadding, 0, mMenuHorizontalPadding, 0);
        addView(mRightMenuContainer);
        // 3. Add center item container associated with this toolbar.
        mCenterContainer = new LinearLayout(getContext());
        LayoutParams centerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        centerParams.gravity = Gravity.CENTER | Gravity.TOP;
        mCenterContainer.setMinimumHeight(mMinimumHeight);
        mCenterContainer.setPadding(mMenuHorizontalPadding, 0, mMenuHorizontalPadding, 0);
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

    public void setTitleText(CharSequence text, float textSize) {
        this.setTitleText(text, textSize, mTitleTextColor);
    }

    public void setTitleText(CharSequence text, float textSize, @ColorInt int textColor) {
        getTitleText().setText(text);
        getTitleText().setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        getTitleText().setTextColor(textColor);
    }

    /**
     * Set image title
     */
    public void setTitleImage(@DrawableRes int imageResId) {
        this.setTitleImage(INVALIDATE_VALUE, INVALIDATE_VALUE, imageResId);
    }

    public void setTitleImage(int width, int height, @DrawableRes int imageResId) {
        // Completion layout params.
        complementImageParams(getTitleImage(), width, height);
        // Setup image resource.
        getTitleImage().setImageResource(imageResId);
    }

    /**
     * Set image title, the image view will load with OnImageLoaderListener.
     */
    public void setTitleImage(@NonNull OnImageLoaderListener listener) {
        this.setTitleImage(INVALIDATE_VALUE, INVALIDATE_VALUE, listener);
    }

    public void setTitleImage(int width, int height, @NonNull OnImageLoaderListener listener) {
        // Completion layout params.
        complementImageParams(getTitleImage(), width, height);
        // Load image.
        listener.onImageLoader(getContext(), getTitleImage());
    }

    /**
     * Add custom title
     */
    public void addCustomTitle(@NonNull View titleView) {
        mCenterContainer.addView(titleView);
    }

    /**
     * Get text title.
     */
    public TextView getTitleText() {
        if (null == mTitleText) {
            mTitleText = new TextView(getContext());
            // Set params for the view.
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            mTitleText.setLayoutParams(params);
            mTitleText.setPadding(mMenuHorizontalPadding, 0, mMenuHorizontalPadding, 0);
            // Config some fields.
            mTitleText.setMaxEms(8);
            mTitleText.setLines(1);
            mTitleText.setEllipsize(TextUtils.TruncateAt.END);
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
            mTitleImage = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mTitleImage.setLayoutParams(params);
            mTitleImage.setPadding(mMenuHorizontalPadding, 0, mMenuHorizontalPadding, 0);
            addCustomTitle(mTitleImage);
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

    public void addLeftText(CharSequence text, /*sp*/float textSize, OnClickListener listener) {
        this.addLeftText(text, textSize, mMenuTextColor, listener);
    }

    public void addLeftText(CharSequence text, /*sp*/float textSize, @ColorInt int textColor, OnClickListener listener) {
        this.addLeftText(
                new Options.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setPaddingRight(mMenuHorizontalPadding)
                        .setListener(listener)
                        .build()
        );
    }

    public void addLeftText(Options options) {
        ensure(options);
        addLeftView(createTextView(options));
    }

    /**
     * Add left menu image item.
     */
    public void addLeftIcon(@DrawableRes int drawableRes, OnClickListener listener) {
        this.addLeftIcon(drawableRes, Options.INVALIDATE_VALUE, Options.INVALIDATE_VALUE, listener);
    }

    public void addLeftIcon(@DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        addLeftIcon(
                new Options.Builder()
                        .setWidth(width)
                        .setHeight(height)
                        .setResId(drawableRes)
                        .setPaddingRight(mMenuHorizontalPadding)
                        .setListener(listener)
                        .build()
        );
    }

    public void addLeftIcon(Options options) {
        addLeftView(createImageView(options));
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

    public void addRightText(CharSequence text, /*sp*/float textSize, @Nullable OnClickListener listener) {
        this.addRightText(text, textSize, mMenuTextColor, listener);
    }

    public void addRightText(CharSequence text, /*sp*/float textSize, @ColorInt int textColor, @Nullable OnClickListener listener) {
        this.addRightText(
                new Options.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setPaddingLeft(mMenuHorizontalPadding)
                        .setListener(listener)
                        .build()
        );
    }

    public void addRightText(Options options) {
        ensure(options);
        addRightView(createTextView(options));
    }

    /**
     * Add right menu image item.
     */
    public void addRightIcon(@DrawableRes int drawableRes, OnClickListener listener) {
        this.addRightIcon(drawableRes, Options.INVALIDATE_VALUE, Options.INVALIDATE_VALUE, listener);
    }

    public void addRightIcon(@DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        addRightIcon(
                new Options.Builder()
                        .setWidth(width)
                        .setHeight(height)
                        .setPaddingLeft(mMenuHorizontalPadding)
                        .setResId(drawableRes)
                        .setListener(listener)
                        .build()
        );
    }

    private void addRightIcon(Options options) {
        addRightView(createImageView(options));
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
    private View createTextView(Options options) {
        TextView textView = new TextView(getContext());
        // Set the padding associated with this textView.
        textView.setPadding(options.paddingLeft, options.paddingTop, options.paddingRight, options.paddingBottom);
        // Set the layout parameters associated with this imageView.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Options.INVALIDATE_VALUE == options.width ? ViewGroup.LayoutParams.WRAP_CONTENT :
                        (int) dp2px(getContext(), options.width),
                Options.INVALIDATE_VALUE == options.height ? ViewGroup.LayoutParams.MATCH_PARENT :
                        (int) dp2px(getContext(), options.height));
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        // Set some fields associated with this textView.
        textView.setText(options.text);
        textView.setTextColor(options.textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, options.textSize);
        // Set OnClickListener.
        if (null != options.listener) {
            textView.setOnClickListener(options.listener);
        }
        return textView;
    }

    /**
     * Get ImageView instance.
     */
    private ImageView createImageView(Options options) {
        // Create ImageView instance.
        ImageView imageView = new ImageView(getContext());
        // Set the padding associated with this imageView.
        imageView.setPadding(options.paddingLeft, options.paddingTop, options.paddingRight, options.paddingBottom);
        // Set the layout parameters associated with this imageView.
        complementImageParams(imageView, options.width, options.height);
        // Set some fields associated with this imageView.
        imageView.setImageResource(options.resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // Set OnClickListener
        if (null != options.listener) {
            imageView.setOnClickListener(options.listener);
        }
        return imageView;
    }

    /**
     * Set layout params from width and height associated with the image view.
     */
    private void complementImageParams(ImageView imageView, int width, int height) {
        int destWidth = INVALIDATE_VALUE == width ? ViewGroup.LayoutParams.WRAP_CONTENT :
                (int) dp2px(getContext(), width);
        int destHeight = INVALIDATE_VALUE == height ? ViewGroup.LayoutParams.WRAP_CONTENT
                : (int) dp2px(getContext(), height);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        if (null == params) {
            params = new LinearLayout.LayoutParams(destWidth, destHeight);
        } else {
            params.width = destWidth;
            params.height = destHeight;
        }
        imageView.setLayoutParams(params);
    }

    private void ensure(Options options) {
        if (null == options) {
            throw new NullPointerException("Please ensure parameter options nonnull.");
        }
    }

}
