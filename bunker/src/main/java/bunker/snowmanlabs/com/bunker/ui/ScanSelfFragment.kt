package bunker.snowmanlabs.com.bunker.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.di.Injectable
import bunker.snowmanlabs.com.bunker.ui.base.BaseViewModelFragment
import com.adityaarora.liveedgedetection.activity.CameraActivity
import com.adityaarora.liveedgedetection.activity.ScanActivity
import com.adityaarora.liveedgedetection.activity.SelfScanActivity
import kotlinx.android.synthetic.main.fragment_steps.*

class ScanSelfFragment : BaseViewModelFragment<ScanCnhViewModel>(), Injectable, WorkingListener {
    override fun onError() {
        bt_ready.isEnabled = true
    }

    override fun requestingService(bitmap: Bitmap) {
        iv_scan.setImageBitmap(bitmap)
        bt_ready.isEnabled = false
    }

    override fun stoppingService() {
        bt_ready.isEnabled = true
    }

    override fun getViewModel() = ScanCnhViewModel::class

    companion object {
        private const val NAME = "name"
        fun newInstance(name: String): ScanSelfFragment{
            val fragment = ScanSelfFragment()
            val args = Bundle()
            args.putString(NAME, name)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_steps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
    }

    private fun initObservers() {

    }

    private fun initViews() {
        var firstname = ""
        arguments?.getString(NAME)?.let {
            firstname = it.split(" ")[0]
        }

        tv_title.text = String.format(getString(R.string.tx_self_name), firstname)
//        tv_body.text = "Text Body Second Step"
        tv_body.visibility = View.GONE
        bt_ready.text = getString(R.string.bt_ready)
        iv_scan.setImageDrawable(context!!.getDrawable(R.drawable.ic_face))

        bt_ready.setOnClickListener {
            startActivityForResult(CameraActivity.newIntent(firstname, activity), 200)
        }

        //start selfie capture
        startActivityForResult(CameraActivity.newIntent(firstname, activity), 200)
    }
}