package com.cjyc.common.until;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {


	static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	static final SimpleDateFormat MMdd = new SimpleDateFormat("MMdd");

	static final SimpleDateFormat formatter_hour = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    static final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
    
    static final SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    
    static final SimpleDateFormat enMd = new SimpleDateFormat("MM月dd日");
    
	public static String getTimeFromLong(Long time) {
		Date date = new Date(time);
		return formatter.format(date);
	}
	
	//根据日期(yyyy-MM-dd HH:mm:ss)获取 yyyy-MM-dd
	public static String getYMDByDate(String date){
		String value = null;
		try {
			value = formatter.format(formatter.parse(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static String getNowDateStr(){
		Date date = new Date();
		return formatter_hour.format(date);
	}

	public static String getNowStr(){
		Date date = new Date();
		return formatter.format(date);
	}
	public static Date parseStr(String dateStr){
		Date date = null;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static String MMdd(long time) {
		Date date = new Date(time);
		return MMdd.format(date);
	}

	public static String hm(long time) {
		Date date = new Date(time);
		return hm.format(date);
	}
	
	public static String YMDHms(long time) {
		Date date = new Date(time);
		return formatter_hour.format(date);
	}
	public static String getTimeFromLong2(Long time) {
		Date date = new Date(time);
		return formatter_hour.format(date);
	}

	public static int getTerm() {
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH) + 1;
		if (m >= 9 || m == 1) {
			return 1;
		} else {
			return 2;
		}

	}

	//获取long类型的秒数
	public static long dateToLong(String source, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime()/1000;
	}

	/***
	 * 计算amount个月份后的日期
	 * 
	 * @param date
	 *            当前日期
	 * @param amount
	 *            在此日期上增加的月份数
	 * @return
	 */
	public static String addMonth2Date(String date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseString2Date(date));
		calendar.add(Calendar.MONTH, amount);
		return getTimeFromLong(calendar.getTimeInMillis());
	}

	/***
	 * 计算amount年后的日期
	 * 
	 * @param date
	 *            当前日期
	 * @param amount
	 *            在此日期上增加的年数
	 * @return HOUR_OF_DAY
	 */
	public static String addYear2Date(String date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseString2Date(date));
		calendar.add(Calendar.YEAR, amount);
		return getTimeFromLong(calendar.getTimeInMillis());
	}

	/***
	 * 计算年后的日期
	 * 
	 * @param date
	 *            当前日期
	 * @param amount
	 *            在此日期上增加的小时
	 * @return
	 */
	public static String addHourDate(String date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseString2Date2(date));
		calendar.add(Calendar.HOUR_OF_DAY, amount);
		return getTimeFromLong2(calendar.getTimeInMillis());
	}

	/***
	 * 将日期字符串按照某种模式，返回Date类型
	 * 
	 * @param date
	 *            日期字符串
	 * @return
	 */
	public static Date parseString2Date(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 将日期字符串按照某种模式，返回Date类型
	 * 
	 * @param date
	 *            日期字符串
	 * @return
	 */
	public static Date parseString2Date2(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按指定日期格式将日期转成字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String date2String(Date date,String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String date2String2(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(date != null){
				return df.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/***
	 * 计算两个日期之间相隔天数
	 * 
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束 日期
	 * @return
	 */
	public static long calDaysBetweenDates(String start, String end) {
		long days = 0;
		Date date1 = parseString2Date(start);
		Date date2 = parseString2Date(end);

		days = (date2.getTime() - date1.getTime()) / 1000 / 60 / 60 / 24;
		if (days < 0) {
			days = days*(-1);
		}
		return days;
	}

	/**
	 * 计算两个日期的先后
	 * @return long  >0 d2后   <0 d1后
	 * */
	public static long calTwoDays(String d1, String d2){
		long days = 0;
		Date date1 = parseString2Date(d1);
		Date date2 = parseString2Date(d2);

		days = date2.getTime() - date1.getTime();
		return days;
	}
	
	/***
	 * 计算两个日期之间的秒数（根据正负可判断先后）
	 * 
	 * @param start
	 *            开始日期   yyyy-MM-dd HH:mm:ss
	 * @param end
	 *            结束 日期  yyyy-MM-dd HH:mm:ss
	 * @return  < 0 end小start大
	 */
	public static int calSecBetweenDates(String start, String end){
		int val = 0;
		Date date1 = parseString2Date2(start);
		Date date2 = parseString2Date2(end);

		val = (int)(date2.getTime() - date1.getTime())/1000;
		
		return val;
	}
	/**
	 * 判断某个日期是星期几
	 * 
	 * @param date
	 *            当前日期
	 * @return String 星期
	 * 
	 * */
	public static String getWeekOfDate(Date date) {
		String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

		Calendar calendar = Calendar.getInstance();

		if (date != null) {

			calendar.setTime(date);

		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];

	}

	/***
	 * 获取当前日期所在月份的天数
	 * 
	 * @return 返回当前月份的天数
	 */
	public static int calDayByCurrentDate() {
		Calendar rightNow = Calendar.getInstance();
		return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);// 根据年月 获取月份天数
	}
	/**
	 * 获取指定日期所在月份的天数
	 * */
	public static int calDayByYearMonth(String yearMonth){
		 int year = Integer.parseInt(yearMonth.split("-")[0]);
		 int month = Integer.parseInt(yearMonth.split("-")[1]);
		 int day = 1;
		 Calendar cal = Calendar.getInstance();
		 cal.set(year,month - 1,day);
		 int last = cal.getActualMaximum(Calendar.DATE);
		 System.out.println(last);
		return last;
	}
	  /** 
	    * 判断当前日期是星期几<br> 
	    * <br> 
	    * @param pTime 要判断的日期<br>
	    * @return int 判断结果<br> 
	    * 
	    */  
	public static int dayForWeek(String pTime) throws Exception {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	} 
	
	/**
	 * 获取当前年月
	 * 
	 * */
	public static String getYearMonth(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	/**
	 * 根据参数获取年-月
	 * 
	 * */
	public static String getYearMonth(String year,String month){
		if(year != null && month != null){
			if( month.length() == 1){
				return year+"-0"+month;
			}else{
				return year+"-"+month;
			}
		}else{
			return null;
		}
		
	}
	
	/**
	 * 根据参数获取年-月 -日
	 * 
	 * */
	public static String getYearMonthDay(int year,int month,int day){
		
		if(Integer.toString(year) != null && Integer.toString(month) != null && Integer.toString(day) != null){
			if( Integer.toString(month).length() == 1 && Integer.toString(day).length() ==1){
				return year+"-0"+month+"-0"+day;
			}else if(Integer.toString(month).length() == 1){
				return year+"-0"+month+"-"+day;
			}else{
				return year+"-"+month+"-0"+day;
			}
		}else{
			return null;
		}
		
	}
	
	/**
	 * 根据参数获取年-月
	 * */
	public static String getYearMonth(int year,int month){
		if(Integer.toString(year) != null && Integer.toString(month) != null){
			if(Integer.toString(month).length() == 1){
				return year+"-0"+month;
			}else{
				return year+"-"+month;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 根据参数判断星期几
	 * */
	public static String getWeekByInt(int year,int month,int day){
		String week ="";
		try {
			String dateStr = getYearMonthDay(year, month, day);
			Date date = formatter.parse(dateStr);
			week = getWeekOfDate(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return week;
	}
	
	/***
	 * 根据指定日期返回日期前一天
	 * @param specifiedDay
	 * @return String
	 * */
	public static String getSpecifiedDayBefore(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
		    date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-1);

		String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
		}
	/***
	 * 根据指定日期返回日期后一天
	 * @param specifiedDay
	 * @return String
	 * */
	public static String getSpecifiedDayAfter(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
		    date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day+1);

		String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
		}
	
	/**
	 * 返回今天 指定天数后的日期
	 * @param param 天数（可为小数）
	 * 
	 * 如param==1 则返回一天后的日期，param== -2 返回两天前的日期
	 * */
	public static String getSpecifiedDayByParam(int param){
		Calendar c = Calendar.getInstance();
		Date date= new Date();
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day+param);

		String someday=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return someday;
	}
	/**
	 * 获取本年年份
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		return y;
	}
	
	/**
	 * 获取指定日期的年份
	 * */
	public static int getYearByDateStr(String date){
		int year = 0;
		try {
			Date d = formatter.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			year = cal.get(Calendar.YEAR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return year;
	}
	
	/**
	 * 获取指定日期的月份
	 * */
	public static int getMonthByDateStr(String date){
		int month = 0;
		try {
			Date d = formatter.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			month = cal.get(Calendar.MONTH)+1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return month;
	}
	
	/**
	 * 获取指定日期的日
	 * @throws ParseException 
	 * */
	public static int getDayByDateStr(String date){
		int day = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		try {
			String dayStr = sdf.format(formatter.parse(date));
			day = Integer.parseInt(dayStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}
	/**
	 * 获取本周一的日期
	 * */
	public static String getCurrentMonDay(){
		Calendar cal =Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String date = df.format(cal.getTime());
        return date;
	}
	
	public static Date getCurrentDateMonDay(){
		Calendar cal =Calendar.getInstance(Locale.CHINA);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        return cal.getTime();
	}
    
	/**
	 * 获取本周几对应的日期
	 * */
	public static String getDay(int week){
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		Date firstDayDate = getCurrentDateMonDay();
		calendar.setTime(firstDayDate);
		
		switch (week) {
		case 1:
			break;
		default:
			calendar.add(Calendar.DATE, week-1);
		}
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(calendar.getTime());
	}
	/**
	 * 获取本月月份
	 * */
	public static String getCurrentMonth(){
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String date = df.format(cal.getTime());
        return date;
	}
	/**
	 * 获取上月月份
	 * */
	public static String getPreMonth(){
		Calendar cal =Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String date = df.format(cal.getTime());
        return date;
	}
	/**
	 * 获取上月第一天
	 * */
	public static String getFirstOfPreMonth(){
		SimpleDateFormat format_month = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance();   
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format_month.format(c.getTime());
        return first;
	}
	/**
	 * 获取上月最后一天
	 * */
	public static String getEndOfPreMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();  
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		String end = format.format(calendar.getTime());
		return end;
	}
	/**
	 * 获取本月第一天
	 * */
	public static String getFirstDayOfMonth(){
		SimpleDateFormat format_month = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance();   
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format_month.format(c.getTime());
        return first;
	}
	/**
	 * 获取本月最后一天
	 * */
	public static String getEndDayOfMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
	    // 获取Calendar  
	    Calendar calendar = Calendar.getInstance();  
	    // 设置时间,当前时间不用设置  
	    // calendar.setTime(new Date());  
	    // 设置日期为本月最大日期  
	    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); 
	    String end = format.format(calendar.getTime());
	    return end;
	}
	/**
	 * 获取某月第一天
	 * */
	public static String getFirstDayByMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
	    //设置年份
	    cal.set(Calendar.YEAR,year);
	    //设置月份
	    cal.set(Calendar.MONTH, month-1);
	    //获取某月最小天数
	    int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
	    //设置日历中月份的最小天数
	    cal.set(Calendar.DAY_OF_MONTH, firstDay);
	    //格式化日期
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String firstDayOfMonth = sdf.format(cal.getTime());
	    
	    return firstDayOfMonth;
	}
	/**
	 * 获取某月最后一天
	 * */
	public static String getLastDayByMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
         
        return lastDayOfMonth;
	}
	/**
	 * 获取本周日的日期
	 * */
	public static String getCurrentSunDay(){
		Calendar cal =Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        
        String date = df.format(cal.getTime());
        return date;
	}
	/**
	 * 得到日期字符串的秒数
	 * @param time
	 */
	public static int getMillSeconds(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return (int)(d.getTime()/1000);
	}
	
	/**
	 * 得到日期字符串的秒数(指定日期格式)
	 */
	public static int getMillSeconds2(String time,String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date d = null;
		try {
			d = sdf.parse(time);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return (int)(d.getTime()/1000);
	}
	
	/**
	 * 得到日期字符串的秒数(指定日期格式)
	 */
	public static int getMillSeconds2(Date time,String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date d = null;
		try {
			d = sdf.parse(sdf.format(time));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return (int)(d.getTime()/1000);
	}
	
	/**
	 * 根据日期，得到int类型的秒数
	 * */
	public static long getLongByDate(Date date){
		Calendar cal =Calendar.getInstance();
		cal.setTime(date);
		return (cal.getTimeInMillis()/1000);
	}
	/**
	 * 根据指定日期格式，返回日期秒数对应的时间
	 */

	public static String formatTimeToString(long time) {
		if(time > 0L){
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(new Date(time*1000));
		}else{
			return "";
		}
	}
	
	/**
	 * 根据指定日期格式，返回日期秒数对应的时间
	 */
	public static String formatHmToString(long time) {
		return getDateString(time, "HH:mm:ss");
	}

	/**
	 * 根据指定日期格式，返回日期秒数对应的时间
	 */
	public static String formatYMDToStringByPattern(long time) {
		return getDateString(time, "yyyy-MM-dd");
	}
	
	/**
	 * 根据指定日期格式，返回日期秒数对应的时间
	 */
	public static String formatYMDToStringByPattern2(long time) {
		return getDateString(time, "yyyy/MM/dd");
	}
	
	/**
	 * 根据指定日期格式，返回日期秒数对应的时间
	 */
	public static String formatMDToStringByPattern(long time) {
		return getDateString(time, "MM-dd");
	}
	
	/**
	 * 根据指定日期格式，返回日期秒数对应的时间
	 */
	public static String formatYMDToEnMD(String dateStr) {
		String v = "";
		try {
			v = enMd.format(formatter.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return  v ;
	}
	
	/***
	 * 根据日期秒数和日期格式 返回日期字符串
	 * @param createTime 秒数
	 * @return
	 */
	public static String getDateString(long createTime,String pattern) {
		Date dt = new Date(createTime*1000);
		String sDateTime = new SimpleDateFormat(pattern).format(dt);
		return sDateTime;
	}

	
	/***
	 * 获取当前日期对应的秒数
	 * @param date
	 * @return
	 */
	public static long getDateSeconds(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis() / 1000;
	}
	
	/***
	 * 获取日期的毫秒数
	 * @param date
	 * @return
	 */
	public static long getDateMillis(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}
	
	/***
	 * 获取指定日期格式的日期字符串的毫秒数
	 * @param date 日期字符串
	 * @param pattern 日期格式
	 * @return
	 */
	public static long formatDateToMillis(String date,String pattern){
		Date d = parseStringToDate(date,pattern);
		return getDateMillis(d);
	}
	
	/***
	 * 将日期字符串按指定格式解析成日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parseStringToDate(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取本周所有日期
	 * @throws ParseException 
	 * */
	public static List<String> getCurrentWeek(Date today){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式  
		List<String> list = new ArrayList<String>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = sdf.format(cal.getTime());//本周一的日期
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());//本周日的日期
		
		//获取周一到周日之间的日期集合
		list = getListBetweenDate(imptimeBegin,imptimeEnd);
		
		return list;
	}
	/**
	 * 获取本月所有日期集合
	 * */
	public static List<String> getCurrentMonthList(){
		List<String> dateList = null;
		//获取本月第一天
		String firstDay = getFirstDayOfMonth();
		//获取本月最后一天
		String lastDay = getEndDayOfMonth();
		dateList = getListBetweenDate(firstDay,lastDay);
		return dateList;
	}
	/**
	 * 获取两个日期之间的日期集合
	 * */
	public static List<String> getListBetweenDate(String s,String e){
		List<String> list = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		    Calendar start = Calendar.getInstance();  
		    start.setTime(sdf.parse(s));  
		    Long startTIme = start.getTimeInMillis();  
		  
		    Calendar end = Calendar.getInstance();  
		    end.setTime(sdf.parse(e));  
		    Long endTime = end.getTimeInMillis();  
		  
		    Long oneDay = 1000 * 60 * 60 * 24l; 
		    
		    Long time = startTIme;  
		    while (time <= endTime) {  
		        Date d = new Date(time);  
		        list.add(sdf.format(d));
		        time += oneDay;  
		    }  
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取某月（年-月）中的所有日期集合
	 * */
	public static List<String> getListBetweenDate(int year,int month){
		List<String> list = new ArrayList<String>();
		String firstDay = getFirstDayByMonth(year,month);
		String lastDay = getLastDayByMonth(year,month);
		list = getListBetweenDate(firstDay,lastDay);
		return list;
	}
	/**
	 * 获取某月（年-月）中的所有日期集合
	 * */
	public static List<String> getDateListByMonth(String dateStr){//形如 ： 2016-06
		List<String> list = null;
		//int year = DateUtils.getYearByDateStr(dateStr);
		//int month = DateUtils.getMonthByDateStr(dateStr);//会报错   
		int year = Integer.parseInt(dateStr.split("-")[0]);
		int month = Integer.parseInt(dateStr.split("-")[1]);
		list = getListBetweenDate(year, month);
		return list;
	}
	/**
	 * 获取本月一号到今天的所有日期集合
	 * */
	public static List<String> getListByToday(){
		String firstDay = getFirstDayOfMonth();
		String today = getNowStr();
		return getListBetweenDate(firstDay,today);
	}
	/**
	 * 获取本周一到今天的所有日期集合
	 * */
	public static List<String> getListBetweenMondayToday(){
		//获取本周一的日期
		String monday = getCurrentMonDay();
		//获取今天的日期
		String today = getNowStr();
		return getListBetweenDate(monday, today);
	}
	public static void main(String[] s) throws Exception{
//		System.out.println(randomVerificationCode(6));
//		System.out.println(hm(1481681747));
//		System.out.println(getMillSeconds2(new Date(), "yyyy-MM-dd"));
//		System.out.println(DateUtils.getMillSeconds2("05:44:00","HH:mm:ss"));
//		System.out.println(DateUtils.getDateString(-8160,"HH:mm:ss"));
//		System.out.println(DateUtils.formatYMDToEnMD("2016-09-07"));
//		System.out.println(DateUtils.formatYMDToStringByPattern(1473264000));
		//System.out.println(DateUtils.getSpecifiedDayBefore("2016-07-06 08:30:01"));
		//System.out.println(DateUtils.getSpecifiedDayAfter("2016-06-30 08:30:01"));
		//System.out.println(DateUtils.getEndOfPreMonth());
		//System.out.println(DateUtils.getListBetweenDate(DateUtils.getFirstOfPreMonth(),DateUtils.getEndOfPreMonth()));
//		System.out.println(DateUtils.getSpecifiedDayBefore("2015-06-15 09:50:01"));
//		System.out.println(DateUtils.getSpecifiedDayAfter("2015-06-15 09:50:01"));
//		System.out.println(DateUtils.calHoursBetweenDates("2015-05-15 09:14:31","2015-05-14 09:15:01"));
//		System.out.println(DateUtils.calDaysBetweenDates("2015-03-11 23:59:00","2015-03-01 00:01:00"));
//		System.out.println(DateUtils.getYearMonthDay(2015, 02, 21));
//		System.out.println(DateUtils.getWeekByInt(2015, 02, 21));
//		System.out.println(DateUtils.getYearMonth("2014", "1"));
//		Date date = new Date();
//		String time = "2015-04-20";
//		String week = DateUtils.getWeekOfDate(date);
//		//System.out.println(week);
//		System.out.println(DateUtils.dayForWeek(time));
//		String d =  DateUtils.addYear2Date("2014-08-08", 1);
//		
//		System.err.println("d:"+d);
//		long days = DateUtils.calDaysBetweenDates("2014-08-08", d);
//		System.err.println("days:"+days);
		
//		System.out.println(getDateString(1439445816,"yyyy-MM-dd HH:mm:ss"));
		//String YZM= randomVerificationCode();  
		//System.out.println(YZM);
	}

	/***
	 * 将日期毫秒数格式化成指定格式日期字符串
	 * @param remindTime 毫秒数
	 * @param pattern 日期格式
	 * @return
	 */
	public static String formatTimeToString(long remindTime, String pattern) {
		Date dt = new Date(remindTime);
		String sDateTime = new SimpleDateFormat(pattern).format(dt);
		return sDateTime;
	}
	
	/**
	 * 随机生成四位验证码
	 * 
	 * **/
	public static String randomVerificationCode(int n)  
    {  
        Date date = new Date();  
        long timeMill = date.getTime();  
        Random rand = new Random(timeMill);
        String YZM="";
        for(int i = 0; i < n; i++)  
        {  
            YZM+=rand.nextInt(10);
        }  
        return YZM;
    }  
  

}
