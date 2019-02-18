package bunker.snowmanlabs.com.bunker

import bunker.snowmanlabs.com.bunker.presentation.api.RestApi
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test

import org.junit.BeforeClass
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    companion object {

        private val BASE_URL = RestApi.BASE_URL
        private lateinit var apiForTest: RestApi
        private lateinit var gson: Gson

        @BeforeClass
        @JvmStatic fun setup() {
            gson = Gson()

            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .readTimeout(40, TimeUnit.SECONDS)
                .build()

            apiForTest = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(RestApi::class.java)
        }
    }

    // region Private methods ----------------------------------------------------------------------
    private var token: String = ""
        get() {
            if(field == "") {
                val response = apiForTest.login(LoginTest.login, LoginTest.senha).blockingGet()
                val loginResponse = gson.fromJson<LoginResponse>(response.body()?.string(), LoginResponse::class.java)
                return loginResponse.token!!
            }
            else return field
        }
    // region

    val id: Long = 27398


    @Test
    fun `getActions should return 200`() {

        // Given
        val pacienteLista = mutableListOf<Map<String, Long>>()
        val map = mutableMapOf<String, Long>()
        map["id"] = id
        pacienteLista.add(map)

        // When
        val actionsRequest = ActionsRequest(pacienteLista, "15/01/2018 00:00:00", "15/01/2019 00:00:00")
        val response = apiForTest.getActions(token, actionsRequest).blockingGet()


        // Assert
        assert_().that(response.code()).isEqualTo(200)
    }


    @Test
    fun `getActions should return id`() {

        // Given
        val pacienteLista = mutableListOf<Map<String, Long>>()
        val map = mutableMapOf<String, Long>()
        map["id"] = id
        pacienteLista.add(map)

        // When
        val actionsRequest = ActionsRequest(pacienteLista, "15/01/2018 00:00:00", "15/01/2019 00:00:00")
        val response = apiForTest.getActions(token, actionsRequest).blockingGet()
        val actionsResponse = gson.fromJson<ActionsResponse>(response.body()?.string(), ActionsResponse::class.java)


        // Assert
        assert_().that(actionsResponse.dados!![0].pacienteCuidado?.paciente?.id).isEqualTo(id)
    }
}
