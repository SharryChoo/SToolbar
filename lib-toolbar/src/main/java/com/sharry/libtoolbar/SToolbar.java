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
import static androidx.annotation.Dimension.PX;
import static androidx.annotation.Dimension.SP;
import static com.sharry.libtoolbar.Utils.dp2px;
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

    private static final int LOCKED_CHILDREN_COUNT = 3;
    @Dimension(unit = DP)
    static final int DEFAULT_INTERVAL = 5;
    private int mTitleTextSize = TextOptions.DEFAULT_TITLE_TEXT_SIZE;
    private int mTitleTextColor = TextOptions.DEFAULT_TEXT_COLOR;
    private int mMenuTextSize = TextOptions.DEFAULT_MENU_TEXT_SIZE;
    private int mMenuTextColor = TextOptions.DEFAULT_TEXT_COLOR;
    @Dimension(unit = PX)
    private int mMinimumHeight;                                     // Minimum Toolbar height.
    @Dimension(unit = DP)
    private int mItemHorizontalInterval;                            // Default padding will be using when create View.

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
            addLeftMenuImage(
                    ImageOptions.Builder()
                            .setDrawableResId(leftMenuIconResId)
                            .setPaddingLeft(mItemHorizontalInterval)
                            .build()
            );
        }
        String leftMenuText = array.getString(R.styleable.SToolbar_menuLeftText);
        if (null != leftMenuText) {
            addLeftMenuText(
                    TextOptions.Builder()
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
            addRightMenuText(
                    TextOptions.Builder()
                            .setText(rightMenuText)
                            .setTextSize(mMenuTextSize)
                            .setTextColor(mMenuTextColor)
                            .setPaddingRight(mItemHorizontalInterval)
                            .build()
            );
        }
        int rightMenuIconResId = array.getResourceId(R.styleable.SToolbar_menuRightIcon, View.NO_ID);
        if (View.NO_ID != rightMenuIconResId) {
            addRightMenuImage(
                    ImageOptions.Builder()
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
        removeAllViews();
        // 1. Add left menu container associated with this toolbar.
        mLeftMenuContainer = new LinearLayout(context);
        LayoutParams leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.gravity = Gravity.START | Gravity.TOP;
        mLeftMenuContainer.setLayoutParams(leftParams);
        mLeftMenuContainer.setMinimumHeight(mMinimumHeight);
        mLeftMenuContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mLeftMenuContainer);
        // 2. Add right menu container associated with this toolbar.
        mRightMenuContainer = new LinearLayout(context);
        LayoutParams rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.gravity = Gravity.END | Gravity.TOP;
        mRightMenuContainer.setLayoutParams(rightParams);
        mRightMenuContainer.setMinimumHeight(mMinimumHeight);
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
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        // Lock height always is WRAP_CONTENT.
        if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        super.setLayoutParams(params);
    }

    /**
     * Set app bar style associated with this Activity.
     */
    public void setStatusBarStyle(Style style) {
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

    public void setTitleText(@NonNull CharSequence text) {
        this.setTitleText(text, mTitleTextSize);
    }

    public void setTitleText(@NonNull CharSequence text, @Dimension(unit = SP) int textSize) {
        this.setTitleText(text, textSize, mTitleTextColor);
    }

    public void setTitleText(@NonNull CharSequence text, @Dimension(unit = SP) int textSize, @ColorInt int textColor) {
        this.setTitleText(
                TextOptions.Builder()
                        .setText(text)
                        .setTextSize(textSize)
                        .setTextColor(textColor)
                        .setPaddingLeft(mItemHorizontalInterval)
                        .setPaddingRight(mItemHorizontalInterval)
                        .build()
        );
    }

    public void setTitleText(TextOptions options) {
        ensure(options);
        options = TextOptions.Builder(options)
                .setTextSize(0 != options.textSize ? options.textSize : mTitleTextSize)
                .setPaddingLeft(0 != options.paddingLeft ? options.paddingLeft : mItemHorizontalInterval)
                .setPaddingRight(0 != options.paddingRight ? options.paddingRight : mItemHorizontalInterval)
                .build();
        complementTextView(getTitleText(), options);
    }

    /**
     * Set image associated with this toolbar title.
     */
    public void setTitleImage(@DrawableRes int resId) {
        this.setTitleImage(resId, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setTitleImage(@DrawableRes int resId, @Dimension(unit = DP) int width,
                              @Dimension(unit = DP) int height) {
        this.setTitleImage(
                ImageOptions.Builder()
                        .setDrawableResId(resId)
                        .setWidth(width)
                        .setHeight(height)
                        .build()
        );
    }

    public void setTitleImage(ImageOptions options) {
        ensure(options);
        options = ImageOptions.Builder(options)
                .setPaddingLeft(0 != options.paddingLeft ? options.paddingLeft : mItemHorizontalInterval)
                .setPaddingRight(0 != options.paddingRight ? options.paddingRight : mItemHorizontalInterval)
                .build();
        complementImageView(getTitleImage(), options);
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

    /**
     * Add back icon associated with this toolbar left menu.
     */
    public void addBackIcon(@DrawableRes int drawableRes) {
        this.addLeftMenuImage(
                ImageOptions.Builder()
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
     * Add text sub item associated with this toolbar left menu.
     */
    public void addLeftMenuText(TextOptions options) {
        TextView textView = createTextView();
        options = TextOptions.Builder(options)
                .setTextSize(0 != options.textSize ? options.textSize : mMenuTextSize)
                .setPaddingLeft(0 != options.paddingLeft ? options.paddingLeft : mItemHorizontalInterval)
                .build();
        complementTextView(textView, options);
        addLeftView(textView);
    }

    /**
     * Add image sub item associated with this toolbar left menu.
     */
    public void addLeftMenuImage(ImageOptions options) {
        ImageView imageView = createImageView();
        options = ImageOptions.Builder(options)
                .setPaddingLeft(0 != options.paddingLeft ? options.paddingLeft : mItemHorizontalInterval)
                .build();
        complementImageView(imageView, options);
        addLeftView(imageView);
    }

    /**
     * Add custom sub item associated with this toolbar left menu.
     */
    public void addLeftView(View view) {
        mLeftMenuContainer.addView(view);
    }

    /**
     * Add text sub item associated with this toolbar right menu.
     */
    public void addRightMenuText(TextOptions options) {
        TextView textView = createTextView();
        options = TextOptions.Builder(options)
                .setTextSize(0 != options.textSize ? options.textSize : mMenuTextSize)
                .setPaddingRight(0 != options.paddingRight ? options.paddingRight : mItemHorizontalInterval)
                .build();
        complementTextView(textView, options);
        addRightView(textView);
    }

    /**
     * Add image sub item associated with this toolbar right menu.
     */
    public void addRightMenuImage(ImageOptions options) {
        ImageView imageView = createImageView();
        options = ImageOptions.Builder(options)
                .setPaddingRight(0 != options.paddingLeft ? options.paddingLeft : mItemHorizontalInterval)
                .build();
        complementImageView(imageView, options);
        addRightView(imageView);
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
        if (LOCKED_CHILDREN_COUNT == getChildCount()) {
            return;
        }
        super.addView(child, index, params);
    }

    @Override
    public void setMinimumHeight(int minimumHeight) {
        mMinimumHeight = minimumHeight;
        // Reset container minimumHeight
        mLeftMenuContainer.setMinimumHeight(mMinimumHeight);
        mRightMenuContainer.setMinimumHeight(mMinimumHeight);
        mCenterContainer.setMinimumHeight(mMinimumHeight);
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
        textView.setGravity(Gravity.CENTER);
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
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(params);
        return imageView;
    }

    /**
     * Set fields from option associated with the text view.
     */
    private void complementTextView(TextView textView, TextOptions option) {
        // Set the layout parameters associated with this textView.
        completionView(textView, option);
        // Set some fields associated with this textView.
        textView.setText(option.text);
        textView.setTextColor(option.textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, option.textSize);
        textView.setMaxEms(option.maxEms);
        textView.setLines(option.lines);
        textView.setEllipsize(option.ellipsize);
    }

    /**
     * Set fields from option associated with the image view.
     */
    private void complementImageView(ImageView imageView, ImageOptions option) {
        // Set the layout params associated with this imageView.
        completionView(imageView, option);
        // Set the padding associated with this imageView.
        imageView.setPadding(
                dp2px(getContext(), option.paddingLeft),
                dp2px(getContext(), option.paddingTop),
                dp2px(getContext(), option.paddingRight),
                dp2px(getContext(), option.paddingBottom)
        );
        // Set some fields associated with this imageView.
        imageView.setImageResource(option.drawableResId);
        imageView.setScaleType(option.scaleType);
    }

    /**
     * Set fields from option associated with the view.
     */
    private void completionView(View view, Options option) {
        // Set padding.
        view.setPadding(
                dp2px(getContext(), option.paddingLeft),
                dp2px(getContext(), option.paddingTop),
                dp2px(getContext(), option.paddingRight),
                dp2px(getContext(), option.paddingBottom)
        );
        // Set the layout parameters associated with this textView.
        int validWidth = isLayoutParamsSpecialValue(option.width) ? option.width :
                dp2px(getContext(), option.width) + view.getPaddingLeft() + view.getPaddingRight();
        int validHeight = isLayoutParamsSpecialValue(option.height) ? option.height :
                dp2px(getContext(), option.height) + view.getPaddingTop() + view.getPaddingBottom();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (null == params) {
            params = new LinearLayout.LayoutParams(validWidth, validHeight);
        } else {
            params.width = validWidth;
            params.height = validHeight;
        }
        view.setLayoutParams(params);
        // Set OnClickListener
        if (null != option.listener) {
            view.setOnClickListener(option.listener);
        }
    }

    /**
     * Ensure options.
     */
    private void ensure(Options options) {
        if (null == options) {
            throw new NullPointerException("Please ensure parameter options nonnull.");
        }
        if (options instanceof TextOptions && null == ((TextOptions) options).text) {
            throw new NullPointerException("Please ensure TextOptions.text nonnull.");
        } else if (options instanceof ImageOptions && View.NO_ID == ((ImageOptions) options).drawableResId) {
            throw new IllegalArgumentException("Please ensure ImageOptions.drawableResId is valuable.");
        }
    }

    private boolean isLayoutParamsSpecialValue(int paramsValue) {
        return ViewGroup.LayoutParams.MATCH_PARENT == paramsValue
                || ViewGroup.LayoutParams.WRAP_CONTENT == paramsValue;
    }

}
