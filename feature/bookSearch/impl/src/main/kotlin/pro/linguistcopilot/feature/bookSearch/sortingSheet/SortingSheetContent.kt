package pro.linguistcopilot.feature.bookSearch.sortingSheet

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun SortingSheetContent(component: SortingSheetComponent) {
    ModalBottomSheet(
        containerColor = Color.Gray,
        onDismissRequest = {
            component.onDismissRequest()
        },
    ) {
        Text(text = "Сортировать по ...")
    }
}