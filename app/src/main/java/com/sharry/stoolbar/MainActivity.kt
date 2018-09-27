package com.sharry.stoolbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sharry.libtoolbar.SToolbar
import com.sharry.libtoolbar.Style
import com.sharry.libtoolbar.ImageOptions
import com.sharry.libtoolbar.TextOptions

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
                .addCustomTitle(titleView)
                .addBackIcon(R.drawable.icon_back)
                .addLeftMenu(
                        TextOptions.Builder()
                                .setText("left")
                                .setListener { showMsg("U click left text") }
                                .build()
                )
                .addRightMenu(
                        TextOptions.Builder()
                                .setText("right")
                                .setListener { showMsg("U click right text") }
                                .build()
                )
                .addRightMenu(
                        ImageOptions.Builder()
                                .setDrawableResId(R.drawable.icon_right)
                                .setWidth(30).setHeight(30)
                                .setListener { showMsg("U click right image") }
                                .build()
                )
                .apply()
    }

    private fun showMsg(msg: CharSequence) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
