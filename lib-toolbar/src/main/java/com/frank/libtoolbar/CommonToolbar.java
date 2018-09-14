package com.frank.libtoolbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.frank.libtoolbar.Utils.dp2px;
import static com.frank.libtoolbar.Utils.getActionBarHeight;
import static com.frank.libtoolbar.Utils.getStatusBarHeight;

/**
 * CommonToolbar 的最小高度为系统 ActionBar 的高度
 * <p>
 * 1. 可以直接在Xml文件中直接使用
 * 2. 可以使用 Builder 动态的植入 {@link Builder}
 *
 * @author Frank <a href="frankchoochina@gmail.com">Contact me.</a>
 * @version 3.0
 * @since 2018/8/27 23:20
 */
public class CommonToolbar extends Toolbar {

    /**
     * ImageLoader interface.
     */
    public interface OnImageLoaderListener {
        void imageLoader(Context context, ImageView titleImage);
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

    private final static int INVALIDATE_VALUE = -1;
    private final static float DEFAULT_TITLE_SIZE = 18f;
    private final static int DEFAULT_COLOR = Color.WHITE;      // Default color will be using when set text color.
    private final int mDefaultPadding;                         // Default padding will be using when create View.
    private int mMinimumHeight;
    private int mStatusBarHeight;

    // Toolbar support container.
    private LinearLayout mLeftItemContainer;
    private LinearLayout mCenterItemContainer;
    private LinearLayout mRightItemContainer;

    // 提供的标题(文本/图片/自定义)
    private TextView mTitleText;
    private ImageView mTitleImage;

    // 添加的所有 View 的缓存, 方便用户通过 getViewByTag() 找到自己添加的View
    private SparseArray<View> mItemViews = new SparseArray<>();

    public CommonToolbar(Context context) {
        this(context, null);
    }

    public CommonToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDefaultPadding = (int) dp2px(context, 5);
        mMinimumHeight = getActionBarHeight(context);
        mStatusBarHeight = getStatusBarHeight(context);
        init();
    }

    private void init() {
        removeAllViews();
        // 1. Add left menu container associated with this toolbar.
        mLeftItemContainer = new LinearLayout(getContext());
        LayoutParams leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mMinimumHeight);
        leftParams.gravity = Gravity.START | Gravity.TOP;
        mLeftItemContainer.setLayoutParams(leftParams);
        mLeftItemContainer.setGravity(Gravity.CENTER_VERTICAL);
        mLeftItemContainer.setPadding(mDefaultPadding, 0, mDefaultPadding, 0);
        addView(mLeftItemContainer);
        // 2. Add right menu container associated with this toolbar.
        mRightItemContainer = new LinearLayout(getContext());
        LayoutParams rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mMinimumHeight);
        rightParams.gravity = Gravity.END | Gravity.TOP;
        mRightItemContainer.setLayoutParams(rightParams);
        mRightItemContainer.setGravity(Gravity.CENTER_VERTICAL);
        mRightItemContainer.setPadding(mDefaultPadding, 0, mDefaultPadding, 0);
        addView(mRightItemContainer);
        // 3. Add center item container associated with this toolbar.
        mCenterItemContainer = new LinearLayout(getContext());
        LayoutParams centerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        centerParams.gravity = Gravity.CENTER | Gravity.TOP;
        mCenterItemContainer.setPadding(mDefaultPadding, 0, mDefaultPadding, 0);
        mCenterItemContainer.setLayoutParams(centerParams);
        mCenterItemContainer.setGravity(Gravity.CENTER);
        addView(mCenterItemContainer);
    }

    /*=========================================  背景色与沉浸式状态栏 ======================================*/

    /**
     * Set the view adjust to fit status bar.
     *
     * @param adjust if true is adjust transparent status bar.
     */
    public void setAdjustToTransparentStatusBar(boolean adjust) {
        if (adjust && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                // Set the layout parameters associated with this view.
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setLayoutParams(params);
            }
            // Setup padding.
            setPadding(getPaddingLeft(), getPaddingTop() + mStatusBarHeight,
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
        LayoutParams params = (LayoutParams) mCenterItemContainer.getLayoutParams();
        params.gravity = gravity;
        mCenterItemContainer.setLayoutParams(params);
    }

    /**
     * Set text title
     */
    @Override
    public void setTitle(@StringRes int stringResId) {
        this.setTitle(getResources().getText(stringResId));
    }

    @Override
    public void setTitle(CharSequence text) {
        this.setTitle(text, DEFAULT_TITLE_SIZE);
    }

    public void setTitle(CharSequence text, float textSize) {
        setTitle(text, textSize, DEFAULT_COLOR);
    }

    public void setTitle(CharSequence text, float textSize, @ColorInt int textColor) {
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
        complementTitleImageParams(width, height);
        // Setup image resource.
        getTitleImage().setImageResource(imageResId);
    }

    /**
     * Set image title, the image view will load with OnImageLoaderListener.
     */
    public void setTitleImage(@NonNull OnImageLoaderListener imageLoader) {
        this.setTitleImage(INVALIDATE_VALUE, INVALIDATE_VALUE, imageLoader);
    }

    public void setTitleImage(int width, int height, @NonNull OnImageLoaderListener imageLoader) {
        // Completion layout params.
        complementTitleImageParams(width, height);
        // Load image.
        imageLoader.imageLoader(getContext(), getTitleImage());
    }

    /**
     * Add custom title
     */
    public void addCustomTitle(@NonNull View titleView) {
        mCenterItemContainer.addView(titleView);
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
            params.leftMargin = mDefaultPadding;
            params.rightMargin = mDefaultPadding;
            mTitleText.setLayoutParams(params);
            // Config some fields.
            mTitleText.setMaxEms(8);
            mTitleText.setLines(1);
            mTitleText.setEllipsize(TextUtils.TruncateAt.END);
            // Add to center container.
            mCenterItemContainer.addView(mTitleText);
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
            mTitleImage.setPadding(mDefaultPadding, 0, mDefaultPadding, 0);
            addCustomTitle(mTitleImage);
        }
        return mTitleImage;
    }

    /* ========================================== 左部菜单 ====================================================*/

    /**
     * Add left menu text item.
     */
    public void addLeftText(int tag, CharSequence text, OnClickListener listener) {
        this.addLeftText(tag, text, 14f, listener);
    }

    public void addLeftText(int tag, CharSequence text, /*sp*/float textSize, OnClickListener listener) {
        this.addLeftText(tag, text, textSize, DEFAULT_COLOR, listener);
    }

    public void addLeftText(int tag, CharSequence text, /*sp*/float textSize, @ColorInt int textColor, OnClickListener listener) {
        addLeftView(tag, createTextView(text, textSize, textColor, listener));
    }

    /**
     * Add left menu image item.
     */
    public void addLeftIcon(int tag, @DrawableRes int drawableRes, OnClickListener listener) {
        this.addLeftIcon(tag, drawableRes, INVALIDATE_VALUE, INVALIDATE_VALUE, listener);
    }

    public void addLeftIcon(int tag, @DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        addLeftView(tag, createImageView(width, height, drawableRes, listener));
    }

    /**
     * Add left menu custom item.
     */
    public void addLeftView(int tag, View view) {
        ensure(tag);
        mItemViews.put(tag, view);
        mLeftItemContainer.addView(view);
    }

    /* ========================================== 右部菜单 ====================================================*/

    /**
     * Add right menu text item.
     */
    public void addRightText(int tag, CharSequence text, OnClickListener listener) {
        this.addRightText(tag, text, 14f, listener);
    }

    public void addRightText(int tag, CharSequence text, /*sp*/float textSize, OnClickListener listener) {
        this.addRightText(tag, text, textSize, DEFAULT_COLOR, listener);
    }

    public void addRightText(int tag, CharSequence text, /*sp*/float textSize, @ColorInt int textColor, OnClickListener listener) {
        addRightView(tag, createTextView(text, textSize, textColor, listener));
    }

    /**
     * Add right menu image item.
     */
    public void addRightIcon(int tag, @DrawableRes int drawableRes, OnClickListener listener) {
        this.addRightIcon(tag, drawableRes, INVALIDATE_VALUE, INVALIDATE_VALUE, listener);
    }

    public void addRightIcon(int tag, @DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        addRightView(tag, createImageView(width, height, drawableRes, listener));
    }

    /**
     * Add right menu custom item.
     */
    public void addRightView(int tag, View view) {
        ensure(tag);
        mItemViews.put(tag, view);
        mRightItemContainer.addView(view);
    }

    /**
     * U can get view instance from tag.
     */
    public <T extends View> T getViewByTag(int tag) {
        return (T) mItemViews.get(tag);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() == 3) {
            return;
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 测量完毕后, 判断我们中间标题布局的高度是否小于ActionBar的高度
        if (mCenterItemContainer.getHeight() >= mMinimumHeight) return;
        LayoutParams params = (LayoutParams) mCenterItemContainer.getLayoutParams();
        params.height = mMinimumHeight;
        mCenterItemContainer.setLayoutParams(params);
    }

    /**
     * Get TextView instance.
     */
    private TextView createTextView(CharSequence text, float textSize, int textColor, OnClickListener listener) {
        TextView textView = new TextView(getContext());
        // Set the padding associated with this textView.
        textView.setPadding(mDefaultPadding, 0, mDefaultPadding, 0);
        // Set the layout parameters associated with this imageView.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        // Set some fields associated with this textView.
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        // Set OnClickListener.
        if (null != listener) {
            textView.setOnClickListener(listener);
        }
        return textView;
    }

    /**
     * Get ImageView instance.
     */
    private ImageView createImageView(int width, int height, int drawableResID, OnClickListener listener) {
        // Create ImageView instance.
        ImageView imageView = new ImageView(getContext());
        // Set the padding associated with this imageView.
        int verticalPadding = (height == INVALIDATE_VALUE) ? (int) (mMinimumHeight * 0.3) :
                (mMinimumHeight - (int) dp2px(getContext(), height)) / 2;
        imageView.setPadding(mDefaultPadding, verticalPadding, mDefaultPadding, verticalPadding);
        // Set the layout parameters associated with this imageView.
        int destWidth = (width == INVALIDATE_VALUE) ?
                (int) (mMinimumHeight * 0.4) : (int) dp2px(getContext(), width);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                destWidth + imageView.getPaddingRight() + imageView.getPaddingLeft(),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        // Set some fields associated with this imageView.
        imageView.setLayoutParams(params);
        imageView.setImageResource(drawableResID);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // Set OnClickListener
        if (null != listener) {
            imageView.setOnClickListener(listener);
        }
        return imageView;
    }

    /**
     * Set layout params from width and height associated with the title image.
     */
    private void complementTitleImageParams(int width, int height) {
        int imageWidth = width == INVALIDATE_VALUE ? (int) (mMinimumHeight * 0.6)
                : (int) dp2px(getContext(), width);
        int imageHeight = height == INVALIDATE_VALUE ? (int) (mMinimumHeight * 0.6)
                : (int) dp2px(getContext(), height);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getTitleImage().getLayoutParams();
        params.width = imageWidth;
        params.height = imageHeight;
        getTitleImage().setLayoutParams(params);
    }

    /**
     * Validate tag is usable.
     */
    private void ensure(int tag) {
        if (null != mItemViews.get(tag)) {
            throw new IllegalArgumentException("CommonToolbar.ensure --> 请检查给View设置的Tag是否唯一");
        }
    }

}
