package uk.co.zac_h.spacex.feature.launch.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import uk.co.zac_h.spacex.core.common.types.CoreType
import uk.co.zac_h.spacex.core.common.types.CrewStatus
import uk.co.zac_h.spacex.feature.launch.CrewItem
import uk.co.zac_h.spacex.feature.launch.FirstStageItem
import uk.co.zac_h.spacex.feature.launch.LaunchItem

class LaunchesPreviewParameterProvider : PreviewParameterProvider<List<LaunchItem>> {
    override val values: Sequence<List<LaunchItem>>
        get() = sequenceOf(
            listOf(
                LaunchItem(
                    id = "da48063e-b50f-4602-adab-ad7331ee6d82",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/ax-32520patch_mission_patch_20231019065301.png",
                    missionName = "Axiom Space Mission 3",
                    rocket = "Falcon 9",
                    launchDate = "17 Jan 24 - 22:11",
                    launchDateUnix = 1705529460000,
                    launchLocation = "Launch Complex 39A",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_87_20200803143537.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.60822681,-80.60428186",
                    status = LaunchItem.Status(
                        type = 8,
                        name = "To Be Confirmed",
                        description = "Awaiting official confirmation - current date is known with some certainty."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "This is a Crew Dragon flight for a private company Axiom Space. The mission will carry a professionally trained commander alongside three private astronauts to and from the International Space Station. This crew will stay aboard space station for  at least eight days.",
                    type = "Tourism",
                    orbit = "Low Earth Orbit",
                    webcast = emptyList(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "401",
                            serial = "B1080",
                            type = CoreType.CORE,
                            reused = true,
                            totalFlights = 4,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will attempt to land back at the launch site after this flight.",
                            landingType = "RTLS",
                            landingLocation = "LZ-1",
                            landingLocationFull = "Landing Zone 1",
                            previousFlight = "Falcon 9 Block 5 | Starlink Group 6-24",
                            turnAroundTimeDays = 87
                        )
                    ),
                    crew = listOf(
                        CrewItem(
                            id = "4800",
                            name = "Michael López-Alegría",
                            status = CrewStatus.ACTIVE,
                            agency = "Axiom Space",
                            bio = "Michael López-Alegría is a Spanish-American astronaut; a veteran of three Space Shuttle missions and one International Space Station mission. He is known for having performed ten spacewalks so far in his career, presently holding the second longest all-time EVA duration record and having the third longest spaceflight of any American at the length of 215 days; this time was spent on board the ISS from September 18, 2006 to April 21, 2007.",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/michael_l25c3_image_20201201090124.jpeg",
                            role = "Commander", firstFlight = "20 Oct 95"
                        ),
                        CrewItem(
                            id = "4802",
                            name = "Walter Villadei",
                            status = CrewStatus.ACTIVE,
                            agency = "Italian Air Force",
                            bio = "Italian Air Force mission commander on the Virgin Galactic 01 flight. Backup pilot for the Axiom Space Mission 2, Pilon for the Axiom Space Mission 3, qualified on Soyuz, ISS systems, and Orlan for EVA activities.",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/walter_villadei_image_20231230180121.jpeg",
                            role = "Pilot",
                            firstFlight = "29 Jun 23"
                        ),
                        CrewItem(
                            id = "4752",
                            name = "Alper Gezeravci",
                            status = CrewStatus.ACTIVE,
                            agency = "Turkish Air Force, bio=Fighter pilot trained on F16, first Turkish astronaut.",
                            bio = "",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/alper_gezeravci_image_20231230181038.jpeg",
                            role = "Mission Specialist",
                            firstFlight = null
                        ),
                        CrewItem(
                            id = "4798",
                            name = "Marcus Wandt",
                            status = CrewStatus.UNKNOWN,
                            agency = "European Space Agency",
                            bio = "Marcus Wandt is a Swedish test pilot and member of the European Space Agency's 2022 astronaut class reserve.",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/marcus_wandt_image_20230912131352.jpg",
                            role = "Mission Specialist",
                            firstFlight = null
                        )
                    )
                ),
                LaunchItem(
                    id = "665678b9-8b91-45de-9e9c-5063622bcca1",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/cygnus2520ng-2_mission_patch_20231206095838.png",
                    missionName = "Cygnus CRS-2 NG-20 (S.S. Patricia “Patty” Hilliard Robertson)",
                    rocket = "Falcon 9",
                    launchDate = "29 Jan 24 - 17:30",
                    launchDateUnix = 1706549400000,
                    launchLocation = "Space Launch Complex 40",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_80_20200803143323.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.56194122,-80.57735736",
                    status = LaunchItem.Status(
                        type = 1,
                        name = "Go for Launch",
                        description = "Current T-0 confirmed by official or reliable sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "This is the 20th flight of the Orbital ATK's uncrewed resupply spacecraft Cygnus and its 19th flight to the International Space Station under the Commercial Resupply Services contract with NASA.",
                    type = "Resupply",
                    orbit = "Low Earth Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "343",
                            serial = "B1072",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = 0,
                            landingAttempt = true,
                            description = "B1072 will land back at the launch site after its first flight.",
                            landingType = "RTLS",
                            landingLocation = "LZ-1",
                            landingLocationFull = "Landing Zone 1",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = listOf()
                ), LaunchItem(
                    id = "839c1427-45db-4b88-ae41-bb666c1dd097",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/space2520x252_mission_patch_20221011205756.png",
                    missionName = "Starlink Group 6-37",
                    rocket = "Falcon 9",
                    launchDate = "31 Jan 24 - 00:00",
                    launchDateUnix = 1706659200000,
                    launchLocation = "Space Launch Complex 40",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_80_20200803143323.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.56194122,-80.57735736",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
                    type = "Communications",
                    orbit = "Low Earth Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "373",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will attempt to land on ASDS OCISLY after this flight.",
                            landingType = "ASDS",
                            landingLocation = "N/A",
                            landingLocationFull = "Unknown",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = null
                ),
                LaunchItem(
                    id = "378bd440-63df-48a6-97f7-c528a9a1968c",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/space2520x252_mission_patch_20221011205756.png",
                    missionName = "Starlink Group 6-38",
                    rocket = "Falcon 9",
                    launchDate = "31 Jan 24 - 00:00",
                    launchDateUnix = 1706659200000,
                    launchLocation = "Unknown Pad",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_72_20200803143403.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.458,-80.528",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
                    type = "Communications",
                    orbit = "Low Earth Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "375",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will attempt to land on ASDS OCISLY after this flight.",
                            landingType = "ASDS",
                            landingLocation = "N/A",
                            landingLocationFull = "Unknown",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = null
                ),
                LaunchItem(
                    id = "0c61bd06-0bd9-40a8-8be3-1d46368ae094",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/space2520x252_mission_patch_20221011205756.png",
                    missionName = "Starlink Group 6-39",
                    rocket = "Falcon 9",
                    launchDate = "31 Jan 24 - 00:00",
                    launchDateUnix = 1706659200000,
                    launchLocation = "Unknown Pad",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_72_20200803143403.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.458,-80.528",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
                    type = "Communications",
                    orbit = "Low Earth Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "482",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will land on an ASDS after this flight.",
                            landingType = "ASDS",
                            landingLocation = "N/A",
                            landingLocationFull = "Unknown",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = null
                ),
                LaunchItem(
                    id = "b76b836f-8e88-4bc3-a3bc-36bb8ee4bb3c",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/space2520x252_mission_patch_20221011205756.png",
                    missionName = "Starlink Group 8-1",
                    rocket = "Falcon 9",
                    launchDate = "31 Jan 24 - 00:00",
                    launchDateUnix = 1706659200000,
                    launchLocation = "Space Launch Complex 4E",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_16_20200803143532.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=34.632,-120.611",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "A batch of satellites for the Starlink mega-constellation - SpaceX's project for space-based Internet communication system.",
                    type = "Communications",
                    orbit = "Low Earth Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "446",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will land on ASDS OCISLY after this flight.",
                            landingType = "ASDS",
                            landingLocation = "OCISLY",
                            landingLocationFull = "Of Course I Still Love You",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = null
                ),
                LaunchItem(
                    id = "549f26ed-a5f7-49fb-97a7-fb2ffa26e3e8",
                    upcoming = true,
                    missionPatch = null,
                    missionName = "PACE (Plankton, Aerosol, Cloud, ocean Ecosystem)",
                    rocket = "Falcon 9",
                    launchDate = "06 Feb 24 - 00:00",
                    launchDateUnix = 1707177600000,
                    launchLocation = "Space Launch Complex 40",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_80_20200803143323.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.56194122,-80.57735736",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "PACE (Plankton, Aerosol, Cloud, ocean Ecosystem) is a NASA Earth observation satellite mission. It will make global ocean color measurements to provide extended data records on ocean ecology and global biogeochemistry (e.g., carbon cycle) along with polarimetry measurements to provide extended data records on clouds and aerosols.",
                    type = "Earth Science",
                    orbit = "Sun-Synchronous Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "398",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will land back at the launch site after this flight.",
                            landingType = "RTLS",
                            landingLocation = "LZ-1",
                            landingLocationFull = "Landing Zone 1",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = null
                ),
                LaunchItem(
                    id = "942a1814-c237-41de-9970-d27bb9630f3b",
                    upcoming = true,
                    missionPatch = null,
                    missionName = "Nova-C IM-1",
                    rocket = "Falcon 9",
                    launchDate = "10 Feb 24 - 00:00",
                    launchDateUnix = 1707523200000,
                    launchLocation = "Launch Complex 39A",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_87_20200803143537.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.60822681,-80.60428186",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "This is the first flight of Nova-C lander which was developed by Intuitive Machines. This mission is a part of CLPS program and carries various NASA payloads in support of Artemis lunar program, as well as multiple commercial payloads.",
                    type = "Lunar Exploration",
                    orbit = "Lunar Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "389",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will land back at the launch site after this flight.",
                            landingType = "RTLS",
                            landingLocation = "LZ-1",
                            landingLocationFull = "Landing Zone 1",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = null
                ),
                LaunchItem(
                    id = "2f101902-03dd-4b23-8546-d25ea514b36f",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/crew-82520patc_mission_patch_20231117112848.png",
                    missionName = "Crew-8",
                    rocket = "Falcon 9",
                    launchDate = "29 Feb 24 - 00:00",
                    launchDateUnix = 1709164800000,
                    launchLocation = "Launch Complex 39A",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_87_20200803143537.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.60822681,-80.60428186",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "SpaceX Crew-6 is the eighth crewed operational flight of a Crew Dragon spacecraft to the International Space Station as part of NASA's Commercial Crew Program.",
                    type = "Human Exploration",
                    orbit = "Low Earth Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "254",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = true,
                            description = "The Falcon 9 first stage will attempt to land back at the launch site after this flight.",
                            landingType = "RTLS",
                            landingLocation = "LZ-1",
                            landingLocationFull = "Landing Zone 1",
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = listOf(
                        CrewItem(
                            id = "4779",
                            name = "Matthew Dominick",
                            status = CrewStatus.ACTIVE,
                            agency = "National Aeronautics and Space Administration",
                            bio = "Matthew Stuart Dominick is a US Navy test pilot and a NASA astronaut of the class of 2017. He has more than 1,600 hours of flight time in 28 aircraft, 400 carrier-arrested landings, 61 combat missions, and almost 200 flight test carrier landings.",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/matthew2520dominick_image_20200110160726.jpeg",
                            role = "Commander",
                            firstFlight = null
                        ),
                        CrewItem(
                            id = "4780",
                            name = "Michael Barratt",
                            status = CrewStatus.ACTIVE,
                            agency = "National Aeronautics and Space Administration",
                            bio = "Michael Reed Barratt is an American physician and a NASA astronaut. Specializing in aerospace medicine, he served as a flight surgeon for NASA before his selection as an astronaut, and has played a role in developing NASA's space medicine programs for both the Shuttle-Mir Program and International Space Station. His first spaceflight was a long-duration mission to the International Space Station, as a Flight Engineer in the Expedition 19 and 20 crew. In March 2011, Barratt completed his second spaceflight as a crew member of STS-133.",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/michael_barratt_image_20220911034324.jpeg",
                            role = "Pilot",
                            firstFlight = "26 Mar 09"
                        ),
                        CrewItem(
                            id = "3768",
                            name = "Alexander Grebenkin",
                            status = CrewStatus.UNKNOWN,
                            agency = "Russian Federal Space Agency (ROSCOSMOS)",
                            bio = "Alexander Grebenkin is a Russian cosmonaut with a background in radio communications engineering.",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/alexander_grebe_image_20230304134134.jpeg",
                            role = "Mission Specialist",
                            firstFlight = null
                        ),
                        CrewItem(
                            id = "4754",
                            name = "Jeanette J. Epps",
                            status = CrewStatus.ACTIVE,
                            agency = "National Aeronautics and Space Administration",
                            bio = "Jeanette Jo Epps is an American aerospace engineer and NASA astronaut.",
                            image = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/jeanette_j._epp_image_20220911034355.jpeg",
                            role = "Mission Specialist",
                            firstFlight = null
                        )
                    )
                ),
                LaunchItem(
                    id = "604e4aa8-73f8-4f1c-bf4c-5a130625cac7",
                    upcoming = true,
                    missionPatch = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/mission_patch_images/nasa2520crs-30_mission_patch_20231217090207.png",
                    missionName = "Dragon CRS-2 SpX-30",
                    rocket = "Falcon 9",
                    launchDate = "04 Mar 24 - 00:00",
                    launchDateUnix = 1709510400000,
                    launchLocation = "Launch Complex 39A",
                    launchLocationMap = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/launch_images/pad_87_20200803143537.jpg",
                    launchLocationMapUrl = "https://www.google.com/maps?q=28.60822681,-80.60428186",
                    status = LaunchItem.Status(
                        type = 2,
                        name = "To Be Determined",
                        description = "Current date is a placeholder or rough estimation based on unreliable or interpreted sources."
                    ),
                    holdReason = "",
                    failReason = "",
                    description = "30th commercial resupply services mission to the International Space Station operated by SpaceX. The flight will be conducted under the second Commercial Resupply Services contract with NASA. Cargo Dragon 2 brings supplies and payloads, including critical materials to directly support science and research investigations that occur onboard the orbiting laboratory.",
                    type = "Resupply",
                    orbit = "Low Earth Orbit",
                    webcast = listOf(),
                    webcastLive = false,
                    firstStage = listOf(
                        FirstStageItem(
                            id = "412",
                            serial = "Unknown F9",
                            type = CoreType.CORE,
                            reused = false,
                            totalFlights = null,
                            landingAttempt = false,
                            description = null,
                            landingType = null,
                            landingLocation = null,
                            landingLocationFull = null,
                            previousFlight = null,
                            turnAroundTimeDays = null
                        )
                    ),
                    crew = listOf()
                )
            )
        )
}