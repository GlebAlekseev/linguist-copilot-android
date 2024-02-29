package pro.linguistcopilot.design.compose_ui.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pro.linguistcopilot.design.compose_ui.color.LightLinguistCopilotPalette
import pro.linguistcopilot.design.compose_ui.theme.LinguistCopilotTheme

internal val PoppinsFontFamily: FontFamily = FontFamily(
    Font(
        resId = pro.linguistcopilot.res.R.font.poppins_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
        loadingStrategy = FontLoadingStrategy.Async,
    ),
    Font(
        resId = pro.linguistcopilot.res.R.font.poppins_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal,
        loadingStrategy = FontLoadingStrategy.Async,
    ),
    Font(
        resId = pro.linguistcopilot.res.R.font.poppins_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal,
        loadingStrategy = FontLoadingStrategy.Async,
    ),
)

@Immutable
class LinguistCopilotTypography(
    val test1: TextStyle = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
    )
)

@Composable
@Preview(device = "spec:width=1920px,height=1080px,dpi=240")
@Suppress("LongMethod", "MagicNumber", "StringLiteralDuplication")
private fun PreviewTypography(){

    fun Modifier.drawDashedBorder(): Modifier =
        drawBehind {
            drawRoundRect(
                color = LightLinguistCopilotPalette.outline,
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(
                    width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(10f, 10f),
                        phase = 0f,
                    )
                ),
            )
        }

    val linePaddingModifier = Modifier.padding(bottom = 8.dp)

    Column(
        modifier = Modifier
            .background(
                color = LightLinguistCopilotPalette.surface,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .drawDashedBorder()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Header H1 32/38",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H2 28/36",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H3 22/28",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Header H4 20/24",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Заголовки экранов",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки экранов",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки экранов",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Заголовки внутри блоков контента",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .drawDashedBorder()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Paragraph Big 16/24",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Normal 14/20",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Big Accent 16/24",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Paragraph Normal Accent 14/20",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Текстовый контент, несущий много смысла",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Короткие надписи внутри UI, подзаголовки внутри контента",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Текстовый контент, несущий много смысла",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Короткие надписи внутри UI, подзаголовки внутри контента",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .drawDashedBorder()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Label Big 14/20",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Label Normal 12/16",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Label Mini 10/16",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                )
            }
            Spacer(Modifier.width(64.dp))
            Column {
                Text(
                    text = "Кнопки, поля ввода, ячейки",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Ячейки и короткие надписи в UI",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                    modifier = linePaddingModifier,
                )
                Text(
                    text = "Ячейки и короткие надписи в UI",
                    style = LinguistCopilotTheme.typography.test1,
                    color = LightLinguistCopilotPalette.content,
                )
            }
        }
    }
}
