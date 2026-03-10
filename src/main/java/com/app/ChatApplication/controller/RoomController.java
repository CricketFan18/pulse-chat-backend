package com.app.ChatApplication.controller;

import com.app.ChatApplication.dto.ChatMessageResponse;
import com.app.ChatApplication.dto.CreateRoomRequest;
import com.app.ChatApplication.dto.RoomResponse;
import com.app.ChatApplication.entity.Room;
import com.app.ChatApplication.entity.User;
import com.app.ChatApplication.service.MessageService;
import com.app.ChatApplication.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    private final MessageService messageService;

    public RoomController(RoomService roomService, MessageService messageService) {
        this.roomService = roomService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody CreateRoomRequest request, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(roomService.createRoom(request, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<RoomResponse> joinRoom(@PathVariable UUID roomId, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(roomService.joinRoom(roomId, currentUser));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getRoomMessages(
            @PathVariable UUID roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(messageService.getRoomMessages(roomId, page, size));
    }
}
