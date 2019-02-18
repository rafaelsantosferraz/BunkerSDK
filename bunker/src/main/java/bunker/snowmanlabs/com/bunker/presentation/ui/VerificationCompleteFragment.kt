package bunker.snowmanlabs.com.bunker.presentation.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.di.Injectable
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModelFragment
import bunker.snowmanlabs.com.bunker.presentation.ui.custom.DimensionUtils
import kotlinx.android.synthetic.main.fragment_steps.*

class VerificationCompleteFragment: BaseViewModelFragment<ScanCnhViewModel>(), Injectable, WorkingListener {
    override fun getViewModel() = ScanCnhViewModel::class

    companion object {
        fun newInstance(): VerificationCompleteFragment = VerificationCompleteFragment()
    }

    override fun requestingService(bitmap: Bitmap) {

    }

    override fun onError() {

    }

    override fun stoppingService() {

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
        tv_title.text = getString(R.string.tx_verification_complete)
        tv_body.visibility = View.GONE
        bt_ready.text = getString(R.string.tx_finish)

        iv_scan.visibility = View.GONE
        cam_container.background = context!!.getDrawable(R.drawable.ic_check)

        bt_ready.setCompoundDrawables(null, null, null, null)
        bt_ready.setPadding(convertDpToPixel(85), 0, convertDpToPixel(85), 0)


        bt_ready.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate(ScanCnhFragment::class.java.simpleName, 0)
//           activity!!.supportFragmentManager.beginTransaction()
//                   .replace(R.id.main_container, activity!!.supportFragmentManager.findFragmentByTag(ScanCnhFragment::class.java.simpleName), ScanCnhFragment::class.java.simpleName)
//                   .addToBackStack(ScanCnhFragment::class.java.simpleName)
//                   .commit()
        }
    }

    private fun convertDpToPixel(dp: Int): Int{
        return DimensionUtils.convertDpToPixel(dp, context!!)
    }
}