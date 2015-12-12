package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Martin on 12/12/15.
 */
@Parcel
public class Group {
    long id;

    String name;

    User owner;

    List<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
