package pro.linguistcopilot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import com.arkivanov.decompose.defaultComponentContext
import pro.linguistcopilot.root.RootComponent
import pro.linguistcopilot.root.RootContent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var rootComponentFactory: RootComponent.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        (this.applicationContext as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(
            ComposeView(this).apply {
                setContent {
                    RootContent(
                        component = rootComponentFactory.invoke(
                            defaultComponentContext()
                        )
                    )
                }
            }
        )
    }
}