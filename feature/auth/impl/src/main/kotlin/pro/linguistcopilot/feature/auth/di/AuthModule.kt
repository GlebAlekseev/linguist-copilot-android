package pro.linguistcopilot.feature.auth.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.auth.DefaultAuthComponent
import pro.linguistcopilot.feature.auth.AuthComponent

@Module
interface AuthModule {

    @Binds
    fun componentFactory(impl: DefaultAuthComponent.Factory): AuthComponent.Factory
}
