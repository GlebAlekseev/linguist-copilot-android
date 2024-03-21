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
import pro.linguistcopilot.feature.translate.di.qualifier.DeeplFreeQualifier
import pro.linguistcopilot.feature.translate.di.qualifier.DeeplProQualifier
import pro.linguistcopilot.feature.translate.retrofit.deepl.DeeplService
import pro.linguistcopilot.feature.translate.retrofit.deepl.interceptor.AuthorizationInterceptor
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
            authorizationInterceptor: AuthorizationInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
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

        @DeeplFreeQualifier
        @AppComponentScope
        @Provides
        fun provideDeeplFreeRetrofitBuilder(): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(Constants.FREE_DEEPL_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
        }

        @DeeplProQualifier
        @AppComponentScope
        @Provides
        fun provideDeeplProRetrofitBuilder(): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(Constants.PRO_DEEPL_API_BASE_URL)
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

        @DeeplFreeQualifier
        @AppComponentScope
        @Provides
        fun provideDeeplFreeService(
            @DeeplFreeQualifier retrofitBuilder: Retrofit.Builder,
            okHttpClient: OkHttpClient
        ): DeeplService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(DeeplService::class.java)
        }

        @DeeplProQualifier
        @AppComponentScope
        @Provides
        fun provideDeeplProService(
            @DeeplProQualifier retrofitBuilder: Retrofit.Builder,
            okHttpClient: OkHttpClient
        ): DeeplService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(DeeplService::class.java)
        }
    }
}