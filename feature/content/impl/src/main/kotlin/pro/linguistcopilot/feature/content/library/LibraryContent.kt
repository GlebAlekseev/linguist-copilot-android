package pro.linguistcopilot.feature.content.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.pages.Pages
import com.arkivanov.decompose.extensions.compose.jetpack.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import pro.linguistcopilot.feature.content.library.externalSources.ExternalSourcesContent
import pro.linguistcopilot.feature.content.library.mySources.MySourcesContent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun LibraryContent(component: LibraryComponent) {
    val selectedIndex by component.selectedIndex.subscribeAsState()
    Column {
        SourcesTabs(
            onSelectExternalSources = component.onSelectExternalSources,
            onSelectMySources = component.onSelectMySources,
            currentPageIndex = selectedIndex
        )
        Pages(
            modifier = Modifier.weight(1f),
            pages = component.libraryPages,
            onPageSelected = component::selectPageByIndex,
            scrollAnimation = PagesScrollAnimation.Disabled
        ) { _, page ->
            when (page) {
                is LibraryComponent.Page.ExternalSources -> ExternalSourcesContent(component = page.externalSourcesComponent)
                is LibraryComponent.Page.MySources -> MySourcesContent(component = page.mySourcesComponent)
            }
        }
    }
}

@Composable
private fun SourcesTabs(
    onSelectMySources: () -> Unit,
    onSelectExternalSources: () -> Unit,
    currentPageIndex: Int
) {
    val tabList = listOf(
        Pair(onSelectMySources, "Мои книги"),
        Pair(onSelectExternalSources, "Внешние источники"),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        for ((index, pair) in tabList.withIndex()) {
            val (action, title) = pair
            AnimatedContent(currentPageIndex == index, label = "currentPageIndex",
                transitionSpec = {
                    (scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                        .togetherWith(fadeOut(animationSpec = tween(90)))
                }) { bool ->
                Button(
                    onClick = action,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (bool) Color.White else Color.Black,
                        containerColor = if (bool) Color.Black else Color.White,
                    ),
                ) {
                    Text(text = title)
                }
            }

        }
    }
}