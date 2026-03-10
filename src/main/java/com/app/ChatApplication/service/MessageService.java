package com.app.ChatApplication.service;

import com.app.ChatApplication.dto.ChatMessageResponse;
import com.app.ChatApplication.entity.Message;
import com.app.ChatApplication.entity.Room;
import com.app.ChatApplication.entity.User;
import com.app.ChatApplication.repository.MessageRepository;
import com.app.ChatApplication.repository.RoomRepository;
import com.app.ChatApplication.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;


    public MessageService(UserRepository userRepository, RoomRepository roomRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    public ChatMessageResponse saveMessage(UUID roomId, String senderEmail, String content)
    {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        Optional<User> userOptional = userRepository.findByEmail(senderEmail);
        if(roomOptional.isPresent() && userOptional.isPresent())
        {
            User user = userOptional.get();
            Room room = roomOptional.get();
            Message msg = new Message(content,user,room);
            messageRepository.save(msg);
            return new ChatMessageResponse(msg.getId(),msg.getContent(),user.getUsername(),new Date());
        }
        else
            throw new RuntimeException("Room or user does not exist");
    }

    public List<ChatMessageResponse> getRoomMessages(UUID roomId, int page, int size) {
        // pagination request (Page X, with Y items per page)
        Pageable pageable = PageRequest.of(page, size);

        Page<Message> messagePage = messageRepository.findByRoomIdOrderByTimestampDesc(roomId, pageable);

        return messagePage.getContent().stream()
                .map(msg -> new ChatMessageResponse(
                        msg.getId(),
                        msg.getContent(),
                        msg.getUser().getUsername(),
                        msg.getTimestamp()
                )).toList();
    }
}
