package com.wizard.monticketci.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dto.SendMail;
import com.wizard.monticketci.entities.Caissier;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class EmailServiceImpl extends AbstractService<Caissier> implements EmailService{

	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
	Environment env;
	
	@Log
	private Logger log;
	
	@Override
	protected PagingAndSortingRepository<Caissier, String> getDao() {
		return null;
	}

	@Override
	public boolean sendEmail(SendMail sendmail) {
		
		try {
			
			/*MimeMessage message = emailSender.createMimeMessage();
	        boolean multipart = true;
	        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
	        String htmlMsg = "<h3>Im testing send a HTML email</h3>"
	                +"<img src='http://www.apache.org/images/asf_logo_wide.gif'>";
	        
	        message.setContent(htmlMsg, "text/html");
	        helper.setTo(sendmail.getAdresseMail().get(0));
	        helper.setSubject("Test send HTML email");
	        this.emailSender.send(message);*/

	        
			SimpleMailMessage message = new SimpleMailMessage(); 
	        message.setFrom(env.getProperty("spring.mail.username"));
	        message.setSubject(sendmail.getObjet()); 
	        message.setText(sendmail.getMessage());
	        
	        for (String adresseemail : sendmail.getAdresseMail()) {
	            message.setTo(adresseemail); 
	            log.debug("Objet final to send: {}", message);
	            emailSender.send(message);
	
			}
		
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

}
