package bunker.snowmanlabs.com.bunker.utils

import android.util.Log
import bunker.snowmanlabs.com.bunker.ui.self.SelfFragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.otaliastudios.cameraview.Frame


class FaceDetector {

    companion object {
        private const val RIGHT_ANGLE = 90
    }


    private val TAG = "FaceDetector"
    private val detector: FirebaseVisionFaceDetector by lazy {
        FirebaseVision.getInstance().getVisionFaceDetector(realTimeOpts)
    }
    private val realTimeOpts: FirebaseVisionFaceDetectorOptions by lazy {
        FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build()
    }


    fun process(frame: Frame) {
        val image = FirebaseVisionImage.fromByteArray(frame.data, extractFrameMetadata(frame))

        detector.detectInImage(image)
            .addOnSuccessListener { faces ->
                for (face in faces) {
                    if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        val smileProb = face.smilingProbability
                    }

                    if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        val rightEyeOpenProb = face.rightEyeOpenProbability
                        Log.d(TAG, rightEyeOpenProb.toString() + " right eye prob")
                        if (rightEyeOpenProb < 0.4) {
                            SelfFragment.isValid = true
                            Log.d(TAG, rightEyeOpenProb.toString() + " right close")
                        }
                    }

                    if (face.leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        val leftEyeOpenProb = face.leftEyeOpenProbability
                        Log.d(TAG, leftEyeOpenProb.toString() + " left eye prob")
                        if (leftEyeOpenProb < 0.4) {
                            SelfFragment.isValid = true
                            Log.d(TAG, leftEyeOpenProb.toString() + " left close")
                        }
                    }
                }
            }
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(e: Exception) {
                }
            })
    }



    private fun extractFrameMetadata(frame: Frame): FirebaseVisionImageMetadata =
        FirebaseVisionImageMetadata.Builder()
            .setWidth(frame.size.width)
            .setHeight(frame.size.height)
            .setFormat(frame.format)
            .setRotation(frame.rotation / RIGHT_ANGLE)
            .build()


}