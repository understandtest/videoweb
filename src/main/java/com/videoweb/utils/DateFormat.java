package com.videoweb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.videoweb.utils.DateUtil;

@SuppressWarnings("serial")
public class DateFormat extends SimpleDateFormat {

	public Date parse(String source) throws ParseException {
		return DateUtil.stringToDate(source);
	}
}
