/**
 * 
 */
package com.wizard.monticketci.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.wizard.monticketci.log.Log;





/**
 * @author augustesoro
 * @create  Fev 22, 2021
 * custom date toold helper
 */
@Component
public class DateTools {
	
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
	
}
