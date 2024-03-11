package pro.linguistcopilot

import android.app.Application
import pro.linguistcopilot.core.python.startPython
import pro.linguistcopilot.di.DaggerComponentHolder
import pro.linguistcopilot.di.Dependencies
import vivid.money.elmslie.core.config.ElmslieConfig

class App : Application(), DaggerComponentHolder {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override val dependenciesComponent: Dependencies
        get() = appComponent

    override fun onCreate() {
        super.onCreate()
        startPython()
        ElmslieConfig.apply {
            // TODO
        }
    }

}