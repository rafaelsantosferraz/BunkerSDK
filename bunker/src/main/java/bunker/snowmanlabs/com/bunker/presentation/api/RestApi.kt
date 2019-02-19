package bunker.snowmanlabs.com.bunker.presentation.api

import bunker.snowmanlabs.com.bunker.domain.ScanResult
import bunker.snowmanlabs.com.bunker.domain.ProcessRequest
import bunker.snowmanlabs.com.bunker.domain.SelfResult
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    companion object {
        //const val BASE_URL = "http://checkify.extractify.ai/api/"
        const val BASE_URL = "https://bunker-api.azurewebsites.net/"
    }

    @GET("")
    fun getSessionId()

//    @POST("cnh-flow")
//    fun sendScan(@Body request: ProcessRequest): Single<ScanResult>

    @POST("api/v1/process/doccheck")
    @Headers(
        "Authorization: Bearer 77C94157-753B-4B7E-8105-F012989E0531")
    fun sendScan(@Body request: ProcessRequest): Single<ScanResult>

//    @POST("face-checking")
//    fun sendSelf(@Body request: SelfProcessRequest): Single<SelfResult>

    @POST("api/v1/process/facecheck")
    @Headers(
        "Authorization: Bearer 77C94157-753B-4B7E-8105-F012989E0531",
        "accept: application/json")
    fun sendSelf(@Body request: ProcessRequest): Single<SelfResult>
}