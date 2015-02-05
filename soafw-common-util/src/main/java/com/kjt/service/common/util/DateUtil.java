package com.kjt.service.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * <p>
 * 只会包装<b>简单的，利用率高</b>的公共工具方法，复杂的自己去实现，勿将工具类搞的过于庞大<br/>
 * 定义新方法时，在方法签名上需要明确工具方法的用途，该类中支持的类型只应该有int、long、Date、String，勿引入过多类型
 * <p>
 * 该工具类中方法分为以下几类<br/>
 * 1.对时间戳的转换<br/>
 * 2.对date、long的format<br/>
 * 3.含有一些计算的方法<br/>
 * 
 * @author zxj
 * 
 */
public class DateUtil {

	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 */
	public static long getCurrentUnixTimestamp() {
		return System.currentTimeMillis() / 1000L;
	}

	/**
	 * unixtime时间戳到时间串的转换
	 * 
	 * @param unixTimestamp
	 *            时间戳
	 * @param formatStr
	 *            格式
	 * @return
	 */
	public static long getCurrentTimeFromUnixTimestamp(long unixTimestamp) {
		return unixTimestamp * 1000L;
	}

	/**
	 * string日期转换为date
	 * 
	 * @param strDate
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static final Date parseDate(String strDate, String format) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(format);
		date = df.parse(strDate);
		return date;
	}

	/**
	 * date日期转换为String
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String formatDate(Date date, String format) throws ParseException {
		SimpleDateFormat inDf = new SimpleDateFormat(format);
		return inDf.format(date);
	}

	/**
	 * 获取当前小时
	 * 
	 * @return
	 */
	public static int getCurrentHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取某年某月的最后一天 yyyy-MM-dd
	 * 
	 * @param year
	 * @param month
	 * @return 返回的日期day(1~31)
	 */
	public static int getLastDate(int year, int month) {
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			monthDay[1] = 29;
		}
		int monthDayNum = monthDay[month - 1];
		return monthDayNum;
	}

	/**
	 * 获取当日的串
	 * 
	 * @param format
	 *            想要的格式
	 * @return
	 */
	public static Date getToday() {
		return new Date();
	}

	/**
	 * 获取昨日的串
	 * 
	 * @param format
	 *            想要的格式
	 * @return
	 */
	public static Date getYesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 相差天数
	 * 
	 * @param dateAfter
	 * @param dateBefore
	 * @return
	 */
	public static long getDiffDay(Date dateAfter, Date dateBefore) {
		if (dateAfter == null || dateBefore == null) {
			throw new IllegalArgumentException("计算日期相差天数，传入的时间不可为空");
		}
		long quot = dateAfter.getTime() - dateBefore.getTime();
		return quot / (1000 * 60 * 60 * 24);
	}

	/**
	 * 判断日期为星期几,1为星期日,依此类推
	 * 
	 * @param date1
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return x;
	}

	/**
	 * 某一天在一个月中的第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 所在小时
	 * 
	 * @param date
	 * @return
	 */
	public static int getHourOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取指定日期往后几天的日期
	 * 
	 * @param oldDate
	 * @param num
	 *            往后天数
	 * @return
	 */
	public static Date getAfterSomeDay(Date oldDate, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldDate);
		calendar.add(Calendar.DATE, num);
		return calendar.getTime();
	}

	/**
	 * 增加或减少几个月
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date getAfterSomeMonth(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.MONTH, num);
		return startDT.getTime();
	}

	/**
	 * 增加或减少小时数
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date getAfterSomeHour(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.HOUR, num);
		return startDT.getTime();
	}
	
	public static Integer getDateUnixTime(Date date){
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		Integer cdate = Long.valueOf(now.getTimeInMillis()/1000).intValue();
		return cdate;		
	}

}