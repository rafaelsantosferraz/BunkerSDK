package bunker.snowmanlabs.com.bunker.presentation.ui.main

import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor (
) : BaseViewModel<MainFragmentViewModel.State, MainFragmentViewModel.Command>() {

    private val TAG = "MainFragmentViewModel"

    override fun initialState() = State()

    data class State(val loading: Boolean? = true)

    sealed class Command
}