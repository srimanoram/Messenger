package com.project2.messenger.rawsocket;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RawWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> groups = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Long> lastSeen = new ConcurrentHashMap<>();

    private static final long HEARTBEAT_INTERVAL = 10_000;
    private static final long SESSION_TIMEOUT = 30_000;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String user = session.getUri().getQuery().split("=")[1]; // Extract username from query parameter
        sessions.put(user, session);
        lastSeen.put(user, System.currentTimeMillis());
        System.out.println("WebSocket connection established: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String user = session.getUri().getQuery().split("=")[1];
        sessions.remove(user);
        lastSeen.remove(user);
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());
        String username = session.getUri().getQuery().split("=")[1];

        lastSeen.put(username, System.currentTimeMillis());

        String type = jsonNode.get("type").asString();
        JsonNode toNode = jsonNode.get("to");
        String to = (toNode == null) ? "" : toNode.asString();
        switch (type) {
            case "ping":
                session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
                break;
            case "joinGroup":
                groups.computeIfAbsent(to, k -> ConcurrentHashMap.newKeySet()).add(username);
                break;
            case "group":
                String content = jsonNode.get("content").asString();
                Set<String> group = groups.get(to);
                if (group != null) {
                    for(String member: group) {
                        WebSocketSession recipientSession = sessions.get(member);
                        if (recipientSession != null) {
                            recipientSession.sendMessage(new TextMessage(content));
                        }
                    }
                }
                break;
            case "direct":
                String grpContent = jsonNode.get("content").asString();
                WebSocketSession recipientSession = sessions.get(to);
                if (recipientSession != null) {
                    recipientSession.sendMessage(new TextMessage(grpContent));
                }
                break;
        }
    }

    @Scheduled(fixedRate = HEARTBEAT_INTERVAL)
    public void closeDeadSession() {
        long now = System.currentTimeMillis();
        sessions.forEach((user, session) -> {
           Long last = lastSeen.get(user);
           if (last == null || now - last > SESSION_TIMEOUT) {
               try {
                   WebSocketSession s = sessions.remove(user);
                   if (s != null) s.close();
                   lastSeen.remove(user);
               }  catch (IOException e) {
                   System.err.println("Error closing session for " + user + ": " + e.getMessage());
               }
           }
        });
    }
}
