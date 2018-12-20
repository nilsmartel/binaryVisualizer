import interfaces.Interpolateable

class TransitionIterator<T> (
    val from: T,
    val to: T,
    val steps: Int,
    var currentStep: Int = 0
): Iterator<T> where T: Interpolateable<T> {
    override fun next() = from.interpolate(to, (currentStep++).toDouble() / steps.toDouble())


    override fun hasNext(): Boolean = currentStep < steps
}

