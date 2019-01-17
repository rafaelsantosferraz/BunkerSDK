package bunker.snowmanlabs.com.bunker.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseViewModelFragment<VM : ViewModel> : BaseFragment() {

    lateinit var viewModel: VM

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    open fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel().java)
    }

    abstract fun getViewModel(): KClass<VM>

}
