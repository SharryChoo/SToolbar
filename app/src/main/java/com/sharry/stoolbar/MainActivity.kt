package com.sharry.stoolbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sharry.libtoolbar.Option
import com.sharry.libtoolbar.SToolbar
import com.sharry.libtoolbar.Style

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTitle()
    }

    private fun initTitle() {
        val titleView = LayoutInflater.from(this).inflate(R.layout.toolbar_title,
                window.decorView as ViewGroup, false)
        SToolbar.Builder(this)
                .setStatusBarStyle(Style.TRANSPARENT)
                .setBackgroundColorRes(R.color.colorAccent)
                .setItemHorizontalInterval(10)
                .setTitleText(Option.Builder().setText("1223").setPaddingRight(10).build())
                .setTitleImage(R.drawable.icon_right, 20, 20)
                .setCustomTitle(titleView)
                .addLeftIcon(Option.Builder().setDrawableResId(R.drawable.icon_back).setPaddingLeft(10).build())
                .addLeftText("Left1") {}// 响应左部文本的点击事件
                .addLeftText("Left2") {}// 响应左部文本的点击事件
                .addRightText(Option.Builder().setText("right1").setTextSize(12).setPaddingRight(5).build())// 响应右部文本的点击事件
                .addRightText("Right2") {}// 响应右部文本的点击事件
                .addRightIcon(R.drawable.icon_right, 20, 20) {}// 响应右部图标的点击事件
                .apply()
    }

}
