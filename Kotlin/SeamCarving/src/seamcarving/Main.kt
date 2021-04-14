package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    try {
        var inputFile: String = ""
        if (args[0] == "-in") {
            inputFile = args[1]
        }
        var outputFile: String = "default.png"
        if (args[2] == "-out") {
            outputFile = args[3]
        }

        val originalImage = readImage(inputFile)
        val negativeImage = makeNegativeImage(originalImage)
        saveImage(negativeImage, outputFile)
    } catch (ex: ArrayIndexOutOfBoundsException) {
        println("Please run programm with command line parameters")
    } catch (ex: FileNotFoundException) {
        println("Can't find source file")
    }
}
fun readImage(fileName: String): BufferedImage {
    val image = ImageIO.read(File(fileName))
    return image
}

fun saveImage(image: BufferedImage, fileName: String) {
    ImageIO.write(image, "png", File(fileName))
}

fun makeNegativeImage(image: BufferedImage): BufferedImage {
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


fun createImage(width: Int, height: Int): BufferedImage {
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val context = image.graphics
    context.color = Color.BLACK
    context.fillRect(0, 0, width - 1, height - 1)
    context.color = Color.RED
    context.drawLine(0, 0, width - 1, height - 1)
    context.drawLine(0, height - 1, width - 1, 0)
    return image
}
