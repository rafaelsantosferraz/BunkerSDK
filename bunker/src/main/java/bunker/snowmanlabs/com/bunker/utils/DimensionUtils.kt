package bunker.snowmanlabs.com.bunker.utils

import android.content.Context
import android.util.DisplayMetrics

class DimensionUtils {
    companion object {
        fun convertDpToPixel(dp: Int, context: Context): Int {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        }
    }
}