package bunker.snowmanlabs.com.bunker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeFile
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException

class ImageConverter {

    companion object {

        fun convertImageToBase64(imageUri: String): String? {
            try {
                val bitmap = decodeFile(imageUri)
                val baos = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos) //bm is the bitmap object
                val byteArrayImage = baos.toByteArray()

                return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }

        fun decodeFile(f: String): Bitmap? {
            try {
                // Decode image size
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeStream(FileInputStream(f), null, o)

                // The new size we want to scale to
                val REQUIRED_SIZE = 512

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                    scale *= 2
                }

                // Decode with inSampleSize
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                return BitmapFactory.decodeStream(FileInputStream(f), null, o2)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return null
        }
    }
}