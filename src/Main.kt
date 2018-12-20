import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFrame
import java.awt.FileDialog
import kotlin.system.exitProcess


const val LENGTH = 16.0
val LENGTH_DIGITS: Int = maxBinaryDigits(LENGTH)
val LENGTH_DIVISOR = 1 shl LENGTH_DIGITS

fun lengthModifier(v: Double) = Math.sqrt(v/ LENGTH) * LENGTH

fun maxBinaryDigits(n: Double) = maxBinaryDigits(Math.ceil(n).toInt())
fun maxBinaryDigits(n: Int) : Int {
    var i = 1
    var digits = 1
    while (i < n) {
        i = i shl 1
        digits++
    }

    return digits
}

fun main(args: Array<String>) {
    val fd = FileDialog(JFrame())
    fd.isVisible = true
    val f = fd.files.first()

    val iter = BoolIterator(f.readBytes())
    println("Read File into Memory")
    val points = mutableListOf<Pair<Point, Color>>()
    var count = 0
    var x = 10000
    try {
        var current = Point(0.0, 0.0)
        while (true) {
            val dir = iter.nextDirection()
            val len = iter.nextLength()
            val col = iter.nextColor()

            val next = current.add(dir, len)

            points.add(next to col)
            current = next
            count++
            if (--x == 0) {
                x = 10000
                println("read ($count) Points")
            }

            iter.getInteger(23)
        }
    } catch (e: Exception) {
        println("Finished deserializing File")
        println("read ($count) Points")
        renderImage(points)
    }
}

fun renderImage(points: List<Pair<Point, Color>>) {
    val img = renderPoints(points)
    try {
        ImageIO.write(img, "png", File("./output.png"))
        exitProcess(1)
    } catch (e: IOException) {
        println("Failed to write Image")
        exitProcess(0)
    }
}

fun renderPoints(list: List<Pair<Point, Color>>) : BufferedImage {
    val minRight = list.map {it.first.x}.min()!!
    val minDown = list.map {it.first.y}.min()!!

    val points =
        list
            .map { it.first.moveDown(-minDown).moveRight(-minRight).mul(0.2)}
            .toMutableList()
    val colors =
        list
            .map { it.second }
            .toMutableList()

    val width = points.map {it.x}.max()!!.toInt()+1
    val height = points.map {it.y}.max()!!.toInt()+1
    println("($width, $height)")
    //val img = Image(Math.ceil(width).toInt()+1, Math.ceil(height).toInt()+1)

    val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    println("Sucessfully Allocated Image")
    var currentPoint = points.removeAt(0)
    var currentColor = colors.removeAt(0)

    for (pair in points.zip(colors)) {
        val steps = currentPoint.toFixed().sideLength(pair.first.toFixed())

        val colorIter = TransitionIterator(currentColor, pair.second, steps)
        for (p in TransitionIterator(currentPoint, pair.first, steps)) {
            val px = p.toFixed()
            img.setRGB(px.x, px.y, colorIter.next().toInt())
        }

        currentPoint = pair.first
        currentColor = pair.second
    }

    return img
}







