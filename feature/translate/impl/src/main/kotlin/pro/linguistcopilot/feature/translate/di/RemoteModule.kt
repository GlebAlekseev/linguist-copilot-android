package pro.linguistcopilot.feature.translate.di

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pro.linguistcopilot.Constants
import pro.linguistcopilot.Constants.CONNECT_TIMEOUT
import pro.linguistcopilot.Constants.READ_TIMEOUT
import pro.linguistcopilot.Constants.WRITE_TIMEOUT
import pro.linguistcopilot.di.scope.AppComponentScope
import pro.linguistcopilot.feature.translate.retrofit.mymemory.MyMemoryService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
interface RemoteModule {
    companion object {
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor {
                Log.d("Network", it)
            }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @AppComponentScope
        @Provides
        fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build()
        }

        @MyMemoryQualifier
        @AppComponentScope
        @Provides
        fun provideMyMemoryRetrofitBuilder(): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(Constants.MYMEMORY_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
        }

        @AppComponentScope
        @Provides
        fun provideMyMemoryService(
            @MyMemoryQualifier retrofitBuilder: Retrofit.Builder,
            okHttpClient: OkHttpClient
        ): MyMemoryService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(MyMemoryService::class.java)
        }
    }
}