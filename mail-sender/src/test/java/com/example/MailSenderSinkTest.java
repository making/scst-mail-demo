package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Security;

import javax.mail.internet.MimeMessage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.ServerSetupTest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MailSenderSinkTest {
	@Autowired
	Sink sink;
	@Autowired
	DemoProperties demoProperties;

	@Rule
	public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTPS);

	static {
		Security.setProperty("ssl.SocketFactory.provider",
				DummySSLSocketFactory.class.getName());
	}

	@Test
	public void sendMail() throws Exception {
		sink.input().send(MessageBuilder.withPayload(new Hello("Hello")).build());
		assertThat(greenMail.waitForIncomingEmail(3000, 1)).isTrue();
		MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		assertThat(receivedMessages.length).isEqualTo(1);
		assertThat(receivedMessages[0].getContent()).isEqualTo("Hello\r\n");
		assertThat(receivedMessages[0].getSubject()).isEqualTo("Welcome!!");
		assertThat(receivedMessages[0].getFrom().length).isEqualTo(1);
		assertThat(receivedMessages[0].getFrom()[0].toString())
				.isEqualTo(demoProperties.getFrom());
		assertThat(receivedMessages[0].getAllRecipients().length).isEqualTo(1);
		assertThat(receivedMessages[0].getAllRecipients()[0].toString())
				.isEqualTo(demoProperties.getTo());
	}

}