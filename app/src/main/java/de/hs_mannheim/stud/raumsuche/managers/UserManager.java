package de.hs_mannheim.stud.raumsuche.managers;

import android.content.Context;

import com.securepreferences.SecurePreferences;

import de.hs_mannheim.stud.raumsuche.models.User;

/**
 * Created by Martin on 12/10/15.
 */
public class UserManager {
    private static String SP_STUDENTID = "sp_studentid";
    private static String SP_PASSWORD = "sp_password";
    private static String SP_NAME = "sp_name";
    private static String SP_FACULTY = "sp_faculty";

    private static UserManager instance;
    private SecurePreferences prefs;

    private UserManager(Context context) {
        prefs = new SecurePreferences(context);
    }

    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }

        return instance;
    }

    public boolean isUserLoggedIn() {
        String studentId = prefs.getString(SP_STUDENTID, null);
        String password = prefs.getString(SP_PASSWORD, null);

        return studentId != null && password != null;
    }

    public void setUser(User user) {
        SecurePreferences.Editor editor = prefs.edit();

        if (user == null) {
            editor.remove(SP_STUDENTID);
            editor.remove(SP_PASSWORD);
            editor.remove(SP_NAME);
            editor.remove(SP_FACULTY);
        } else {
            editor.putString(SP_STUDENTID, user.getMtklNr());
            editor.putString(SP_PASSWORD, user.getPassword());
            editor.putString(SP_NAME, user.getName());
            editor.putString(SP_FACULTY, user.getFaculty());
        }

        editor.commit();
    }

    public void removeUser() {
        setUser(null);
    }

    public User getUser() {
        String studentId = prefs.getString(SP_STUDENTID, null);
        String name = prefs.getString(SP_NAME, "");
        String faculty = prefs.getString(SP_FACULTY, "");

        User user = new User();
        user.setMtklNr(studentId);
        user.setName(name);
        user.setFaculty(faculty);

        return user;
    }

    public String getUserPassword() {
        return prefs.getString(SP_PASSWORD, null);
    }
}
