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
import java.util.*
import kotlin.math.min

class Clock(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mCentreX = 0F
    private var mCentreY = 0F
    private var mRadius = 0F
    private var mPadding = 0F
    private var mPinRadius = 0F
    private var mInitialized = false

    private val mDialTagsColor: Int = Color.BLACK
    private val mHandsColor: Int = Color.DKGRAY
    private val mSecondsHandColor : Int = Color.RED
    private val mDialBgColor : Int = Color.WHITE
    private val mDialOutlineColor : Int = Color.BLACK
    private val mPinColor : Int = Color.WHITE

    private var mHoursHandPaint: Paint = Paint()
    private var mMinutesHandPaint: Paint = Paint()
    private var mSecondsHandPaint: Paint = Paint()
    private var mPrimaryDialTagsPaint: Paint = Paint()
    private var mSecondaryDialTagsPaint: Paint = Paint()
    private var mDialOutlinePaint: Paint = Paint()
    private var mDialBgPaint: Paint = Paint()
    private var mPinPaint: Paint = Paint()

    private var mDialOutlineLineWidth = 0f
    private var mDialPrimaryTagsLineWidth = 0f
    private var mDialSecondaryTagsLineWidth = 0f
    private var mHoursHandLineWidth = 0f
    private var mMinutesHandLineWidth = 0f
    private var mSecondsHandLineWidth = 0f

    private val mPrimaryTagsRect = Rect()
    private val mSecondaryTagsRect = Rect()
    private val mHoursHandRect = Rect()
    private val mMinutesHandRect = Rect()
    private val mSecondsHandRect = Rect()

    private var mHoursHandAngle = 0f
    private var mMinutesHandAngle = 0f
    private var mSecondsHandAngle = 0f

    private fun init() {
        if (mInitialized)
            return

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

        setHandsPosition(Calendar.getInstance())
        drawDial(canvas)
        drawHands(canvas)

        postInvalidateDelayed(1000);
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

    private fun setHandsPosition(calendar: Calendar) {
        mSecondsHandAngle = calendar.get(Calendar.SECOND) * 360f / 60f
        mMinutesHandAngle = calendar.get(Calendar.MINUTE) * 360f / 60f + mSecondsHandAngle / 60f
        mHoursHandAngle = calendar.get(Calendar.HOUR_OF_DAY) * 360f / 12f + mMinutesHandAngle / 12f
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putFloat("mHoursHandAngle", mHoursHandAngle)
        bundle.putFloat("mMinutesHandAngle", mMinutesHandAngle)
        bundle.putFloat("mSecondsHandAngle", mSecondsHandAngle)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle)
        {
            val bundle: Bundle = state
            this.mHoursHandAngle = bundle.getFloat("mHoursHandAngle")
            this.mMinutesHandAngle = bundle.getFloat("mMinutesHandAngle")
            this.mSecondsHandAngle = bundle.getFloat("mSecondsHangAngle")
        }
        super.onRestoreInstanceState(state)
    }
}