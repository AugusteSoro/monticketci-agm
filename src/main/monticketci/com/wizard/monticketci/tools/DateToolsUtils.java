package com.wizard.monticketci.tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wizard.monticketci.dto.WeekDayDto;




@Component
public class DateToolsUtils {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public  java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
	
	
	public  java.sql.Timestamp addHours(Timestamp old , int hours) {
		Timestamp later = new Timestamp(old.getTime() + (1000 * 60 * 60 * hours));
		return later ;	
	}
	
	public java.sql.Timestamp addDay(Timestamp old , int day) {
		
		ZonedDateTime zonedDateTime = old.toInstant().atZone(ZoneId.of("UTC"));
		Timestamp n = Timestamp.from(zonedDateTime.plus(day, ChronoUnit.DAYS).toInstant());
		
		return n ;
	}	
	

	public java.sql.Timestamp removeDay(Timestamp old , int day) {
		
		ZonedDateTime zonedDateTime = old.toInstant().atZone(ZoneId.of("UTC"));
		Timestamp n = Timestamp.from(zonedDateTime.minus(day, ChronoUnit.DAYS).toInstant());
		
		return n ;
	}	
	
	public static long getDay(Timestamp nowdate , Timestamp findate) {
	   return 	Math.abs(findate.getTime() - nowdate.getTime()) ;
	}
	
	

	public static long getDays(Timestamp nowdate , Timestamp findate) {
		long firstTime = (getTimeNoMillis(nowdate) * 1000000) + nowdate.getNanos();
        long secondTime = (getTimeNoMillis(findate) * 1000000) + findate.getNanos();
        long diff = Math.abs(firstTime - secondTime);
	    return (int) diff / (1000*60*60*24) ;
	}
	
	
	 private static long getTimeNoMillis(Timestamp t) {
	        return t.getTime() - (t.getNanos()/1000000);
	    }
	
		public  int getNewDays(Timestamp nowdate , Timestamp findate) {
			
			 Calendar cal = Calendar.getInstance();
			 cal.setTimeInMillis(nowdate.getTime());	
			    // add a bunch of seconds to the calendar 
			 cal.add(Calendar.SECOND, 98765);
			 Timestamp t1 = new Timestamp(cal.getTime().getTime());
			 
			 cal.setTimeInMillis(findate.getTime());	
			    // add a bunch of seconds to the calendar 
			 cal.add(Calendar.SECOND, 98765);
			 Timestamp t2 = new Timestamp(cal.getTime().getTime());

			 long milliseconds = t2.getTime() - t1.getTime();
			 long day =  milliseconds / 86400000;
			 
			 logger.trace(" milliseconds : "+milliseconds+" ; day : "+(int) day);

		    return ((int) day)+1 ;

		}
		
		public String getMonthYear() {
			try {

				
				return 	LocalDate
						.now()
						.format(
						    DateTimeFormatter.ofPattern( "MM/uuuu" ) 
						);
				
			}catch (Exception e) {
				// TODO: handle exception
				logger.error(" : "+e.getMessage());
				return "" ;
			}
		}
		
		
		
		
		public java.sql.Timestamp convertStringToTimestamp(String yourString){
			try {
				 //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			       // you can change format of date
				 java.util.Date date = formatter.parse(yourString);
			      Timestamp timeStampDate = new Timestamp(date.getTime());
			      
			      return timeStampDate;
			} catch(Exception e) { //this generic but you can control another types of exception
			    // look the origin of excption
				logger.error(" : "+e.getMessage());
				return null ;
			}
		}
		
		public DateDayDto generateNowDayDto() {
			DateDayDto daynow = new DateDayDto();
			java.sql.Timestamp nowts = this.getCurrentTimeStamp() ;
			String tmstpToSTring = nowts.toString() ;
			
			String dateStart = tmstpToSTring.substring(0, 11)+"00:00:01" ;
			String dateEnd = tmstpToSTring.substring(0, 11)+"23:59:00" ;			
			logger.info(" date : "+dateStart +"  ///  "+dateEnd);
			
			Timestamp tmstpStart = this.convertStringToTimestamp(dateStart) ;
			Timestamp tmstpEnd = this.convertStringToTimestamp(dateEnd) ;
			daynow.start=tmstpStart ;
			daynow.end=tmstpEnd ;
			return daynow ;
				
		}

		
		
		public WeekDayDto generateWeekDayDto() {
			
			java.sql.Timestamp nowts = this.getCurrentTimeStamp() ;
			String tmstpToSTring = nowts.toString() ;
			
			String dateStart = tmstpToSTring.substring(0, 11)+"00:00:01" ;
			String dateEnd = tmstpToSTring.substring(0, 11)+"23:59:00" ;			
			logger.info(" date : "+dateStart +"  ///  "+dateEnd);
			
			Timestamp tmstpStart = this.convertStringToTimestamp(dateStart) ;
			Timestamp tmstpEnd = this.convertStringToTimestamp(dateEnd) ;
			
			logger.info(" timestamp : "+tmstpStart +"  ///  "+tmstpEnd);
			
			logger.info(" covertToDate : "+this.convertTimestampToDate(tmstpStart));
			
			WeekDayDto weekday = new WeekDayDto();
			DateDayDto[] lastweeks =  new DateDayDto[7];
			DateDayDto[] nowweeks =  new DateDayDto[7];
			
			DateDayDto datedto =  new DateDayDto();
			datedto.start=this.convertTimestampToDate(tmstpStart);
			datedto.end=this.convertTimestampToDate(tmstpEnd);
			nowweeks[0]=datedto ;
			
			for(int i = 1 ; i<=6 ; i++) {
				
				tmstpStart = this.removeDay(tmstpStart, 1);
				tmstpEnd = this.removeDay(tmstpEnd, 1);
				logger.info(" timestamp : "+tmstpStart +"  ///  "+tmstpEnd);
				datedto =  new DateDayDto();
				datedto.start=this.convertTimestampToDate(tmstpStart);
				datedto.end=this.convertTimestampToDate(tmstpEnd);
				nowweeks[i]=datedto ;
				
			}
			
			weekday.nowweeks = nowweeks ;
			
			Timestamp tmstpStartLast = this.removeDay(tmstpStart, 1);
			Timestamp tmstpEndLast = this.removeDay(tmstpEnd, 1);
			logger.info(" timestamp : "+tmstpStartLast +"  ///  "+tmstpEndLast);
			datedto =  new DateDayDto();
			datedto.start=this.convertTimestampToDate(tmstpStartLast);
			datedto.end=this.convertTimestampToDate(tmstpEndLast);
			lastweeks[0]=datedto ;
			
			for(int i = 1 ; i<=6 ; i++) {
				 tmstpStartLast = this.removeDay(tmstpStartLast, 1);
				 tmstpEndLast = this.removeDay(tmstpEndLast, 1);
				logger.info(" timestamp : "+tmstpStartLast +"  ///  "+tmstpEndLast);
				datedto =  new DateDayDto();
				datedto.start=this.convertTimestampToDate(tmstpStartLast);
				datedto.end=this.convertTimestampToDate(tmstpEndLast);
				lastweeks[i]=datedto ;
			}	
			
			weekday.lastweeks=lastweeks ;

			
			return weekday ;
		}
		
		
		public DateDayDto generateDateByDayInterval(int i) {
			
			DateDayDto response = new DateDayDto();
			
			java.sql.Timestamp nowts = this.getCurrentTimeStamp() ;
			String tmstpToSTring = nowts.toString() ;
			
			//String dateStart = tmstpToSTring.substring(0, 11)+"00:00:01" ;
			String dateEnd = tmstpToSTring.substring(0, 11)+"23:59:00" ;			
			//logger.info(" date : "+dateStart +"  ///  "+dateEnd);
			
			Timestamp tmstpEnd = this.convertStringToTimestamp(dateEnd) ;
			Timestamp tmstpStart = this.removeDay(tmstpEnd, i) ;

			response.start=tmstpStart;
			response.end=tmstpEnd ;
			return response ;
		}
		
		
		
		public java.util.Date convertTimestampToDate(Timestamp ts){
			long millis2 = ts.getTime();
			java.util.Date date = new java.util.Date( millis2 );
			return ts ;
		}
		

	 
	 /*
	  	java.util.Date date = new java.util.Date();
		Timestamp timestamp1 = new Timestamp(date.getTime());

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp1.getTime());

		// add a bunch of seconds to the calendar
		cal.add(Calendar.SECOND, 98765);

		// create a second time stamp
		Timestamp timestamp2 = new Timestamp(cal.getTime().getTime());

		long milliseconds = timestamp2.getTime() - timestamp1.getTime();
		int seconds = (int) milliseconds / 1000;
	  */
}
