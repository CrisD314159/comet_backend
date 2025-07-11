# Comet - Social App

<p align="center">
  <img alt="Comet" src="https://res.cloudinary.com/dw43hgf5p/image/upload/v1750709766/AF3173AB-74B3-42F6-95B3-E77602CBA371_ob2zlc.png" width="300">
</p>

Comet is a portfolio social application built with Java, Spring Boot, and PostgreSQL. It provides a modern backend for user management, authentication (including Google OAuth2), friendships, chat integration, and email notifications.

## 🌐 Public Deployment

- **Frontend:** [https://comet-woad.vercel.app/](https://comet-woad.vercel.app/)

## Features

- **User Registration & Verification**
    - Sign up with email and password.
    - Email verification with code.
    - Google OAuth2 login and registration.

- **Authentication & Security**
    - JWT-based authentication and refresh tokens.
    - Secure password hashing (BCrypt).
    - Session management for refresh tokens.
    - Custom authentication handlers for OAuth2.

- **User Profile Management**
    - Update profile information (name, bio, country, profile picture).
    - Delete (soft-delete) user accounts.
    - Search users by name.

- **Friendship System**
    - Send, accept, and reject friend requests.
    - Block and unblock friends.
    - Delete friends.
    - View friends and blocked friends lists.

- **Chat Integration**
    - Integration with [GetStream Chat](https://getstream.io/chat/) for real-time messaging.
    - Generate chat tokens for authenticated users.

- **Password Recovery**
    - Request password reset via email.
    - Secure password change with verification code.

- **Email Notifications**
    - Templated emails for account verification and password recovery.
    - Configurable SMTP settings.

## Main Services

- **UserService**: Handles user registration, profile updates, verification, and search.
- **AccountService**: Manages authentication, sessions, password changes, and recovery.
- **FriendshipService**: Manages friend requests, friendships, blocking, and related queries.
- **ChatService**: Integrates with GetStream for chat user management and token generation.
- **EmailService**: Sends templated emails for verification and recovery.
- **ThirdPartyAccountService**: Handles Google OAuth2 login and user creation.

## Technologies Used

- Java 17+
- Spring Boot
- Spring Security (JWT, OAuth2)
- Spring Data JPA (Hibernate)
- PostgreSQL
- Flyway (database migrations)
- GetStream Chat API
- JavaMailSender (email)
- MapStruct (DTO mapping)
- Lombok

## Getting Started

1. **Clone the repository**
2. **Configure environment variables** in `application.properties` (see placeholders for DB, JWT, mail, OAuth, etc).
3. **Run database migrations** (Flyway runs automatically on startup).
4. **Start the application**
   ```bash
   ./mvnw spring-boot:run
   ```
5. **API Endpoints**: See `controllers` package for available endpoints.

## Folder Structure

- `src/main/java/com/crisd/comet/`
    - `controllers/` - REST API controllers
    - `services/` - Business logic and service interfaces/implementations
    - `model/` - JPA entities
    - `repositories/` - Spring Data repositories
    - `dto/` - Data transfer objects (input/output)
    - `security/` - Security configuration and JWT utilities
    - `exceptionHandling/` - Custom exceptions and global error handling
    - `mappers/` - MapStruct mappers for DTO/entity conversion

- `src/main/resources/templates/` - Email HTML templates
- `src/main/resources/db/migration/` - Flyway SQL migrations

## License

This project is for portfolio and demonstration purposes.

---
Made with ❤️ by Cristian David Vargas Loaiza

---

## 👤 About the Creator

Created by **Cristian David Vargas Loaiza**  
[LinkedIn](https://www.linkedin.com/in/cristian-david-vargas-loaiza-982314271) | [GitHub](https://github.com/CrisD314159) | [Portfolio](https://crisdev-pi.vercel.app)

