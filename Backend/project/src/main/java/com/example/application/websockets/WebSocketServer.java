package com.example.application.websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/chat/{id}/{id2}")
@Component
public class WebSocketServer {
	private Session session;
	private static Set<WebSocketServer> chatEndpoints = new CopyOnWriteArraySet<>();
	private static Map<String, List<Long>> users = new HashMap<String, List<Long>>();
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private long id, id2;

	@OnOpen
	public void onOpen(Session session, @PathParam("id") long id, @PathParam("id2") long id2) throws IOException {
		System.out.println("testing");
		logger.info("Entered into Open");
		this.session = session;
		this.id = id;
		this.id2 = id2;
		chatEndpoints.add(this);
		List<Long> conversingUsers = new ArrayList<Long>();
		conversingUsers.add(id);
		conversingUsers.add(id2);
		users.put(session.getId(), conversingUsers);

		String message = "Joined the chat!";
		sendMessageToSingleUser(session, message);
	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		logger.info("Entered into Message: got message: " + message);
	//	String echo = "recieved text: " + message;
		sendMessageToSingleUser(session, message);
	//	broadcast(echo);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("entered into close");
		chatEndpoints.remove(this);
		String message = "disconnected";
		broadcast(message);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("error");
	}
	
	private void sendMessageToSingleUser(Session session, String message) {
		chatEndpoints.forEach(endpoint -> {
			synchronized (endpoint) {
				try {
					if(users.get(endpoint.session.getId()).contains(id) &&
							users.get(endpoint.session.getId()).contains(id2)) {
						endpoint.session.getBasicRemote().sendText(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void broadcast(String message) throws IOException {
		chatEndpoints.forEach(endpoint -> {
			synchronized (endpoint) {
				try {
					
					endpoint.session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
