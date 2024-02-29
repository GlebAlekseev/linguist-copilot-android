package pro.linguistcopilot.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pro.linguistcopilot.App
import pro.linguistcopilot.MainActivity
import pro.linguistcopilot.core.utils.di.ApplicationContext
import pro.linguistcopilot.di.scope.AppComponentScope


@AppComponentScope
@Component(
    modules = []
)
interface AppComponent {
    fun inject(application: App)
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @[BindsInstance ApplicationContext] context: Context
        ): AppComponent
    }
}