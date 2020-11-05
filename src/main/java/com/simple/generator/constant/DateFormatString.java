package com.simple.generator.constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface DateFormatString {
	public static final String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	
	public static final String yyyyMMddHHmm = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
	
	public static final String yyyyMMddHH = new SimpleDateFormat("yyyyMMddHH").format(new Date());
	
	public static final String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
}
