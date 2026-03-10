package com.app.ChatApplication.repository;

import com.app.ChatApplication.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByRoomIdOrderByTimestampAsc(UUID roomId);
    Page<Message> findByRoomIdOrderByTimestampDesc(UUID roomId, Pageable pageable);
}
