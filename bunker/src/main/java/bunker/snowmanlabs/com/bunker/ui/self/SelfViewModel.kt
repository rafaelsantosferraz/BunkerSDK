package bunker.snowmanlabs.com.bunker.ui.self

import bunker.snowmanlabs.com.bunker.data.CnhResult
import bunker.snowmanlabs.com.bunker.data.ProcessRequest
import bunker.snowmanlabs.com.bunker.data.SelfResult
import bunker.snowmanlabs.com.bunker.data.api.RestApi
import bunker.snowmanlabs.com.bunker.di.qualifiers.IO
import bunker.snowmanlabs.com.bunker.di.qualifiers.UI
import bunker.snowmanlabs.com.bunker.ui.ScanProcessViewModel
import bunker.snowmanlabs.com.bunker.ui.base.BaseViewModel
import io.reactivex.Scheduler
import java.security.InvalidKeyException
import java.util.*
import javax.inject.Inject

class SelfViewModel  @Inject constructor(
    private val restApi: RestApi,
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

    fun sendSelfPicture(convertImageToBase64: String?) {
        newState(currentState().copy(loading = true, error = null))
        val sessionId = getSessionId()
        val request = ProcessRequest(sessionId = sessionId, codedImage = convertImageToBase64!!)
        restApi.sendFacePicture(request)
            .subscribeOn(io)
            .observeOn(ui)
            .subscribe({
                handleSelfResult(it)
            },{
                newState(currentState().copy(loading = false, error = it))
            })
    }

    private fun handleSelfResult(it: SelfResult) {
        if (it.data.isValid == "true"){
            newState(currentState().copy(loading = false, error = null))
            command.value = Command.SELFResult(it)
        }else{
            newState(currentState().copy(loading = false, error = InvalidKeyException()))
        }

    }

    data class State(val loading: Boolean = false,
                     val error: Throwable? = null)

    sealed class Command {
        class SELFResult(val selfResult: SelfResult): Command()
    }
}