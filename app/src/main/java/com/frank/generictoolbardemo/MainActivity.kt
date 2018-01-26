package com.frank.generictoolbardemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.frank.generictoolbardemo.toolbar.GenericToolbar
import com.frank.generictoolbardemo.toolbar.Style

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTitle()
    }

    private fun initTitle() {
        val titleView = LayoutInflater.from(this).inflate(R.layout.toolbar_title,
                window.decorView as ViewGroup, false)
        GenericToolbar.Builder(this)
                .setStatusBarStyle(Style.TRANSPARENT)
                .setBackgroundColorResource(R.color.colorAccent)
                .addCustomTitle(titleView)
                .addLeftIcon(1, R.drawable.icon_back) {}// 响应左部图标的点击事件
                .addLeftText(2, "Left") {}// 响应左部文本的点击事件
                .addRightText(3, "Right") {}// 响应右部文本的点击事件
                .addRightIcon(4, R.drawable.icon_left) {}// 响应右部图标的点击事件
                .setTextColor(Color.BLACK)// 会全局设置字体的颜色, 自定义标题除外
                .apply()
    }
}
