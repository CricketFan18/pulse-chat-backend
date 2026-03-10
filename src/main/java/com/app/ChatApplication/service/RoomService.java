package com.app.ChatApplication.service;

import com.app.ChatApplication.dto.CreateRoomRequest;
import com.app.ChatApplication.dto.RoomResponse;
import com.app.ChatApplication.entity.Room;
import com.app.ChatApplication.entity.RoomMember;
import com.app.ChatApplication.entity.User;
import com.app.ChatApplication.repository.RoomMemeberRepository;
import com.app.ChatApplication.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private  final RoomMemeberRepository roomMemeberRepository;


    public RoomService(RoomRepository roomRepository, RoomMemeberRepository roomMemeberRepository) {
        this.roomRepository = roomRepository;
        this.roomMemeberRepository = roomMemeberRepository;
    }

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request, User currentUser)
    {
        Room room = new Room(request.name(),request.description());
        roomRepository.save(room);

        RoomMember roomMember = new RoomMember(currentUser,room);
        roomMemeberRepository.save(roomMember);

        return new RoomResponse(room.getId(),room.getName(),room.getDescription());
    }

    public List<RoomResponse> getAllRooms()
    {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(room -> new RoomResponse(room.getId(),room.getName(),room.getDescription()))
                .toList();
    }

    public RoomResponse joinRoom(UUID roomId, User currentUser) {
        // .orElseThrow is the cleanest way to unwrap an Optional or crash immediately
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Check for duplicates before saving
        if (!roomMemeberRepository.existsByUserAndRoom(currentUser, room)) {
            RoomMember roomMember = new RoomMember(currentUser, room);
            roomMemeberRepository.save(roomMember);
        }

        return new RoomResponse(room.getId(), room.getName(), room.getDescription());
    }
}
