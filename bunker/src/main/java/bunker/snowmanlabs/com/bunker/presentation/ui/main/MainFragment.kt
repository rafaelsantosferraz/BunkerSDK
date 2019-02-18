package bunker.snowmanlabs.com.bunker.presentation.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.di.Injectable
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModelFragment
import kotlinx.android.synthetic.main.activity_start.*
import kotlin.reflect.KClass


class MainFragment : BaseViewModelFragment<MainFragmentViewModel>(), Injectable {




    // region Fragment superclass -------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }
    // endregion



    // BaseViewModelFragment -----------------------------------------------------------------------
    override fun getViewModel(): KClass<MainFragmentViewModel> = MainFragmentViewModel::class




    // region Setup --------------------------------------------------------------------------------
    private fun setupListeners(){
        bt_ready.setOnClickListener {
            //MainFragmentDirections.actionMainFragmentToFormFragment()
            //val action = MainFragmentDirections.actionMainFragmentToFormFragment()
//            action.pacienteCuidadoId = item.pacienteCuidado?.id!!.toString()
//            action.previsto = item.previsto!!.toString()
            //findNavController(this@MainFragment).navigate(action)
            //startActivity(Intent(this, ScanProcessActivity::class.java))
        }
    }
    // endregion
}
