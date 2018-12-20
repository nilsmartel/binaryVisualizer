import interfaces.Interpolateable

data class Point(
    val x: Double,
    val y: Double
): Interpolateable<Point> {
    fun toFixed() = PointFixed(x.toInt(), y.toInt())

    fun moveRight(v: Double) = Point(x+v, y)
    fun moveDown(v: Double) = Point(x, y+v)

    fun add(dir: Direction, len: Double) = when(dir) {
        Direction.UP -> Point(x, y-len)
        Direction.RIGHT -> Point(x+len, y)
        Direction.DOWN -> Point(x, y+len)
        Direction.LEFT -> Point(x-len, y)
    }

    override fun interpolate(other: Point, factor: Double) = mul(1-factor).add(other.mul(factor))
    fun mul(v: Double) = Point(x*v, y*v)
    fun add(p: Point) = Point(x+p.x, y+p.y)
}
