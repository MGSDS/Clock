package com.mgsds.clock

import java.util.*
import com.mgsds.clock.Clock
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ClockTimer(clock: Clock) : ScheduledThreadPoolExecutor(1) {
    private val mClock: Clock = clock
    var isRunning = false
        private set

    private val mTimerTask: TimerTask = object : TimerTask() {
        override fun run() {
            mClock.updateTime(Calendar.getInstance())
            mClock.postInvalidate()
        }
    }

    fun start() {
        scheduleAtFixedRate(mTimerTask, 0, 1, TimeUnit.SECONDS)
        isRunning = true
    }

    fun stop() {
        mTimerTask.cancel()
        isRunning = false
    }
}