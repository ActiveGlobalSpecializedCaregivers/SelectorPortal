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
}
