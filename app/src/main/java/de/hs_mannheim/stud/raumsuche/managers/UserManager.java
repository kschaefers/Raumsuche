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

    private static UserManager instance;
    private SecurePreferences prefs;

    private UserManager(Context context) {
        prefs = new SecurePreferences(context);
    }

    public static UserManager getInstance(Context context) {
        if(instance == null){
            instance = new UserManager(context.getApplicationContext());
        }

        return  instance;
    }

    public boolean isUserLoggedIn() {
        String studentId = prefs.getString(SP_STUDENTID, null);
        String password = prefs.getString(SP_PASSWORD, null);

        return studentId != null && password != null;
    }

    public void setUser(User user) {
        SecurePreferences.Editor editor = prefs.edit();

        if (user == null){
            editor.remove(SP_STUDENTID);
            editor.remove(SP_PASSWORD);
        } else {
            editor.putString(SP_STUDENTID, user.getMtklNr());
            editor.putString(SP_PASSWORD, user.getPassword());
        }

        editor.commit();
    }

    public void removeUser() {
        setUser(null);
    }
}
