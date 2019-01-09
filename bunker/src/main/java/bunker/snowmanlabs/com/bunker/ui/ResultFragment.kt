package bunker.snowmanlabs.com.bunker.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.data.CnhResult
import bunker.snowmanlabs.com.bunker.di.Injectable
import bunker.snowmanlabs.com.bunker.ui.base.BaseViewModelFragment
import com.adityaarora.liveedgedetection.activity.SelfScanActivity
import kotlinx.android.synthetic.main.cnh_data_container.*
import kotlinx.android.synthetic.main.fragment_steps.*

class ResultFragment : BaseViewModelFragment<ScanCnhViewModel>(), Injectable, WorkingListener {
    override fun onError() {
        bt_ready.isEnabled = true
    }

    override fun requestingService(bitmap: Bitmap) {

    }

    override fun stoppingService() {
        bt_ready.isEnabled = true
    }

    override fun getViewModel() = ScanCnhViewModel::class

    companion object {
        private const val RESULT = "result"
        fun newInstance(result: CnhResult): ResultFragment{
            val fragment = ResultFragment()
            val args = Bundle()
            args.putSerializable(RESULT, result)
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
        val cnhData = (arguments?.getSerializable(RESULT) as CnhResult).data.result

        var firstname = cnhData.nome.split(" ")[0]

        tv_title.text = String.format(getString(R.string.tx_result_name), firstname)

        tv_body.text = getString(R.string.tx_result_body)

        cam_container.visibility = View.GONE


        bt_ready.text = getString(R.string.bt_confirm)

        bt_ready.setOnClickListener {
            val fragment = VerificationCompleteFragment.newInstance()
            fragmentManager!!.beginTransaction()
                    .replace(R.id.main_container, fragment, VerificationCompleteFragment::class.simpleName)
                    .addToBackStack(VerificationCompleteFragment::class.simpleName)
                    .commit()
        }

        result_container.visibility = View.VISIBLE
        tv_result_name.text = cnhData.nome
        tv_result_cpf.text = cnhData.cpf
        tv_result_cnh.text = String.format(getString(R.string.result_cnh_body), cnhData.registro, cnhData.validade, cnhData.categoria)
    }
}