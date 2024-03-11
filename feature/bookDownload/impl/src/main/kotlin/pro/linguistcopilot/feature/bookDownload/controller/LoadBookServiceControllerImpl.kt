package pro.linguistcopilot.feature.bookDownload.controller

import android.content.Context
import android.os.Build
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.feature.bookDownload.service.LoadBookForegroundService
import javax.inject.Inject


class LoadBookServiceControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LoadBookServiceController {
    override fun start(uri: String) {
        val serviceIntent = LoadBookForegroundService.createIntent(context, uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}