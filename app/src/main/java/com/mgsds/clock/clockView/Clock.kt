package com.mgsds.clock.clockView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*
import kotlin.math.min

class Clock(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mCentreX: Float = 0F
    private var mCentreY: Float = 0F
    private var mRadius: Float = 0F
    private var mPadding: Float = 0F
    private var mPinRadius: Float = 0F
    private var mInitialized: Boolean = false

    private val mDialTagsColor: Int = Color.BLACK
    private val mHandsColor: Int = Color.DKGRAY
    private val mSecondsHandColor : Int = Color.RED
    private val mDialBgColor : Int = Color.WHITE
    private val mDialOutlineColor : Int = Color.BLACK
    private val mPinColor : Int = Color.WHITE

    private val mHoursHandPaint: Paint = Paint()
    private val mMinutesHandPaint: Paint = Paint()
    private val mSecondsHandPaint: Paint = Paint()
    private val mPrimaryDialTagsPaint: Paint = Paint()
    private val mSecondaryDialTagsPaint: Paint = Paint()
    private val mDialOutlinePaint: Paint = Paint()
    private val mDialBgPaint: Paint = Paint()
    private val mPinPaint: Paint = Paint()

    private var mDialOutlineLineWidth: Float = 0f
    private var mDialPrimaryTagsLineWidth: Float = 0f
    private var mDialSecondaryTagsLineWidth: Float = 0f
    private var mHoursHandLineWidth: Float = 0f
    private var mMinutesHandLineWidth:Float = 0f
    private var mSecondsHandLineWidth: Float = 0f

    private val mPrimaryTagsRect: Rect = Rect()
    private val mSecondaryTagsRect: Rect = Rect()
    private val mHoursHandRect: Rect = Rect()
    private val mMinutesHandRect: Rect = Rect()
    private val mSecondsHandRect: Rect = Rect()

    private var mHoursHandAngle: Float = 0f
    private var mMinutesHandAngle: Float = 0f
    private var mSecondsHandAngle:Float = 0f
    private var mTimer: ClockTimer = ClockTimer(this)

    private fun init() {
        if (mInitialized)
            return

        if (!mTimer.isRunning) {
            mTimer.start()
        }

        isSaveEnabled = true
        mDialOutlineLineWidth = min(width, height) * 0.01f
        mDialPrimaryTagsLineWidth = mDialOutlineLineWidth * 1.5f
        mDialSecondaryTagsLineWidth = mDialOutlineLineWidth
        mHoursHandLineWidth = mDialOutlineLineWidth * 2f
        mMinutesHandLineWidth = mDialOutlineLineWidth
        mSecondsHandLineWidth = mDialOutlineLineWidth * 0.75f

        mCentreX = width.toFloat() / 2
        mCentreY = height.toFloat() / 2
        mPadding = mDialOutlineLineWidth / 2
        mRadius = min(mCentreX, mCentreY) - mPadding
        mPinRadius = mRadius / 60



        mDialOutlinePaint.color = mDialOutlineColor
        mDialOutlinePaint.style = Paint.Style.STROKE
        mDialOutlinePaint.strokeWidth = mDialOutlineLineWidth
        mDialOutlinePaint.isAntiAlias = true

        mDialBgPaint.color = mDialBgColor
        mDialBgPaint.style = Paint.Style.FILL
        mDialBgPaint.isAntiAlias = true

        mPrimaryDialTagsPaint.color = mDialTagsColor
        mPrimaryDialTagsPaint.style = Paint.Style.FILL_AND_STROKE
        mPrimaryDialTagsPaint.strokeWidth = mDialPrimaryTagsLineWidth
        mPrimaryDialTagsPaint.isAntiAlias = true

        mSecondaryDialTagsPaint.color = mDialTagsColor
        mSecondaryDialTagsPaint.style = Paint.Style.FILL_AND_STROKE
        mSecondaryDialTagsPaint.strokeWidth = mDialSecondaryTagsLineWidth
        mSecondaryDialTagsPaint.isAntiAlias = true

        mHoursHandPaint.color = mHandsColor
        mHoursHandPaint.style = Paint.Style.STROKE;
        mHoursHandPaint.strokeWidth = mHoursHandLineWidth;
        mHoursHandPaint.isAntiAlias = true;


        mMinutesHandPaint.color = mHandsColor
        mMinutesHandPaint.style = Paint.Style.STROKE;
        mMinutesHandPaint.strokeWidth = mMinutesHandLineWidth;
        mMinutesHandPaint.isAntiAlias = true;

        mSecondsHandPaint.color = mSecondsHandColor
        mSecondsHandPaint.style = Paint.Style.STROKE;
        mSecondsHandPaint.strokeWidth = mSecondsHandLineWidth;
        mSecondsHandPaint.isAntiAlias = true;

        mPinPaint.color = mPinColor
        mPinPaint.style = Paint.Style.FILL
        mSecondsHandPaint.isAntiAlias = true;


        val x1 = mCentreX - mRadius
        var x2 = mCentreX - mRadius + mRadius / 6
        val y = mCentreY
        mPrimaryTagsRect.set(x1.toInt(), y.toInt(), x2.toInt(), y.toInt())

        x2 = mCentreX - mRadius + mRadius / 8
        mSecondaryTagsRect.set(x1.toInt(), y.toInt(), x2.toInt(), y.toInt())

        setHandRect(mHoursHandRect, mRadius/3*2, mRadius/10)
        setHandRect(mMinutesHandRect, mRadius * 1.15f, mRadius/5)
        setHandRect(mSecondsHandRect, mRadius * 1.15f, mRadius/5)

        mInitialized = true

        Log.println(Log.DEBUG, "Clock", "Clock initialized")

    }

    private fun setHandRect(rect: Rect, length: Float, offset: Float)
    {
        val y1 = mCentreY + offset
        val y2 = mCentreY - (length - offset)
        val x = mCentreX
        rect.set(x.toInt(), y1.toInt(), x.toInt(), y2.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!mInitialized)
            init()

        drawDial(canvas)
        drawHands(canvas)
    }

    private fun drawDial(canvas: Canvas?) {
        drawCircle(canvas)
        drawTags(canvas)
    }

    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawCircle(mCentreX, mCentreY, mRadius, mDialBgPaint)
        canvas?.drawCircle(mCentreX, mCentreY, mRadius, mDialOutlinePaint)
    }

    private fun drawTags(canvas : Canvas?) {
        for (i in 0 until 12) {
            val degree = 360F / 12F
            canvas?.rotate(degree, mCentreX, mCentreY)
            if (i % 3 == 2) {
                canvas?.drawRect(mPrimaryTagsRect, mPrimaryDialTagsPaint)
            }
            else{
                canvas?.drawRect(mSecondaryTagsRect, mSecondaryDialTagsPaint)
            }
        }
    }

    private fun drawHands(canvas: Canvas?) {
        drawHourHand(canvas)
        drawMinuteHand(canvas)
        drawSecondsHand(canvas)
        drawPin(canvas)
    }

    private fun drawHand(canvas: Canvas?, angle: Float, rect: Rect, paint: Paint) {
        canvas?.rotate(angle, mCentreX, mCentreY)
        canvas?.drawRect(rect, paint)
        canvas?.rotate(-angle, mCentreX, mCentreY)
    }

    private fun drawHourHand(canvas: Canvas?) {
        drawHand(canvas, mHoursHandAngle, mHoursHandRect, mHoursHandPaint)
    }

    private fun drawMinuteHand(canvas: Canvas?) {
        drawHand(canvas, mMinutesHandAngle, mMinutesHandRect, mMinutesHandPaint)
    }

    private fun drawSecondsHand(canvas: Canvas?) {
        drawHand(canvas, mSecondsHandAngle, mSecondsHandRect, mSecondsHandPaint)
    }

    private fun drawPin(canvas: Canvas?) {
        canvas?.drawCircle(mCentreX, mCentreY, mPinRadius, mPinPaint)
    }

    public fun updateTime(calendar: Calendar) {
        mSecondsHandAngle = calendar.get(Calendar.SECOND) * 360f / 60f
        mMinutesHandAngle = calendar.get(Calendar.MINUTE) * 360f / 60f + mSecondsHandAngle / 60f
        mHoursHandAngle = calendar.get(Calendar.HOUR_OF_DAY) * 360f / 12f + mMinutesHandAngle / 12f
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = ClockState(superState)
        ss.secondsAngle = mSecondsHandAngle
        ss.hoursAngle = mHoursHandAngle
        ss.minutesAngle = mMinutesHandAngle
        Log.println(Log.DEBUG, "Clock", "Clock saved $mSecondsHandAngle $mMinutesHandAngle $mHoursHandAngle")
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is ClockState)
        {
            val ss : ClockState  = state
            this.mHoursHandAngle = ss.hoursAngle
            this.mMinutesHandAngle = ss.minutesAngle
            this.mSecondsHandAngle = ss.secondsAngle
            Log.println(Log.DEBUG, "Clock", "Clock restored $mSecondsHandAngle $mMinutesHandAngle $mHoursHandAngle")
        }
    }
}