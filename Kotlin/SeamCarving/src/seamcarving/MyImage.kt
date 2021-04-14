package seamcarving
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow

class MyImage(val image: BufferedImage) {

    fun makeNegative() : BufferedImage {
        val negImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        val rgbColors = IntArray(image.width * image.height)
        image.getRGB(0,0, image.width, image.height, rgbColors, 0, image.width)

        // negative photo
        for (i in rgbColors.indices) {
            var pixel = rgbColors[i]
            val red = 255 - ((pixel and 0xff0000) shr 16)
            val green = 255 - ((pixel and 0xff00) shr 8)
            val blue = 255 - ((pixel and 0xff))
            pixel = (red shl 16) or (green shl 8) or blue
            rgbColors[i] = pixel
        }
        negImage.setRGB(0, 0, image.width, image.height, rgbColors, 0, image.width)
        return negImage
    }


    private fun calcSquareGradient(colorBefore: Color, colorAfter: Color): Double {
        val colorContentBefore = intArrayOf(colorBefore.red, colorBefore.green, colorBefore.blue)
        val colorContentAfter = intArrayOf(colorAfter.red, colorAfter.green, colorAfter.blue)
        var gradient: Double = 0.0
        for (i in 0..3) {
            gradient += (colorContentAfter[i] - colorContentBefore[i]).toDouble().pow(2)
        }
        return gradient
    }

    fun calcPixelEnergy(_x: Int, _y: Int): Double {
        val x = if(_x == 0) 1 else _x
        val y = if(_y == 0) 1 else _y

        var pxColorBefore: Color = Color(image.getRGB(x - 1, y))
        var pxColorAfter: Color = Color(image.getRGB(x - 1, y))

        val gradX = calcSquareGradient(pxColorBefore, pxColorAfter)

        pxColorBefore = Color(image.getRGB(x, y - 1))
        pxColorAfter = Color(image.getRGB(x, y + 1))

        val gradY = calcSquareGradient(pxColorBefore, pxColorAfter)

        return (gradX + gradY).pow(0.5)
    }
}