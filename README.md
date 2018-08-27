# CommonToolbar
通用Toolbar, 支持 90% 的使用场景

### 效果展示
1. 非沉浸式<br>
![非沉浸式.png](http://upload-images.jianshu.io/upload_images/4147272-93dae86b25387b72.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. 沉浸式<br>
![沉浸式.png](http://upload-images.jianshu.io/upload_images/4147272-57f89cb712fd9487.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

3. 自定义标题<br>
![定义标题栏.png](http://upload-images.jianshu.io/upload_images/4147272-2211c8000e1ca3e9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 使用方式
1. 在 style.xml 中，将主题改为 NoActionBar 的样式

2. 基本用法
```
        GenericToolbar.Builder(this)
                .addTitleImage(R.mipmap.ic_launcher)// 标题头像
                .addTitleText("FrankChoo")// 标题文本
                .setBackgroundColorResource(R.color.colorAccent)// 背景颜色
                .addLeftIcon(1, R.drawable.icon_back) {}// 响应左部图标的点击事件
                .addLeftText(2, "Left") {}// 响应左部文本的点击事件
                .addRightText(3, "Right") {}// 响应右部文本的点击事件
                .addRightIcon(4, R.drawable.icon_left) {}// 响应右部图标的点击事件
                .setTextColor(Color.BLACK)// 会全局设置字体的颜色, 自定义标题除外
                .apply()
```
3. 调整图标和文本大小
```
    // 图标宽高
    GenericToolbar.Builder(this)
        .addLeftIcon(1, R.drawable.icon_back, /*Width*/50, /*Height*/50) {}
        ...
    // 文本大小
    GenericToolbar.Builder(this)
        .addLeftText(2, "Left", 20f) {}// 响应左部文本的点击事件
```
4. 自定义标题
``` 
    // 1. 创建View
    val titleView = LayoutInflater.from(this).inflate(R.layout.toolbar_title,
                window.decorView as ViewGroup, false)
    // 2. 动态填充
    GenericToolbar.Builder(this)
            .addCustomTitle(titleView)
            ...
```
5. 状态栏
```
public enum Style {
    TRANSPARENT,// 全透明
    TRANSLUCENCE,// 半透明
    HIDE,// 隐藏
    DEFAULT// 默认
}

// 直接使用的方式
AppBarHelper.with(this).setStatusBarStyle(Style.TRANSPARENT).apply()

// 通过Builder直接构建的方式
GenericToolbar.Builder(this)
        .setStatusBarStyle(Style.TRANSPARENT)// 只需要在这里集成即可
```
