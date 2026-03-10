package com.app.ChatApplication.repository;

import com.app.ChatApplication.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByName(String name);

    Room getAllById(UUID id);
}
