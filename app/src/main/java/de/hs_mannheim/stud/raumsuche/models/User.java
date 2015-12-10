package de.hs_mannheim.stud.raumsuche.models;

/**
 * Created by Martin on 12/10/15.
 */
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
}
