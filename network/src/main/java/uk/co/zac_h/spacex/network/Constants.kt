package uk.co.zac_h.spacex.network

//Hosts
const val MOCK_LAUNCH_LIBRARY_BASE_URL = "https://lldev.thespacedevs.com/2.2.0/"
const val LAUNCH_LIBRARY_BASE_URL = "https://ll.thespacedevs.com/2.2.0/"
const val SPACEFLIGHT_NEWS_BASE_URL = "https://api.spaceflightnewsapi.net/v4/"
const val REDDIT_BASE_URL = "https://reddit.com/"

//Legacy API
const val SPACEX_BASE_URL_V4 = "https://api.spacexdata.com/v4/"

//region SpaceX

//Endpoints
const val SPACEX_PREVIOUS_LAUNCHES = "launch/previous"
const val SPACEX_UPCOMING_LAUNCHES = "launch/upcoming"
const val SPACEX_ASTRONAUTS = "astronaut"
const val SPACEX_AGENCY = "agencies/121"

//Legacy endpoints
const val SPACEX_CAPSULES = "capsules"
const val SPACEX_CORES = "cores"
const val SPACEX_CREW = "crew"
const val SPACEX_LANDING_PADS = "landpads"

const val SPACEX_LAUNCHPADS = "launchpads"

const val SPACEX_QUERY = "/query"

//Query
const val SPACEX_CAPSULES_QUERY = "$SPACEX_CAPSULES$SPACEX_QUERY"
const val SPACEX_CORES_QUERY = "$SPACEX_CORES$SPACEX_QUERY"
const val SPACEX_CREW_QUERY = "$SPACEX_CREW$SPACEX_QUERY"
const val SPACEX_LANDING_PADS_QUERY = "$SPACEX_LANDING_PADS$SPACEX_QUERY"
const val SPACEX_LAUNCHPADS_QUERY = "$SPACEX_LAUNCHPADS$SPACEX_QUERY"

//Fields
const val SPACEX_FIELD_ID = "id"

//CAPSULE
const val SPACEX_FIELD_CAPSULE_SERIAL = "serial"
const val SPACEX_FIELD_CAPSULE_STATUS = "status"
const val SPACEX_FIELD_CAPSULE_TYPE = "type"
const val SPACEX_FIELD_CAPSULE_DRAGON = "dragon"
const val SPACEX_FIELD_CAPSULE_REUSE_COUNT = "reuse_count"
const val SPACEX_FIELD_CAPSULE_WATER_LANDINGS = "water_landings"
const val SPACEX_FIELD_CAPSULE_LAND_LANDINGS = "land_landings"
const val SPACEX_FIELD_CAPSULE_LAST_UPDATE = "last_update"
const val SPACEX_FIELD_CAPSULE_LAUNCHES = "launches"

const val SPACEX_CAPSULE_STATUS_ACTIVE = "active"
const val SPACEX_CAPSULE_STATUS_RETIRED = "retired"
const val SPACEX_CAPSULE_STATUS_DESTROYED = "destroyed"

const val SPACEX_CAPSULE_TYPE_DRAGON_1 = "Dragon 1.0"
const val SPACEX_CAPSULE_TYPE_DRAGON_1_1 = "Dragon 1.1"
const val SPACEX_CAPSULE_TYPE_DRAGON_2 = "Dragon 2.0"

//CORE
const val SPACEX_FIELD_CORE_SERIAL = "serial"
const val SPACEX_FIELD_CORE_BLOCK = "block"
const val SPACEX_FIELD_CORE_STATUS = "status"
const val SPACEX_FIELD_CORE_REUSE_COUNT = "reuse_count"
const val SPACEX_FIELD_CORE_RTLS_ATTEMPTS = "rtls_attempts"
const val SPACEX_FIELD_CORE_RTLS_LANDING = "rtls_landings"
const val SPACEX_FIELD_CORE_ASDS_ATTEMPTS = "asds_attempts"
const val SPACEX_FIELD_CORE_ASDS_LANDINGS = "asds_landings"
const val SPACEX_FIELD_CORE_LAST_UPDATE = "last_update"
const val SPACEX_FIELD_CORE_LAUNCHES = "launches"

const val SPACEX_CORE_STATUS_ACTIVE = "active"
const val SPACEX_CORE_STATUS_INACTIVE = "inactive"
const val SPACEX_CORE_STATUS_EXPENDED = "expended"
const val SPACEX_CORE_STATUS_LOST = "lost"
const val SPACEX_CORE_STATUS_RETIRED = "retired"

//CREW
const val SPACEX_FIELD_CREW_NAME = "name"
const val SPACEX_FIELD_CREW_STATUS = "status"
const val SPACEX_FIELD_CREW_AGENCY = "agency"
const val SPACEX_FIELD_CREW_IMAGE = "image"
const val SPACEX_FIELD_CREW_WIKI = "wikipedia"
const val SPACEX_FIELD_CREW_LAUNCHES = "launches"

const val SPACEX_CREW_STATUS_IN_TRAINING = "In-Training"
const val SPACEX_CREW_STATUS_ACTIVE = "Active"
const val SPACEX_CREW_STATUS_INACTIVE = "Inactive"
const val SPACEX_CREW_STATUS_RETIRED = "Retired"
const val SPACEX_CREW_STATUS_DECEASED = "Deceased"
const val SPACEX_CREW_LOST_IN_TRAINING = "Lost In Training"

//LANDING PAD
const val SPACEX_FIELD_LANDING_PAD_NAME = "name"
const val SPACEX_FIELD_LANDING_PAD_FULL_NAME = "full_name"
const val SPACEX_FIELD_LANDING_PAD_STATUS = "status"
const val SPACEX_FIELD_LANDING_PAD_TYPE = "type"
const val SPACEX_FIELD_LANDING_PAD_LOCALITY = "locality"
const val SPACEX_FIELD_LANDING_PAD_REGION = "region"
const val SPACEX_FIELD_LANDING_PAD_LATITUDE = "latitude"
const val SPACEX_FIELD_LANDING_PAD_LONGITUDE = "longitude"
const val SPACEX_FIELD_LANDING_PAD_LANDING_ATTEMPTS = "landing_attempts"
const val SPACEX_FIELD_LANDING_PAD_LANDING_SUCCESS = "landing_successes"
const val SPACEX_FIELD_LANDING_PAD_WIKI = "wikipedia"
const val SPACEX_FIELD_LANDING_PAD_DETAILS = "details"
const val SPACEX_FIELD_LANDING_PAD_LAUNCHES = "launches"

const val SPACEX_LANDING_PAD_STATUS_ACTIVE = "active"
const val SPACEX_LANDING_PAD_STATUS_INACTIVE = "inactive"
const val SPACEX_LANDING_PAD_STATUS_RETIRED = "retired"
const val SPACEX_LANDING_PAD_STATUS_LOST = "lost"
const val SPACEX_LANDING_PAD_STATUS_UNDER_CONSTRUCTION = "under construction"

//LAUNCH
const val SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER = "flight_number"
const val SPACEX_FIELD_LAUNCH_NAME = "name"
const val SPACEX_FIELD_LAUNCH_DATE_UTC = "date_utc"
const val SPACEX_FIELD_LAUNCH_DATE_UNIX = "date_unix"
const val SPACEX_FIELD_LAUNCH_DATE_LOCAL = "date_local"
const val SPACEX_FIELD_LAUNCH_DATE_PRECISION = "date_precision"
const val SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UTC = "static_fire_date_utc"
const val SPACEX_FIELD_LAUNCH_STATIC_FIRE_DATE_UNIX = "static_fire_date_unix"
const val SPACEX_FIELD_LAUNCH_TBD = "tbd"
const val SPACEX_FIELD_LAUNCH_NET = "net"
const val SPACEX_FIELD_LAUNCH_WINDOW = "window"
const val SPACEX_FIELD_LAUNCH_ROCKET = "rocket"
const val SPACEX_FIELD_LAUNCH_SUCCESS = "success"
const val SPACEX_FIELD_LAUNCH_FAILURES = "failures"
const val SPACEX_FIELD_LAUNCH_FAILURES_TIME = "time"
const val SPACEX_FIELD_LAUNCH_FAILURES_ALTITUDE = "altitude"
const val SPACEX_FIELD_LAUNCH_FAILURES_REASON = "reason"
const val SPACEX_FIELD_LAUNCH_UPCOMING = "upcoming"
const val SPACEX_FIELD_LAUNCH_DETAILS = "details"
const val SPACEX_FIELD_LAUNCH_FAIRINGS = "fairings"
const val SPACEX_FIELD_LAUNCH_FAIRINGS_REUSED = "reused"
const val SPACEX_FIELD_LAUNCH_FAIRINGS_RECOVERY_ATTEMPT = "recovery_attempt"
const val SPACEX_FIELD_LAUNCH_FAIRINGS_RECOVERED = "recovered"
const val SPACEX_FIELD_LAUNCH_FAIRINGS_SHIPS = "ships"
const val SPACEX_FIELD_LAUNCH_CREW = "crew"
const val SPACEX_FIELD_LAUNCH_CREW_ROLE = "role"
const val SPACEX_FIELD_LAUNCH_SHIPS = "ships"
const val SPACEX_FIELD_LAUNCH_CAPSULES = "capsules"
const val SPACEX_FIELD_LAUNCH_PAYLOADS = "payloads"
const val SPACEX_FIELD_LAUNCH_LAUNCHPAD = "launchpad"
const val SPACEX_FIELD_LAUNCH_CORES = "cores"
const val SPACEX_FIELD_LAUNCH_CORES_CORE = "core"
const val SPACEX_FIELD_LAUNCH_CORES_FLIGHT = "flight"
const val SPACEX_FIELD_LAUNCH_CORES_GRIDFINS = "gridfins"
const val SPACEX_FIELD_LAUNCH_CORES_LEGS = "legs"
const val SPACEX_FIELD_LAUNCH_CORES_REUSED = "reused"
const val SPACEX_FIELD_LAUNCH_CORES_LANDING_ATTEMPT = "landing_attempt"
const val SPACEX_FIELD_LAUNCH_CORES_LANDING_SUCCESS = "landing_success"
const val SPACEX_FIELD_LAUNCH_CORES_LANDING_TYPE = "landing_type"
const val SPACEX_FIELD_LAUNCH_CORES_LANDING_PAD = "landpad"
const val SPACEX_FIELD_LAUNCH_LINKS = "links"
const val SPACEX_FIELD_LAUNCH_PATCH = "patch"
const val SPACEX_FIELD_LAUNCH_PATCH_SMALL = "small"
const val SPACEX_FIELD_LAUNCH_PATCH_LARGE = "large"
const val SPACEX_FIELD_LAUNCH_REDDIT = "reddit"
const val SPACEX_FIELD_LAUNCH_REDDIT_CAMPAIGN = "campaign"
const val SPACEX_FIELD_LAUNCH_REDDIT_LAUNCH = "launch"
const val SPACEX_FIELD_LAUNCH_REDDIT_MEDIA = "media"
const val SPACEX_FIELD_LAUNCH_REDDIT_RECOVERY = "recovery"
const val SPACEX_FIELD_LAUNCH_FLICKR = "flickr"
const val SPACEX_FIELD_LAUNCH_FLICKR_SMALL = "small"
const val SPACEX_FIELD_LAUNCH_FLICKR_ORIGINAL = "original"
const val SPACEX_FIELD_LAUNCH_WEBCAST = "webcast"
const val SPACEX_FIELD_LAUNCH_YOUTUBE_ID = "youtube_id"
const val SPACEX_FIELD_LAUNCH_ARTICLE = "article"
const val SPACEX_FIELD_LAUNCH_WIKI = "wikipedia"
const val SPACEX_FIELD_LAUNCH_AUTO_UPDATE = "auto_update"

//Launchpad
const val SPACEX_FIELD_LAUNCHPAD_NAME = "name"
const val SPACEX_FIELD_LAUNCHPAD_FULL_NAME = "full_name"
const val SPACEX_FIELD_LAUNCHPAD_STATUS = "status"
const val SPACEX_FIELD_LAUNCHPAD_LOCALITY = "locality"
const val SPACEX_FIELD_LAUNCHPAD_REGION = "region"
const val SPACEX_FIELD_LAUNCHPAD_TIMEZONE = "timezone"
const val SPACEX_FIELD_LAUNCHPAD_LAT = "latitude"
const val SPACEX_FIELD_LAUNCHPAD_LNG = "longitude"
const val SPACEX_FIELD_LAUNCHPAD_LAUNCH_ATTEMPTS = "launch_attempts"
const val SPACEX_FIELD_LAUNCHPAD_LAUNCH_SUCCESSES = "launch_successes"
const val SPACEX_FIELD_LAUNCHPAD_ROCKETS = "rockets"
const val SPACEX_FIELD_LAUNCHPAD_LAUNCHES = "launches"

//Rocket
const val SPACEX_FIELD_ROCKET_NAME = "name"
const val SPACEX_FIELD_ROCKET_TYPE = "type"
const val SPACEX_FIELD_ROCKET_ACTIVE = "active"
const val SPACEX_FIELD_ROCKET_STAGES = "stages"
const val SPACEX_FIELD_ROCKET_BOOSTERS = "boosters"
const val SPACEX_FIELD_ROCKET_COST_PER_LAUNCH = "cost_per_launch"
const val SPACEX_FIELD_ROCKET_SUCCESS_RATE_PCT = "success_rate_pct"
const val SPACEX_FIELD_ROCKET_FIRST_FLIGHT = "first_flight"
const val SPACEX_FIELD_ROCKET_COUNTRY = "country"
const val SPACEX_FIELD_ROCKET_COMPANY = "company"
const val SPACEX_FIELD_ROCKET_HEIGHT = "height"
const val SPACEX_FIELD_ROCKET_DIAMETER = "diameter"
const val SPACEX_FIELD_ROCKET_MASS = "mass"
const val SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS = "payload_weights"
const val SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS_NAME = "name"
const val SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS_KG = "kg"
const val SPACEX_FIELD_ROCKET_PAYLOAD_WEIGHTS_LB = "lb"
const val SPACEX_FIELD_ROCKET_FIRST_STAGE = "first_stage"
const val SPACEX_FIELD_ROCKET_FIRST_STAGE_REUSABLE = "reusable"
const val SPACEX_FIELD_ROCKET_FIRST_STAGE_ENGINES = "engines"
const val SPACEX_FIELD_ROCKET_FIRST_STAGE_FUEL_AMOUNT_TONS = "fuel_amount_tons"
const val SPACEX_FIELD_ROCKET_FIRST_STAGE_BURN_TIME_SEC = "burn_time_sec"
const val SPACEX_FIELD_ROCKET_FIRST_STAGE_THRUST_SEA_LEVEL = "thrust_sea_level"
const val SPACEX_FIELD_ROCKET_FIRST_STAGE_THRUST_VACUUM = "thrust_vacuum"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE = "second_stage"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_REUSABLE = "reusable"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_ENGINES = "engines"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_FUEL_AMOUNT_TONS = "fuel_amount_tons"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_BURN_TIME_SEC = "burn_time_sec"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_THRUST = "thrust"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOADS = "payloads"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_OPTION_1 = "option_1"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_COMPOSITE_FAIRING = "composite_fairing"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_FAIRING_HEIGHT = "height"
const val SPACEX_FIELD_ROCKET_SECOND_STAGE_PAYLOAD_FAIRING_DIAMETER = "diameter"
const val SPACEX_FIELD_ROCKET_ENGINES = "engines"
const val SPACEX_FIELD_ROCKET_ENGINES_NUMBER = "number"
const val SPACEX_FIELD_ROCKET_ENGINES_TYPE = "type"
const val SPACEX_FIELD_ROCKET_ENGINES_VERSION = "version"
const val SPACEX_FIELD_ROCKET_ENGINES_LAYOUT = "layout"
const val SPACEX_FIELD_ROCKET_ENGINES_ISP = "isp"
const val SPACEX_FIELD_ROCKET_ENGINES_ENGINE_LOSS_MAX = "engine_loss_max"
const val SPACEX_FIELD_ROCKET_ENGINES_PROPELLANT_1 = "propellant_1"
const val SPACEX_FIELD_ROCKET_ENGINES_PROPELLANT_2 = "propellant_2"
const val SPACEX_FIELD_ROCKET_ENGINES_THRUST_SEA_LEVEL = "thrust_sea_level"
const val SPACEX_FIELD_ROCKET_ENGINES_THRUST_VACUUM = "thrust_vacuum"
const val SPACEX_FIELD_ROCKET_ENGINES_THRUST_TO_WEIGHT = "thrust_to_weight"
const val SPACEX_FIELD_ROCKET_LANDING_LEGS = "landing_legs"
const val SPACEX_FIELD_ROCKET_LANDING_LEGS_NUMBER = "number"
const val SPACEX_FIELD_ROCKET_LANDING_LEGS_MATERIAL = "material"
const val SPACEX_FIELD_ROCKET_FLICKR = "flickr_images"
const val SPACEX_FIELD_ROCKET_WIKIPEDIA = "wikipedia"
const val SPACEX_FIELD_ROCKET_DESCRIPTION = "description"

//Status
const val SPACEX_IN_TRAINING = "In-Training"
const val SPACEX_ACTIVE = "Active"
const val SPACEX_INACTIVE = "Inactive"
const val SPACEX_UNKNOWN = "Unknown"
const val SPACEX_EXPENDED = "Expended"
const val SPACEX_LOST = "Lost"
const val SPACEX_RETIRED = "Retired"
const val SPACEX_DECEASED = "Deceased"
const val SPACEX_LOST_IN_TRAINING = "Lost In Training"
const val SPACEX_DESTROYED = "Destroyed"
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
