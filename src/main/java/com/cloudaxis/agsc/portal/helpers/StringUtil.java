/***************************************************************************
 *
 * This document contains confidential and proprietary information 
 * subject to non-disclosure agreements with Cloudaxis. This 
 * information shall not be distributed or copied without written 
 * permission from the Cloudaxis.
 *
 ***************************************************************************/

package com.cloudaxis.agsc.portal.helpers;

import java.util.regex.Pattern;

/***************************************************************************
 * <PRE>
 *  Project Name    : cloudaxis-portal
 * 
 *  Package Name    : com.cloudaxis.portal.helpers
 * 
 *  File Name       : StringUtil.java
 * 
 *  Creation Date   : 2015
 * 
 *  Author          : Contributor
 * 
 *  Purpose         : TODO
 * 
 * 
 *  History         : TODO
 * 
 * </PRE>
 ***************************************************************************/
public final class StringUtil {

	private static final Pattern EMPTY_STRING_Pattern = Pattern.compile("\\s*");

	private StringUtil() {
	}

	/**
	 * To test whether a string is null or empty.
	 * 
	 * @param str
	 *            : The string to be tested.
	 * @return: return true if str is null or is empty or is composed by
	 *          white-spaces.
	 */
	public static boolean isBlank(String str) {
		if (str == null)
			return true;
		else if (EMPTY_STRING_Pattern.matcher(str).matches())
			return true;
		else if ("null".equalsIgnoreCase(str.trim()))
			return true;
		else
			return false;
	}

	/**
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		if (s == null)
			return null;
		return s.trim();
	}

	/**
	 * To upper case.
	 * 
	 * @param s
	 * @return
	 */
	public static String upper(String s) {
		if (s == null)
			return null;
		return s.toUpperCase();
	}

	/**
	 * To upper case.
	 * 
	 * @param s
	 * @return
	 */
	public static String lower(String s) {
		if (s == null)
			return null;
		return s.toLowerCase();
	}

	public static String filterSurrogateCharacters(String text)
	{
		StringBuilder sb = new StringBuilder();
		if (text != null) {
			text = replaceStrangeSequences(text);
			for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
                    sb.append(ch);
                }
            }
		}
		return sb.toString();
	}

	private static String replaceStrangeSequences(String text) {
		return text.replace("\\xe2\\x80\\x99", "'").
				replace("\\xc3\\xa9", "e").
				replace("\\xe2\\x80\\x90", "-").
				replace("\\xe2\\x80\\x91", "-").
				replace("\\xe2\\x80\\x92", "-").
				replace("\\xe2\\x80\\x93", "-").
				replace("\\xe2\\x80\\x94", "-").
				replace("\\xe2\\x80\\x94", "-").
				replace("\\xe2\\x80\\x98", "'").
				replace("\\xe2\\x80\\x9b", "'").
				replace("\\xe2\\x80\\x9c", "\"").
				replace("\\xe2\\x80\\x9c", "\"").
				replace("\\xe2\\x80\\x9d", "\"").
				replace("\\xe2\\x80\\x9e", "\"").
				replace("\\xe2\\x80\\x9f", "\"").
				replace("\\xe2\\x80\\xa6", "...").
				replace("\\xe2\\x80\\xb2", "'").
				replace("\\xe2\\x80\\xb3", "'").
				replace("\\xe2\\x80\\xb4", "'").
				replace("\\xe2\\x80\\xb5", "'").
				replace("\\xe2\\x80\\xb6", "'").
				replace("\\xe2\\x80\\xb7", "'").
				replace("\\xe2\\x81\\xba", "+").
				replace("\\xe2\\x81\\xbb", "-").
				replace("\\xe2\\x81\\xbc", "=").
				replace("\\xe2\\x81\\xbd", "(").
				replace("\\xe2\\x81\\xbe", ")");
	}
}
