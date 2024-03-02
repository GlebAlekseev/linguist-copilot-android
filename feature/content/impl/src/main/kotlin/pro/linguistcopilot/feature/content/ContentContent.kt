package pro.linguistcopilot.feature.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
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
import pro.linguistcopilot.feature.content.library.LibraryContent
import pro.linguistcopilot.feature.content.overview.OverviewContent
import pro.linguistcopilot.feature.content.profile.ProfileContent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun ContentContent(component: ContentComponent) {
    val selectedIndex by component.selectedIndex.subscribeAsState()
    Column {
        TopAppBar(
            onNavigateToAuth = component.onNavigateToAuth,
            onOpenBookSearch = component.onOpenBookSearch
        )

        Pages(
            modifier = Modifier.weight(1f),
            pages = component.contentPages,
            onPageSelected = component::selectPageByIndex,
            scrollAnimation = PagesScrollAnimation.Disabled
        ) { _, page ->
            when (page) {
                is ContentComponent.Page.Library -> LibraryContent(component = page.libraryComponent)
                is ContentComponent.Page.Overview -> OverviewContent(component = page.overviewComponent)
                is ContentComponent.Page.Profile -> ProfileContent(component = page.profileComponent)
            }
        }

        BottomAppBar(
            component.onSelectLibrary,
            component.onSelectOverview,
            component.onSelectProfile,
            selectedIndex
        )
    }
}


@Composable
fun TopAppBar(onNavigateToAuth: () -> Unit, onOpenBookSearch: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Linguist Copilot")
        Button(onClick = onNavigateToAuth) {
            Text(text = "Авторизоваться")
        }
        Button(onClick = onOpenBookSearch) {
            Text(text = "Поиск")
        }
    }
}

@Composable
fun BottomAppBar(
    onSelectLibrary: () -> Unit,
    onSelectOverview: () -> Unit,
    onSelectProfile: () -> Unit,
    currentPageIndex: Int,
) {
    val actionList = listOf(
        Pair(onSelectLibrary, "Библиотека"),
        Pair(onSelectOverview, "Обзор"),
        Pair(onSelectProfile, "Профиль")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        for ((index, pair) in actionList.withIndex()) {
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