package bunker.snowmanlabs.com.bunker.data.repository.self

import bunker.snowmanlabs.com.bunker.data.datasource.self.RemoteSelfDataSource
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelfRepository @Inject constructor(
    val remoteSelfDataSource: RemoteSelfDataSource
) {

    fun sendSelf(processRequest: ProcessRequest): Single<SelfResult> {
        return remoteSelfDataSource.sendSelf(processRequest)
    }
}