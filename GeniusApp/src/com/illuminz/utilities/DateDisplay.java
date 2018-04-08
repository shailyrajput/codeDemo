package com.illuminz.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
@SuppressLint("SimpleDateFormat")
public class DateDisplay {



	public static String getdDate()
	{

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		Date currentLocalTime = cal.getTime();
		DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date.setTimeZone(TimeZone.getTimeZone("GMT"));
		String localTime = date.format(currentLocalTime);
		return localTime;

	}


	
	public static String getDataChangeFormate(String postDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		 
	        Date myDate = null;
	        try {
	            myDate = dateFormat.parse(postDate);

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy");
	        String finalDate = timeFormat.format(myDate);
            return finalDate;

	}
	

	public static ArrayList<String> getAllDays(int numebrofDays)
	{
		ArrayList<String> weekDate = new ArrayList<String>();
		for(int i = 0; i<numebrofDays;i++){
		Calendar someDate = GregorianCalendar.getInstance();
		someDate.add(Calendar.DAY_OF_YEAR, -i);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMM dd, h:mm");
		//Calendar currentDate = Calendar.getInstance();
		String formattedDate = dateFormat.format(someDate.getTime());
		int h=someDate.get(Calendar.HOUR_OF_DAY);
		//int m = someDate.get(Calendar.MINUTE);
		String ampm = h > 12 ? "PM" : "AM";
		String originaldate = formattedDate+" "+ampm;
		System.out.println("DateDisplay.getAllDays()"+originaldate);
		weekDate.add(originaldate);
		}
		return weekDate;
	}

	
	public ArrayList<String> getDates(String startDate, String endDate)
	{
		ArrayList<String> dateList = new ArrayList<String>();
		ArrayList<Date> dates = new ArrayList<Date>();
		try {
			
			  //create SimpleDateFormat object with source string date format
		      SimpleDateFormat sdfSource = new SimpleDateFormat("dd/MM/yy");
		     
		      //parse the string into Date object
			  Date sDate=sdfSource.parse(startDate);
		      Date eDate = sdfSource.parse(endDate);
		      
		      //create SimpleDateFormat object with desired date format
		      SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy MMMM dd");
		     
	         //parse the date into another format
			 startDate = sdfDestination.format(sDate);
			 endDate = sdfDestination.format(eDate);
			
			long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
			long endTime = eDate.getTime(); // create your endtime here,
											// possibly using Calendar
											// or Date
			long curTime = sDate.getTime();
			while (curTime <= endTime) {
				dates.add(new Date(curTime));
				curTime += interval;
			}
			for (int i = 0; i < dates.size(); i++) {
				Date lDate = (Date) dates.get(i);
				String ds = sdfDestination.format(lDate);
				System.out.println(" Date is ..." + ds);
				dateList.add(ds);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateList;
	}
	
}