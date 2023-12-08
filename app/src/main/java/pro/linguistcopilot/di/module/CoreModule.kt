package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.AppLogger
import pro.linguistcopilot.core.utils.ILogger


@Module
interface CoreModule {
    @Binds
    fun bindAppLogger(impl: AppLogger): ILogger
}