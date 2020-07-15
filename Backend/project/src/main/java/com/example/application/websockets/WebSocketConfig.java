package com.example.application.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	/*
	 * This is for if we ever wanted to create a open group chat. It uses stomp
	 * endpoints to configure mapping for where the message would get broadcasted to
	 * everyone subscribed to "topic"
	 *
	 * public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	 * 
	 * @Override public void registerStompEndpoints(StompEndpointRegistry registry)
	 * { registry.addEndpoint("/websocket-example").withSockJS(); }
	 * 
	 * @Override public void configureMessageBroker(MessageBrokerRegistry registry)
	 * { registry.enableSimpleBroker("/topic");
	 * registry.setApplicationDestinationPrefixes("/app"); } }
	 */
}
