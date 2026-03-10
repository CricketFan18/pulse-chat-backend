# Pulse-Chat-Backend

A real-time chat backend built with **Spring Boot**, featuring WebSocket messaging, JWT authentication, room management, message history with pagination, and live user presence tracking.

---

## Features

- **JWT Authentication** — Register and login with BCrypt-hashed passwords and signed JWT tokens
- **Room Management** — Create rooms, join rooms, list all rooms
- **Real-Time Messaging** — WebSocket (STOMP protocol) for live message broadcasting per room
- **Message History** — Paginated REST endpoint to fetch past messages, sorted by timestamp
- **User Presence** — Live online/offline events broadcast over WebSocket with multi-tab session tracking
- **Global Error Handling** — Centralized exception handler with correct HTTP status codes

---

## 🛠️ Tech Stack

| Layer | Technology                  |
|---|-----------------------------|
| Framework | Spring Boot 4.0.3           |
| Language | Java 21                     |
| Database | PostgreSQL                  |
| ORM | Spring Data JPA / Hibernate |
| Auth | Spring Security + JJWT      |
| Real-Time | WebSocket + STOMP           |
| Build Tool | Maven                       |

---

## 📁 Project Structure

```
src/main/java/com/app/ChatApplication/
├── config/
│   ├── JwtAuthenticationFilter.java   # HTTP request JWT filter
│   ├── SecurityConfig.java            # Spring Security filter chain
│   ├── WebSocketAuthInterceptor.java  # WebSocket STOMP auth interceptor
│   └── WebSocketConfig.java           # STOMP endpoint + broker config
├── controller/
│   ├── AuthController.java            # POST /api/auth/register, /login
│   ├── ChatController.java            # @MessageMapping WebSocket handler
│   └── RoomController.java            # Room CRUD + message history
├── dto/                               # Request/Response records
├── entity/
│   ├── User.java
│   ├── Room.java
│   ├── RoomMember.java                # Join table: User ↔ Room
│   └── Message.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── UserAlreadyExistsException.java
│   ├── UserNotFoundException.java
│   └── InvalidCredentialsException.java
├── listener/
│   └── WebSocketEventListener.java    # Connect/disconnect presence events
├── repository/                        # Spring Data JPA interfaces
└── service/
    ├── UserService.java
    ├── RoomService.java
    ├── MessageService.java
    └── JwtService.java
```

---

## Setup & Running

### Prerequisites
- Java 21+
- PostgreSQL running locally
- Maven

### 1. Clone the repo
```bash
git clone https://github.com/CricketFan18/ChatApplication.git
cd ChatApplication
```

### 2. Configure environment

Create `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/chatdb
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_minimum_32_character_secret_key_here
```

> ⚠️ Never commit `application.properties` with real secrets. Use environment variables in production.

### 3. Create the database
```sql
CREATE DATABASE chatdb;
```

### 4. Run
```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`

---

## 📡 API Reference

### Auth

| Method | Endpoint | Body | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | `{ username, email, password }` | Register new user |
| `POST` | `/api/auth/login` | `{ email, password }` | Returns JWT token |

### Rooms

> All room endpoints require `Authorization: Bearer <token>` header

| Method | Endpoint | Body | Description |
|---|---|---|---|
| `POST` | `/api/rooms` | `{ name, description }` | Create a room |
| `GET` | `/api/rooms` | — | List all rooms |
| `POST` | `/api/rooms/:id/join` | — | Join a room |
| `GET` | `/api/rooms/:id/messages?page=0&size=20` | — | Paginated message history |

### WebSocket

Connect to: `ws://localhost:8080/ws`

Pass JWT in the STOMP CONNECT headers:
```
Authorization: Bearer <token>
```

| Action | Destination |
|---|---|
| Send a message | `/app/chat/{roomId}` |
| Subscribe to room | `/topic/room/{roomId}` |
| Subscribe to presence | `/topic/presence` |

---

## Auth Flow

```
Client                        Server
  │                              │
  ├─── POST /api/auth/login ────►│
  │◄── { token: "eyJ..." } ──────┤
  │                              │
  ├─── GET /api/rooms ──────────►│
  │    Authorization: Bearer eyJ │  ← JwtAuthenticationFilter validates
  │◄── [ rooms... ] ─────────────┤
  │                              │
  ├─── WS CONNECT ──────────────►│
  │    Authorization: Bearer eyJ │  ← WebSocketAuthInterceptor validates
  │◄── CONNECTED ────────────────┤
```

---

## Key Design Decisions

**UUID primary keys** — Avoids sequential ID enumeration attacks. Better for distributed systems.

**STOMP over raw WebSockets** — Provides pub/sub routing out of the box. Scales to a message broker (RabbitMQ/Redis) with minimal config changes.

**Separate WebSocket interceptor** — HTTP filters (`OncePerRequestFilter`) don't run on WebSocket frames. STOMP connections need their own auth interceptor on the inbound channel.

**ConcurrentHashMap for presence** — Thread-safe session counting handles multiple tabs per user correctly. One user online across 3 tabs only broadcasts one "online" event.

**`@PrePersist` for timestamps** — Timestamps are set by the server, not the client. Prevents clock skew issues.

---

## 🗺️ Coming Next

- [ ] Redis Pub/Sub for distributed WebSocket support (multi-instance)
- [ ] Refresh token rotation
- [ ] Room roles (admin / member)
- [ ] Message search (PostgreSQL full-text)
- [ ] File uploads (S3/MinIO)
- [ ] Next.js frontend

---