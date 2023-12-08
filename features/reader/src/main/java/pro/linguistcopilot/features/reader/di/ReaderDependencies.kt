package pro.linguistcopilot.features.reader.di

import android.content.Context
import pro.linguistcopilot.core.utils.ILogger
import pro.linguistcopilot.core.utils.di.ApplicationContext
import pro.linguistcopilot.core.utils.di.Dependencies

interface ReaderDependencies : Dependencies {
    fun logger(): ILogger

    @ApplicationContext
    fun getContext(): Context
}