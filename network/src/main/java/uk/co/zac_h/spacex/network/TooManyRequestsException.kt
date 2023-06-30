package uk.co.zac_h.spacex.network

class TooManyRequestsException(val time: Int? = null) : Exception()