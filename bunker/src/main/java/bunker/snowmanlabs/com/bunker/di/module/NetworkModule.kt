package bunker.snowmanlabs.com.bunker.di.module

import com.google.gson.Gson
import bunker.snowmanlabs.com.bunker.presentation.api.RestApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {



    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideRestApi(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): RestApi {
        return Retrofit.Builder()
                .baseUrl(RestApi.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .build()
                .create(RestApi::class.java)
    }

}
