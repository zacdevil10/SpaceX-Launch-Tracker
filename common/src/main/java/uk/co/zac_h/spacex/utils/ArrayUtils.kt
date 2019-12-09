package uk.co.zac_h.spacex.utils

fun <T> ArrayList<T>.addAllExcludingPosition(list: List<T>, position: Int) {
    list.forEachIndexed { index, t ->
        if (index != position) this.add(t)
    }
}