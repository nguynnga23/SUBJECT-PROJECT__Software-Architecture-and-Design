# Notification Service

## Overview
The Notification Service is responsible for sending notifications to users through various channels (e.g., email, SMS, push notifications). It leverages Apache Kafka for message queuing and ensures scalability and reliability in delivering notifications.

## Features
- Publish and consume messages using Kafka.
- Support for multiple notification channels.
- Scalable and fault-tolerant architecture.

## Architecture
1. **Producer**: Sends notification requests to a Kafka topic.
2. **Kafka Broker**: Acts as the message queue for the notification requests.
3. **Consumer**: Listens to the Kafka topic and processes the messages.
4. **Notification Handlers**: Handle specific notification channels (e.g., email, SMS).

## Technologies Used
- **Apache Kafka**: For message queuing and streaming.
- **Spring Boot**: Framework for building the backend service.
- **Gradle**: Build tool for managing dependencies and building the project.
- **MongoDB**: Database for storing notification logs and metadata.
- **Docker**: Containerization for deployment.

## Prerequisites
- Docker and Docker Compose installed.
- Kafka and Zookeeper setup (can be done using Docker Compose).
- Java 17 or higher installed.
- Gradle installed.

## Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd notification-service
   ```

2. Build the project using Gradle:
   ```bash
   ./gradlew build
   ```

3. Start Kafka and Zookeeper using Docker Compose:
   ```bash
   docker-compose up -d
   ```

4. Configure environment variables in `application.properties` or `application.yml`:
   ```properties
   kafka.bootstrap-servers=localhost:9092
   spring.data.mongodb.uri=mongodb://localhost:27017/notification-service
   ```

5. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Usage
- Send a notification request by making a POST request to the API endpoint:
  ```
  POST /api/notifications
  {
    "type": "email",
    "recipient": "user@example.com",
    "message": "Your notification message here."
  }
  ```

- Monitor logs to verify message processing.

## Contributing
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request with a detailed description.

## License
This project is licensed under the MIT License.
