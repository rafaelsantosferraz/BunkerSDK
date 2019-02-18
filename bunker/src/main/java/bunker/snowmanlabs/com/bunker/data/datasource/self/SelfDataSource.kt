package bunker.snowmanlabs.com.bunker.data.datasource.self

import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import io.reactivex.Single

interface SelfDataSource {
    fun sendSelf(processRequest: ProcessRequest): Single<SelfResult>
}