package com.wizard.monticketci.tools;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wizard.monticketci.log.Log;




@Component
public class WebUtils {
	
	@Log
	private static Logger log;
	
	private static HttpServletRequest request;

	@Autowired
	public void setRequest(HttpServletRequest request) {
	    this.request = request;
	}
	
	public static String getClientIp() {
	
	    String remoteAddr = "";
	
	    if (request != null) {
	        remoteAddr = request.getHeader("X-FORWARDED-FOR");
	        if (remoteAddr == null || "".equals(remoteAddr)) {
	            remoteAddr = request.getRemoteAddr();
	        }
	    }
	    
	    log.debug("remoteAddr {}", remoteAddr);

	    return remoteAddr;
	}


}
