package com.toptal.migration.model;

/**
 * <code>StringUtil</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class StringUtil {

    public static String stripQuotes(String val) {
        if(val.startsWith("\"")){
            val = val.substring(1);
        }
        if(val.endsWith("\n")){
            val = val.substring(0, val.length() - 1);
        }
        return val;
    }
}
