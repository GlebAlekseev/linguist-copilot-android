package pro.linguistcopilot

import android.app.Application
import pro.linguistcopilot.di.AppComponent
import pro.linguistcopilot.di.DaggerAppComponent
import vivid.money.elmslie.core.config.ElmslieConfig

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        ElmslieConfig.apply {
            // TODO
        }
    }
}