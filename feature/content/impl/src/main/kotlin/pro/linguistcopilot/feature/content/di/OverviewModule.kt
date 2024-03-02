package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.overview.DefaultOverviewComponent
import pro.linguistcopilot.feature.content.overview.OverviewComponent

@Module
interface OverviewModule {

    @Binds
    fun componentFactory(impl: DefaultOverviewComponent.Factory): OverviewComponent.Factory
}
