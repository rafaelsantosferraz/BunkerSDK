package bunker.snowmanlabs.com.bunker.ui

import androidx.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Base64
import android.view.View
import android.widget.Toast
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.data.CnhResult
import bunker.snowmanlabs.com.bunker.data.SelfResult
import bunker.snowmanlabs.com.bunker.ui.base.BaseViewModelActivity
import bunker.snowmanlabs.com.bunker.ui.self.SelfFragment
import com.adityaarora.liveedgedetection.constants.ScanConstants
import com.adityaarora.liveedgedetection.util.ScanUtils
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import kotlinx.android.synthetic.main.activity_scan_process.*
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.security.InvalidKeyException

class ScanProcessActivity : BaseViewModelActivity<ScanProcessViewModel>() {
    override fun getViewModel() = ScanProcessViewModel::class
    private lateinit var currentFragment: WorkingListener
    private var cnhResult: CnhResult? = null
    private var selfResult: SelfResult? = null
    lateinit var detector: FirebaseVisionFaceDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scan_process)
        setOnBackstackChangedListener()

        initScanSteps()
        initObservers()
        initCommandObservers()

        FirebaseApp.initializeApp(applicationContext)

        val highAccuracyOpts = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build()



        detector = FirebaseVision.getInstance()
            .getVisionFaceDetector(highAccuracyOpts)

    }


    private fun initCommandObservers() {
        viewModel.command.observe(this, Observer {
            when(it){
                is ScanProcessViewModel.Command.CNHResult -> handleCnhResultSuccess(it.cnhResult)
                is ScanProcessViewModel.Command.SELFResult -> handleSelfResult(it.selfResult)
            }
        })
    }

    fun handleSelfResult(selfResult: SelfResult?) {
        this.selfResult = selfResult
        if (selfResult == null) {
            currentFragment.onError()
        } else {
            currentFragment.stoppingService()
            handleNextStep()
        }
    }

    private fun initObservers() {
        viewModel.state.observe(this, Observer {
            it?.let {
                if (it.loading) {
                    setLoading(true)
                } else {
                    setLoading(false)
                }

                if (it.error != null) {
                    handleError(it.error)
                }
            }
        })
    }

    private fun handleError(error: Throwable) {
        currentFragment.onError()
        if (error is InvalidKeyException){
            showError(InvalidKeyException(getString(R.string.result_invalid)))
        }else{
            showError(Throwable(getString(R.string.result_invalid)))
        }
    }

    private fun showError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

    private fun handleCnhResultSuccess(result: CnhResult?) {
        cnhResult = result
        if (cnhResult == null) {
            currentFragment.onError()
        } else {
            currentFragment.stoppingService()
            handleNextStep()
        }
    }

    private fun handleNextStep() {
        when(currentFragment){
            is ScanCnhFragment -> {
                cnhResult?.let {
                    replaceFragment(SelfFragment.newInstance(),
                            SelfFragment::class.simpleName!!)
                }
            }
            is SelfFragment -> {
                cnhResult?.let {
                    replaceFragment(ResultFragment.newInstance(it),
                            ResultFragment::class.simpleName!!)
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            loading_container.visibility = View.VISIBLE
        } else {
            loading_container.visibility = View.GONE
        }
    }

    private fun initScanSteps() {
        replaceFragment(ScanCnhFragment.newInstance(),
                ScanCnhFragment::class.simpleName!!)

//        replaceFragment(SelfFragment.newInstance(),
//            SelfFragment::class.simpleName!!)

//        replaceFragment(ScanSelfFragment.newInstance("teste"),
//                ScanSelfFragment::class.simpleName!!)
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
//        currentFragment = fragment as WorkingListener
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    private fun setOnBackstackChangedListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            setFragmentTitle()
        }
    }

    private fun setFragmentTitle() {
        when (supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1].tag) {
            ScanCnhFragment::class.java.simpleName -> handleFirstFragment()
            ScanSelfFragment::class.java.simpleName -> setupStepIndicator(1)
            ResultFragment::class.java.simpleName -> setupStepIndicator(2)
            VerificationCompleteFragment::class.java.simpleName -> setupStepIndicator(3)
        }

        handlePreviousFragment()
    }

    fun handleFirstFragment() {
        setupStepIndicator(0)
        viewModel.resetSessionId()
    }

    private fun setupStepIndicator(step: Int) {
        step_indicator.setStep(step)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            if (it.hasExtra(ScanConstants.SCANNED_RESULT)) {
                val filePath = data.extras.getString(ScanConstants.SCANNED_RESULT)
                val baseBitmap = ScanUtils.decodeBitmapFromFile(filePath, ScanConstants.IMAGE_NAME)

                currentFragment.requestingService(baseBitmap)

                viewModel.sendCnhPicture(convertImageToBase64(filePath + "/" + ScanConstants.IMAGE_NAME))

            } else if (it.hasExtra(ScanConstants.SCANNED_SELF_RESULT)) {
                val filePath = data.extras.getString(ScanConstants.SCANNED_SELF_RESULT)
                val baseBitmap = ScanUtils.decodeBitmapFromFile(filePath, "")

                currentFragment.requestingService(baseBitmap)
                viewModel.sendSelfPicture(convertImageToBase64(filePath))
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
//            val baos = ByteArrayOutputStream()
//            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos) //bm is the bitmap object
//            val byteArrayImage = baos.toByteArray()
//
//            viewModel.sendSelfPicture( Base64.encodeToString(byteArrayImage, Base64.DEFAULT))


            val image = FirebaseVisionImage.fromBitmap(bitmap)
            detector.detectInImage(image)
                .addOnSuccessListener { faces ->
                    for (face in faces) {

                        if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            val smileProb = face.smilingProbability
                            Toast.makeText(this, smileProb.toString(), Toast.LENGTH_LONG).show()

                            val baos = ByteArrayOutputStream()
                            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos) //bm is the bitmap object
                            val byteArrayImage = baos.toByteArray()

                            viewModel.sendSelfPicture( Base64.encodeToString(byteArrayImage, Base64.DEFAULT))
                        }

                        if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            val rightEyeOpenProb = face.rightEyeOpenProbability
                        }

                        if (face.leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                            val leftEyeOpenProb = face.leftEyeOpenProbability
                        }
                    }
                }
                .addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(e: Exception) {
                        }
                })
        }
    }

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

    private fun decodeFile(f: String): Bitmap? {
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

    private fun handleOnBackPressed(): Boolean {
        //back stack count return the size, so we decrease 2 to get the last but one element of the stack
        return supportFragmentManager.backStackEntryCount - 1 <= 0
    }

    override fun onBackPressed() {
        if (!handleOnBackPressed()) {
            super.onBackPressed()
            handlePreviousFragment()
        } else {
            finish()
        }
    }

    private fun handlePreviousFragment() {
        currentFragment = supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] as WorkingListener
    }
}