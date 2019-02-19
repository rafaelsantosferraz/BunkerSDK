package bunker.snowmanlabs.com.bunker.presentation.ui.scan

import bunker.snowmanlabs.com.bunker.di.qualifiers.IO
import bunker.snowmanlabs.com.bunker.di.qualifiers.UI
import bunker.snowmanlabs.com.bunker.domain.ScanResult
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseViewModel
import bunker.snowmanlabs.com.bunker.usecase.scan.SendScan
import io.reactivex.Scheduler
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScanCnhViewModel @Inject constructor(
    private val sendScan: SendScan,
    @IO private val io: Scheduler,
    @UI private val ui: Scheduler
): BaseViewModel<ScanCnhViewModel.State, ScanCnhViewModel.Command>() {
    override fun initialState() = State()

    companion object {
        var mSessionId: String = ""
    }


    private fun getSessionId(): String {
        if (mSessionId == ""){
            mSessionId = UUID.randomUUID().toString()
        }
        return mSessionId
    }




    //region SendScan --------------------------------------------------------------------------------------------------
    fun sendScan(convertImageToBase64: String?) {
        newState(currentState().copy(loading = true))
        val sessionId = getSessionId()
        val request = ProcessRequest(
            sessionId = sessionId,
            codedImage = convertImageToBase64
        )
        add(sendScan.buildObservable(request)
            .subscribeOn(io)
            .observeOn(ui)
            .subscribe(::sendCnhPictureHandleSuccess,::sendCnhPictureHandleError))
    }

    private fun sendCnhPictureHandleSuccess(it: ScanResult) {
        newState(currentState().copy(loading = false))
        if(it.data.classifier.isValid == "true"){
            command.value = Command.ScanSuccess(it)
        }else{
            command.value = Command.ScanFailed(it)
        }
    }

    private fun sendCnhPictureHandleError(error: Throwable) {
        newState(currentState().copy(loading = false))
        command.value = Command.Error(error)
    }
    //endregion




    data class State(val loading: Boolean? = null,
                     val result: String? = "Result")





    sealed class Command {
        class Error(val error: Throwable): Command()
        class ScanSuccess(val result: ScanResult): Command()
        class ScanFailed(val result: ScanResult): Command()
    }
}