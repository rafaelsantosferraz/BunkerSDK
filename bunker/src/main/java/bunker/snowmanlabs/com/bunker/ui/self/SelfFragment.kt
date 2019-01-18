package bunker.snowmanlabs.com.bunker.ui.self


import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.data.SelfResult
import bunker.snowmanlabs.com.bunker.di.Injectable
import bunker.snowmanlabs.com.bunker.ui.ScanProcessActivity
import bunker.snowmanlabs.com.bunker.ui.WorkingListener
import bunker.snowmanlabs.com.bunker.ui.base.BaseViewModelFragment
import bunker.snowmanlabs.com.bunker.utils.FaceDetector
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.FrameProcessor
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.activity_scan_process.*
import kotlinx.android.synthetic.main.self_fragment.*
import java.io.ByteArrayOutputStream
import kotlin.reflect.KClass


class SelfFragment : BaseViewModelFragment<SelfViewModel>(), Injectable, WorkingListener {


    override fun getViewModel(): KClass<SelfViewModel> = SelfViewModel::class

    companion object {
        @JvmStatic
        fun newInstance() = SelfFragment()

        var isValid = false
    }

    private val TAG = "SelfFragment"
    private var isButtonHold = false
    private val timer = object : CountDownTimer(10000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            self_fragment_timer.setText("" + millisUntilFinished / 1000)
            //here you can have your logic to set text to edittext
        }

        override fun onFinish() {
            self_fragment_timer.setText("done!")
        }
    }

    private var faceDetector: FaceDetector? = null
    private var frameProcessor =
        FrameProcessor {
            if(it.size != null && isButtonHold) {
                faceDetector?.process(it)
            }
        }
    private var down: Long = 0
    private var up: Long = 0




    //region Fragment --------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.self_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).actionBar?.hide()
        self_fragment_camera_view.setLifecycleOwner(viewLifecycleOwner)

        setupCamera()
        setupObservers()
        setupListeners()

    }

    override fun onStop() {
        super.onStop()
        self_fragment_camera_view.clearFrameProcessors()
    }
    // endregion




    //region WorkingListener -------------------------------------------------------------------------------------------
    override fun requestingService(bitmap: Bitmap) {
    }

    override fun onError() {
        Toast.makeText(activity!!, "Face not recognized. Try Again", Toast.LENGTH_LONG).show()
    }

    override fun stoppingService() {
        Log.d(TAG, "stoppingService")
        self_fragment_camera_view.clearFrameProcessors()

    }
    // endregion




    //region Setup() ---------------------------------------------------------------------------------------------------
    private fun setupCamera(){
        self_fragment_camera_view.toggleFacing()
    }

    private fun setupObservers() {
        viewModel.command.observe(this, Observer {
            when(it){
                is SelfViewModel.Command.SELFResult -> handleSelfResult(it.selfResult)
            }
        })
        viewModel.state.observe(this, Observer {
            it?.apply {
                if (loading) {
                    setLoading(true)
                } else {
                    setLoading(false)
                }

                if (error != null) {
                    handleError(error)
                }
            }
        })

    }

    private fun setupListeners(){
        self_fragment_button.setOnTouchListener { view, motionEvent ->
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    onButtonDown()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    onButtonUp()
                    true
                }
                else -> false
            }
        }

        self_fragment_camera_view.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap{
                    val baos = ByteArrayOutputStream()
                    it!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val byteArrayImage = baos.toByteArray()
                    viewModel.sendSelfPicture( Base64.encodeToString(byteArrayImage, Base64.DEFAULT))
                    Log.d(TAG, "send")
                }
            }
        })
    }
    //endregion


    //region Private() -------------------------------------------------------------------------------------------------
    private fun handleSelfResult(selfResult: SelfResult?) {
        if (selfResult == null) {
        } else {
            (activity!! as ScanProcessActivity).handleSelfResult(selfResult)
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            self_fragment_loading.visibility = View.VISIBLE
        } else {
            self_fragment_loading.visibility = View.GONE
        }
    }

    private fun onButtonDown(){
        Log.d(TAG, "Down")
        isButtonHold = true
        timer.start()
        faceDetector = FaceDetector()
        self_fragment_camera_view.addFrameProcessor(frameProcessor)

        down = System.currentTimeMillis()
    }

    private fun onButtonUp(){
        Log.d(TAG, "Up")
        isButtonHold = false
        timer.cancel()
        faceDetector = null
        self_fragment_timer.text = "10"
        self_fragment_camera_view.removeFrameProcessor(frameProcessor)

        up = System.currentTimeMillis()

        if ((up - down) > 9000) {
            Log.d(TAG, "More then 10 seconds")
            if (isValid) {
                self_fragment_camera_view.takePictureSnapshot()
                Log.d(TAG, "isValid")
            } else {
                Toast.makeText(activity!!,"Move your eyes and blink naturally.", Toast.LENGTH_LONG).show()
            }
        }

        isValid = false
    }
}
