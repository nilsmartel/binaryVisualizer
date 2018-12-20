enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    companion object {
        fun fromInt(v: Int) = when (v) {
            0 -> UP
            1 -> RIGHT
            2 -> DOWN
            3 -> LEFT
            else -> throw Exception("Can't create Direction from value $v")
        }
    }
}
