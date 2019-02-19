package bunker.snowmanlabs.com.bunker.usecase.scan

import bunker.snowmanlabs.com.bunker.data.repository.scan.ScanRepository
import bunker.snowmanlabs.com.bunker.data.repository.self.SelfRepository
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.ScanResult
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import com.snowmanlabs.appify.model.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class SendScan @Inject constructor(
       private val scanRepository: ScanRepository
) : SingleUseCase<ScanResult, ProcessRequest>() {
    override fun buildObservable(processRequest: ProcessRequest?): Single<ScanResult> {
        return scanRepository.sendScan(processRequest!!)
    }
}