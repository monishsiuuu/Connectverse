package gmc.project.reactive.management.project.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import gmc.project.reactive.management.project.models.MailModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	@Value("${spring.mail.username}")
	private String from;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendMail(MailModel mail) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setFrom(new InternetAddress(from, "Project Manager 3d"));
			mimeMessageHelper.setTo(mail.to());
			mimeMessageHelper.setSubject(mail.subject());
			mimeMessageHelper.setText(mail.body(), true);

			mailSender.send(mimeMessageHelper.getMimeMessage());
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
