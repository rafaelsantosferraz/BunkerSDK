package bunker.snowmanlabs.com.bunker.presentation.ui

import bunker.snowmanlabs.com.bunker.presentation.api.RestApi
import bunker.snowmanlabs.com.bunker.di.qualifiers.IO
import bunker.snowmanlabs.com.bunker.di.qualifiers.UI
import bunker.snowmanlabs.com.bunker.domain.CnhResult
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModel
import io.reactivex.Scheduler
import java.security.InvalidKeyException
import java.util.*
import javax.inject.Inject


class ScanProcessViewModel @Inject constructor(
        private val restApi: RestApi,
        @IO private val io: Scheduler,
        @UI private val ui: Scheduler
) : BaseViewModel<ScanProcessViewModel.State, ScanProcessViewModel.Command>() {
    override fun initialState() = State()

    companion object {
        var mSessionId: String? = null
    }

    fun sendCnhPicture(convertImageToBase64: String?) {
        newState(currentState().copy(loading = true, error = null))
        val sessionId = getSessionId()
        val request = ProcessRequest(
            sessionId = sessionId,
            codedImage = convertImageToBase64
        )
        restApi.sendCnhPicture(request)
                .subscribeOn(io)
                .observeOn(ui)
                .subscribe({
                    handleResult(it)
                },{
                    newState(currentState().copy(loading = false, error = it))
                })
    }

    fun handleResult(it: CnhResult) {
        if(it.data.classifier.isValid == "true"){
            newState(currentState().copy(loading = false, error = null))
            command.value = Command.CNHResult(it)
        }else{
            newState(currentState().copy(loading = false, error = InvalidKeyException()))
        }
    }

    private fun getSessionId(): String? {
        if (mSessionId == null){
            mSessionId = UUID.randomUUID().toString()
        }

        return mSessionId
    }

    fun sendSelfPicture(convertImageToBase64: String?) {
        newState(currentState().copy(loading = true, error = null))
        val sessionId = getSessionId()
        val request = ProcessRequest(
            sessionId = sessionId!!,
            codedImage = convertImageToBase64!!
        )
        restApi.sendSelf(request)
                .subscribeOn(io)
                .observeOn(ui)
                .subscribe({
                    handleSelfResult(it)
                },{
                    newState(currentState().copy(loading = false, error = it))
                })
    }

    fun handleSelfResult(it: SelfResult) {
        if (it.data.isValid == "true"){
            newState(currentState().copy(loading = false, error = null))
            command.value = Command.SELFResult(it)
        }else{
            newState(currentState().copy(loading = false, error = InvalidKeyException()))
        }

    }

    fun resetSessionId() {
        mSessionId = null
    }


    data class State(val loading: Boolean = false,
                     val error: Throwable? = null)

    sealed class Command {
        class CNHResult(val cnhResult: CnhResult): Command()
        class SELFResult(val selfResult: SelfResult): Command()
    }
}