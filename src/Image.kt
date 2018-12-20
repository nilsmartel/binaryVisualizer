import java.awt.image.BufferedImage

class Image(val width: Int, val height: Int) {
    private val data = IntArray(width*height)

    fun setColor(p: PointFixed, c: Color) {
        if (p.x > width || p.y > height) throw IndexOutOfBoundsException("$p exceeds limit (x=$width, y=$height)")
        data[p.y*width + p.x] = c.toInt()
    }

    fun getColor(p: PointFixed) = data[p.x + p.y*width]

    fun toImage(): BufferedImage {
        val bufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)
        (0 until this.width).forEach { x ->
            (0 until this.height).forEach { y ->
                val pxl = getColor(PointFixed(x, y))
                bufferedImage.setRGB(x, y, pxl)
            }
        }

        return bufferedImage
    }
}
