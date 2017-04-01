package com.example;

import java.util.Properties;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Profile("cloud")
@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class CloudConfig extends AbstractCloudConfig {

	@Bean
	MailSender mailSender(MailProperties properties) {
		MailSender mailSender = connectionFactory().service(MailSender.class);
		if (mailSender instanceof JavaMailSenderImpl) {
			JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) mailSender;
			if (properties.getDefaultEncoding() != null) {
				javaMailSender.setDefaultEncoding(properties.getDefaultEncoding().name());
			}
			if (!properties.getProperties().isEmpty()) {
				Properties javaMailProperties = new Properties();
				javaMailProperties.putAll(properties.getProperties());
				javaMailSender.setJavaMailProperties(javaMailProperties);
			}
		}
		return mailSender;
	}
}
