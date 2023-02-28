package com.mgsds.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.*
import java.util.*
import kotlin.math.min

class Clock(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mCentreX = 0F
    private var mCentreY = 0F
    private var mRadius = 0F
    private var mPaint: Paint = Paint()
    private var mInitialized = false
    private val mColor: Int = Color.BLACK
    private val mHandsColor: Int = Color.GRAY
    private var mLineWidth = 15F
    private val mRect = Rect()
    private val mBgColor: Int = Color.WHITE
    private val mSecondsHandColor : Int = Color.RED
    private var mHourHangAngle = 0f
    private var mMinuteHangAngle = 0f
    private var mSecondsHangAngle = 0f

    private fun init() {
        if (mInitialized)
            return

        mCentreX = width.toFloat() / 2
        mCentreY = height.toFloat() / 2
        mInitialized = true
        mRadius = min(mCentreX, mCentreY) - mLineWidth / 2
        mInitialized = true
        mLineWidth = min(width, height) * 0.01f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!mInitialized)
            init()

        val currentTimestamp = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        calendar.timeInMillis = currentTimestamp

        setHandsPosition(calendar)
        drawDial(canvas)
        drawHands(canvas)

        postInvalidateDelayed(500);
        invalidate();
    }

    private fun drawDial(canvas: Canvas?) {
        drawCircle(canvas)
        drawTags(canvas)
    }

    private fun drawCircle(canvas: Canvas?) {
        mPaint.reset()
        mPaint.color = mBgColor
        mPaint.style = Paint.Style.FILL;
        mPaint.isAntiAlias = true;
        canvas?.drawCircle(mCentreX, mCentreY, mRadius, mPaint)
        mPaint.strokeWidth = mLineWidth;
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE;
        mPaint.isAntiAlias = true;
        canvas?.drawCircle(mCentreX, mCentreY, mRadius, mPaint)
    }

    private fun drawTags(canvas : Canvas?) {
        mPaint.reset()
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE;
        mPaint.isAntiAlias = true;
        val x1 = mCentreX - mRadius
        val x2 = mCentreX - mRadius + mRadius / 6
        val y = mCentreY
        val shortX2 = mCentreX - mRadius + mRadius / 8

        for (i in 0 until 12) {
            val degree = 360F / 12F
            canvas?.rotate(degree, mCentreX, mCentreY)
            if (i % 3 == 2) {
                mRect.set(x1.toInt(), y.toInt(), x2.toInt(), y.toInt())
                mPaint.strokeWidth = mLineWidth * 2;
                canvas?.drawRect(mRect, mPaint)
            }
            else{
                mRect.set(x1.toInt(), y.toInt(), shortX2.toInt(), y.toInt())
                mPaint.strokeWidth = mLineWidth;
                canvas?.drawRect(mRect, mPaint)
            }
        }
    }

    private fun drawHands(canvas: Canvas?) {
        drawHourHand(canvas)
        drawMinuteHand(canvas)
        drawSecondsHand(canvas)
        drawPin(canvas)
    }


    private fun drawHand(canvas: Canvas?,paint: Paint, length: Float, offset: Float)
    {
        val y1 = mCentreY + offset
        val y2 = mCentreY - (length - offset)
        val x = mCentreX
        mRect.set(x.toInt(), y1.toInt(), x.toInt(), y2.toInt())
        canvas?.drawRect(mRect, mPaint)
    }

    private fun drawHourHand(canvas: Canvas?) {
        mPaint.reset()
        mPaint.color = mHandsColor
        mPaint.style = Paint.Style.STROKE;
        mPaint.strokeWidth = mLineWidth * 2;
        mPaint.isAntiAlias = true;
        canvas?.rotate(mHourHangAngle, mCentreX, mCentreY)
        drawHand(canvas, mPaint, mRadius/3*2, mRadius/10)
        canvas?.rotate(-mHourHangAngle, mCentreX, mCentreY)
    }

    private fun drawMinuteHand(canvas: Canvas?) {
        mPaint.reset()
        mPaint.color = mHandsColor
        mPaint.style = Paint.Style.STROKE;
        mPaint.strokeWidth = mLineWidth;
        mPaint.isAntiAlias = true;
        canvas?.rotate(mMinuteHangAngle, mCentreX, mCentreY)
        drawHand(canvas, mPaint, mRadius * 1.15f, mRadius/5)
        canvas?.rotate(-mMinuteHangAngle, mCentreX, mCentreY)
    }

    private fun drawSecondsHand(canvas: Canvas?) {
        mPaint.reset()
        mPaint.color = mSecondsHandColor;
        mPaint.style = Paint.Style.STROKE;
        mPaint.strokeWidth = mLineWidth/2;
        mPaint.isAntiAlias = true;
        canvas?.rotate(mSecondsHangAngle, mCentreX, mCentreY)
        drawHand(canvas, mPaint, mRadius * 1.15f, mRadius/5)
        canvas?.rotate(-mSecondsHangAngle, mCentreX, mCentreY)
    }

    private fun drawPin(canvas: Canvas?) {
        mPaint.reset()
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL_AND_STROKE;
        mPaint.strokeWidth = mLineWidth/5;
        mPaint.isAntiAlias = true;
        canvas?.drawCircle(mCentreX, mCentreY, mRadius/60, mPaint)
    }

    private fun setHandsPosition(calendar: Calendar) {
        mSecondsHangAngle = calendar.get(Calendar.SECOND) * 360f / 60f
        mMinuteHangAngle = calendar.get(Calendar.MINUTE) * 360f / 60f + mSecondsHangAngle / 60f
        mHourHangAngle = calendar.get(Calendar.HOUR_OF_DAY) * 360f / 12f + mMinuteHangAngle / 12f
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putFloat("mHourHangAngle", mHourHangAngle)
        bundle.putFloat("mMinuteHangAngle", mMinuteHangAngle)
        bundle.putFloat("mSecondsHangAngle", mSecondsHangAngle)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle)
        {
            val bundle: Bundle = state
            this.mHourHangAngle = bundle.getFloat("mHourHangAngle")
            this.mMinuteHangAngle = bundle.getFloat("mMinuteHangAngle")
            this.mSecondsHangAngle = bundle.getFloat("mSecondsHangAngle")
        }
        super.onRestoreInstanceState(state)
    }
}