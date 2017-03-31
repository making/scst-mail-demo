package com.example;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailSenderSink {
	private final MailSender mailSender;

	public MailSenderSink(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@StreamListener(Sink.INPUT)
	public void sendMail(String body) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo("hello@example.com");
		email.setFrom("noreply@example.com");
		email.setSubject("Welcome!!");
		email.setText(body);
		mailSender.send(email);
	}
}
