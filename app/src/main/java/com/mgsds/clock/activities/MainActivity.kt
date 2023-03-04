package com.mgsds.clock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mgsds.clock.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openFullPage(view: View) {
        intent = Intent(this, FullPageActivity::class.java)
        startActivity(intent)
    }
    fun openMovable(view: View) {
        intent = Intent(this, ResizeableViewActivity::class.java)
        startActivity(intent)
    }
}