package uk.co.zac_h.spacex.network

//Hosts
const val SPACEFLIGHT_NEWS_BASE_URL = "https://api.spaceflightnewsapi.net/v4/"

//region SpaceX

//Endpoints
const val SPACEX_PREVIOUS_LAUNCHES = "launches/previous/"
const val SPACEX_UPCOMING_LAUNCHES = "launches/upcoming/"
const val SPACEX_LAUNCHER = "launchers/"
const val SPACEX_SPACECRAFT = "spacecraft/"
const val SPACEX_ASTRONAUTS = "astronauts/"
const val SPACEX_AGENCY = "agencies/121/"

// Free: calls to it do not count toward the 15 per hour limit.
const val API_THROTTLE = "api-throttle/"

//CREW
const val SPACEX_CREW_STATUS_IN_TRAINING = "In-Training"
const val SPACEX_CREW_STATUS_ACTIVE = "Active"
const val SPACEX_CREW_STATUS_INACTIVE = "Inactive"
const val SPACEX_CREW_STATUS_RETIRED = "Retired"
const val SPACEX_CREW_STATUS_DECEASED = "Deceased"
const val SPACEX_CREW_LOST_IN_TRAINING = "Lost In Training"

//Status
const val SPACEX_IN_TRAINING = "In-Training"
const val SPACEX_ACTIVE = "Active"
const val SPACEX_INACTIVE = "Inactive"
const val SPACEX_UNKNOWN = "Unknown"
const val SPACEX_LOST = "Lost"
const val SPACEX_RETIRED = "Retired"
const val SPACEX_DECEASED = "Deceased"
const val SPACEX_LOST_IN_TRAINING = "Lost In Training"
const val SPACEX_UNDER_CONSTRUCTION = "Under construction"

//endregion

//region Spaceflight News

const val SPACEFLIGHT_NEWS_ARTICLES = "articles"

//endregion

const val DASHBOARD_PREFERENCES = "dashboard_preferences"
