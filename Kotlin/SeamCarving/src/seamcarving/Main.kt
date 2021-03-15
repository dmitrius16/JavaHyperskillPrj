package seamcarving

import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main() {
    println("Enter rectangle width:")
    val width = readLine()!!.toInt()
    println("Enter rectangle height:")
    val height = readLine()!!.toInt()
    println("Enter output image name:")
    val name = readLine()!!//.substringBefore('.')
    val image = createImage(width, height)
    ImageIO.write(image, "png", File(name))
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
