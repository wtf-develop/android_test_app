package ru.wtfdev.kitty.utils

import android.view.GestureDetector
import android.view.MotionEvent

class CloseGestureListener(val closeMe: () -> Unit) :
    GestureDetector.SimpleOnGestureListener() {
    override fun onDown(event: MotionEvent): Boolean {
        return true
    }


    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (Math.abs(velocityX) * 3f < Math.abs(velocityY)) {
            closeMe()
        }
        return super.onFling(e1, e2, velocityX, velocityY)
    }
}

