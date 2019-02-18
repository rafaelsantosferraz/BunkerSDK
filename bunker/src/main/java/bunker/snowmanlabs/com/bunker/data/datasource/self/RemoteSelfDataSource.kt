package bunker.snowmanlabs.com.bunker.data.datasource.self


import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import bunker.snowmanlabs.com.bunker.presentation.api.RestApi
import io.reactivex.Single
import javax.inject.Inject

class RemoteSelfDataSource @Inject constructor(
        private val restApi: RestApi
):  SelfDataSource {

    override fun sendSelf(processRequest: ProcessRequest): Single<SelfResult> {
        return restApi.sendSelf(processRequest)
    }
}