# 📚 Digital Library Service

A Spring Boot–based microservice for managing a digital library system. The service allows you to **add, borrow, return, and manage books and borrowers** with REST APIs, equipped with authentication, observability, and modern deployment support.

## 🚀 Features

- **Book Management** – Add, update, and fetch books with copies
- **Borrow & Return** – Track borrowers and loan history
- **Basic Authentication** – Secure endpoints with username/password
- **OpenAPI/Swagger UI** – API documentation and testing
- **Test Coverage with JaCoCo** – Enforced code quality via unit/integration tests
- **Docker & Docker Compose** – Local containerized environment setup
- **Kubernetes Deployment** – Production-ready manifests with Postgres, HPA, and ConfigMaps/Secrets

## 🛠️ Tech Stack

- **Java 21** + **Spring Boot 3.x**
- **Spring Data JPA** + **PostgreSQL**
- **Spring Security (Basic Auth)**
- **MapStruct** (DTO mapping)
- **JUnit 5 / Mockito** (Testing)
- **Swagger / OpenAPI** (API Docs)
- **Docker & Docker Compose**
- **Kubernetes (K8s)** with HPA

## 📦 Running Locally

### 1. Clone the Repository

```bash
git clone https://github.com/its-me-gayan/digital-library-service.git
cd digital-library-service
```

### 2. Build the Project

```bash
./mvnw clean install
```

### 3. Run with Docker Compose

Bring up **service + Postgres** in containers:

```bash
docker compose up --build
```

- App will be available at: http://localhost:8181
- Swagger UI: http://localhost:8181/swagger-ui.html

## ☸️ Running on Kubernetes

### 1. Build Image Locally

```bash
docker build -t digital-library-service:latest .
```

### 2. Apply Kubernetes Manifests

```bash
kubectl apply -f k8s/
```

### 3. Components Overview

- **Postgres**
  - 1 replica, backed by **1Gi PVC**
  - Exposed via ClusterIP

- **Digital Library Service**
  - 2 replicas (initial)
  - CPU: `500m` request, `1 core` limit
  - Memory: `500Mi` request, `500Mi` limit
  - Exposed via ClusterIP on port `8181`

- **Horizontal Pod Autoscaler (HPA)**
  - Scales **1 → 10 pods** automatically
  - Trigger: **60% CPU utilization**

### 4. Access the Service

Forward service port to local machine:

```bash
kubectl port-forward svc/digital-library-service-svc 8181:8181
```

Now available at: http://localhost:8181

## ✅ Testing & Coverage

Run tests with coverage:

```bash
./mvnw test
```

View JaCoCo coverage report:

```bash
target/site/jacoco/index.html
```

## 🔐 Authentication

All APIs are secured with **Basic Authentication**.

- **Username:** `contact me for details`
- **Password:** `contact me for details` (configurable via Kubernetes Secret or `.env`)

Example cURL:

```bash
curl -u admin:secret123 http://localhost:8181/api/books
```

## 📖 API Documentation

Once the service is running, access **Swagger UI** at: 👉 http://localhost:8181/swagger-ui.html

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
