# 📚 Digital Library Service  

A Spring Boot–based microservice for managing a digital library system. The service allows you to **add, borrow, return, and manage books and borrowers** with REST APIs, equipped with authentication, observability, and modern deployment support.  

---

## 🚀 Features  

- **Book Management** – Add, update, and fetch books with copies  
- **Borrow & Return** – Track borrowers and loan history  
- **Basic Authentication** – Secure endpoints with username/password  
- **OpenAPI/Swagger UI** – API documentation and testing  
- **Test Coverage with JaCoCo** – Enforced code quality via unit/integration tests  
- **Docker & Docker Compose** – Local containerized environment setup  
- **Kubernetes Deployment** – Production-ready manifests with Postgres, HPA, and ConfigMaps/Secrets  

---

## 🛠️ Tech Stack  

- **Java 21** + **Spring Boot 3.x**  
- **Spring Data JPA** + **PostgreSQL**  
- **Spring Security (Basic Auth)**  
- **MapStruct** (DTO mapping)  
- **JUnit 5 / Mockito** (Testing)  
- **Swagger / OpenAPI** (API Docs)  
- **Docker & Docker Compose**  
- **Kubernetes (K8s)** with HPA  

---

## 📦 Running Locally  

### 1. Clone the Repository  
```bash
git clone https://github.com/its-me-gayan/digital-library-service.git
cd digital-library-service
