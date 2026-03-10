package com.app.ChatApplication.repository;

import com.app.ChatApplication.entity.Room;
import com.app.ChatApplication.entity.RoomMember;
import com.app.ChatApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomMemeberRepository extends JpaRepository<RoomMember, UUID> {
    boolean existsByUserAndRoom(User user, Room room);
}
