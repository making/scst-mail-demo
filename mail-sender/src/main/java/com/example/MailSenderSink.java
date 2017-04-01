package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MailSenderSink {
	private final MailSender mailSender;
	private final DemoProperties demoProperties;
	private static final Logger log = LoggerFactory.getLogger(MailSenderSink.class);

	public MailSenderSink(MailSender mailSender, DemoProperties demoProperties) {
		this.mailSender = mailSender;
		this.demoProperties = demoProperties;
	}

	@StreamListener(Sink.INPUT)
	public void sendMail(@Payload Hello hello) {
		log.info("Received {}", hello);
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(demoProperties.getTo());
		email.setFrom(demoProperties.getFrom());
		email.setSubject("Welcome!!");
		email.setText(hello.getText());
		mailSender.send(email);
	}
}
