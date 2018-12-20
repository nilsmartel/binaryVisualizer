import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import com.sun.tools.corba.se.idl.Util.getAbsolutePath
import javax.swing.JFrame
import java.awt.FileDialog
import kotlin.system.exitProcess


const val LENGTH = 6.0
val LENGTH_DIGITS: Int = maxBinaryDigits(LENGTH)
val LENGTH_DIVISOR = 1 shl LENGTH_DIGITS

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

    // val input = args.getOrElse(0) {
    //    "./Archiv.zip"
    // }



    // val f = File(input).readBytes()

    val iter = BoolIterator(f.readBytes())

    val points = mutableListOf<Pair<Point, Color>>()

    try {
        var current = Point(0.0, 0.0)
        while (true) {
            val dir = iter.nextDirection()
            val len = iter.nextLength()
            val col = iter.nextColor()

            val next = current.add(dir, len)

            points.add(next to col)
            current = next
        }
    } catch (e: Exception) {
        val img = renderPoints(points)
        try {
            val output = args.getOrElse(1) {
                "./output.png"
            }
            ImageIO.write(img, "png", File(output))
            exitProcess(1)
        } catch (e: IOException) {
            println("Failed to write Image")
            exitProcess(0)
        }

    }
}

fun renderPoints(list: List<Pair<Point, Color>>) : BufferedImage {
    val minRight = list.map {it.first.x}.min()!!
    val minDown = list.map {it.first.y}.min()!!

    val points =
        list
            .map { it.first.moveDown(-minDown).moveRight(-minRight) }
            .toMutableList()
    val colors =
        list
            .map { it.second }
            .toMutableList()

    val width = points.map {it.x}.max()!!
    val height = points.map {it.y}.max()!!

    //val img = Image(Math.ceil(width).toInt()+1, Math.ceil(height).toInt()+1)

    val img = BufferedImage(Math.ceil(width).toInt(), Math.ceil(height).toInt(), BufferedImage.TYPE_INT_RGB)

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







