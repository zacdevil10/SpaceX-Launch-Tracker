package uk.co.zac_h.spacex.network

/** A Launch Library server. Both serve the same API under the same path. */
enum class LaunchLibraryEnvironment(val label: String, val baseUrl: String) {
    PRODUCTION("Production (ll.thespacedevs.com)", "https://ll.thespacedevs.com/2.3.0/"),

    /** Not rate limited, but its data can be stale. Meant for development only. */
    DEVELOPMENT("Development (lldev.thespacedevs.com)", "https://lldev.thespacedevs.com/2.3.0/")
}

/**
 * Says which Launch Library server to call. Release builds always use [LaunchLibraryEnvironment.PRODUCTION];
 * debug builds read the choice from the debug tools.
 *
 * Called on OkHttp's threads for every request, so it may block briefly.
 */
fun interface LaunchLibraryEnvironmentSource {
    fun current(): LaunchLibraryEnvironment
}
