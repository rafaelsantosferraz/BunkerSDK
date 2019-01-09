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
import com.adityaarora.liveedgedetection.activity.ScanActivity
import kotlinx.android.synthetic.main.fragment_steps.*

class ScanCnhFragment: BaseViewModelFragment<ScanCnhViewModel>(), Injectable, WorkingListener {
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
        fun newInstance(): ScanCnhFragment = ScanCnhFragment()
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
        tv_title.text = getString(R.string.tx_document_photo)
        tv_body.text = getString(R.string.tx_body_1)
        bt_ready.text = getString(R.string.bt_ready)
        iv_scan.setImageDrawable(context!!.getDrawable(R.drawable.ic_id))

        bt_ready.setOnClickListener {
            startActivityForResult(Intent(context, ScanActivity::class.java), 200)
        }
    }
}