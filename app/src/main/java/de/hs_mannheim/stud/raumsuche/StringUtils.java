package de.hs_mannheim.stud.raumsuche;

import android.text.TextUtils;

/**
 * Created by Martin on 12/4/15.
 */
public class StringUtils {
    public static String join(Iterable<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(String s: strings) {
            sb.append(sep).append(s);
            sep = separator;
        }

        return sb.toString();
    }
}
