package uk.co.zac_h.spacex

/** Which navigation chrome the window shows. */
enum class NavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

/**
 * Whether the window is wide enough for list and detail panes side by side. Only the app's
 * navigation host reads this; screens get what they need from it as plain parameters.
 */
enum class ContentType {
    SINGLE_PANE, DUAL_PANE
}
