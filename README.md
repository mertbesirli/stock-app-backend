## Midas-Style FinTech Backend (Saga Pattern)
<a name="readme-top"></a>

[![MIT License][license-shield]][license-url]
[![Java Platform](https://img.shields.io/badge/platform-Java-blue.svg)](https://docs.oracle.com/en/java/)
[![REST Architecture](https://img.shields.io/badge/architecture-REST-5DADE2.svg)](http://www.vogella.com/tutorials/REST/article.html)
[![Spring Boot Framework](https://img.shields.io/badge/framework-Spring%20Boot-brightgreen.svg)](https://projects.spring.io/spring-boot/)

A robust, event-driven microservices architecture designed for a high-concurrency financial trading platform. This project demonstrates advanced distributed system concepts, specifically addressing data consistency across multiple services using the Saga Pattern.


## 🏗 System Architecture
The system is built on a resilient, decoupled architecture where services communicate asynchronously via Apache Kafka.

wallet-service (Orchestrator): Manages user balances, implements pessimistic locking, and initiates distributed transactions.

stock-service: Simulates order matching logic and market operations.

notification-service: Handles event-driven updates for users.

midas-common: A shared library containing cross-service DTOs and event contracts.

## 🚀 Key Technical Features
#### 1. Saga Pattern & Rollback Mechanism
   To ensure ACID compliance in a distributed environment, we utilize the Saga orchestration pattern.

Commit Phase: When an order is placed, funds are moved to a locked_balance state.

Compensating Transaction (Rollback): If the stock-service fails to process the order, the orchestrator triggers a rollback, reverting the funds from locked_balance back to the available balance.

#### 2. Pessimistic Locking
   To prevent race conditions during concurrent requests (e.g., rapid buy/sell orders), we implement database-level pessimistic locking via Hibernate, ensuring data integrity for sensitive financial balances.

#### 3. Event-Driven Communication
   Broker: Apache Kafka

Consistency: Eventual consistency achieved through asynchronous messaging.

Resilience: Decoupled services allow for independent scaling and fault tolerance.

## 🛠 Tech Stack
Java 17+

Spring Boot 3.x

PostgreSQL (with Pessimistic Locking)

Apache Kafka (Event streaming)

Hibernate/JPA

Docker (Containerized infrastructure)

## ⚙️ Quick Start
Start Infrastructure:
Ensure Docker is running and start your Kafka and PostgreSQL containers.

Bash
docker-compose up -d
Run Services:
Build and start each service in the order: midas-common (install) -> wallet-service -> stock-service.

Test the Saga Flow:

Success Scenario:
POST /api/order with symbol AAPL.

Rollback Scenario (Simulated Failure):
POST /api/order with symbol FAIL.

## 📈 Roadmap (Next Steps)
[ ] Market Data Integration: Real-time price streaming via WebSockets.

[ ] API Gateway: Centralized authentication (JWT) and routing.

[ ] Frontend Integration: React-based Trading Dashboard.

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.md` for more information


[license-shield]: https://img.shields.io/badge/license-MIT%20License-green.svg
[license-url]: [https://github.com/mertbesirli/ticket-app/blob/main/LICENSE](https://github.com/mertbesirli/kafka-app/blob/main/LICENSE)