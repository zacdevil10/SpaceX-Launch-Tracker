package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.types.Order

fun <T> ArrayList<T>.addAllExcludingPosition(list: List<T>, position: Int) {
    list.forEachIndexed { index, t ->
        if (index != position) this.add(t)
    }
}

fun <T> Iterable<T>.reverse(value: Boolean): List<T> {
    val list = toMutableList()
    if (value) list.reverse()
    return list
}

fun <T> Iterable<T>.filterAll(vararg predicates: ((T) -> Boolean?)?): List<T> = filter { value ->
    predicates.filterNotNull().all { it(value) ?: true }
}

inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
    direction: Order,
    crossinline selector: (T) -> R?
): List<T> = when (direction) {
    Order.ASCENDING -> sortedWith(compareBy(selector))
    Order.DESCENDING -> sortedWith(compareByDescending(selector))
}
