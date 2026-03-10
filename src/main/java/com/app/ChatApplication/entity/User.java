package com.app.ChatApplication.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<RoomMember> roomMember;

    public User(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        roomMember = new HashSet<RoomMember>();
    }

    public User() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoomMember> getRoomMember() {
        return roomMember;
    }

    public void setRoomMember(Set<RoomMember> roomMember) {
        this.roomMember = roomMember;
    }
}
