package com.greeting.greet_app.sticker

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.gestures.Orientation


class OpositySeekbar(context: Context) : androidx.appcompat.widget.AppCompatSeekBar(context) {

    var linearGradient: GradientDrawable? = null

    init {
        linearGradient = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.RED, Color.RED)
        )

        background = linearGradient
        indeterminateDrawable = linearGradient
    }

    fun setColor(color:Array<Int>){
        linearGradient!!.colors = color.toIntArray()
    }
}