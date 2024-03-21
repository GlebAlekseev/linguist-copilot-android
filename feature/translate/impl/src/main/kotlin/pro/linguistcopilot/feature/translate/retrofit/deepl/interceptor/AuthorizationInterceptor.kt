package pro.linguistcopilot.feature.translate.retrofit.deepl.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import pro.linguistcopilot.Constants
import pro.linguistcopilot.feature.settings.repository.SettingsRepository
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val settingsRepository: SettingsRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        return when {
            url.startsWith(Constants.FREE_DEEPL_API_BASE_URL) -> {
                request
                    .addAuthorizationHeader(settingsRepository.freeDeeplApiKey ?: "")
                    .let { chain.proceed(it) }
            }

            url.startsWith(Constants.PRO_DEEPL_API_BASE_URL) -> {
                request
                    .addAuthorizationHeader(settingsRepository.proDeeplApiKey ?: "")
                    .let { chain.proceed(it) }
            }

            else -> chain.proceed(request)
        }
    }

    private fun Request.addAuthorizationHeader(apiKey: String): Request {
        val authHeaderName = "Authorization"
        return newBuilder()
            .apply {
                header(authHeaderName, "DeepL-Auth-Key $apiKey")
            }
            .build()
    }
}