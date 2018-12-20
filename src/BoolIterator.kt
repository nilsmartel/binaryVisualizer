class BoolIterator(a: ByteArray) : Iterator<Boolean> {
    private var iter: Iterator<Byte> = a.iterator()
    private var current = 0
    private var bitIndex = 8

    override fun hasNext(): Boolean = bitIndex == 8 && !iter.hasNext()

    override fun next(): Boolean {
        if (bitIndex == 8) {
            current = iter.next().toInt()
            bitIndex = 0
        }

        return current and (1 shl bitIndex++) > 0
    }

    fun getInteger(digits: Int) = (0 until digits).fold(0) { number, digit ->
        if (next()) number or (1 shl digit)
        else number
    }

    fun nextDirection() = Direction.fromInt(getInteger(2))

    fun nextColor() = Color.fromInt(getInteger(24))

    fun nextLength() = (getInteger(LENGTH_DIGITS).toDouble() / LENGTH_DIVISOR.toDouble()) * LENGTH
}
