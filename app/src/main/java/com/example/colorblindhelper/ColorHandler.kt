package com.example.colorblindhelper
import android.graphics.Bitmap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import kotlin.math.pow
import kotlin.math.sqrt


class ColorHandler {

    fun detectColor(bitmap: Bitmap, radius: Dp, density: Density): Triple<Int, Int, Int> {
        val radiusPx = with(density) { 5 * radius.toPx() }
        val centerX = bitmap.width / 2
        val centerY = bitmap.height / 2

        val colorCountMap = mutableMapOf<Int, Int>()

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                // Kiểm tra xem pixel có nằm trong bán kính không
                if (sqrt((x - centerX).toDouble().pow(2.0) + (y - centerY).toDouble().pow(2.0)) <= radiusPx) {
                    val pixel = bitmap.getPixel(x, y)
                    colorCountMap[pixel] = colorCountMap.getOrDefault(pixel, 0) + 1
                }
            }
        }

        // Tìm màu có số lần xuất hiện nhiều nhất
        val mostFrequentColor = colorCountMap.maxByOrNull { it.value }?.key ?: 0

        // Trả về giá trị RGB của màu phổ biến nhất
        val r = (mostFrequentColor shr 16) and 0xFF
        val g = (mostFrequentColor shr 8) and 0xFF
        val b = mostFrequentColor and 0xFF

        return Triple(r, g, b)
    }

    // Chuyển đổi RGB sang mã hex
    fun rgbToHex(r: Int, g: Int, b: Int): String {
        return String.format("#%02X%02X%02X", r, g, b)
    }

}