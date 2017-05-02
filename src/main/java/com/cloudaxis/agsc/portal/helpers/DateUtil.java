/***************************************************************************
 *
 * This document contains confidential and proprietary information 
 * subject to non-disclosure agreements with Cloudaxis. This 
 * information shall not be distributed or copied without written 
 * permission from the Cloudaxis.
 *
 ***************************************************************************/

package com.cloudaxis.agsc.portal.helpers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cloudaxis.agsc.portal.constants.CandidateProfileConstants;

/***************************************************************************
 * <PRE>
 *  Project Name    : cloudaxis-portal
 * 
 *  Package Name    : com.cloudaxis.portal.helpers
 * 
 *  File Name       : DateUtil.java
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
public final class DateUtil {

	/**
	 * Get Current date & time.
	 * 
	 * @return
	 */
	public static Date now() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Transform the specified dater to string, by specifying the format the
	 * returned value to be.
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2String(Date date, String format) {
		if (date == null)
			throw new NullPointerException("date is null: " + format);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * Transform the specified dateStr to Date, by specifying the date format.
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date string2Date(String dateStr, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.parse(dateStr);
		}
		catch (ParseException e) {
			throw new IllegalArgumentException("Date: " + dateStr
					+ " | format: " + format + " | Exception: " + e);
		}
	}

	/**
	 * @return
	 */
	public static Timestamp currentSQLTimestamp() {
		long millis = Calendar.getInstance().getTimeInMillis();
		return new Timestamp(millis);
	}

	/**
	 * Get the difference in milliseconds between a startDate and a endDate.
	 * 
	 * @param startDate
	 *            start Date
	 * @param endDate
	 *            end Date
	 * @return difference in seconds between a startDate and and endDate
	 */
	public static Long getTimeDifference(Date startDate, Date endDate) {
		return endDate.getTime() - startDate.getTime();
	}

	/**
	 * Get Current date without time part.
	 * 
	 * @return
	 */
	public static Date today() {
		Date now = now();
		final String format = "yyyy/MM/dd";
		String dateString = date2String(now, format);
		return string2Date(dateString, format);
	}

	/**
	 * Calculate how many days between d1 and d2.
	 * 
	 * Note that this method may return negative value, which indicating d1 is
	 * after(larger than) d2.
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int countDays(Date d1, Date d2) {
		long span = (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24);
		return Long.valueOf(span).intValue();
	}

	/**
	 * Decrease the num days beginning from the specified date.
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date decreaseDay(Date date, int num) {
		if (date == null)
			return null;
		if (num < 0)
			throw new IllegalArgumentException("num < 0");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				- num);
		return calendar.getTime();
	}

	/**
	 * Increase the num days beginning from the specified date.
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date increaseDay(Date date, int num) {
		if (num < 0)
			throw new IllegalArgumentException("num < 0");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				+ num);
		return calendar.getTime();
	}

	/**
	 * Increase the num of months beginning from the specified date.
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date increaseMonth(Date date, int num) {
		if (num < 0)
			throw new IllegalArgumentException("num < 0");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + num);
		return calendar.getTime();
	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp toSQLTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	public static String trimDateForExcel(Date date){
		if(date == null){
			return CandidateProfileConstants.EMPTY_DISPLAY;
		}else{
			return date.toString();
		}
	}
}
