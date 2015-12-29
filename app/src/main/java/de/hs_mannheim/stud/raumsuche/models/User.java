package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

/**
 * Created by Martin on 12/10/15.
 */
@Parcel
public class User {
    String mtklNr;

    String password;

    String name;

    String faculty;

    public String getMtklNr() {
        return mtklNr;
    }

    public void setMtklNr(String mtklNr) {
        this.mtklNr = mtklNr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDisplayName() {
        if (name == null || name.isEmpty()) {
            return mtklNr;
        }

        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof User)) {
            return false;
        }

        return mtklNr.equals(((User) other).getMtklNr());
    }

    public String getBuilding() {
        switch (getFaculty()) {
            case "Informatik":
                return "A";
            case "Biotechnologie":
                return "G";
            case "Elektrotechnik":
                return "B";
            case "Informationstechnik":
                return "S";
            case "Maschinenbau":
                return "B";
            case "Verfahrens- und Chemietechnik":
                return "G";
            case "Wirtschaftsingenieurwesen":
                return "L";
            default:
                return "H";
        }
    }
}
