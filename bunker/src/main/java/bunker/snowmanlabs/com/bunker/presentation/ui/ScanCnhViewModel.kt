package bunker.snowmanlabs.com.bunker.presentation.ui

import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class ScanCnhViewModel @Inject constructor(): BaseViewModel<ScanCnhViewModel.State, ScanCnhViewModel.Command>() {
    override fun initialState() = State()


    fun sendCnhPicture(convertImageToBase64: String?) {

    }

    private fun getSessionId(): String? {
        return "ABC"
    }

    fun sendSelfPicture(convertImageToBase64: String?) {

    }


    data class State(val loading: Boolean = false,
                     val result: String? = "Result")

    sealed class Command {

    }
}