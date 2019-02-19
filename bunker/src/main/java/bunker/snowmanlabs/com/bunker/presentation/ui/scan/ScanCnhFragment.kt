package bunker.snowmanlabs.com.bunker.presentation.ui.scan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.di.Injectable
import bunker.snowmanlabs.com.bunker.domain.ScanResult
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModelFragment
import bunker.snowmanlabs.com.bunker.utils.ImageConverter
import com.adityaarora.liveedgedetection.activity.ScanActivity
import com.adityaarora.liveedgedetection.constants.ScanConstants
import com.adityaarora.liveedgedetection.util.ScanUtils
import kotlinx.android.synthetic.main.fragment_steps.*

class ScanCnhFragment: BaseViewModelFragment<ScanCnhViewModel>(), Injectable {


    override fun getViewModel() = ScanCnhViewModel::class


    private val TAG = "ScanCnhFragment"




    //region Fragment --------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_steps, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        setupObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1) {
            data?.let {
                if (it.hasExtra(ScanConstants.SCANNED_RESULT)) {
                    val filePath = data.extras?.getString(ScanConstants.SCANNED_RESULT)
                    val baseBitmap = ScanUtils.decodeBitmapFromFile(filePath, ScanConstants.IMAGE_NAME)

                    iv_scan.setImageBitmap(baseBitmap)
                    bt_ready.isEnabled = false

                    viewModel.sendScan(ImageConverter.convertImageToBase64(filePath + "/" + ScanConstants.IMAGE_NAME))

                }
            }
        }
    }
    //endregion




    //region Setup ---------------------------------------------------------------------------------------------------
    private fun setupViews() {
        tv_title.text = getString(R.string.tx_document_photo)
        tv_body.text = getString(R.string.tx_body_1)
        bt_ready.text = getString(R.string.bt_ready)
        iv_scan.setImageDrawable(context!!.getDrawable(R.drawable.ic_id))
    }

    private fun setupListeners(){
        bt_ready.setOnClickListener {
            startActivityForResult(Intent(context, ScanActivity::class.java), 1)
        }
    }

    private fun setupObservers(){
        viewModel.command.observe(this, Observer {
            when(it){
                is ScanCnhViewModel.Command.ScanSuccess -> handleScanSuccess(it.result)
                is ScanCnhViewModel.Command.ScanFailed -> handleScanFailed(it.result)
                is ScanCnhViewModel.Command.Error -> handleError(it.error)
            }
        })
        viewModel.state.observe(this, Observer {
            it?.apply {
                render(it)
            }
        })
    }
    //endregion




    //region Command ---------------------------------------------------------------------------------------------------
    private fun handleScanSuccess(scanResult: ScanResult) {
        val action = ScanCnhFragmentDirections.actionScanCnhFragmentToSelfFragment()
        findNavController().navigate(action)
    }

    // todo: Nao continuar o fluxo
    private fun handleScanFailed(scanResult: ScanResult) {
        val action = ScanCnhFragmentDirections.actionScanCnhFragmentToSelfFragment()
        findNavController().navigate(action)
    }

    // todo: Nao continuar o fluxo
    override fun handleError(throwable: Throwable) {
        super.handleError(throwable)
        val action = ScanCnhFragmentDirections.actionScanCnhFragmentToSelfFragment()
        findNavController().navigate(action)
    }
    //endregion




    //region State -----------------------------------------------------------------------------------------------------
    private fun render(state: ScanCnhViewModel.State) {
        Log.d(TAG, "render($state)")

        part("loading", state.loading) {
            if (it != null) {
                handleLoading(it)
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            fragment_steps_loading_fl.visibility = View.VISIBLE
        } else {
            fragment_steps_loading_fl.visibility = View.GONE
        }
    }
}