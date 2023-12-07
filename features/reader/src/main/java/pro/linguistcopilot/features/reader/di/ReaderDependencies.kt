package pro.linguistcopilot.features.reader.di

import android.content.Context
import pro.linguistcopilot.core.utils.di.ApplicationContext

interface ReaderDependencies {
    @ApplicationContext
    fun getContext(): Context
}