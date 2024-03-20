package pro.linguistcopilot.feature.translate.retrofit.mymemory

import pro.linguistcopilot.feature.translate.retrofit.mymemory.dto.MyMemoryTranslationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MyMemoryService {
    @GET("get")
    suspend fun translate(
        @Query("q") query: String,
        @Query("langpair") langPair: String,
        @Query("de") de: String?
    ): MyMemoryTranslationResponse
}
