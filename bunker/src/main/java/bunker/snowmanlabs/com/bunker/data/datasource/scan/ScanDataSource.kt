package bunker.snowmanlabs.com.bunker.data.datasource.scan

import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.ScanResult
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import io.reactivex.Single

interface ScanDataSource {
    fun sendScan(processRequest: ProcessRequest): Single<ScanResult>
}