import interfaces.Interpolateable

data class Color(val red: Short, val green: Short, val blue: Short): Interpolateable<Color> {
    override fun interpolate(other: Color, factor: Double) = mul(1-factor).add(other.mul(factor))

    fun mul(v: Double) = Color((red*v).toShort(), (green*v).toShort(), (blue*v).toShort())
    fun add(c: Color) = Color((red + c.red).toShort(), (green + c.green).toShort(), (blue + c.blue).toShort())

    // fun toInt(): Int = red.toInt() shl 24 or green.toInt() shl 16 or blue.toInt() shl 8 or 0xFF
    fun toInt(): Int = 0 or (red.toInt() shl 16) or (green.toInt() shl 8) or blue.toInt()

    companion object {
        fun fromInt(v: Int): Color {
            val red = (v and (0xFF shl 16)) shr 16
            val green = (v and (0xFF shl 8)) shr 8
            val blue = (v and (0xFF shl 0)) shr 0

            return Color(red.toShort(), green.toShort(), blue.toShort())
        }
    }

    override fun toString() = "($red, $green, $blue)"
}
