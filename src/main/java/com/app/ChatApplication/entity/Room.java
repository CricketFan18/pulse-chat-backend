package com.app.ChatApplication.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "room")
    private Set<RoomMember> roomMember;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.roomMember = new HashSet<RoomMember>();
    }

    public Room(String name)
    {
        this(name,"");
    }

    public Room() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public Set<RoomMember> getRoomMember() {
        return roomMember;
    }

    public void setRoomMember(Set<RoomMember> roomMember) {
        this.roomMember = roomMember;
    }
}
