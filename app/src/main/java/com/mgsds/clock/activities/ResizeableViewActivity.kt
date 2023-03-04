package com.mgsds.clock.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.mgsds.clock.R
import com.mgsds.clock.clockView.Clock

class ResizeableViewActivity : AppCompatActivity() {
    private lateinit var mClock : Clock;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resizeable_view)
        mClock = findViewById(R.id.resizeableClock)
    }

    var x = 0F
    var y = 0F

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            x = event.x
            y = event.y
        }

        if(event?.action == MotionEvent.ACTION_MOVE) {
            val dx = event.x - x
            val dy = event.y - y

            mClock.x += dx
            mClock.y += dy

            x = event.x
            y = event.y
        }
        return super.onTouchEvent(event)
    }
}