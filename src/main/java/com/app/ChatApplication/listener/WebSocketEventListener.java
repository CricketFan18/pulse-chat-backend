package com.app.ChatApplication.listener;

import com.app.ChatApplication.dto.PresenceResponse;
import com.app.ChatApplication.entity.User;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    // Thread-safe map to track how many active sessions a user has
    private final Map<String, Integer> onlineUsers = new ConcurrentHashMap<>();

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Principal principal = event.getUser();
        if (principal != null) {
            User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            assert user != null;
            String username = user.getUsername();

            int currentCount = onlineUsers.getOrDefault(username, 0);
            onlineUsers.put(username, currentCount + 1);

            if (currentCount == 0) {
                messagingTemplate.convertAndSend("/topic/presence", new PresenceResponse(username, true));
                System.out.println("🟢 " + username + " came online.");
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Principal principal = event.getUser();
        if (principal != null) {
            User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            assert user != null;
            String username = user.getUsername();

            int currentCount = onlineUsers.getOrDefault(username, 1);
            onlineUsers.put(username, currentCount - 1);

            if (currentCount - 1 == 0) {
                onlineUsers.remove(username);
                messagingTemplate.convertAndSend("/topic/presence", new PresenceResponse(username, false));
                System.out.println("🔴 " + username + " went offline.");
            }
        }
    }
}