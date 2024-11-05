import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build


fun getFilterMatrix(filterType: String): FloatArray {
    return when (filterType) {
        "Protanopia" -> floatArrayOf(
            0.56667F, 0.43333F, 0F, 0F, 0F,
            0.55833F, 0.44167F, 0F, 0F, 0F,
            0F, 0.24167F, 0.75833F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        "Deuteranopia" -> floatArrayOf(
            0.625F, 0.375F, 0F, 0F, 0F,
            0.7F, 0.3F, 0F, 0F, 0F,
            0F, 0.3F, 0.7F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        "Tritanopia" -> floatArrayOf(
            0.95F, 0.05F, 0F, 0F, 0F,
            0F, 0.43333F, 0.56667F, 0F, 0F,
            0F, 0.475F, 0.525F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        "Protanomaly" -> floatArrayOf(
            0.81667F, 0.18333F, 0F, 0F, 0F,
            0.33333F, 0.66667F, 0F, 0F, 0F,
            0F, 0.125F, 0.875F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        "Deuteranomaly" -> floatArrayOf(
            0.8F, 0.2F, 0F, 0F, 0F,
            0.25833F, 0.74167F, 0F, 0F, 0F,
            0F, 0.3F, 0.7F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        "Tritanomaly" -> floatArrayOf(
            0.96667F, 0.03333F, 0F, 0F, 0F,
            0F, 0.73333F, 0.26667F, 0F, 0F,
            0F, 0.18333F, 0.81667F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        "Achromatopsia" -> floatArrayOf(
            0.299F, 0.587F, 0.114F, 0F, 0F,
            0.299F, 0.587F, 0.114F, 0F, 0F,
            0.299F, 0.587F, 0.114F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        "Achromatomaly" -> floatArrayOf(
            0.618F, 0.32F, 0.062F, 0F, 0F,
            0.163F, 0.775F, 0.062F, 0F, 0F,
            0.163F, 0.32F, 0.516F, 0F, 0F,
            0F, 0F, 0F, 1F, 0F
        )
        else -> floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f)
             // None
    }
}