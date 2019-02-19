package bunker.snowmanlabs.com.bunker.presentation.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_result, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        ResultFragmentArgs.fromBundle(arguments!!).apply{
            tv_result_name.text = nome
            tv_result_cpf.text = cpf
            tv_result_rg.text = rg

            tv_result_cnh.text = String.format(getString(R.string.result_cnh_body),
                registro,
                validade,
                categoria,
                dataNascimento,
                nomeMae,
                nomePai)
        }
    }
}