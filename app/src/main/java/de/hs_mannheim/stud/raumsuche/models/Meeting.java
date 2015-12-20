package de.hs_mannheim.stud.raumsuche.models;

import org.parceler.Parcel;

/**
 * Created by Christoph on 20.12.2015.
 */
@Parcel
public class Meeting {
    int meetingId;
    String room;
    Group group;
    String day;
    int hour;

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
