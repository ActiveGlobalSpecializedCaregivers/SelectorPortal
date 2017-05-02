package com.toptal.migration.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <code>DateUtils</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class DateUtils {

    public static Date parse(String value, SimpleDateFormat parser){
        try {
            return value == null || value.isEmpty() ? null : parser.parse(value);
        }
        catch (ParseException e) {
            throw new RuntimeException("Error parsing date:["+value+"]", e);
        }
    }
}
