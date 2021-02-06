package uk.co.zac_h.spacex.utils

const val SPACEX_STAGE_BASE_URL = "https://stage.spacexdata.com/v4/"
const val SPACEX_BASE_URL = "https://api.spacexdata.com/v4/"
const val TWITTER_BASE_URL = "https://api.twitter.com/1.1/"
const val REDDIT_BASE_URL = "https://reddit.com/"


/*SpaceX =================================================================================
=========================================================================================>*/

//Endpoints
const val SPACEX_CAPSULES = "capsules"
const val SPACEX_COMPANY = "company"
const val SPACEX_CORES = "cores"
const val SPACEX_CREW = "crew"
const val SPACEX_DRAGONS = "dragons"
const val SPACEX_LANDING_PADS = "landpads"

const val SPACEX_LAUNCHES = "launches"
const val SPACEX_LAUNCHES_PAST = "$SPACEX_LAUNCHES/past"
const val SPACEX_LAUNCHES_UPCOMING = "$SPACEX_LAUNCHES/upcoming"
const val SPACEX_LAUNCHES_LATEST = "$SPACEX_LAUNCHES/latest"
const val SPACEX_LAUNCHES_NEXT = "$SPACEX_LAUNCHES/next"

const val SPACEX_LAUNCHPADS = "launchpads"
const val SPACEX_PAYLOADS = "payloads"
const val SPACEX_ROADSTER = "roadster"
const val SPACEX_ROCKETS = "rockets"
const val SPACEX_SHIPS = "ships"
const val SPACEX_STARLINK = "starlink"
const val SPACEX_HISTORY = "history"

const val SPACEX_QUERY = "/query"

const val SPACEX_PARAM_ID = "id"
const val SPACEX_GET_BY_PARAM_ID = "/{$SPACEX_PARAM_ID}"

//Query
const val SPACEX_CAPSULES_QUERY = "$SPACEX_CAPSULES$SPACEX_QUERY"
const val SPACEX_CORES_QUERY = "$SPACEX_CORES$SPACEX_QUERY"
const val SPACEX_CREW_QUERY = "$SPACEX_CREW$SPACEX_QUERY"
const val SPACEX_DRAGONS_QUERY = "$SPACEX_DRAGONS$SPACEX_QUERY"
const val SPACEX_LANDING_PADS_QUERY = "$SPACEX_LANDING_PADS$SPACEX_QUERY"
const val SPACEX_LAUNCHES_QUERY = "$SPACEX_LAUNCHES$SPACEX_QUERY"
const val SPACEX_LAUNCHPADS_QUERY = "$SPACEX_LAUNCHPADS$SPACEX_QUERY"
const val SPACEX_PAYLOADS_QUERY = "$SPACEX_PAYLOADS$SPACEX_QUERY"
const val SPACEX_ROADSTER_QUERY = "$SPACEX_ROADSTER$SPACEX_QUERY"
const val SPACEX_ROCKETS_QUERY = "$SPACEX_ROCKETS$SPACEX_QUERY"
const val SPACEX_SHIPS_QUERY = "$SPACEX_SHIPS$SPACEX_QUERY"
const val SPACEX_STARLINK_QUERY = "$SPACEX_STARLINK$SPACEX_QUERY"
const val SPACEX_HISTORY_QUERY = "$SPACEX_HISTORY$SPACEX_QUERY"

//Fields
const val SPACEX_FIELD_ID = "id"

const val SPACEX_FIELD_KG = "kg"
const val SPACEX_FIELD_LB = "lb"
const val SPACEX_FIELD_METERS = "meters"
const val SPACEX_FIELD_FEET = "feet"
const val SPACEX_FIELD_CUBIC_METERS = "cubic_meters"
const val SPACEX_FIELD_CUBIC_FEET = "cubic_feet"


//CAPSULE___________________________________________________________________________________________
const val SPACEX_FIELD_CAPSULE_SERIAL = "serial"
const val SPACEX_FIELD_CAPSULE_STATUS = "status"
const val SPACEX_FIELD_CAPSULE_TYPE = "type"
const val SPACEX_FIELD_CAPSULE_DRAGON = "dragon"
const val SPACEX_FIELD_CAPSULE_REUSE_COUNT = "reuse_count"
const val SPACEX_FIELD_CAPSULE_WATER_LANDINGS = "water_landings"
const val SPACEX_FIELD_CAPSULE_LAND_LANDINGS = "land_landings"
const val SPACEX_FIELD_CAPSULE_LAST_UPDATE = "last_update"
const val SPACEX_FIELD_CAPSULE_LAUNCHES = "launches"

const val SPACEX_CAPSULE_STATUS_UNKNOWN = "unknown"
const val SPACEX_CAPSULE_STATUS_ACTIVE = "active"
const val SPACEX_CAPSULE_STATUS_RETIRED = "retired"
const val SPACEX_CAPSULE_STATUS_DESTROYED = "destroyed"

const val SPACEX_CAPSULE_TYPE_DRAGON_1 = "Dragon 1.0"
const val SPACEX_CAPSULE_TYPE_DRAGON_1_1 = "Dragon 1.1"
const val SPACEX_CAPSULE_TYPE_DRAGON_2 = "Dragon 2.0"

//COMPANY___________________________________________________________________________________________
const val SPACEX_FIELD_COMPANY_NAME = "name"
const val SPACEX_FIELD_COMPANY_FOUNDER = "founder"
const val SPACEX_FIELD_COMPANY_FOUNDED = "founded"
const val SPACEX_FIELD_COMPANY_EMPLOYEES = "employees"
const val SPACEX_FIELD_COMPANY_VEHICLES = "vehicles"
const val SPACEX_FIELD_COMPANY_LAUNCH_SITES = "launch_sites"
const val SPACEX_FIELD_COMPANY_TEST_SITES = "test_sites"
const val SPACEX_FIELD_COMPANY_CEO = "ceo"
const val SPACEX_FIELD_COMPANY_CTO = "cto"
const val SPACEX_FIELD_COMPANY_COO = "coo"
const val SPACEX_FIELD_COMPANY_CTO_PROPULSION = "cto_propulsion"
const val SPACEX_FIELD_COMPANY_VALUATION = "valuation"
const val SPACEX_FIELD_COMPANY_HEADQUATERS = "headquarters"
const val SPACEX_FIELD_COMPANY_ADDRESS = "address"
const val SPACEX_FIELD_COMPANY_CITY = "city"
const val SPACEX_FIELD_COMPANY_STATE = "state"
const val SPACEX_FIELD_COMPANY_LINKS = "links"
const val SPACEX_FIELD_COMPANY_WEBSITE = "website"
const val SPACEX_FIELD_COMPANY_FLICKR = "flickr"
const val SPACEX_FIELD_COMPANY_TWITTER = "twitter"
const val SPACEX_FIELD_COMPANY_ELON_TWITTER = "elon_twitter"
const val SPACEX_FIELD_COMPANY_SUMMARY = "summary"

//CORE______________________________________________________________________________________________
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
const val SPACEX_CORE_STATUS_UNKNOWN = "unknown"
const val SPACEX_CORE_STATUS_EXPENDED = "expended"
const val SPACEX_CORE_STATUS_LOST = "lost"
const val SPACEX_CORE_STATUS_RETIRED = "retired"

//CREW______________________________________________________________________________________________
const val SPACEX_FIELD_CREW_NAME = "name"
const val SPACEX_FIELD_CREW_STATUS = "status"
const val SPACEX_FIELD_CREW_AGENCY = "agency"
const val SPACEX_FIELD_CREW_IMAGE = "image"
const val SPACEX_FIELD_CREW_WIKI = "wikipedia"
const val SPACEX_FIELD_CREW_LAUNCHES = "launches"

const val SPACEX_CREW_STATUS_ACTIVE = "active"
const val SPACEX_CREW_STATUS_INACTIVE = "inactive"
const val SPACEX_CREW_STATUS_RETIRED = "retired"
const val SPACEX_CREW_STATUS_UNKNOWN = "unknown"

//DRAGON____________________________________________________________________________________________
const val SPACEX_FIELD_DRAGON_NAME = "name"
const val SPACEX_FIELD_DRAGON_TYPE = "type"
const val SPACEX_FIELD_DRAGON_ACTIVE = "active"
const val SPACEX_FIELD_DRAGON_CREW_CAPACITY = "crew_capacity"
const val SPACEX_FIELD_DRAGON_SIDEWALL_ANGLE_DEG = "sidewall_angle_deg"
const val SPACEX_FIELD_DRAGON_ORBIT_DURATION_YR = "orbit_duration_yr"
const val SPACEX_FIELD_DRAGON_DRY_MASS_KG = "dry_mass_kg"
const val SPACEX_FIELD_DRAGON_DRY_MASS_LB = "dry_mass_lb"
const val SPACEX_FIELD_DRAGON_FIRST_FLIGHT = "first_flight"
const val SPACEX_FIELD_DRAGON_HEAT_SHIELD = "heat_shield"
const val SPACEX_FIELD_DRAGON_MATERIAL = "material"
const val SPACEX_FIELD_DRAGON_SIZE_METERS = "size_meters"
const val SPACEX_FIELD_DRAGON_TEMP_DEGREES = "temp_degrees"
const val SPACEX_FIELD_DRAGON_DEV_PARTNER = "dev_partner"
const val SPACEX_FIELD_DRAGON_THRUSTERS = "thrusters"
const val SPACEX_FIELD_DRAGON_THRUSTERS_TYPE = "type"
const val SPACEX_FIELD_DRAGON_THRUSTERS_AMOUNT = "amount"
const val SPACEX_FIELD_DRAGON_THRUSTERS_PODS = "pods"
const val SPACEX_FIELD_DRAGON_THRUSTERS_FUEL_1 = "fuel_1"
const val SPACEX_FIELD_DRAGON_THRUSTERS_FUEL_2 = "fuel_2"
const val SPACEX_FIELD_DRAGON_THRUSTERS_ISP = "isp"
const val SPACEX_FIELD_DRAGON_THRUSTERS_THRUST = "thrust"
const val SPACEX_FIELD_DRAGON_LAUNCH_PAYLOAD_MASS = "launch_payload_mass"
const val SPACEX_FIELD_DRAGON_LAUNCH_PAYLOAD_VOL = "launch_payload_vol"
const val SPACEX_FIELD_DRAGON_RETURN_PAYLOAD_MASS = "return_payload_mass"
const val SPACEX_FIELD_DRAGON_RETURN_PAYLOAD_VOL = "return_payload_vol"
const val SPACEX_FIELD_DRAGON_PRESSURIZED_CAPSULE = "pressurized_capsule"
const val SPACEX_FIELD_DRAGON_PAYLOAD_VOLUME = "payload_volume"
const val SPACEX_FIELD_DRAGON_TRUNK = "trunk"
const val SPACEX_FIELD_DRAGON_TRUNK_VOLUME = "trunk_volume"
const val SPACEX_FIELD_DRAGON_CARGO = "cargo"
const val SPACEX_FIELD_DRAGON_SOLAR_ARRAY = "solar_array"
const val SPACEX_FIELD_DRAGON_UNPRESSURIZED_CARGO = "unpressurized_cargo"
const val SPACEX_FIELD_DRAGON_HEIGHT_W_TRUNK = "height_w_trunk"
const val SPACEX_FIELD_DRAGON_DIAMETER = "diameter"
const val SPACEX_FIELD_DRAGON_FLICKR_IMAGES = "flickr_images"
const val SPACEX_FIELD_DRAGON_WIKIPEDIA = "wikipedia"
const val SPACEX_FIELD_DRAGON_DESCRIPTION = "description"

//LANDING PAD_______________________________________________________________________________________
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
const val SPACEX_LANDING_PAD_STATUS_UNKNOWN = "unknown"
const val SPACEX_LANDING_PAD_STATUS_RETIRED = "retired"
const val SPACEX_LANDING_PAD_STATUS_LOST = "lost"
const val SPACEX_LANDING_PAD_STATUS_UNDER_CONSTRUCTION = "under construction"

//LAUNCH____________________________________________________________________________________________
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
const val SPACEX_FIELD_LAUNCH_PRESSKIT = "presskit"
const val SPACEX_FIELD_LAUNCH_WEBCAST = "webcast"
const val SPACEX_FIELD_LAUNCH_YOUTUBE_ID = "youtube_id"
const val SPACEX_FIELD_LAUNCH_ARTICLE = "article"
const val SPACEX_FIELD_LAUNCH_WIKI = "wikipedia"
const val SPACEX_FIELD_LAUNCH_AUTO_UPDATE = "auto_update"

const val SPACEX_LAUNCH_DATE_PRECISION_HALF = "half"
const val SPACEX_LAUNCH_DATE_PRECISION_QUARTER = "quarter"
const val SPACEX_LAUNCH_DATE_PRECISION_YEAR = "year"
const val SPACEX_LAUNCH_DATE_PRECISION_MONTH = "month"
const val SPACEX_LAUNCH_DATE_PRECISION_DAY = "day"
const val SPACEX_LAUNCH_DATE_PRECISION_HOUR = "hour"

//Launchpad_________________________________________________________________________________________
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

//Payload___________________________________________________________________________________________
const val SPACEX_FIELD_PAYLOAD_NAME = "name"
const val SPACEX_FIELD_PAYLOAD_TYPE = "type"
const val SPACEX_FIELD_PAYLOAD_REUSED = "reused"
const val SPACEX_FIELD_PAYLOAD_LAUNCH = "launch"
const val SPACEX_FIELD_PAYLOAD_CUSTOMERS = "customers"
const val SPACEX_FIELD_PAYLOAD_NORAD_IDS = "norad_ids"
const val SPACEX_FIELD_PAYLOAD_NATIONALITIES = "nationalities"
const val SPACEX_FIELD_PAYLOAD_MANUFACTURERS = "manufacturers"
const val SPACEX_FIELD_PAYLOAD_MASS_KG = "mass_kg"
const val SPACEX_FIELD_PAYLOAD_MASS_LBS = "mass_lbs"
const val SPACEX_FIELD_PAYLOAD_ORBIT = "orbit"
const val SPACEX_FIELD_PAYLOAD_REFERENCE_SYSTEM = "reference_system"
const val SPACEX_FIELD_PAYLOAD_REGIME = "regime"
const val SPACEX_FIELD_PAYLOAD_LONGITUDE = "longitude"
const val SPACEX_FIELD_PAYLOAD_SEMI_MAJOR_AXIS_KM = "semi_major_axis_km"
const val SPACEX_FIELD_PAYLOAD_ECCENTRICITY = "eccentricity"
const val SPACEX_FIELD_PAYLOAD_PERIAPSIS_KM = "periapsis_km"
const val SPACEX_FIELD_PAYLOAD_APOAPSIS_KM = "apoapsis_km"
const val SPACEX_FIELD_PAYLOAD_INCLINATION_DEG = "inclination_deg"
const val SPACEX_FIELD_PAYLOAD_PERIOD_MIN = "period_min"
const val SPACEX_FIELD_PAYLOAD_LIFESPAN_YEARS = "lifespan_years"
const val SPACEX_FIELD_PAYLOAD_EPOCH = "epoch"
const val SPACEX_FIELD_PAYLOAD_MEAN_MOTION = "mean_motion"
const val SPACEX_FIELD_PAYLOAD_RAAN = "raan"
const val SPACEX_FIELD_PAYLOAD_ARG_OF_PERICENTER = "arg_of_pericenter"
const val SPACEX_FIELD_PAYLOAD_MEAN_ANOMALY = "mean_anomaly"
const val SPACEX_FIELD_PAYLOAD_DRAGON = "dragon"
const val SPACEX_FIELD_PAYLOAD_DRAGON_CAPSULE = "capsule"
const val SPACEX_FIELD_PAYLOAD_DRAGON_MASS_RETURNED_KG = "mass_returned_kg"
const val SPACEX_FIELD_PAYLOAD_DRAGON_MASS_RETURNED_LBS = "mass_returned_lbs"
const val SPACEX_FIELD_PAYLOAD_DRAGON_FLIGHT_TIME_SEC = "flight_time_sec"
const val SPACEX_FIELD_PAYLOAD_DRAGON_MANIFEST = "manifest"
const val SPACEX_FIELD_PAYLOAD_DRAGON_WATER_LANDING = "water_landing"
const val SPACEX_FIELD_PAYLOAD_DRAGON_LAND_LANDING = "land_landing"

//Fairing___________________________________________________________________________________________
const val SPACEX_FIELD_FAIRING_SERIAL = "serial"
const val SPACEX_FIELD_FAIRING_VERSION = "version"
const val SPACEX_FIELD_FAIRING_STATUS = "status"
const val SPACEX_FIELD_FAIRING_REUSE_COUNT = "reuse_count"
const val SPACEX_FIELD_FAIRING_NET_LANDING_ATTEMPTS = "net_landing_attempts"
const val SPACEX_FIELD_FAIRING_NET_LANDING = "net_landing"
const val SPACEX_FIELD_FAIRING_WATER_LANDING_ATTEMPTS = "water_landing_attempts"
const val SPACEX_FIELD_FAIRING_WATER_LANDING = "water_landing"
const val SPACEX_FIELD_FAIRING_LAST_UPDATE = "last_update"
const val SPACEX_FIELD_FAIRING_LAUNCHES = "launches"

const val SPACEX_FAIRING_VERSION_1 = "1.0"
const val SPACEX_FAIRING_VERSION_2 = "2.0"
const val SPACEX_FAIRING_VERSION_2_1 = "2.1"

//Roadster__________________________________________________________________________________________
const val SPACEX_FIELD_ROADSTER_NAME = "name"
const val SPACEX_FIELD_ROADSTER_LAUNCH_DATE_UTC = "launch_date_utc"
const val SPACEX_FIELD_ROADSTER_LAUNCH_DATE_UNIX = "launch_date_unix"
const val SPACEX_FIELD_ROADSTER_LAUNCH_MASS_KG = "launch_mass_kg"
const val SPACEX_FIELD_ROADSTER_LAUNCH_MASS_LBS = "launch_mass_lbs"
const val SPACEX_FIELD_ROADSTER_NORAD_ID = "norad_id"
const val SPACEX_FIELD_ROADSTER_EPOCH_JD = "epoch_jd"
const val SPACEX_FIELD_ROADSTER_ORBIT_TYPE = "orbit_type"
const val SPACEX_FIELD_ROADSTER_APOAPSIS_AU = "apoapsis_au"
const val SPACEX_FIELD_ROADSTER_PERIAPSIS_AU = "periapsis_au"
const val SPACEX_FIELD_ROADSTER_SEMI_MAJOR_AXIS_AU = "semi_major_axis_au"
const val SPACEX_FIELD_ROADSTER_ECCENTRICITY = "eccentricity"
const val SPACEX_FIELD_ROADSTER_INCLINATION = "inclination"
const val SPACEX_FIELD_ROADSTER_LNG = "longitude"
const val SPACEX_FIELD_ROADSTER_PERIAPSIS_ARG = "periapsis_arg"
const val SPACEX_FIELD_ROADSTER_PERIOD_DAYS = "period_days"
const val SPACEX_FIELD_ROADSTER_SPEED_KPH = "speed_kph"
const val SPACEX_FIELD_ROADSTER_SPEED_MPH = "speed_mph"
const val SPACEX_FIELD_ROADSTER_EARTH_DISTANCE_KM = "earth_distance_km"
const val SPACEX_FIELD_ROADSTER_EARTH_DISTANCE_MI = "earth_distance_mi"
const val SPACEX_FIELD_ROADSTER_MARS_DISTANCE_KM = "mars_distance_km"
const val SPACEX_FIELD_ROADSTER_MARS_DISTANCE_MI = "mars_distance_mi"
const val SPACEX_FIELD_ROADSTER_FLICKR_IMAGES = "flickr_images"
const val SPACEX_FIELD_ROADSTER_WIKI = "wikipedia"
const val SPACEX_FIELD_ROADSTER_VIDEO = "video"
const val SPACEX_FIELD_ROADSTER_DETAILS = "details"

//Rocket____________________________________________________________________________________________
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

//Ship______________________________________________________________________________________________
const val SPACEX_FIELD_SHIP_NAME = "name"
const val SPACEX_FIELD_SHIP_LEGACY_ID = "legacy_id"
const val SPACEX_FIELD_SHIP_MODEL = "model"
const val SPACEX_FIELD_SHIP_TYPE = "type"
const val SPACEX_FIELD_SHIP_ROLES = "roles"
const val SPACEX_FIELD_SHIP_ACTIVE = "active"
const val SPACEX_FIELD_SHIP_IMO = "imo"
const val SPACEX_FIELD_SHIP_MMSI = "mmsi"
const val SPACEX_FIELD_SHIP_ABS = "abs"
const val SPACEX_FIELD_SHIP_CLASS = "class"
const val SPACEX_FIELD_SHIP_MASS_KG = "mass_kg"
const val SPACEX_FIELD_SHIP_MASS_LBS = "mass_lbs"
const val SPACEX_FIELD_SHIP_YEAR_BUILT = "year_built"
const val SPACEX_FIELD_SHIP_HOME_PORT = "home_port"
const val SPACEX_FIELD_SHIP_STATUS = "status"
const val SPACEX_FIELD_SHIP_SPEED_KN = "speed_kn"
const val SPACEX_FIELD_SHIP_COURSE_DEG = "course_deg"
const val SPACEX_FIELD_SHIP_LATITUDE = "latitude"
const val SPACEX_FIELD_SHIP_LONGITUDE = "longitude"
const val SPACEX_FIELD_SHIP_LAST_AIS_UPDATE = "last_ais_update"
const val SPACEX_FIELD_SHIP_LINK = "link"
const val SPACEX_FIELD_SHIP_IMAGE = "image"
const val SPACEX_FIELD_SHIP_LAUNCHES = "launches"

//Starlink__________________________________________________________________________________________
const val SPACEX_FIELD_STARLINK_ = ""

//History___________________________________________________________________________________________
const val SPACEX_FIELD_HISTORY_LINKS = "links"
const val SPACEX_FIELD_HISTORY_TITLE = "title"
const val SPACEX_FIELD_HISTORY_EVENT_DATE_UTC = "event_date_utc"
const val SPACEX_FIELD_HISTORY_EVENT_DATE_UNIX = "event_date_unix"
const val SPACEX_FIELD_HISTORY_DETAILS = "details"
const val SPACEX_FIELD_HISTORY_ARTICLE = "article"

//Status____________________________________________________________________________________________
const val SPACEX_ACTIVE = "Active"
const val SPACEX_INACTIVE = "Inactive"
const val SPACEX_UNKNOWN = "Unknown"
const val SPACEX_EXPENDED = "Expended"
const val SPACEX_LOST = "Lost"
const val SPACEX_RETIRED = "Retired"
const val SPACEX_DESTROYED = "Destroyed"
const val SPACEX_UNDER_CONSTRUCTION = "Under construction"

/*<=======================================================================================
====================================================================================SpaceX

Twitter ==================================================================================
=========================================================================================>*/

const val TWITTER_TIMELINE = "statuses/user_timeline.json"

const val TWITTER_QUERY_SCREEN_NAME = "screen_name"
const val TWITTER_QUERY_INCLUDE_RTS = "include_rts"
const val TWITTER_QUERY_TRIM_USER = "trim_user"
const val TWITTER_QUERY_TWEET_MODE = "tweet_mode"
const val TWITTER_QUERY_COUNT = "count"
const val TWITTER_QUERY_MAX_ID = "max_id"

/*<=======================================================================================
====================================================================================Twitter

Reddit ==================================================================================
=========================================================================================>*/

const val REDDIT_PARAM_SUBREDDIT = "subreddit"
const val REDDIT_PARAM_ORDER = "order"

const val REDDIT_SUBREDDIT =
    "r/{$REDDIT_PARAM_SUBREDDIT}/{$REDDIT_PARAM_ORDER}/.json?raw_json=1&limit=15"

const val REDDIT_PARAM_ORDER_HOT = "hot"
const val REDDIT_PARAM_ORDER_NEW = "new"

const val REDDIT_QUERY_AFTER = "after"

/*<=======================================================================================
====================================================================================Reddit

Navigation ===============================================================================
=========================================================================================>*/

const val LAUNCH_SHORT_KEY = "launch_short"