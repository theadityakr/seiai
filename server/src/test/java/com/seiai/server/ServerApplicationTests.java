package com.seiai.server;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Disabled
@SpringBootTest
class ServerApplicationTests {

	@Test
	void contextLoads() {
		// Replicate the headless(false) setup from main application
		new SpringApplicationBuilder(ServerApplication.class)
				.headless(false)
				.run();
	}
}