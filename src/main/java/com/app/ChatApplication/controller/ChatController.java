package com.app.ChatApplication.controller;

import com.app.ChatApplication.dto.ChatMessageRequest;
import com.app.ChatApplication.dto.ChatMessageResponse;
import com.app.ChatApplication.entity.User;
import com.app.ChatApplication.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class ChatController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            @Payload ChatMessageRequest request,
            @DestinationVariable UUID roomId,
            Principal principal // Spring the user from your interceptor here!
    ) {
        // principal.getName() will be whatever you passed as the first argument to UsernamePasswordAuthenticationToken (in our case, the User object, but it might need a cast!)
        User sender = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        assert sender != null;
        ChatMessageResponse response = messageService.saveMessage(roomId, sender.getEmail(), request.content());
        messagingTemplate.convertAndSend("/topic/room/" + roomId, response);
    }
}
