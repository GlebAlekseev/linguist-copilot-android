package pro.linguistcopilot.root

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import pro.linguistcopilot.details.DetailsContent
import pro.linguistcopilot.list.ListContent

@Composable
fun RootContent(component: RootComponent){
    Children(
        stack = component.stack,
        animation = stackAnimation(animator = fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootComponent.RootState.ListState -> ListContent(component = child.listComponent, modifier = Modifier.fillMaxWidth())
            is RootComponent.RootState.DetailsState -> DetailsContent(component = child.detailsComponent, modifier = Modifier.fillMaxWidth())
        }
    }
}