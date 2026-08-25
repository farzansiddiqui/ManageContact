# English Speaking Practice App Backend

This project is a Spring Boot backend for an English speaking practice application. It provides REST APIs for user registration and login, stores user records in an H2 in-memory database, and hashes passwords using BCrypt before saving them.

## Tech Stack

- Java 21
- Spring Boot 3.3.x
- Maven
- Spring Web
- Spring Data JPA
- H2 Database
- Spring Security Crypto (BCrypt)
- Jakarta Validation

## Project Structure

```text
src/
  main/
    java/
      com/englishspeaking/app/
        config/
          PasswordConfig.java
          WebConfig.java
        controller/
          AuthController.java
        dto/
          request/
            LoginRequest.java
            SignupRequest.java
          response/
            ApiResponse.java
            ErrorResponse.java
            UserResponse.java
        entity/
          User.java
        exception/
          DuplicateEmailException.java
          GlobalExceptionHandler.java
          InvalidCredentialsException.java
        repository/
          UserRepository.java
        service/
          AuthService.java
        EnglishSpeakingAppApplication.java
    resources/
      application.properties
  test/
    java/
      com/englishspeaking/app/
        controller/
          AuthControllerIntegrationTest.java
        service/
          AuthServiceTest.java
```

## How to Run

1. Open a terminal in the project root.
2. Run:

```bash
mvn clean install
mvn spring-boot:run
```

3. The app starts on:

```text
http://localhost:8080
```

## H2 Console

H2 is configured with an in-memory database:

- JDBC URL: jdbc:h2:mem:englishapp
- Username: sa
- Password: empty
- Console URL: http://localhost:8080/h2-console

Open the console and connect using the values above.

## API Endpoints

### Signup

- POST /api/auth/signup

Request body:

```json
{
  "name": "Faraz Rahman",
  "email": "faraz@gmail.com",
  "password": "Password@123"
}
```

Success response (201):

```json
{
  "success": true,
  "message": "User registered successfully",
  "user": {
    "id": 1,
    "name": "Faraz Rahman",
    "email": "faraz@gmail.com"
  }
}
```

Duplicate email response (409):

```json
{
  "success": false,
  "message": "Email already registered",
  "timestamp": "2026-08-23T10:00:00Z",
  "errors": {}
}
```

### Login

- POST /api/auth/login

Request body:

```json
{
  "email": "faraz@gmail.com",
  "password": "Password@123"
}
```

Success response (200):

```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "name": "Faraz Rahman",
    "email": "faraz@gmail.com"
  }
}
```

Invalid login response (401):

```json
{
  "success": false,
  "message": "Invalid email or password",
  "timestamp": "2026-08-23T10:00:00Z",
  "errors": {}
}
```

Validation errors use the same error envelope and include field-specific messages:

```json
{
  "success": false,
  "message": "Validation failed",
  "timestamp": "2026-08-23T10:00:00Z",
  "errors": {
    "email": "Email should be valid",
    "password": "Password must be between 6 and 72 characters"
  }
}
```

Email values are trimmed and normalized to lowercase before lookup and storage. The database unique constraint remains the final protection against concurrent duplicate registrations.

## Password Hashing

Passwords are never stored in plain text. The application uses Spring Security's BCrypt password encoder:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

During signup, the input password is encoded and saved. During login, the entered password is compared with the stored hashed value using `matches()`. This keeps the backend secure and simple.

## Postman Testing Instructions

1. Start the app with Maven.
2. Open Postman.
3. Create a new POST request.
4. Use the URL:

```text
http://localhost:8080/api/auth/signup
```

5. Set body type to JSON.
6. Use the signup payload shown above.
7. Send the request and verify the response status is 201.
8. Repeat with login using:

```text
http://localhost:8080/api/auth/login
```

9. Try an incorrect password to verify the 401 response.

## Notes

- The app uses H2 in-memory storage, so data resets when the application restarts.
- This project intentionally keeps the architecture simple and interview-friendly.
- No JWT is used in this version.
- Duplicate email registration returns HTTP 409 Conflict.
