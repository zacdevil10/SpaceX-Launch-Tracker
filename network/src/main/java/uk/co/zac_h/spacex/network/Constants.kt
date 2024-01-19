package uk.co.zac_h.spacex.network

//Hosts
const val SPACEFLIGHT_NEWS_BASE_URL = "https://api.spaceflightnewsapi.net/v4/"
const val REDDIT_BASE_URL = "https://reddit.com/"

//region SpaceX

//Endpoints
const val SPACEX_PREVIOUS_LAUNCHES = "launch/previous"
const val SPACEX_UPCOMING_LAUNCHES = "launch/upcoming"
const val SPACEX_LAUNCHER = "launcher"
const val SPACEX_SPACECRAFT = "spacecraft"
const val SPACEX_ASTRONAUTS = "astronaut"
const val SPACEX_AGENCY = "agencies/121"

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

//region Reddit

const val REDDIT_PARAM_SUBREDDIT = "subreddit"
const val REDDIT_PARAM_ORDER = "order"
const val REDDIT_PARAM_LIMIT = "limit"

const val REDDIT_SUBREDDIT =
    "r/{$REDDIT_PARAM_SUBREDDIT}/{$REDDIT_PARAM_ORDER}/.json?raw_json=1"

const val REDDIT_PARAM_ORDER_HOT = "hot"
const val REDDIT_PARAM_ORDER_NEW = "new"

const val REDDIT_QUERY_AFTER = "after"

//endregion

const val DASHBOARD_PREFERENCES = "dashboard_preferences"
