package pro.linguistcopilot.feature.settings.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.settings.controller.SettingsRepositoryImpl
import pro.linguistcopilot.feature.settings.repository.SettingsRepository


@Module
interface SettingsModule {

    @Binds
    fun settingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
