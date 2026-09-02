package com.sithu.courtease.utils;

import com.sithu.courtease.models.Court;

import java.util.ArrayList;
import java.util.List;

public class CourtData {

    public static List<Court> getCourts() {

        List<Court> courts = new ArrayList<>();

        courts.add(new Court(
                "court_001",
                "Singapore Badminton Hall",
                "Badminton",
                "Geylang",
                "1 Lorong 23 Geylang, Singapore 388352",
                "A dedicated badminton venue suitable for casual games, training sessions and competitive play.",
                18.00,
                4.6,
                "court_badminton"
        ));

        courts.add(new Court(
                "court_002",
                "Bishan Sport Hall",
                "Badminton",
                "Bishan",
                "5 Bishan Street 14, Singapore 579783",
                "A community sports facility offering indoor court facilities in the Bishan area.",
                16.00,
                4.5,
                "court_badminton"
        ));

        courts.add(new Court(
                "court_003",
                "Delta Sport Hall",
                "Badminton",
                "Tiong Bahru",
                "900 Tiong Bahru Road, Singapore 158790",
                "A sports hall serving the Tiong Bahru community with indoor sports facilities.",
                15.00,
                4.4,
                "court_indoor"
        ));

        courts.add(new Court(
                "court_004",
                "MOE Evans Sport Hall",
                "Badminton",
                "Evans Road",
                "21 Evans Road, Singapore 259366",
                "An indoor sports hall with badminton courts that can also support other court sports.",
                18.00,
                4.5,
                "court_indoor"
        ));

        courts.add(new Court(
                "court_005",
                "SP Basketball Courts",
                "Basketball",
                "Dover",
                "500 Dover Road, Singapore 139651",
                "Outdoor basketball facilities located within the Singapore Polytechnic campus.",
                20.00,
                4.4,
                "court_basketball"
        ));

        courts.add(new Court(
                "court_006",
                "The Kallang Sports Court",
                "Basketball",
                "Kallang",
                "1 Stadium Drive, Singapore 397629",
                "A sports venue located within the Kallang sports precinct.",
                25.00,
                4.6,
                "court_basketball"
        ));

        courts.add(new Court(
                "court_007",
                "ARK Sports Village",
                "Multi-Sport",
                "Segar",
                "20A Segar Road, Singapore 679350",
                "A multi-sport venue designed for recreational and community sporting activities.",
                22.00,
                4.5,
                "court_multisport"
        ));

        courts.add(new Court(
                "court_008",
                "Smashing Pickle Pickleball Club",
                "Pickleball",
                "Jurong East",
                "2 Jurong Gateway Road, Singapore 608512",
                "A pickleball venue located within Jurong Play Grounds.",
                22.00,
                4.4,
                "court_pickleball"
        ));

        courts.add(new Court(
                "court_009",
                "Yio Chu Kang Sport Hall",
                "Badminton",
                "Ang Mo Kio",
                "214 Ang Mo Kio Avenue 9, Singapore 569780",
                "Public sport hall with badminton facilities.",
                14.80,
                4.7,
                "court_badminton"
        ));

        courts.add(new Court(
                "court_010",
                "Jurong East Sport Hall",
                "Badminton",
                "Jurong East",
                "21 Jurong East Street 31, Singapore 609517",
                "Air-conditioned sport hall suitable for indoor sports.",
                14.80,
                4.8,
                "court_badminton"
        ));

        courts.add(new Court(
                "court_011",
                "Jurong West Sport Hall",
                "Basketball",
                "Jurong West",
                "20 Jurong West Street 93, Singapore 648965",
                "Sport hall with access to community sports facilities.",
                30.00,
                4.6,
                "court_basketball"
        ));

        courts.add(new Court(
                "court_012",
                "Clementi Sport Hall",
                "Badminton",
                "Clementi",
                "518 Clementi Avenue 3, Singapore 129907",
                "Popular indoor sports facility close to Clementi town centre.",
                14.80,
                4.8,
                "court_badminton"
        ));

        courts.add(new Court(
                "court_013",
                "Hougang Sport Hall",
                "Badminton",
                "Hougang",
                "93 Hougang Avenue 4, Singapore 538832",
                "Community sport hall serving residents in Hougang.",
                14.80,
                4.6,
                "court_badminton"
        ));

        courts.add(new Court(
                "court_014",
                "Pasir Ris Sport Hall",
                "Basketball",
                "Pasir Ris",
                "120 Pasir Ris Central, Singapore 519640",
                "Modern community sports facility in Pasir Ris.",
                30.00,
                4.7,
                "court_basketball"
        ));

        courts.add(new Court(
                "court_015",
                "Our Tampines Hub",
                "Badminton",
                "Tampines",
                "1 Tampines Walk, Singapore 528523",
                "Large integrated community hub with multiple badminton courts.",
                14.80,
                4.9,
                "court_badminton"
        ));

        courts.add(new Court(
                "court_016",
                "Choa Chu Kang Sport Hall",
                "Basketball",
                "Choa Chu Kang",
                "1 Choa Chu Kang Street 53, Singapore 689236",
                "Community sport hall with indoor and outdoor sports facilities.",
                30.00,
                4.6,
                "court_basketball"
        ));

        return courts;
    }
}