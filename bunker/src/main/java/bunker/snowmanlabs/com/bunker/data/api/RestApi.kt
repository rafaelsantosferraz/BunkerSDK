package bunker.snowmanlabs.com.bunker.data.api

import bunker.snowmanlabs.com.bunker.data.CnhResult
import bunker.snowmanlabs.com.bunker.data.ProcessRequest
import bunker.snowmanlabs.com.bunker.data.SelfProcessRequest
import bunker.snowmanlabs.com.bunker.data.SelfResult
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
//    fun sendCnhPicture(@Body request: ProcessRequest): Single<CnhResult>

    @POST("api/v1/process/doccheck")
    @Headers(
        "Authorization: Bearer 77C94157-753B-4B7E-8105-F012989E0531")
    fun sendCnhPicture(@Body request: ProcessRequest): Single<CnhResult>

//    @POST("face-checking")
//    fun sendFacePicture(@Body request: SelfProcessRequest): Single<SelfResult>

    @POST("api/v1/process/facecheck")
    @Headers(
        "Authorization: Bearer 77C94157-753B-4B7E-8105-F012989E0531",
        "accept: application/json")
    fun sendFacePicture(@Body request: ProcessRequest): Single<SelfResult>
}