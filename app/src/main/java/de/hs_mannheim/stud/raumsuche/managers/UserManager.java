package de.hs_mannheim.stud.raumsuche.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

import de.hs_mannheim.stud.raumsuche.models.User;

/**
 * Created by Martin on 12/10/15.
 */
public class UserManager {
    private static String SP_MTKLNR = "sp_password";
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
        String mtklNr = prefs.getString(SP_MTKLNR, null);
        String password = prefs.getString(SP_PASSWORD, null);

        return mtklNr != null && password != null;
    }

    public void setUser(User user) {
        SecurePreferences.Editor editor = prefs.edit();

        editor.putString(SP_MTKLNR, user.getMtklNr());
        editor.putString(SP_PASSWORD, user.getPassword());

        editor.commit();
    }
}
