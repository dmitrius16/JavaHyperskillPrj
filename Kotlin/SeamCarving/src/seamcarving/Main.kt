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
        val originalImage = MyImage(readImage(inputFile))
        val energyImage = originalImage.calcImageEnergy()
        saveImage(energyImage, outputFile)
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

