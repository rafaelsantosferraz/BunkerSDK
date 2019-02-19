package bunker.snowmanlabs.com.bunker.data.repository.scan

import bunker.snowmanlabs.com.bunker.data.datasource.scan.RemoteScanDataSource
import bunker.snowmanlabs.com.bunker.domain.ScanResult
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanRepository @Inject constructor(
    val remoteScanDataSource: RemoteScanDataSource
) {

    fun sendScan(processRequest: ProcessRequest): Single<ScanResult> {
        return remoteScanDataSource.sendScan(processRequest)
    }
}