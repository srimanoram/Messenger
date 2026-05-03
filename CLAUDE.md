# CLAUDE.md — Messenger Project

This file governs how Claude Code should behave in this project.

## Project

A WhatsApp-style real-time messaging system built with Spring Boot 4.0.3 and Java 25.
Goal: Master System Design, Microservices, and Spring concepts through live implementation.

**Stack:**
- Spring Boot 4.0.3 / Java 25
- PostgreSQL (will migrate to Cassandra later to learn the difference)
- Spring Security + JWT (same pattern as prior Trimmer project)
- WebSocket for real-time messaging
- Kafka for message fan-out at scale
- Lombok, Validation

App runs on `http://localhost:8080` by default.

**Java 25 required.** Terminal default `java` points to Java 8 — always use IntelliJ or set `JAVA_HOME` to Java 25 SDK before running Maven commands.

## Commands

```bash
./mvnw clean package       # build
./mvnw spring-boot:run     # run the app
./mvnw test                # run all tests
```

---

## Behaviour Rules

### 1. Never change project files
Do NOT modify any source or config files in this project.
The user is learning — only explain what the problem is and how to fix it. Let the user make all changes themselves.

**Exceptions (do without asking):**
- `TODO.txt` — edit freely (bookkeeping, not learning material)
- Git commits — write proper commit messages, commit, and push when asked
- Starting services (Kafka, Nginx, MongoDB) — run directly

### 2. Let user debug their own code
When the user asks to check their code:
1. First: say "I see some issues" — do NOT reveal what they are
2. If user can't find it: give test cases that will expose the bug
3. If still stuck: then explain the actual issue

Resist pointing out bugs immediately. Guide progressively: hint → test case → answer.

### 3. Always explain WHY
When identifying any issue (error, bug, code style, naming), always include:
1. What the issue is
2. Why it happened
3. A teaching note so the user learns the concept

This applies to ALL issues — not just errors but also style, naming, missing things. Every issue needs a "why" explanation. User is learning Spring Boot/Java and wants to understand root causes.

### 4. Git commits — no AI attribution
Never add "Co-Authored-By: Claude..." or any AI attribution lines to git commit messages. Keep messages clean.

### 5. Git config — always local
Always use `git config --local`, never `git config --global`.
User has multiple projects with different git identities — global config would overwrite them.

---

## Service Start Commands

### Start Kafka (when user says "fix kafka" or Kafka needs starting)
Run without asking:
```powershell
Start-Process -FilePath "cmd.exe" -ArgumentList "/c set JAVA_HOME=C:\Users\mano-13607\.jdks\openjdk-25.0.2&& set KAFKA_LOG4J_OPTS=-Dlog4j2.configurationFile=file:E:\Kafka\kafka_2.13-4.2.0\config\log4j2.yaml&& E:\Kafka\kafka_2.13-4.2.0\bin\windows\kafka-server-start.bat E:\Kafka\kafka_2.13-4.2.0\config\server.properties > C:\tmp\kafka-out.log 2>&1" -WindowStyle Normal
```
Then verify: `netstat -ano | findstr :9092`

### Start Nginx (when user says "fix nginx" or Nginx needs starting)
Run without asking:
```powershell
Start-Process -FilePath "E:\nginx-1.30.0\nginx.exe" -WorkingDirectory "E:\nginx-1.30.0" -WindowStyle Hidden
```
Then verify: `netstat -ano | findstr " :80 "`

### Start MongoDB
```bat
start "" "E:\MongoDB\bin\mongod.exe" --dbpath "E:\MongoDB\data\db" --logpath "E:\MongoDB\logs\mongod.log" --port 27017
```

---

## Architecture (to be built out)

Package root: `com.project1.messenger`

- **Controllers** — HTTP + WebSocket endpoints
- **Services** — business logic
- **Repositories** — JPA data access
- **Models/Entities** — JPA entities
- **Security** — JWT filter, SecurityConfig (stateless, no sessions)
- **Config** — Kafka, WebSocket configuration

---

## TODO Reference

Building in this order (from TODO.txt in Trimmer project):
1. User registration + JWT auth
2. WebSocket for real-time messaging
3. Message persistence (PostgreSQL → Cassandra)
4. Message status — SENT, DELIVERED, READ
5. Offline message queue
6. Group chat
7. Kafka for message fan-out
8. Media upload
