# 💳 Credit System – Credit Analysis and Granting Platform

A modern platform for credit analysis and approval, featuring traditional and social login, a microservices architecture,
asynchronous communication via RabbitMQ, PDF generation, AWS S3 integration, and much more.

**Ideal for simulating a real-world credit system with a robust, scalable, and secure architecture.**

---

# 🗂 Project Overview

### This project is composed of five microservices:

- **user-service:** Handles user registration, authentication, social login, proposal submission, and access to proposal
  history.

- **proposal-service:** Receives submitted proposals, stores them with user information and a status of **"PENDING"**, 
then forwards them for analysis.

- **credit-analysis-service:** Analyzes proposals based on the user's **CPF** and a randomly generated **score**.
  Returns the analyzed proposal with the status updated to **"APPROVED"** or **"REJECTED"**.

- **notification-service:** Sends emails regarding proposal status, user validation links, and links to download
  receipts.

- **documentation-service:** Generates PDF receipts for "**APPROVED**" proposals, uploads them to AWS S3, and sends the
  download link to the notification-service.

- **Tests:** Includes 244 unit and integration tests, with 100% code coverage.


# ✨ Features

- 🔐 User registration and authentication (traditional and social login with Google)  
  On the first Google login, only the user's email and a default password are stored in the database.  
  A temporary validation link is sent via email, allowing the user to complete missing data 
  (name, CPF, password, and income) to become a valid user.
- 📧 Email notifications for validation, proposal status, and document download (via JavaMail)
- 📄 PDF receipt generation for approved proposals
- ☁️ Integration with AWS S3 for uploading and retrieving receipts
- 🔁 Asynchronous communication between services via RabbitMQ
- 🌐 Synchronous REST communication between services via WebClient + OAuth2 (Client Credentials)
- 🧠 Automated credit analysis with random score logic
- 🧾 Account deletion request flow: users must submit a reason; administrators receive a notification and decide whether
  to approve the deletion
- ⛔ Security filters to block access from unvalidated users
- 🔄 Temporary links with expiration (10 minutes) for email validation.
- 🔄 Temporary links with expiration (1 hour) for receipt download.
- ⏱️ Rate limiting: only one proposal submission every 24 hours
- ♻️ PDF regeneration and re-sending if the link has expired
- 💥 Circuit Breaker and fallback mechanism for service resilience (using Resilience4j)
- 📑 API documentation with OpenAPI and Swagger UI
- 📊 Application monitoring with Prometheus and Grafana
- 🐳 Dockerized services and deployment via Docker Hub

# 🧑‍💻 Technologies

### 🔧 Backend & Security
- Spring Boot, Spring Security, OAuth2 (Resource & Auth Server), JWT
- Jakarta Bean Validation, MapStruct, Thymeleaf

### 📡 Communication
- RabbitMQ (Async), WebFlux + WebClient (Sync)

### 📦 Cloud & Storage
- AWS S3, JavaMailSender

### 📊 Monitoring & Documentation
- SpringDoc OpenAPI, Spring Boot Actuator, Micrometer, Prometheus, Grafana

### 🧪 Testing
- JUnit, Mockito, TestContainers, WireMock, REST Assured

### 🛠️ Resilience & Patterns
- Resilience4j (Circuit Breaker, Fallback), Design Patterns (Factory, Strategy, Builder, etc.)

# 🧱 Design Patterns

The project applies well-known design patterns to improve maintainability, testability, and scalability:

- Factory
- Strategy
- Singleton
- Builder
- Observer
- Proxy
- Adapter

# 🧪 Development Practices

- TDD (Test-Driven Development)
- BDD (Behavior-Driven Development)

# 🚀 Getting Started

To run this system locally, you need **Docker** and **MySQL Workbench**.
No manual dependency installation is required — all services run via Docker Compose.

---

### 🐳 Step 1: Run Docker Compose

Download and use this `docker-compose.yml` file:[Docker file](aInfrastructure/docker-compose.yml)

**⚠️ Before running, set the following environment variables in the file:**

#### 🔐 Social Login (Google)

- `GOOGLE_CLIENT`
- `GOOGLE_SECRET`

#### ☁️ AWS S3 Access (requires an IAM user with S3FullAccess)

- `ACCESS_KEY`
- `SECRET_KEY`

#### 📧 Email Configuration (for notifications)

- `EMAIL`
- `EMAIL_PASSWORD` → *This must be an "App Password", not your regular email password.*

Then, navigate to the folder containing the file and run:

```bash
docker-compose up 
```

### 🛠️ Step 2: Manual Setup (one-time only)

Open **MySQL Workbench** and create **two separate connections**:

- Connection 1: `localhost:3307` → **User DB**
- Connection 2: `localhost:3308` → **Proposal DB**  
  *(Username and password for both: `root`)*

Now, insert the following values manually:

#### 🔐 User DB (`Roles` table)

| Id | Name  |
|----|-------|
| 1  | ADMIN |
| 2  | USER  |

#### 🔑 Proposal DB (`Client` table)

| ClientId          | ClientSecret                                                   | Scope |
|-------------------|----------------------------------------------------------------|-------|
| userserviceclient | `$2a$10$KJz93hA5eQ9re6RIeXZwl.eqFb5au6//25IRdw19f/T9bsa5WpFR2` | ADMIN |

---

### 📬 Try it Out

Once everything is set up, open the API documentation:

[🔗 Swagger UI](http://localhost:8080/swagger-ui.html)


# 📞 Info

Murillo Marques 

[Linkedin](http://www.linkedin.com/in/murillomsantos)

[Email](mailto:murillomarques2001@gmail.com)
