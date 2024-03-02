package pro.linguistcopilot.feature.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.pages.Pages
import com.arkivanov.decompose.extensions.compose.jetpack.pages.PagesScrollAnimation
import pro.linguistcopilot.feature.content.library.LibraryContent
import pro.linguistcopilot.feature.content.overview.OverviewContent
import pro.linguistcopilot.feature.content.profile.ProfileContent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun ContentContent(component: ContentComponent) {
    Column {
        TopAppBar(component.onNavigateToAuth)
        Pages(
            modifier = Modifier.weight(1f),
            pages = component.contentPages,
            onPageSelected = component::selectPageByIndex,
            scrollAnimation = PagesScrollAnimation.Default
        ) { _, page ->
            when (page) {
                is ContentComponent.Page.Library -> LibraryContent(component = page.libraryComponent)
                is ContentComponent.Page.Overview -> OverviewContent(component = page.overviewComponent)
                is ContentComponent.Page.Profile -> ProfileContent(component = page.profileComponent)
            }
        }
        BottomAppBar()
    }
}


@Composable
fun TopAppBar(onNavigateToAuth: () -> Unit) {
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
    }
}

@Composable
fun BottomAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Text(text = "Библиотека")
        Text(text = "Обзор")
        Text(text = "Профиль")
    }
}