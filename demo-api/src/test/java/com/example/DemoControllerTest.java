package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoControllerTest {
	@Autowired
	MessageCollector messageCollector;
	@Autowired
	Source source;
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void hi() throws Exception {
		restTemplate.postForObject("/", null, Void.class);
		Message message = messageCollector.forChannel(source.output()).poll(2,
				TimeUnit.SECONDS);
		assertThat(message.getPayload()).isEqualTo("{\"text\":\"こんにちは!!\"}");
	}

}