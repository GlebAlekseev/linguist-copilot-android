package pro.linguistcopilot.feature.translate.retrofit.deepl

import pro.linguistcopilot.feature.translate.retrofit.deepl.dto.DeeplTranslationResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface DeeplService {
    @POST("v2/translate")
    suspend fun translate(
        @Query("text") query: String,
        @Query("source_lang") source: String,
        @Query("target_lang") target: String,
    ): DeeplTranslationResponse
}
