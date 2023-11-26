package pro.linguistcopilot.features.splash.di

import pro.linguistcopilot.core.featureToggles.IFeatureToggles
import pro.linguistcopilot.core.utils.di.Dependencies

interface SplashDependencies : Dependencies {
    val iFeatureToggles : IFeatureToggles
}