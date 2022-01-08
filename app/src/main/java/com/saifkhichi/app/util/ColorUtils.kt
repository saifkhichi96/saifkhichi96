package com.saifkhichi.app.util

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors
import com.saifkhichi.app.R

object ColorUtils {

    fun recolorButton(button: TextView, o: Any) {
        val colors = convertToColor(button.context, o)
        button.setBackgroundColor(colors.accent)
        button.setTextColor(colors.onAccent)
    }

    fun recolorButtonAsContainer(button: TextView, o: Any) {
        val colors = convertToColor(button.context, o)
        button.setBackgroundColor(colors.accentContainer)
        button.setTextColor(colors.onAccentContainer)
    }

    private fun convertToColor(context: Context, o: Any): ColorRoles {
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