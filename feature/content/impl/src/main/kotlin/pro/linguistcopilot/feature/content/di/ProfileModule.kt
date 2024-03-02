package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.profile.DefaultProfileComponent
import pro.linguistcopilot.feature.content.profile.ProfileComponent

@Module
interface ProfileModule {

    @Binds
    fun componentFactory(impl: DefaultProfileComponent.Factory): ProfileComponent.Factory
}
