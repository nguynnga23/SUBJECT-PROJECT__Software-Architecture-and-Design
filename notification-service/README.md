# Notification Service

## Overview
The Notification Service is responsible for managing and delivering notifications to users. It listens to events from other services (e.g., Borrowing Service) via Kafka and stores notifications in a MongoDB database.

## Features
- **Kafka Integration**: Listens to messages from the `borrowing-topic` Kafka topic.
- **Notification Management**: Stores notifications in MongoDB and provides APIs to retrieve and manage them.
- **Eureka Client**: Registers with the Discovery Service for service discovery.

## Technologies
- **Spring Boot**: Backend framework.
- **Spring Kafka**: Kafka integration.
- **MongoDB**: Database for storing notifications.
- **Eureka Client**: Service discovery.

## Prerequisites
- **Java**: JDK 17 or higher.
- **MongoDB**: Running instance for storing notifications.
- **Kafka**: Running instance for message communication.
- **Eureka Server**: For service registration and discovery.

## Configuration
The service configuration is located in `src/main/resources/application.properties`:
```properties
spring.application.name=notification-service
server.port=8085
spring.data.mongodb.uri=mongodb://mongodb:root@localhost:27017/notification-service?authSource=admin
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.topic.notification=notification-topic
```

## Endpoints
### 1. Get Notifications for a User
- **URL**: `/api/notifications/{userId}`
- **Method**: `GET`
- **Description**: Retrieves all notifications for a specific user.
- **Response**: List of notifications.

### 2. Create a Notification
- **URL**: `/api/notifications/`
- **Method**: `POST`
- **Description**: Creates a new notification.
- **Request Body**:
  ```json
  {
    "userId": "UUID",
    "title": "String",
    "message": "String",
    "type": "NotificationType",
    "metadata": { "key": "value" }
  }
  ```
- **Response**: Created notification.

## Kafka Integration
### Consumer
- **Topic**: `borrowing-topic`
- **Group ID**: `notification-group`
- **Description**: Listens to messages from the Borrowing Service and processes them.

### Producer
- **Topic**: `notification-topic`
- **Description**: Sends notifications to Kafka for further processing.

## Running the Service
### 1. Start MongoDB
Ensure MongoDB is running locally or update the connection string in `application.properties`.

### 2. Start Kafka
Ensure Kafka is running locally and the `borrowing-topic` exists.

### 3. Run the Service
```bash
./gradlew bootRun
```

### 4. Verify
- **Eureka Dashboard**: Check if the service is registered at `http://localhost:8761`.
- **API Test**: Use tools like Postman or `curl` to test the endpoints.

## Testing
Run the tests using:
```bash
./gradlew test
```

## Future Enhancements
- Add email and SMS notification support.
- Implement notification scheduling.
- Add user preferences for notification delivery.

