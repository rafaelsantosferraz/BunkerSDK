package bunker.snowmanlabs.com.bunker.presentation.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : BaseFragment() {




    // region Fragment -------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }
    // endregion




    // region Setup ----------------------------------------------------------------------------------------------------
    private fun setupListeners(){
        main_fragment_start_bt.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToScanCnhFragment()
            findNavController(this@MainFragment).navigate(action)
        }
    }
    // endregion
}
