package com.sharry.libtoolbar;

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

import static com.sharry.libtoolbar.Utils.dp2px;
import static com.sharry.libtoolbar.Utils.getActionBarHeight;
import static com.sharry.libtoolbar.Utils.getStatusBarHeight;

/**
 * SToolbar 的最小高度为系统 ActionBar 的高度
 * <p>
 * 1. 可以直接在Xml文件中直接使用
 * 2. 可以使用Builder动态的植入 {@link Builder}
 *
 * @author Sharry <a href="frankchoochina@gmail.com">Contact me.</a>
 * @version 3.0
 * @since 2018/8/27 23:20
 */
public class SToolbar extends Toolbar {

    private final static int INVALIDATE_VALUE = -1;
    private final static int DEFAULT_COLOR = Color.WHITE;

    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    public static Builder Builder(View contentView) {
        return new Builder(contentView);
    }

    // Toolbar中的三个容器
    private LinearLayout mLeftItemContainer;
    private LinearLayout mCenterItemContainer;
    private LinearLayout mRightItemContainer;

    // 提供的标题(文本/图片/自定义)
    private TextView mTitleText;
    private ImageView mTitleImage;
    private int mTextColor = DEFAULT_COLOR;

    // 添加的所有View的缓存, 方便用户通过getViewByTag()找到自己添加的View
    private SparseArray<View> mItemViews = new SparseArray<>();

    public SToolbar(Context context) {
        this(context, null);
    }

    public SToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        removeAllViews();
        // 添加左部容器
        mLeftItemContainer = new LinearLayout(getContext());
        LayoutParams leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                getActionBarHeight(getContext()));
        leftParams.gravity = Gravity.START | Gravity.TOP;
        mLeftItemContainer.setLayoutParams(leftParams);
        mLeftItemContainer.setGravity(Gravity.CENTER_VERTICAL);
        mLeftItemContainer.setPadding((int) dp2px(getContext(), 5),
                0, (int) dp2px(getContext(), 5), 0);
        addView(mLeftItemContainer);

        // 添加右部容器
        mRightItemContainer = new LinearLayout(getContext());
        LayoutParams rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                getActionBarHeight(getContext()));
        rightParams.gravity = Gravity.END | Gravity.TOP;
        mRightItemContainer.setLayoutParams(rightParams);
        mRightItemContainer.setGravity(Gravity.CENTER_VERTICAL);
        mRightItemContainer.setPadding((int) dp2px(getContext(), 5),
                0, (int) dp2px(getContext(), 5), 0);
        addView(mRightItemContainer);

        // 添加中间容器(最后添加, 它的Gravity不会影响其他位置Child的改变)
        mCenterItemContainer = new LinearLayout(getContext());
        LayoutParams centerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        centerParams.gravity = Gravity.CENTER | Gravity.TOP;
        mCenterItemContainer.setPadding((int) dp2px(getContext(), 5), 0,
                (int) dp2px(getContext(), 5), 0);
        mCenterItemContainer.setLayoutParams(centerParams);
        mCenterItemContainer.setGravity(Gravity.CENTER);
        addView(mCenterItemContainer);
    }

    /**
     * 设置标题的位置
     */
    public void setTitleGravity(int gravity) {
        LayoutParams params = (LayoutParams) mCenterItemContainer.getLayoutParams();
        params.gravity = gravity;
        mCenterItemContainer.setLayoutParams(params);
    }

    /**
     * 设置文本标题
     */
    @Override
    public void setTitle(@StringRes int stringResId) {
        this.setTitle(getResources().getText(stringResId), 18f);
    }

    @Override
    public void setTitle(CharSequence text) {
        this.setTitle(text, 18f);
    }

    public void setTitle(CharSequence text, float textSize) {
        if (mTitleText == null) {
            initTitleText(textSize, DEFAULT_COLOR);
        }
        mTitleText.setText(text);
    }

    public void setTitle(CharSequence text, float textSize, @ColorInt int textColor) {
        if (mTitleText == null) {
            initTitleText(textSize, textColor);
        }
        mTitleText.setText(text);
    }

    public TextView getTitleText() {
        if (mTitleText == null) {
            initTitleText(18f, DEFAULT_COLOR);
        }
        return mTitleText;
    }

    /**
     * 设置标题图片
     */
    public void setTitleImage(@DrawableRes int imageResId) {
        this.setTitleImage(imageResId, INVALIDATE_VALUE, INVALIDATE_VALUE);
    }

    public void setTitleImage(@DrawableRes int imageResId, int width, int height) {
        if (mTitleImage == null) {
            initTitleImage(width, height);
        }
        mTitleImage.setImageResource(imageResId);
    }

    public void setTitleImage(@NonNull TitleImageLoader imageLoader, int width, int height) {
        if (mTitleImage == null) {
            initTitleImage(width, height);
        }
        imageLoader.displayImage(getContext(), mTitleImage);
    }

    public ImageView getTitleImage() {
        if (mTitleImage == null) {
            initTitleImage(INVALIDATE_VALUE, INVALIDATE_VALUE);
        }
        return mTitleImage;
    }

    public void setTitleImage(@NonNull TitleImageLoader imageLoader) {
        this.setTitleImage(imageLoader, INVALIDATE_VALUE, INVALIDATE_VALUE);
    }

    /**
     * 添加用户自定义的标题
     */
    public void addCustomTitle(@NonNull View titleView) {
        mCenterItemContainer.addView(titleView);
    }

    /**
     * 添加左部文本
     */
    public void addLeftText(int tag, CharSequence text, OnClickListener listener) {
        this.addLeftText(tag, text, 14f, listener);
    }

    public void addLeftText(int tag, CharSequence text, /*sp*/float textSize, OnClickListener listener) {
        this.addLeftText(tag, text, textSize, DEFAULT_COLOR, listener);
    }

    public void addLeftText(int tag, CharSequence text, /*sp*/float textSize, @ColorInt int textColor, OnClickListener listener) {
        ensure(tag);
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setPadding((int) dp2px(getContext(), 5), 0, (int) dp2px(getContext(), 5), 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(mTextColor);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setOnClickListener(listener);
        mItemViews.put(tag, textView);
        mLeftItemContainer.addView(textView);
    }

    /**
     * 添加左部图标
     */
    public void addLeftIcon(int tag, @DrawableRes int drawableRes, OnClickListener listener) {
        this.addLeftIcon(tag, drawableRes, INVALIDATE_VALUE, INVALIDATE_VALUE, listener);
    }

    public void addLeftIcon(int tag, @DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        ensure(tag);
        int destWidth = (width == INVALIDATE_VALUE) ?
                (int) (getActionBarHeight(getContext()) * 0.4) : (int) dp2px(getContext(), width);
        // 增大触控面积
        int verticalPadding = (height == INVALIDATE_VALUE) ?
                (int) (getActionBarHeight(getContext()) * 0.3) :
                (getActionBarHeight(getContext()) - (int) dp2px(getContext(), height)) / 2;
        ImageView imageView = new ImageView(getContext());
        imageView.setPadding(
                (int) dp2px(getContext(), 5),
                verticalPadding,
                (int) dp2px(getContext(), 5),
                verticalPadding
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                destWidth + imageView.getPaddingRight() + imageView.getPaddingLeft(),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(params);
        imageView.setImageResource(drawableRes);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(listener);
        mItemViews.put(tag, imageView);
        mLeftItemContainer.addView(imageView);
    }

    /**
     * 添加左部的 View
     */
    public void addLeftView(int tag, View view, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        ensure(tag);
        int destWidth = (width == INVALIDATE_VALUE) ? (int) (getActionBarHeight(getContext()) * 0.4) : (int) dp2px(getContext(), width);
        // 这样处理是为了增大触控面积
        int verticalPadding = (height == INVALIDATE_VALUE) ? (int) (getActionBarHeight(getContext()) * 0.3)
                : (getActionBarHeight(getContext()) - (int) dp2px(getContext(), height)) / 2;
        view.setPadding(
                (int) dp2px(getContext(), 5),
                verticalPadding,
                (int) dp2px(getContext(), 5),
                verticalPadding
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                destWidth + view.getPaddingRight() + view.getPaddingLeft(),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(params);
        view.setOnClickListener(listener);
        mItemViews.put(tag, view);
        mLeftItemContainer.addView(view);
    }

    /**
     * 添加右部文本
     */
    public void addRightText(int tag, CharSequence text, OnClickListener listener) {
        this.addRightText(tag, text, 14f, listener);
    }

    public void addRightText(int tag, CharSequence text, /*sp*/float textSize, OnClickListener listener) {
        this.addRightText(tag, text, textSize, DEFAULT_COLOR, listener);
    }

    public void addRightText(int tag, CharSequence text, /*sp*/float textSize, @ColorInt int textColor, OnClickListener listener) {
        ensure(tag);
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setPadding((int) dp2px(getContext(), 5), 0,
                (int) dp2px(getContext(), 5), 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(mTextColor);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setOnClickListener(listener);
        mItemViews.put(tag, textView);
        mRightItemContainer.addView(textView);
    }

    /**
     * 添加右部图标
     */
    public void addRightIcon(int tag, @DrawableRes int drawableRes, OnClickListener listener) {
        this.addRightIcon(tag, drawableRes, INVALIDATE_VALUE, INVALIDATE_VALUE, listener);
    }

    public void addRightIcon(int tag, @DrawableRes int drawableRes, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        ensure(tag);
        int destWidth = (width == INVALIDATE_VALUE) ?
                (int) (getActionBarHeight(getContext()) * 0.4) : (int) dp2px(getContext(), width);
        // 这样处理是为了增大触控面积
        int imageVerticalPadding = (height == INVALIDATE_VALUE) ?
                (int) (getActionBarHeight(getContext()) * 0.3) :
                (getActionBarHeight(getContext()) - (int) dp2px(getContext(), height)) / 2;
        ImageView imageView = new ImageView(getContext());
        imageView.setPadding(
                (int) dp2px(getContext(), 5),
                imageVerticalPadding,
                (int) dp2px(getContext(), 5),
                imageVerticalPadding
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                destWidth + imageView.getPaddingRight() + imageView.getPaddingLeft(),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(params);
        imageView.setImageResource(drawableRes);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(listener);
        mItemViews.put(tag, imageView);
        mRightItemContainer.addView(imageView);
    }

    /**
     * 添加右部的 View
     */
    public void addRightView(int tag, View view, /*dp*/int width, /*dp*/int height, OnClickListener listener) {
        ensure(tag);
        int destWidth = (width == INVALIDATE_VALUE) ?
                (int) (getActionBarHeight(getContext()) * 0.4) : (int) dp2px(getContext(), width);
        // 这样处理是为了增大触控面积
        int verticalPadding = (height == INVALIDATE_VALUE) ?
                (int) (getActionBarHeight(getContext()) * 0.3) :
                (getActionBarHeight(getContext()) - (int) dp2px(getContext(), height)) / 2;
        view.setPadding(
                (int) dp2px(getContext(), 5),
                verticalPadding,
                (int) dp2px(getContext(), 5),
                verticalPadding
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                destWidth + view.getPaddingRight() + view.getPaddingLeft(),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(params);
        view.setOnClickListener(listener);
        mItemViews.put(tag, view);
        mRightItemContainer.addView(view);
    }

    /**
     * 通过Tag获取View
     */
    public <T extends View> T getViewByTag(int tag) {
        return (T) mItemViews.get(tag);
    }

    /**
     * 调整适应沉浸式状态栏
     */
    public void setAdjustToTransparentStatusBar(boolean adjust) {
        if (adjust && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                // 强制将Toolbar设置为wrap_content, 用来适应padding
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                setLayoutParams(params);
            }
            setPadding(0, getStatusBarHeight(getContext()), 0, 0);
        }
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
        if (mCenterItemContainer.getHeight() >= getActionBarHeight(getContext())) return;
        LayoutParams params = (LayoutParams) mCenterItemContainer.getLayoutParams();
        params.height = getActionBarHeight(getContext());
        mCenterItemContainer.setLayoutParams(params);
    }

    /**
     * 设置背景色
     *
     * @param colorRes color 的 ID
     */
    public void setBackgroundColorRes(@ColorRes int colorRes) {
        setBackgroundColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * 设置背景图片
     *
     * @param drawableRes Drawable 的 ID
     */
    public void setBackgroundDrawableRes(@DrawableRes int drawableRes) {
        setBackgroundResource(drawableRes);
    }

    private void initTitleText(float textSize, int textColor) {
        mTitleText = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int) dp2px(getContext(), 5);
        params.rightMargin = (int) dp2px(getContext(), 5);
        mTitleText.setLayoutParams(params);
        mTitleText.setTextColor(mTextColor);
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        mTitleText.setMaxEms(8);
        mTitleText.setLines(1);
        mTitleText.setEllipsize(TextUtils.TruncateAt.END);
        mTitleText.setTextColor(textColor);
        mCenterItemContainer.addView(mTitleText);
    }

    private void initTitleImage(int width, int height) {
        mTitleImage = new ImageView(getContext());
        int imageWidth = width == INVALIDATE_VALUE ? (int) (getActionBarHeight(getContext()) * 0.6)
                : (int) dp2px(getContext(), width);
        int imageHeight = height == INVALIDATE_VALUE ? (int) (getActionBarHeight(getContext()) * 0.6)
                : (int) dp2px(getContext(), height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        params.leftMargin = (int) dp2px(getContext(), 5);
        params.rightMargin = (int) dp2px(getContext(), 5);
        mTitleImage.setLayoutParams(params);
        mCenterItemContainer.addView(mTitleImage);
    }

    private void ensure(int tag) {
        if (mItemViews.get(tag) != null) {
            throw new IllegalArgumentException("CommonToolbar.ensure --> 请检查给View设置的Tag是否唯一");
        }
    }

    /**
     * 图片加载接口, 用户自己实现加载策略
     */
    public interface TitleImageLoader {
        void displayImage(Context context, ImageView titleImage);
    }

}
