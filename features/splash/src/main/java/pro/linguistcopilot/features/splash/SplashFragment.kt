package pro.linguistcopilot.features.splash

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.linguistcopilot.core.featureToggles.IFeatureToggles
import pro.linguistcopilot.core.utils.Constants.SPLASH_DELAY
import pro.linguistcopilot.core.utils.FragmentWithBinding
import pro.linguistcopilot.core.utils.di.findDependencies
import pro.linguistcopilot.features.splash.databinding.FragmentSplashBinding
import pro.linguistcopilot.features.splash.di.DaggerSplashComponent
import pro.linguistcopilot.navigation.navigate
import javax.inject.Inject

class SplashFragment : FragmentWithBinding<FragmentSplashBinding>(FragmentSplashBinding::inflate){
    @Inject
    lateinit var iFeatureToggles: IFeatureToggles

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerSplashComponent.factory()
            .create(findDependencies())
            .apply {
                inject(this@SplashFragment)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFeatureToggles()
    }

    private fun initializeFeatureToggles(){
        lifecycleScope.launch(Dispatchers.IO) {
            iFeatureToggles.load()
            delay(SPLASH_DELAY)
            withContext(Dispatchers.Main){
                navigate(R.id.action_splashFragment_to_mainFragment)
            }
        }
    }
}
