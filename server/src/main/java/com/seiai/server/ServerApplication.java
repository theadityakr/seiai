package com.seiai.server;

import com.seiai.server.config.AzureOpenAIConfig;
import com.seiai.server.model.AzureOpenAIModel;
import com.seiai.server.model.CompletionModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AzureOpenAIConfig.class, CompletionModel.class})
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
