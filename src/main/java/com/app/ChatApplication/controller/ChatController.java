package com.app.ChatApplication.controller;

import com.app.ChatApplication.dto.ChatMessageRequest;
import com.app.ChatApplication.dto.ChatMessageResponse;
import com.app.ChatApplication.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
    public void sendMessage(@Payload ChatMessageRequest request, @DestinationVariable UUID roomId)
    {
        ChatMessageResponse response =  messageService.saveMessage(roomId,request.senderEmail(),request.content());
        messagingTemplate.convertAndSend("/topic/room/"+roomId, response);
    }
}
