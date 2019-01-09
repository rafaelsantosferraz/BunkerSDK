package bunker.snowmanlabs.com.bunker.data.api

import bunker.snowmanlabs.com.bunker.data.CnhResult
import bunker.snowmanlabs.com.bunker.data.ProcessRequest
import bunker.snowmanlabs.com.bunker.data.SelfProcessRequest
import bunker.snowmanlabs.com.bunker.data.SelfResult
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestApi {
    @GET("")
    fun getSessionId()

    @POST("cnh-flow")
    fun sendCnhPicture(@Body request: ProcessRequest): Single<CnhResult>

    @POST("face-checking")
    fun sendFacePicture(@Body request: SelfProcessRequest): Single<SelfResult>
}
