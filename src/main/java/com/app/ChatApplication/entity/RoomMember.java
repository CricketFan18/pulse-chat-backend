package com.app.ChatApplication.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "roomMember")
public class RoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column
    private Date joinedAt;

    public RoomMember(User user, Room room) {
        this.user = user;
        this.room = room;
        joinedAt = new Date(System.currentTimeMillis());
    }

    public RoomMember() { }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

}