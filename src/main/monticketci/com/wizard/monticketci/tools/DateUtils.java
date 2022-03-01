/**
 * 
 */
package com.wizard.monticketci.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.wizard.monticketci.log.Log;




/**
 * @author ars
 * @create  Nov 13, 2018
 * custom date toold helper
 */
@Component
public class DateUtils {
	
	@Log
	private Logger log;

	/**
	 * this function return the current timeStamp
	 * @return
	 */
	public java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
	
	


	public String generateTransactionIdonTime() {
		return this.formatDateddMMyy(getCurrentTimeStamp());
	}
	

	public String generateTransactionIdonTimeByLastvalue() {
		return this.formatDateddMMyy(getCurrentTimeStamp()).substring(9, 13);
	}

	public String formatDateddMMyy(Date date) {
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssS");

		String formateddate;
		formateddate = dateFormat.format(date);
		return formateddate;
	}
	
	/**
	 * Fonction permettant de comparer 2 dates
	 * @param date1
	 * @param date2
	 * @return
	 */
	public int compareDate(Date date1, Date date2) {
		log.debug("Conversion, date1: " + date1 + " date2: " + date2);
		int result = 0;
        if (date1.compareTo(date2) > 0) {

        	log.debug("Date1 is after Date2");
        	result = 1;

        } else if (date1.compareTo(date2) < 0) {

        	log.debug("Date1 is before Date2");
        	result = -1;

        } else if (date1.compareTo(date2) == 0) {

        	log.debug("Date1 is equal to Date2");
        	result = 0;

        }
        return result;
	}
	
	/**
	 * 
	 * Fonction pour ajouter des jours a une date
	 * 
	 */
	public Date addDay(Date checkDate, int nombreAjouter, String type) {
		
		// convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(checkDate);
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        switch (type.toLowerCase()) {
		case "year":
	        c.add(Calendar.YEAR, nombreAjouter);
			break;
		case "month":
			c.add(Calendar.MONTH, nombreAjouter);
			break;
		case "date":
			c.add(Calendar.DATE, nombreAjouter);
			break;
		case "hour":
			c.add(Calendar.HOUR, nombreAjouter);
			break;
		case "minute":
			c.add(Calendar.MINUTE, nombreAjouter);
			break;
		case "second":
			c.add(Calendar.SECOND, nombreAjouter);
			break;

		default:
			break;
		}

        // convert calendar to date
        Date currentDateFin = c.getTime();

        log.debug("date de fin: {}", dateFormat.format(currentDateFin));
        
        return currentDateFin;
		
	}
	
}
