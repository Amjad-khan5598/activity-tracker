# Daily Activity Tracker 🚀

<p align="center">
  <img src="https://img.shields.io/badge/Java-24-orange?style=for-the-badge&logo=openjdk" alt="Java 24">
  <img src="https://img.shields.io/badge/Spring_Boot-4.x-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot 4.x">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" alt="MySQL 8.0">
  <img src="https://img.shields.io/badge/Security-JWT_%26_BCrypt-red?style=for-the-badge&logo=jsonwebtokens" alt="JWT and BCrypt">
  <img src="https://img.shields.io/badge/AI-Google_Gemini-purple?style=for-the-badge&logo=google" alt="Google Gemini">
</p>

Daily Activity Tracker is a personal productivity application backend designed to help users record daily tasks, track completion progress, view daily motivational quotes, and receive AI-generated productivity coaching.

The project demonstrates layered backend development with Spring Boot, secure authentication, input validation, database persistence, and REST API design.

---

## 🏛️ Core Architectural Highlights

* **Server-Side AI Integration:** The Spring Boot backend calls the Google Gemini API, builds prompts from the user's task history and progress, and deserializes the response into a structured DTO. The API key stays on the server rather than being exposed in the frontend.
* **Stateless Security:** Uses JSON Web Tokens (JWT) for authentication and BCrypt password hashing. Protected endpoints use the authenticated user's identity when accessing personal task data.
* **Centralized Exception Handling:** Uses `@ControllerAdvice` to handle application exceptions and return consistent JSON error responses for supported cases.
* **Input Validation:** Uses Jakarta Bean Validation annotations such as `@Valid`, `@NotBlank`, and `@Email` to validate incoming request data.

---

## 🚀 Features

### Implemented
* **User Authentication:** Signup and login with BCrypt password hashing, JWT authentication, and duplicate email handling (`409 Conflict`).
* **Daily Task Management:** CRUD operations for tasks associated with dates and completion statuses, with ownership checks to prevent users from accessing other users' tasks.
* **Progress Analytics:** Calculates total tasks, completion rates, current streaks, and daily metrics.
* **Daily Quote Engine:** Startup logic and scheduled jobs maintain a daily quote shared across users.
* **AI Productivity Coach:** Integrates Google Gemini to analyze task history and return structured feedback (`summary`, `feedback`, `suggestion`, `motivation`).

### Planned / In Progress
* Dedicated AI error handling for rate limits, timeouts, and malformed upstream responses.
* Swagger UI / OpenAPI documentation.
* Automated unit and integration tests.
* React frontend integration and cloud deployment.

---

## 🛠️ Technology Stack

* **Language:** Java (confirm the project version in `pom.xml`)
* **Framework:** Spring Boot (confirm the project version in `pom.xml`), Spring Web
* **Security:** Spring Security, JWT, BCrypt
* **Database & Persistence:** MySQL, Spring Data JPA / Hibernate
* **Validation & Processing:** Jakarta Bean Validation, Jackson
* **AI Integration:** Google GenAI Java SDK with Gemini
* **Build Tool:** Maven

---

## 📁 Project Structure

The backend uses a layered structure to separate API controllers, business logic, persistence, security, and configuration:

```text
src/main/java/com/example/activitytracker/
├── config/          # Gemini configuration, quote seeding and scheduling
├── controller/      # REST API controllers
├── DTO/             # Request and response data-transfer objects
├── exception/       # Custom exceptions and global exception handling
├── model/           # JPA entities
├── repository/      # Spring Data JPA repositories
├── security/        # JWT authentication filter and entry point
├── service/         # Service interfaces and implementations
└── util/            # JWT utilities
```

---

## 🔌 API Endpoints Summary

Base URL: `http://localhost:8080`

* **`POST /api/auth/signup`**
  * Purpose: Register a new user
  * Auth Required: No

* **`POST /api/auth/login`**
  * Purpose: Authenticate and receive a JWT
  * Auth Required: No

* **`GET /api/tasks`**
  * Purpose: List the authenticated user's tasks
  * Auth Required: Yes

* **`GET /api/tasks/{id}`**
  * Purpose: Retrieve a task by ID, subject to ownership checks
  * Auth Required: Yes

* **`POST /api/tasks`**
  * Purpose: Create a new task entry
  * Auth Required: Yes

* **`PUT /api/tasks/{id}`**
  * Purpose: Update an existing task, subject to ownership checks
  * Auth Required: Yes

* **`DELETE /api/tasks/{id}`**
  * Purpose: Delete a task, subject to ownership checks
  * Auth Required: Yes

* **`GET /api/progress`**
  * Purpose: Retrieve productivity statistics for the authenticated user
  * Auth Required: Yes

* **`GET /api/daily-quote`**
  * Purpose: Get today's stored motivational quote
  * Auth Required: Yes, unless the current security configuration explicitly permits this route

* **`GET /api/ai-coach`**
  * Purpose: Generate AI coaching feedback
  * Auth Required: Yes

---

## 🚦 HTTP Status Code Reference

The API uses standard HTTP status codes for successful requests and common errors:

* **`200 OK`** - Request succeeded
* **`201 Created`** - Resource created successfully (e.g., signup or task creation)
* **`400 Bad Request`** - Request validation failed; validation responses include `fieldErrors`
* **`401 Unauthorized`** - Authentication token is missing or invalid, or credentials are invalid
* **`403 Forbidden`** - Authenticated user lacks permission to access a resource
* **`404 Not Found`** - Requested resource was not found
* **`409 Conflict`** - Data conflict, such as duplicate email registration
* **`500 Internal Server Error`** - Unexpected server-side error; the response uses a generic message for supported exception handling

---

## ⚙️ Local Setup & Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Amjad-khan5598/activity-tracker.git
cd activity-tracker
```

### 2. Configure the Database

Create your local MySQL database:

```sql
CREATE DATABASE activitytracker;
```

### 3. Set Environment Variables

Set the required environment variables in your local terminal session:

```bat
:: Windows Command Prompt example
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_mysql_password
set JWT_SECRET=your_base64_encoded_jwt_secret
set GEMINI_API_KEY=your_gemini_api_key
```

Use your own credentials and a sufficiently strong, Base64-encoded JWT secret. Keep real credentials and API keys out of version control. The environment variables must be available to the terminal session that starts the application.

### 4. Run the Application

```bash
mvn spring-boot:run
```

---

## 👨‍💻 Author

**Amjad Khan**

* Software Engineering Student & Backend Developer
* GitHub: [@Amjad-khan5598](https://github.com/Amjad-khan5598)
