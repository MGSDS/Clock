package com.mgsds.clock.clockView

import android.os.Parcel
import android.os.Parcelable
import android.view.View.BaseSavedState

class ClockState : BaseSavedState {
    var secondsAngle = 0F
    var hoursAngle = 0F
    var minutesAngle = 0F

    constructor(superState: Parcelable?) : super(superState)
    constructor(parcel: Parcel) : super(parcel){
        secondsAngle = parcel.readFloat()
        minutesAngle = parcel.readFloat()
        hoursAngle = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeFloat(secondsAngle)
        parcel.writeFloat(minutesAngle)
        parcel.writeFloat(hoursAngle)
    }

    companion object CREATOR : Parcelable.Creator<ClockState> {
        override fun createFromParcel(parcel: Parcel): ClockState {
            return ClockState(parcel)
        }

        override fun newArray(size: Int): Array<ClockState?> {
            return arrayOfNulls(size)
        }
    }

}