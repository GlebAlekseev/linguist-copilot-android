package pro.linguistcopilot.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import pro.linguistcopilot.core.featureToggles.AssetsFeatureToggles
import pro.linguistcopilot.core.featureToggles.IFeatureToggles
import pro.linguistcopilot.core.utils.di.ApplicationContext
import pro.linguistcopilot.di.scope.AppComponentScope

@Module
interface FeatureTogglesModule {
    companion object {
        @AppComponentScope
        @Provides
        fun bindIFeatureToggles(
            @ApplicationContext context: Context,
        ): IFeatureToggles {
            return AssetsFeatureToggles(context)
        }
    }
}