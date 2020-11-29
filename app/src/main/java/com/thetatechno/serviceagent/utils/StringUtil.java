package com.thetatechno.serviceagent.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtil {
    public static String toCamelCase(final String words) {
        if (words == null)
            return null;

        final StringBuilder builder = new StringBuilder(words.length());

        for (final String word : words.split(" ")) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0)));
                builder.append(word.substring(1).toLowerCase());
            }
            if (!(builder.length() == words.length()))
                builder.append(" ");
        }

        return builder.toString();
    }

    public static String displayTime(String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.ENGLISH);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Date date = simpleDateFormat.parse(time);
            String dateString = simpleTimeFormat.format(date).toString();
            if (time.contains("PM"))
                return dateString.concat(" PM");
            else
                return dateString.concat(" AM");

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }
}
