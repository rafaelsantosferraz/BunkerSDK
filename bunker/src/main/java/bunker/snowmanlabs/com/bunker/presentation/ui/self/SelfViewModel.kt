package bunker.snowmanlabs.com.bunker.presentation.ui.self

import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import bunker.snowmanlabs.com.bunker.presentation.api.RestApi
import bunker.snowmanlabs.com.bunker.di.qualifiers.IO
import bunker.snowmanlabs.com.bunker.di.qualifiers.UI
import bunker.snowmanlabs.com.bunker.presentation.ui.ScanProcessViewModel
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModel
import bunker.snowmanlabs.com.bunker.usecase.self.SendSelf
import io.reactivex.Scheduler
import java.security.InvalidKeyException
import java.util.*
import javax.inject.Inject

class SelfViewModel  @Inject constructor(
    private val sendSelf: SendSelf,
    @IO private val io: Scheduler,
    @UI private val ui: Scheduler
) : BaseViewModel<SelfViewModel.State, SelfViewModel.Command>() {


    override fun initialState() = State()


    var mSessionId: String = ""


    private fun getSessionId(): String {
        if (ScanProcessViewModel.mSessionId == null){
            mSessionId = UUID.randomUUID().toString()
        } else {
            mSessionId = ScanProcessViewModel.mSessionId!!
        }

        return mSessionId
    }


    //region sendSelf()
    fun sendSelf(convertImageToBase64: String?) {
        newState(currentState().copy(loading = true, error = null))
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
        if (result.data.isValid == "true"){
            newState(currentState().copy(loading = false, error = null))
            command.value = Command.SELFResult(result)
        }else{
            newState(currentState().copy(loading = false, error = InvalidKeyException()))
        }
    }

    private fun sendSelfHandleError(throwable: Throwable) {
        newState(currentState().copy(loading = false, error = throwable))
    }
    //endregion




    data class State(
        val loading: Boolean = false,
        val error: Throwable? = null)




    sealed class Command {
        class SELFResult(val selfResult: SelfResult): Command()
    }
}