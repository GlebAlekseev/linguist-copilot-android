package pro.linguistcopilot.feature.content.library.mySources

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MySourcesContent(component: MySourcesComponent) {
    val titles = listOf(
        "Lorem ipsum...",
        "Lorem ipsum...",
        "Lorem ipsum...",
        "Lorem ipsum...",
        "Lorem ipsum..."
    )
    Column {
        LazyRow {
            item {
                Column(modifier = Modifier.clickable { component.onBookDownload.invoke() }) {
                    Box(
                        Modifier
                            .width(60.dp)
                            .height(70.dp)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+", fontSize = 80.sp, color = Color.White)
                    }
                    Text(text = "Загрузить книгу")
                }
            }

            items(titles) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        Modifier
                            .width(60.dp)
                            .height(70.dp)
                            .background(Color.Gray)
                    )
                    Text(text = it)
                }
            }
        }

    }
}