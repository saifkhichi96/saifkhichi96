package com.saifkhichi.app.core.util

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors
import com.saifkhichi.app.R

object ColorUtils {

    fun recolor(textView: TextView, o: Any) {
        val colors = convertToColor(textView.context, o)
        textView.setBackgroundColor(colors.accent)
        textView.setTextColor(colors.onAccent)
    }

    fun recolorAsContainer(textView: TextView, o: Any) {
        val colors = convertToColor(textView.context, o)
        textView.setBackgroundColor(colors.accentContainer)
        textView.setTextColor(colors.onAccentContainer)
    }

    fun convertToColor(context: Context, o: Any): ColorRoles {
        val baseColor = try {
            val i = o.hashCode()
            Color.parseColor(
                "#FF" + Integer.toHexString(i shr 16 and 0xFF) +
                        Integer.toHexString(i shr 8 and 0xFF) +
                        Integer.toHexString(i and 0xFF)
            )
        } catch (ignored: Exception) {
            context.getColor(R.color.colorPrimary)
        }

        val harmonizedColor = MaterialColors.harmonizeWithPrimary(context, baseColor)
        return MaterialColors.getColorRoles(context, harmonizedColor)
    }

}