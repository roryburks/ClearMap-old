package rb.extendo.extensions

infix fun ClosedFloatingPointRange<Double>.step(step: Double) = DoubleStepIterable(this, step)

class DoubleStepIterable(
    val range : ClosedFloatingPointRange<Double>,
    val step: Double) : Iterable<Double>
{
    override fun iterator() = Iter()

    inner class Iter() : Iterator<Double>{
        var caret = range.start

        override fun hasNext() = if( step <= 0) false else caret < range.endInclusive

        override fun next(): Double {
            val old = caret
            caret += step
            return old
        }
    }
}