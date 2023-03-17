package com.ead.project.dreamer.data.utils.ui

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.ead.project.dreamer.R
import com.ead.project.dreamer.app.DreamerApp
import com.google.android.material.snackbar.Snackbar


class DreamerLayout {

    companion object {

        fun setColorFilter(drawable: Drawable, @ColorInt color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                @Suppress("DEPRECATION") drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }


        private fun getColor(colorId: Int) = ContextCompat.getColor(DreamerApp.Instance, colorId)

        @SuppressLint("CutPasteId")
        fun showSnackbar(view: View, text : String, color: Int = R.color.blackPrimary,size : Int = R.dimen.snackbar_text_size,length : Int = Snackbar.LENGTH_SHORT) {
            val snackbar: Snackbar = Snackbar.make(view, text, length)
            snackbar.setBackgroundTint(ContextCompat.getColor(DreamerApp.Instance, color))
            val viewGroup = snackbar.view
                .findViewById<View>(com.google.android.material.R.id.snackbar_text).parent as ViewGroup
            viewGroup.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            viewGroup.setPadding(64,0,64,0)
            val textView = snackbar.view
                .findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DreamerApp.Instance.resources.getDimension(size))

            if (length == Snackbar.LENGTH_INDEFINITE) {
                val progressBar = ProgressBar(DreamerApp.Instance)

                setColorFilter(progressBar.indeterminateDrawable, getColor(R.color.blue_light))
                viewGroup.addView(progressBar)
                progressBar.layoutParams.height = 80
                (progressBar.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER
            }
            snackbar.show()
        }
    }
}