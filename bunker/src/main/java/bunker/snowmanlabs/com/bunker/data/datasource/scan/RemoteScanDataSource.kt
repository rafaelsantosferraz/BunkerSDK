package bunker.snowmanlabs.com.bunker.data.datasource.scan


import bunker.snowmanlabs.com.bunker.domain.ScanResult
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.presentation.api.RestApi
import io.reactivex.Single
import javax.inject.Inject

class RemoteScanDataSource @Inject constructor(
        private val restApi: RestApi
):  ScanDataSource {

    override fun sendScan(processRequest: ProcessRequest): Single<ScanResult> {
        return restApi.sendScan(processRequest)
    }
}