package bunker.snowmanlabs.com.bunker.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseViewModelFragment<VM : ViewModel> : BaseFragment() {

    lateinit var viewModel: VM

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel().java)
    }

    abstract fun getViewModel(): KClass<VM>

}
