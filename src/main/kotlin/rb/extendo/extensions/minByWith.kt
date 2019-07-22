package rb.extendo.extensions


inline fun <T, R : Comparable<R>> Iterable<T>.minByWith(selector: (T) -> R): Pair<T,R>? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minElem = iterator.next()
    var minValue = selector(minElem)
    while (iterator.hasNext()) {
        val e = iterator.next()
        val v = selector(e)
        if (minValue > v) {
            minElem = e
            minValue = v
        }
    }
    return Pair(minElem,minValue)
}
inline fun <T, R : Comparable<R>> Sequence<T>.minByWith(selector: (T) -> R): Pair<T,R>? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minElem = iterator.next()
    var minValue = selector(minElem)
    while (iterator.hasNext()) {
        val e = iterator.next()
        val v = selector(e)
        if (minValue > v) {
            minElem = e
            minValue = v
        }
    }
    return Pair(minElem,minValue)
}