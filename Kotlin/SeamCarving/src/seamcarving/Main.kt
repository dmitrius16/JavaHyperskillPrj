package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    var inputFile: String = ""
    if (args[0] == "-in") {
        inputFile = args[1]
    }
    var outputFile: String = "default.png"
    if (args[2] == "-out") {
        outputFile = args[3]
    }

    println(inputFile)
    println(outputFile)




    /*
    println("Enter rectangle width:")
    val width = readLine()!!.toInt()
    println("Enter rectangle height:")
    val height = readLine()!!.toInt()
    println("Enter output image name:")
    val name = readLine()!!//.substringBefore('.')
    val image = createImage(width, height)
    ImageIO.write(image, "png", File(name))

     */
}
fun readImage(fileName: String): BufferedImage {
    val image = ImageIO.read(File(fileName))
    return image
}

fun makeNegativeImage(image: BufferedImage): BufferedImage {
    val negImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    //negImage.set
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
