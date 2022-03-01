package com.wizard.monticketci.service;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dto.SendMail;

@Service
public interface EmailService {
	
	boolean sendEmail(SendMail sendmail);

}
