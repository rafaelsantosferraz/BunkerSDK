package bunker.snowmanlabs.com.bunker.usecase.self

import bunker.snowmanlabs.com.bunker.data.repository.self.SelfRepository
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import com.snowmanlabs.appify.model.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class SendSelf @Inject constructor(
       private val selfRepository: SelfRepository
) : SingleUseCase<SelfResult, ProcessRequest>() {
    override fun buildObservable(processRequest: ProcessRequest?): Single<SelfResult> {
        return selfRepository.sendSelf(processRequest!!)
    }
}