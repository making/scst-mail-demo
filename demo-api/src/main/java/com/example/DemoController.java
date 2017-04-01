package com.example;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	private final Source source;

	public DemoController(Source source) {
		this.source = source;
	}

	@PostMapping
	public void hi() {
		Message<Hello> message = MessageBuilder.withPayload(new Hello("こんにちは!!")).build();
		this.source.output().send(message);
	}
}
