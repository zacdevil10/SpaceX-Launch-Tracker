package uk.co.zac_h.spacex.core.common.utils

import uk.co.zac_h.spacex.core.common.types.Order

fun <T> Iterable<T>.filterAll(vararg predicates: ((T) -> Boolean?)?): List<T> = filter { value ->
    predicates.filterNotNull().all { it(value) ?: true }
}

inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
    direction: Order,
    crossinline selector: (T) -> R?
): List<T> = when (direction) {
    Order.ASCENDING -> sortedWith(compareBy(nullsLast(), selector))
    Order.DESCENDING -> sortedWith(compareByDescending(selector)).sortedWith(
        compareByDescending(
            nullsLast(),
            selector
        )
    )
}
