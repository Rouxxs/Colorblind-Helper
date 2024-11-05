package com.example.colorblindhelper

import kotlin.math.max
import kotlin.math.min

class ColorUtils {
    // Chuyển đổi từ RGB sang HSB
    fun rgbToHsb(r: Int, g: Int, b: Int): Triple<Float, Float, Float> {
        val rf = r / 255.0f
        val gf = g / 255.0f
        val bf = b / 255.0f

        val max = max(rf, max(gf, bf))
        val min = min(rf, min(gf, bf))
        val delta = max - min

        val hue = when {
            delta == 0f -> 0f
            max == rf -> (60 * ((gf - bf) / delta) + 360) % 360
            max == gf -> (60 * ((bf - rf) / delta) + 120) % 360
            else -> (60 * ((rf - gf) / delta) + 240) % 360
        }

        val saturation = if (max == 0f) 0f else delta / max
        val brightness = max

        return Triple(hue, saturation, brightness)
    }

    // Xác định hue dựa trên giá trị HSB
    fun getHueName(hue: Float): String = when (hue) {
        in 0f..15f, in 345f..360f -> "Red"
        in 15f..45f -> "Orange"
        in 45f..75f -> "Yellow"
        in 75f..150f -> "Green"
        in 150f..210f -> "Cyan"
        in 210f..270f -> "Blue"
        in 270f..330f -> "Purple"
        else -> "Magenta"
    }

    // Xác định tone dựa trên độ bão hòa và độ sáng
    fun getToneName(saturation: Float, brightness: Float): String = when {
        brightness < 0.2f -> "Dark"
        brightness > 0.8f && saturation < 0.3f -> "Light"
        saturation < 0.2f -> "Muted"
        brightness > 0.7f -> "Bright"
        saturation > 0.7f -> "Vibrant"
        else -> "Soft"
    }

    // Tạo danh sách màu với tên và miêu tả
//    fun generateColorDescriptionList(count: Int): List<ColorName> {
//        val colors = mutableListOf<ColorName>()
//        val step = 256 / Math.cbrt(count.toDouble()).toInt()
//
//        for (r in 0 until 256 step step) {
//            for (g in 0 until 256 step step) {
//                for (b in 0 until 256 step step) {
//                    if (colors.size >= count) break
//
//                    // Chuyển đổi RGB sang HSB và xác định hue và tone
//                    val (hue, saturation, brightness) = rgbToHsb(r, g, b)
//                    val hueName = getHueName(hue)
//                    val toneName = getToneName(saturation, brightness)
//
//                    // Tạo tên
//                    val colorName = "$toneName $hueName"
//
//                    colors.add(ColorName(colorName,r, g, b))
//                }
//            }
//        }
//        colors.add(ColorName("Black", 0, 0, 0))
//        colors.add(ColorName("White", 255, 255, 255))
//        return colors
//    }

    //val colors: List<ColorName> = generateColorDescriptionList(30000)
    private fun initColorList(): List<ColorName> {
        return listOf(
            ColorName("Aqua", 0, 255, 255),
            ColorName("Black", 0, 0, 0),
            ColorName("Blue", 0, 0, 255),
            ColorName("Fuchsia", 255, 0, 255),
            ColorName("Gray", 128, 128, 128),
            ColorName("Green", 0, 128, 0),
            ColorName("Lime", 0, 255, 0),
            ColorName("Maroon", 128, 0, 0),
            ColorName("Navy", 0, 0, 128),
            ColorName("Olive", 128, 128, 0),
            ColorName("Purple", 128, 0, 128),
            ColorName("Red", 255, 0, 0),
            ColorName("Silver", 192, 192, 192),
            ColorName("Teal", 0, 128, 128),
            ColorName("White", 255, 255, 255),
            ColorName("Yellow", 255, 255, 0),
            ColorName("AliceBlue", 240, 248, 255),
            ColorName("AntiqueWhite", 250, 235, 215),
            ColorName("Azure", 240, 255, 255),
            ColorName("Beige", 245, 245, 220),
            ColorName("Bisque", 255, 228, 196),
            ColorName("BlanchedAlmond", 255, 235, 205),
            ColorName("BlueViolet", 138, 43, 226),
            ColorName("Brown", 165, 42, 42),
            ColorName("BurlyWood", 222, 184, 135),
            ColorName("CadetBlue", 95, 158, 160),
            ColorName("Chartreuse", 127, 255, 0),
            ColorName("Chocolate", 210, 105, 30),
            ColorName("Coral", 255, 127, 80),
            ColorName("CornflowerBlue", 100, 149, 237),
            ColorName("Cornsilk", 255, 248, 220),
            ColorName("Crimson", 220, 20, 60),
            ColorName("Cyan", 0, 255, 255),
            ColorName("DarkBlue", 0, 0, 139),
            ColorName("DarkCyan", 0, 139, 139),
            ColorName("DarkGoldenRod", 184, 134, 11),
            ColorName("DarkGray", 169, 169, 169),
            ColorName("DarkGreen", 0, 100, 0),
            ColorName("DarkKhaki", 189, 183, 107),
            ColorName("DarkMagenta", 139, 0, 139),
            ColorName("DarkOliveGreen", 85, 107, 47),
            ColorName("DarkOrange", 255, 140, 0),
            ColorName("DarkOrchid", 153, 50, 204),
            ColorName("DarkRed", 139, 0, 0),
            ColorName("DarkSalmon", 233, 150, 122),
            ColorName("DarkSeaGreen", 143, 188, 143),
            ColorName("DarkSlateBlue", 72, 61, 139),
            ColorName("DarkSlateGray", 47, 79, 79),
            ColorName("DarkTurquoise", 0, 206, 209),
            ColorName("DarkViolet", 148, 0, 211),
            ColorName("DeepPink", 255, 20, 147),
            ColorName("DeepSkyBlue", 0, 191, 255),
            ColorName("DimGray", 105, 105, 105),
            ColorName("DodgerBlue", 30, 144, 255),
            ColorName("FireBrick", 178, 34, 34),
            ColorName("FloralWhite", 255, 250, 240),
            ColorName("ForestGreen", 34, 139, 34),
            ColorName("Gainsboro", 220, 220, 220),
            ColorName("GhostWhite", 248, 248, 255),
            ColorName("Gold", 255, 215, 0),
            ColorName("GoldenRod", 218, 165, 32),
            ColorName("GreenYellow", 173, 255, 47),
            ColorName("HoneyDew", 240, 255, 240),
            ColorName("HotPink", 255, 105, 180),
            ColorName("IndianRed", 205, 92, 92),
            ColorName("Indigo", 75, 0, 130),
            ColorName("Ivory", 255, 255, 240),
            ColorName("Khaki", 240, 230, 140),
            ColorName("Lavender", 230, 230, 250),
            ColorName("LavenderBlush", 255, 240, 245),
            ColorName("LawnGreen", 124, 252, 0),
            ColorName("LemonChiffon", 255, 250, 205),
            ColorName("LightBlue", 173, 216, 230),
            ColorName("LightCoral", 240, 128, 128),
            ColorName("LightCyan", 224, 255, 255),
            ColorName("LightGoldenRodYellow", 250, 250, 210),
            ColorName("LightGray", 211, 211, 211),
            ColorName("LightGreen", 144, 238, 144),
            ColorName("LightPink", 255, 182, 193),
            ColorName("LightSalmon", 255, 160, 122),
            ColorName("LightSeaGreen", 32, 178, 170),
            ColorName("LightSkyBlue", 135, 206, 250),
            ColorName("LightSlateGray", 119, 136, 153),
            ColorName("LightSteelBlue", 176, 196, 222),
            ColorName("LightYellow", 255, 255, 224),
            ColorName("LimeGreen", 50, 205, 50),
            ColorName("Linen", 250, 240, 230),
            ColorName("Magenta", 255, 0, 255),
            ColorName("MediumAquaMarine", 102, 205, 170),
            ColorName("MediumBlue", 0, 0, 205),
            ColorName("MediumOrchid", 186, 85, 211),
            ColorName("MediumPurple", 147, 112, 219),
            ColorName("MediumSeaGreen", 60, 179, 113),
            ColorName("MediumSlateBlue", 123, 104, 238),
            ColorName("MediumSpringGreen", 0, 250, 154),
            ColorName("MediumTurquoise", 72, 209, 204),
            ColorName("MediumVioletRed", 199, 21, 133),
            ColorName("MidnightBlue", 25, 25, 112),
            ColorName("MintCream", 245, 255, 250),
            ColorName("MistyRose", 255, 228, 225),
            ColorName("Moccasin", 255, 228, 181),
            ColorName("NavajoWhite", 255, 222, 173),
            ColorName("OldLace", 253, 245, 230),
            ColorName("Orange", 255, 165, 0),
            ColorName("OrangeRed", 255, 69, 0),
            ColorName("Orchid", 218, 112, 214),
            ColorName("PaleGoldenRod", 238, 232, 170),
            ColorName("PaleGreen", 152, 251, 152),
            ColorName("PaleTurquoise", 175, 238, 238),
            ColorName("PaleVioletRed", 219, 112, 147),
            ColorName("PapayaWhip", 255, 239, 213),
            ColorName("PeachPuff", 255, 218, 185),
            ColorName("Peru", 205, 133, 63),
            ColorName("Pink", 255, 192, 203),
            ColorName("Plum", 221, 160, 221),
            ColorName("PowderBlue", 176, 224, 230),
            ColorName("RebeccaPurple", 102, 51, 153),
            ColorName("RosyBrown", 188, 143, 143),
            ColorName("RoyalBlue", 65, 105, 225),
            ColorName("SaddleBrown", 139, 69, 19),
            ColorName("Salmon", 250, 128, 114),
            ColorName("SandyBrown", 244, 164, 96),
            ColorName("SeaGreen", 46, 139, 87),
            ColorName("SeaShell", 255, 245, 238),
            ColorName("Sienna", 160, 82, 45),
            ColorName("SkyBlue", 135, 206, 235),
            ColorName("SlateBlue", 106, 90, 205),
            ColorName("SlateGray", 112, 128, 144),
            ColorName("Snow", 255, 250, 250),
            ColorName("SpringGreen", 0, 255, 127),
            ColorName("SteelBlue", 70, 130, 180),
            ColorName("Tan", 210, 180, 140),
            ColorName("Thistle", 216, 191, 216),
            ColorName("Tomato", 255, 99, 71),
            ColorName("Turquoise", 64, 224, 208),
            ColorName("Violet", 238, 130, 238),
            ColorName("Wheat", 245, 222, 179),
            ColorName("WhiteSmoke", 245, 245, 245),
            ColorName("YellowGreen", 154, 205, 50),

        )

        //return colors
    }
    fun getColorDescription(r: Int, g: Int, b: Int): String {
        return when {
            r > 220 && g > 220 && b > 220 -> "White"
            r < 80 && g < 80 && b < 80 -> "Black"
            r > g + 50 && r > b + 50 -> "Red"
            r > 200 && g > 180 && b < 100 -> "Yellow"
            g > r + 50 && g > b + 50 -> "Green"
            b > r + 50 && b > g + 50 -> "Blue"
            r > 150 && g > 150 && b < 100 -> "Light yellow"
            r > 150 && b > 150 && g < 100 -> "Pink/Purple"
            g > 150 && b > 150 && r < 100 -> "Cyan"
            r > 120 && g > 70 && b < 70 -> "Orange"
            r in 100..160 && g in 70..130 && b in 50..90 -> "Brown"
            r in 140..200 && g in 140..200 && b in 140..200 -> "Gray"
            g > 100 && b > 100 && r < 100 -> "Teal"
            r > 150 && g < 100 && b > 150 -> "Magenta"
            r < 150 && g > 150 && b < 150 -> "Light green"
            r > 200 && g > 200 && b > 150 -> "Beige"
            r > 120 && g > 120 && b > 180 -> "Light blue"
            else -> "Medium gray" // To ensure no RGB value is left unspecified
        }
    }
    // Lấy tên màu gần nhất từ RGB
//    fun getColorNameFromRgb(r: Int, g: Int, b: Int): String {
//        val colorList = initColorList()
//        var closestMatch: ColorName? = null
//        var minMSE = Int.MAX_VALUE
//
//        for (color in colorList) {
//            val mse = color.computeMSE(r, g, b)
//            if (mse < minMSE) {
//                minMSE = mse
//                closestMatch = color
//            }
//        }
//
//        return closestMatch?.name ?: "No matched color name."
//    }

//    fun colorDistance(r1: Int, g1: Int, b1: Int, r2: Int, g2: Int, b2: Int): Double {
//        return Math.sqrt(
//            ((r1 - r2) * (r1 - r2) +
//                    (g1 - g2) * (g1 - g2) +
//                    (b1 - b2) * (b1 - b2)).toDouble()
//        )
//    }


    // Chuyển hexColor thành RGB, rồi gọi getColorNameFromRgb
//    fun getColorNameFromHex(hexColor: Int): String {
//        val r = (hexColor and 0xFF0000) shr 16
//        val g = (hexColor and 0xFF00) shr 8
//        val b = (hexColor and 0xFF)
//        return getColorNameFromRgb(r, g, b)
//    }

    // Lấy tên màu từ đối tượng Color
//    fun getColorNameFromColor(color : Triple<Int, Int, Int>): String {
//        return getColorNameFromRgb(color.first, color.second, color.third)
//    }
    data class ColorName(val name: String, val r: Int, val g: Int, val b: Int) {
        fun computeMSE(pixR: Int, pixG: Int, pixB: Int): Int {
            return ((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b) * (pixB - b)) / 3
        }
    }
}

