package bunker.snowmanlabs.com.bunker.presentation.ui.self

import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import bunker.snowmanlabs.com.bunker.di.qualifiers.IO
import bunker.snowmanlabs.com.bunker.di.qualifiers.UI
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModel
import bunker.snowmanlabs.com.bunker.presentation.ui.scan.ScanCnhViewModel
import bunker.snowmanlabs.com.bunker.usecase.self.SendSelf
import io.reactivex.Scheduler
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SelfViewModel  @Inject constructor(
    private val sendSelf: SendSelf,
    @IO private val io: Scheduler,
    @UI private val ui: Scheduler
) : BaseViewModel<SelfViewModel.State, SelfViewModel.Command>() {


    override fun initialState() = State()


    private fun getSessionId(): String {
        return ScanCnhViewModel.mSessionId
    }


    //region sendSelf() ------------------------------------------------------------------------------------------------
    fun sendSelf(convertImageToBase64: String?) {
        newState(currentState().copy(loading = true))
        val request = ProcessRequest(
            sessionId = getSessionId(),
            codedImage = convertImageToBase64!!
        )
        add(sendSelf.buildObservable(request)
            .subscribeOn(io)
            .observeOn(ui)
            .subscribe(::handleSelfResult, ::sendSelfHandleError))
    }

    private fun handleSelfResult(result: SelfResult) {
        newState(currentState().copy(loading = false))
        if (result.data.isValid == "true"){
            command.value = Command.SelfSuccess(result)
        }else{
            command.value = Command.SelfFailed(result)
        }
    }

    private fun sendSelfHandleError(throwable: Throwable) {
        newState(currentState().copy(loading = false))
        command.value = Command.Error(throwable)
    }
    //endregion




    data class State(
        val loading: Boolean = false)




    sealed class Command {
        class SelfSuccess(val selfResult: SelfResult): Command()
        class SelfFailed(val selfResult: SelfResult): Command()
        class Error(val error: Throwable): Command()
    }
}