package uk.co.zac_h.spacex.feature.assets.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import uk.co.zac_h.spacex.core.common.types.CrewStatus
import uk.co.zac_h.spacex.feature.assets.astronauts.AstronautItem

class AstronautsPreviewParameterProvider : PreviewParameterProvider<List<AstronautItem>> {

    override val values: Sequence<List<AstronautItem>>
        get() = sequenceOf(
            listOf(
                AstronautItem(
                    id = 636,
                    title = "Little Earth",
                    status = CrewStatus.ACTIVE,
                    agency = "SpaceX",
                    nationality = "Earthling",
                    description = "Little Earth is the nickname of a Celestial Buddies plushtoy brought to the International Space Station aboard the first SpaceX Crew Dragon on its inaugural flight. It was used as a Zero-Gravity Indicator aboard the Crew Dragon spacecraft during ascent and became a resident of the ISS after the docking.",
                    longDescription = "Little Earth is the nickname of a Celestial Buddies plushtoy brought to the International Space Station aboard the first SpaceX Crew Dragon on its inaugural flight. It was used as a Zero-Gravity Indicator aboard the Crew Dragon spacecraft during ascent and became a resident of the ISS after the docking.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/little2520earth_image_20190307215152.jpg",
                    firstFlight = "02 Mar 19"
                ),
                AstronautItem(
                    id = 637,
                    title = "Ripley",
                    status = CrewStatus.ACTIVE,
                    agency = "SpaceX",
                    nationality = "Earthling",
                    description = "Ripley is an Anthropomorphic Test Device (ATD), named as a homage to Sigourney Weaver\'s character in the Alien movies franchise, launched aboard the SpaceX Crew Dragon for its inaugural flight to the International Space Station and back.",
                    longDescription = "Ripley is an Anthropomorphic Test Device (ATD), named as a homage to Sigourney Weaver\'s character in the Alien movies franchise, launched aboard the SpaceX Crew Dragon for its inaugural flight to the International Space Station and back.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/ripley_image_20210520140321.png",
                    firstFlight = "02 Mar 19"
                ),
                AstronautItem(
                    id = 638,
                    title = "Starman",
                    status = CrewStatus.ACTIVE,
                    agency = "SpaceX",
                    nationality = "Earthling",
                    description = "Starman is a mannequin dressed in a spacesuit occupying the driver's seat of Elon Musk\'s Tesla Roadster, launched to an heliocentric orbit on Falcon Heavy's inaugural launch.",
                    longDescription = "Starman is a mannequin dressed in a spacesuit occupying the driver's seat of Elon Musk\'s Tesla Roadster, launched to an heliocentric orbit on Falcon Heavy's inaugural launch.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/starman_image_20190307220126.jpg",
                    firstFlight = "06 Feb 18"
                ),
                AstronautItem(
                    id = 657,
                    title = "Jared Isaacman",
                    status = CrewStatus.ACTIVE,
                    agency = "SpaceX",
                    nationality = "American",
                    description = "Jared Isaacman is an American businessman and pilot. He is the co-founder and CEO of Harbortouch, a payment processor and manufacturer of payment-processing hardware.",
                    longDescription = "Jared Isaacman is an American businessman and pilot. He is the co-founder and CEO of Harbortouch, a payment processor and manufacturer of payment-processing hardware.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/jared_isaacman_image_20220214140401.jpeg",
                    firstFlight = "16 Sep 21"
                ), AstronautItem(
                    id = 658,
                    title = "Hayley Arceneaux",
                    status = CrewStatus.UNKNOWN,
                    agency = "SpaceX",
                    nationality = "American",
                    description = "Hayley Arceneaux is a St. Jude Children's Research Hospital employee, bone cancer survivor and private astronaut who is now a physician assistant.",
                    longDescription = "Hayley Arceneaux is a St. Jude Children's Research Hospital employee, bone cancer survivor and private astronaut who is now a physician assistant.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/hayley_arceneau_image_20210914154211.jpeg",
                    firstFlight = "16 Sep 21"
                ),
                AstronautItem(
                    id = 660,
                    title = "Sian Proctor",
                    status = CrewStatus.UNKNOWN,
                    agency = "SpaceX",
                    nationality = "American",
                    description = "Sian Proctor is an American explorer, scientist, STEM communicator, and aspiring astronaut. She is a geology, sustainability and planetary science professor at South Mountain Community College. Proctor was the education outreach officer for the first Hawai’i Space Exploration Analog and Simulation (HI-SEAS) Mission.",
                    longDescription = "Sian Proctor is an American explorer, scientist, STEM communicator, and aspiring astronaut. She is a geology, sustainability and planetary science professor at South Mountain Community College. Proctor was the education outreach officer for the first Hawai’i Space Exploration Analog and Simulation (HI-SEAS) Mission.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/sian_proctor_image_20210914154206.jpeg",
                    firstFlight = "16 Sep 21"
                ),
                AstronautItem(
                    id = 661,
                    title = "Chris Sembroski",
                    status = CrewStatus.UNKNOWN,
                    agency = "SpaceX",
                    nationality = "American",
                    description = "Chris Sembroski is an aerospace worker at Lockheed Martin who used to work on Minuteman missiles in the Air Force. Birth date is unknown.",
                    longDescription = "Chris Sembroski is an aerospace worker at Lockheed Martin who used to work on Minuteman missiles in the Air Force. Birth date is unknown.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/chris_sembroski_image_20210914154201.jpeg",
                    firstFlight = "16 Sep 21"
                ),
                AstronautItem(
                    id = 716,
                    title = "Scott Poteet",
                    status = CrewStatus.IN_TRAINING,
                    agency = "SpaceX",
                    nationality = "American",
                    description = "Scott “Kidd” Poteet is a retired United States Air Force Lieutenant Colonel who served 20 years in various roles that include Commander of the 64th Aggressor Squadron, USAF Thunderbird #4 Demonstration Pilot, USAF Weapons School Graduate, Operational Test & Evaluation Pilot, and Flight Examiner. Kidd is a command pilot with over 3,200 flying hours in the F-16, A-4, T-38, T-37, T-3, and Alpha Jet. Kidd has logged over 400 hours of combat time during Operations Northern Watch, Southern Watch, Joint Guardian, Freedom’s Sentinel, and Resolute Support. Following his Air Force career, Kidd served in various roles to include Director of Business Development at Draken International and VP of Strategy at Shift4 (NYSE: FOUR). He most recently served as the Mission Director of Inspiration4. Kidd is also an accomplished collegiate runner and triathlete, competing in 15 Ironman triathlons since 2000, which includes four Ironman World Championships in Kailua-Kona, Hawaii.",
                    longDescription = "Scott “Kidd” Poteet is a retired United States Air Force Lieutenant Colonel who served 20 years in various roles that include Commander of the 64th Aggressor Squadron, USAF Thunderbird #4 Demonstration Pilot, USAF Weapons School Graduate, Operational Test & Evaluation Pilot, and Flight Examiner. Kidd is a command pilot with over 3,200 flying hours in the F-16, A-4, T-38, T-37, T-3, and Alpha Jet. Kidd has logged over 400 hours of combat time during Operations Northern Watch, Southern Watch, Joint Guardian, Freedom’s Sentinel, and Resolute Support. Following his Air Force career, Kidd served in various roles to include Director of Business Development at Draken International and VP of Strategy at Shift4 (NYSE: FOUR). He most recently served as the Mission Director of Inspiration4. Kidd is also an accomplished collegiate runner and triathlete, competing in 15 Ironman triathlons since 2000, which includes four Ironman World Championships in Kailua-Kona, Hawaii.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/scott_poteet_image_20220214140414.jpeg",
                    firstFlight = null
                ),
                AstronautItem(
                    id = 717,
                    title = "Sarah Gillis",
                    status = CrewStatus.IN_TRAINING,
                    agency = "SpaceX",
                    nationality = "American",
                    description = "Sarah Gillis is a Lead Space Operations Engineer at SpaceX, responsible for overseeing the company’s astronaut training program. This includes the development of mission-specific curriculum and training execution for both NASA and commercial astronauts who fly aboard the Dragon spacecraft. She prepared NASA astronauts for the Demo-2 and Crew-1 missions as well as directly trained the Inspiration4 astronauts. She is an experienced mission control operator, who has supported real-time operations for Dragon’s cargo resupply missions to and from the International Space Station as a Navigation Officer, and as a crew communicator for Dragon’s human spaceflight missions.",
                    longDescription = "Sarah Gillis is a Lead Space Operations Engineer at SpaceX, responsible for overseeing the company’s astronaut training program. This includes the development of mission-specific curriculum and training execution for both NASA and commercial astronauts who fly aboard the Dragon spacecraft. She prepared NASA astronauts for the Demo-2 and Crew-1 missions as well as directly trained the Inspiration4 astronauts. She is an experienced mission control operator, who has supported real-time operations for Dragon’s cargo resupply missions to and from the International Space Station as a Navigation Officer, and as a crew communicator for Dragon’s human spaceflight missions.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/sarah_gillis_image_20220214140409.jpeg",
                    firstFlight = null
                ), AstronautItem(
                    id = 718,
                    title = "Anna Menon",
                    status = CrewStatus.IN_TRAINING,
                    agency = "SpaceX",
                    nationality = "American",
                    description = "Anna Menon is a Lead Space Operations Engineer at SpaceX, where she manages the development of crew operations and serves in mission control as both a Mission Director and crew communicator. During her tenure at SpaceX, she has led the implementation of Dragon’s crew capabilities, helped create the crew communicator operator role, and developed critical operational responses to vehicle emergencies such as a fire or cabin depressurization. Anna served in mission control during multiple Dragon missions, such as Demo-2, Crew-1, CRS-22, and CRS-23.",
                    longDescription = "Anna Menon is a Lead Space Operations Engineer at SpaceX, where she manages the development of crew operations and serves in mission control as both a Mission Director and crew communicator. During her tenure at SpaceX, she has led the implementation of Dragon’s crew capabilities, helped create the crew communicator operator role, and developed critical operational responses to vehicle emergencies such as a fire or cabin depressurization. Anna served in mission control during multiple Dragon missions, such as Demo-2, Crew-1, CRS-22, and CRS-23.",
                    imageUrl = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/astronaut_images/anna_menon_image_20220214140406.jpeg",
                    firstFlight = null
                )
            )
        )
}