package interfaces

interface Interpolateable<T> {
    fun interpolate(other: T, factor: Double): T
}
