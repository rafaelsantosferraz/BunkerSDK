package bunker.snowmanlabs.com.bunker.presentation.ui

import android.graphics.Bitmap

interface WorkingListener {
    fun requestingService(bitmap: Bitmap)
    fun onError()
    fun stoppingService()
}