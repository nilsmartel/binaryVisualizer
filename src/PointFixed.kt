
data class PointFixed(
    val x: Int,
    val y: Int
) {
    fun sideLength(p: PointFixed): Int = Math.max(Math.abs(x-p.x), Math.abs(y-p.y))
}
