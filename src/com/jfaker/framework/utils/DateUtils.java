package com.jfaker.framework.utils;

import org.joda.time.DateTime;

public class DateUtils {
	private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
	
	public static String getCurrentTime() {
		return new DateTime().toString(DATE_FORMAT_DEFAULT);
	}
	
	public static String getCurrentDay() {
		return new DateTime().toString(DATE_FORMAT_YMD);
	}
}
