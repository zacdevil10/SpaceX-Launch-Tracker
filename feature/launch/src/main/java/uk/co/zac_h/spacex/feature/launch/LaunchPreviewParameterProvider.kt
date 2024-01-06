package uk.co.zac_h.spacex.feature.launch

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import uk.co.zac_h.spacex.core.common.types.CoreType
import uk.co.zac_h.spacex.core.common.types.CrewStatus

class LaunchPreviewParameterProvider : PreviewParameterProvider<LaunchItem> {
    override val values: Sequence<LaunchItem>
        get() = sequenceOf(
            LaunchItem(
                id = "0098c032-73de-4c6f-8d73-5d68b9a12fdf",
                upcoming = true,
                missionPatch = null,
                missionName = "OTV-7 (X-37B) (USSF-52)",
                rocket = "Falcon Heavy",
                launchDate = "29 Dec 23 - 01:07",
                launchDateUnix = System.currentTimeMillis() + 10000L,
                launchLocation = "Launch Location",
                launchLocationMap = null,
                launchLocationMapUrl = null,
                status = LaunchItem.Status(
                    type = 1,
                    name = "Go for Launch",
                    description = "Current T-0 confirmed by official or reliable sources."
                ),
                holdReason = "Hold Reason",
                failReason = "Fail Reason",
                description = "It is the seventh flight of the X-37B program. United States Air Force Orbital Test Vehicle is built by Boeing. It's an uncrewed 5000 kg, 8.8 m-long reusable mini-spaceplane capable of autonomous re-entry and landing.",
                type = "Government/Top Secret",
                orbit = "Elliptical Orbit",
                webcast = listOf(
                    VideoItem(
                        title = "Watch live: SpaceX Falcon Heavy launches secretive X-37B military spaceplane",
                        description = "Watch live coverage as SpaceX launches a Falcon Heavy rocket carrying the U.S. military's secretive X-37B spaceplane. Liftoff from historic launch pad 39A at...",
                        imageUrl = null,
                        url = "https://www.youtube.com/watch?v=wnfddhDuWDE",
                        source = "Youtube"
                    ),
                    VideoItem(
                        title = "Livestream on X",
                        description = "This browser is no longer supported.\\n\\nPlease switch to a supported browser to continue using twitter.com. You can see a list of supported browsers in our Help Center.",
                        imageUrl = null,
                        url = "https://twitter.com/i/broadcasts/1ynKOyeDmrwJR",
                        source = "Twitter"
                    )
                ),
                webcastLive = true,
                firstStage = listOf(
                    FirstStageItem(
                        id = "235",
                        serial = "B1084",
                        type = CoreType.CORE,
                        reused = false,
                        totalFlights = 0,
                        landingAttempt = false,
                        landingDescription = "The Falcon Heavy core stage will be expended .",
                        landingType = null,
                        landingLocation = null,
                        landingLocationFull = "Atlantic Ocean",
                        previousFlight = null,
                        turnAroundTimeDays = null
                    )
                ),
                crew = listOf(
                    CrewItem(
                        id = "id",
                        name = "Crew 1",
                        status = CrewStatus.ACTIVE,
                        agency = "NASA",
                        bio = "Crew Bio",
                        image = null,
                        role = "Commander",
                        firstFlight = "First Flight Date"
                    ),
                    CrewItem(
                        id = "id",
                        name = "Crew 2",
                        status = CrewStatus.ACTIVE,
                        agency = "NASA",
                        bio = "Crew Bio",
                        image = null,
                        role = "Pilot",
                        firstFlight = "First Flight Date"
                    )
                )
            ),
            LaunchItem(
                id = "0098c032-73de-4c6f-8d73-5d68b9a12fdf",
                upcoming = true,
                missionPatch = null,
                missionName = "OTV-7 (X-37B) (USSF-52)",
                rocket = "Falcon Heavy",
                launchDate = "29 Dec 23 - 01:07",
                launchDateUnix = System.currentTimeMillis() + 10000L,
                launchLocation = "Launch Location",
                launchLocationMap = null,
                launchLocationMapUrl = null,
                status = LaunchItem.Status(
                    type = 1,
                    name = "Go for Launch",
                    description = "Current T-0 confirmed by official or reliable sources."
                ),
                holdReason = "Hold Reason",
                failReason = "Fail Reason",
                description = "It is the seventh flight of the X-37B program. United States Air Force Orbital Test Vehicle is built by Boeing. It's an uncrewed 5000 kg, 8.8 m-long reusable mini-spaceplane capable of autonomous re-entry and landing.",
                type = "Government/Top Secret",
                orbit = "Elliptical Orbit",
                webcast = listOf(
                    VideoItem(
                        title = "Watch live: SpaceX Falcon Heavy launches secretive X-37B military spaceplane",
                        description = "Watch live coverage as SpaceX launches a Falcon Heavy rocket carrying the U.S. military's secretive X-37B spaceplane. Liftoff from historic launch pad 39A at...",
                        imageUrl = null,
                        url = "https://www.youtube.com/watch?v=wnfddhDuWDE",
                        source = "Youtube"
                    ),
                    VideoItem(
                        title = "Livestream on X",
                        description = "This browser is no longer supported.\\n\\nPlease switch to a supported browser to continue using twitter.com. You can see a list of supported browsers in our Help Center.",
                        imageUrl = null,
                        url = "https://twitter.com/i/broadcasts/1ynKOyeDmrwJR",
                        source = "Twitter"
                    )
                ),
                webcastLive = true,
                firstStage = listOf(
                    FirstStageItem(
                        id = "235",
                        serial = "B1084",
                        type = CoreType.CORE,
                        reused = false,
                        totalFlights = 0,
                        landingAttempt = false,
                        landingDescription = "The Falcon Heavy core stage will be expended .",
                        landingType = "EXP",
                        landingLocation = "ATL",
                        landingLocationFull = "Atlantic Ocean",
                        previousFlight = null,
                        turnAroundTimeDays = null
                    )
                ),
                crew = listOf(
                    CrewItem(
                        id = "id",
                        name = "Crew 1",
                        status = CrewStatus.ACTIVE,
                        agency = "NASA",
                        bio = "Crew Bio",
                        image = null,
                        role = "Commander",
                        firstFlight = "First Flight Date"
                    ),
                    CrewItem(
                        id = "id",
                        name = "Crew 2",
                        status = CrewStatus.ACTIVE,
                        agency = "NASA",
                        bio = "Crew Bio",
                        image = null,
                        role = "Pilot",
                        firstFlight = "First Flight Date"
                    )
                )
            ),
            LaunchItem(
                id = "0098c032-73de-4c6f-8d73-5d68b9a12fdf",
                upcoming = true,
                missionPatch = null,
                missionName = "OTV-7 (X-37B) (USSF-52)",
                rocket = "Falcon Heavy",
                launchDate = "29 Dec 23 - 01:07",
                launchDateUnix = System.currentTimeMillis() + 10000L,
                launchLocation = "Launch Location",
                launchLocationMap = null,
                launchLocationMapUrl = null,
                status = LaunchItem.Status(
                    type = 1,
                    name = "Go for Launch",
                    description = "Current T-0 confirmed by official or reliable sources."
                ),
                holdReason = "Hold Reason",
                failReason = "Fail Reason",
                description = "It is the seventh flight of the X-37B program. United States Air Force Orbital Test Vehicle is built by Boeing. It's an uncrewed 5000 kg, 8.8 m-long reusable mini-spaceplane capable of autonomous re-entry and landing.",
                type = "Government/Top Secret",
                orbit = "Elliptical Orbit",
                webcast = listOf(
                    VideoItem(
                        title = "Livestream on X",
                        description = "This browser is no longer supported.\\n\\nPlease switch to a supported browser to continue using twitter.com. You can see a list of supported browsers in our Help Center.",
                        imageUrl = null,
                        url = "https://twitter.com/i/broadcasts/1ynKOyeDmrwJR",
                        source = "Twitter"
                    )
                ),
                webcastLive = true,
                firstStage = listOf(
                    FirstStageItem(
                        id = "236",
                        serial = "B1064",
                        type = CoreType.BOOSTER,
                        reused = true,
                        totalFlights = 4,
                        landingAttempt = true,
                        landingDescription = "B1064 will attempt to landed back at the launch site after its fifth flight .",
                        landingType = "RTLS",
                        landingLocation = "LZ - 1",
                        landingLocationFull = "Landing Zone 1",
                        previousFlight = "Falcon Heavy | Psyche",
                        turnAroundTimeDays = 76
                    ),
                    FirstStageItem(
                        id = "237",
                        serial = "B1065",
                        type = CoreType.BOOSTER,
                        reused = true,
                        totalFlights = 4,
                        landingAttempt = true,
                        landingDescription = "B1065 will attempt to landed back at the launch site after its fifth flight .",
                        landingType = "RTLS",
                        landingLocation = "LZ - 2",
                        landingLocationFull = "Landing Zone 2",
                        previousFlight = "Falcon Heavy | Psyche",
                        turnAroundTimeDays = 76
                    ),
                    FirstStageItem(
                        id = "235",
                        serial = "B1084",
                        type = CoreType.CORE,
                        reused = false,
                        totalFlights = 0,
                        landingAttempt = false,
                        landingDescription = "The Falcon Heavy core stage will be expended .",
                        landingType = "EXP",
                        landingLocation = "ATL",
                        landingLocationFull = "Atlantic Ocean",
                        previousFlight = null,
                        turnAroundTimeDays = null
                    )
                ),
                crew = listOf(
                    CrewItem(
                        id = "id",
                        name = "Crew 1",
                        status = CrewStatus.ACTIVE,
                        agency = "NASA",
                        bio = "Crew Bio",
                        image = null,
                        role = "Commander",
                        firstFlight = "First Flight Date"
                    ),
                    CrewItem(
                        id = "id",
                        name = "Crew 2",
                        status = CrewStatus.ACTIVE,
                        agency = "NASA",
                        bio = "Crew Bio",
                        image = null,
                        role = "Pilot",
                        firstFlight = "First Flight Date"
                    )
                )
            ),
        )
}