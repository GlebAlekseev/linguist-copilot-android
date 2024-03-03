package pro.linguistcopilot.feature.bookSearch.filtersSheet

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun FiltersSheetContent(component: FiltersSheetComponent) {
    ModalBottomSheet(
        containerColor = Color.Gray,
        onDismissRequest = {
            component.onDismissRequest()
        },
    ) {
        Text(text = "Фильтровать по ...")
    }
}