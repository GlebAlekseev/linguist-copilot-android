package pro.linguistcopilot.feature.bookDownload.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import pro.linguistcopilot.di.DaggerComponentHolder
import pro.linguistcopilot.feature.bookDownload.di.BookDownloadDependencies
import pro.linguistcopilot.feature.bookDownload.usecase.LoadBookUseCase

class LoadBookForegroundService : Service() {
    private val loadBookUseCase: LoadBookUseCase by lazy {
        (applicationContext as DaggerComponentHolder).currentDependencies.loadBookUseCase
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val uri = intent?.getStringExtra(URI_KEY)
        if (uri == null) {
            stopSelf()
            return START_NOT_STICKY
        }

        val channelId = createNotificationChannel()

        val notification = createForegroundNotificationBuilder(uri, channelId)
        startForeground(NOTIFICATION_ID, notification.build())

        loadBook(uri, notification)
        return START_NOT_STICKY
    }

    private fun createNotificationChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "BookDownloadChannel"
            val channelName = "Book Download Channel"
            val chan = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
            channelId
        } else {
            ""
        }
    }

    private fun createForegroundNotificationBuilder(
        uri: String,
        channelId: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Book Downloading")
            .setContentText("Book processing with URI: $uri")
            .setSmallIcon(pro.linguistcopilot.res.R.drawable.ic_launcher_background)
            .setOngoing(true)
    }

    private fun updateNotificationProgress(
        progress: Int,
        notificationCompatBuilder: NotificationCompat.Builder
    ) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = notificationCompatBuilder
            .setProgress(100, progress, false)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun loadBook(
        uri: String,
        notificationCompatBuilder: NotificationCompat.Builder
    ) {
        loadBookUseCase.invoke(LoadBookUseCase.Params(uri)) { result ->
            result.collect { state ->
                state.toastMessage?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
                state.progress.let { progress ->
                    updateNotificationProgress(progress, notificationCompatBuilder)
                }
                if (state.progress == 100) {
                    val notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(NOTIFICATION_ID)
                    stopSelf()
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val URI_KEY = "uri"
        private const val NOTIFICATION_ID = 20772077

        fun createIntent(context: Context, uri: String): Intent {
            val intent = Intent(context, LoadBookForegroundService::class.java)
            val bundle = Bundle().apply {
                putString(URI_KEY, uri)
            }
            intent.putExtras(bundle)
            return intent
        }
    }
}

val DaggerComponentHolder.currentDependencies: BookDownloadDependencies
    get() = this.dependenciesComponent

