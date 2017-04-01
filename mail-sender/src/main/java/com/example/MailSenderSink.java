package com.example;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailSenderSink {
	private final MailSender mailSender;
	private final DemoProperties demoProperties;

	public MailSenderSink(MailSender mailSender, DemoProperties demoProperties) {
		this.mailSender = mailSender;
		this.demoProperties = demoProperties;
	}

	@StreamListener(Sink.INPUT)
	public void sendMail(String body) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(demoProperties.getTo());
		email.setFrom(demoProperties.getFrom());
		email.setSubject("Welcome!!");
		email.setText(body);
		mailSender.send(email);
	}
}
