package com.sharry.stoolbar

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.sharry.libtoolbar.*

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
                .setStatusBarStyle(Style.TRANSLUCENCE)
                .setBackgroundColor(Color.WHITE)
                .setSubItemInterval(10)
                .addTitleView(titleView,
                        ViewOptions.Builder()
                                .setPaddingBottom(20)
                                .setPaddingTop(20)
                                .setListener { showMsg("U click title item") }
                                .build()
                )
                .addBackIcon(R.drawable.icon_back)
                .addLeftMenuText(
                        TextViewOptions.Builder()
                                .setText("left")
                                .setTextColor(Color.BLACK)
                                .setListener { showMsg("U click left text") }
                                .build()
                )
                .addRightMenuText(
                        TextViewOptions.Builder()
                                .setText("right")
                                .setTextColor(Color.BLACK)
                                .setListener { showMsg("U click right text") }
                                .build()
                )
                .addRightMenuImage(
                        ImageViewOptions.Builder()
                                .setDrawableResId(R.drawable.icon_right)
                                .setListener { showMsg("U click right image") }
                                .build()
                )
                .apply()

    }

    private fun showMsg(msg: CharSequence) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}
