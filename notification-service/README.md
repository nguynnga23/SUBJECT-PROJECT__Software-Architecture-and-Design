## Notification System for Book Borrowing and Management

This system provides a set of use cases for managing notifications related to book borrowing, including overdue reminders, borrowing requests, new book additions, and more. The system uses Kafka as a messaging platform to facilitate communication between multiple services: **Borrowing Service**, **User Service**, **Notification Service**, and **Inventory Service**.

### Overview

The following use cases are supported by the system:
- **Gửi thông báo quá hạn trả sách**: Sends notifications when books are overdue.
- **Gửi thông báo nhắc nhở khi sách quá hạn trả**: Sends reminder notifications when a book is overdue.
- **Gửi thông báo mượn sách**: Sends notifications when a borrowing request is approved or rejected.
- **Gửi thông báo sách mới**: Sends notifications when a new book is added to the system.
- **Xem danh sách thông báo**: Allows users to view their notifications.
- **Đánh dấu thông báo đã đọc**: Allows users to mark notifications as read.
- **Gửi email nhắc nhở hạn trả sách**: Automatically sends email reminders when a book is nearing its return deadline.

Each use case communicates through Kafka messages and is processed by various services in the system.

---

### System Workflow

The workflow involves the following services and Kafka topics for each use case:

#### 1. **Gửi thông báo quá hạn trả sách (Overdue Book Notifications)**

- **Workflow**:
  1. **Borrowing Service** detects overdue books based on the borrowing dates.
  2. It sends an **overdue notification event** to the Kafka topic `overdue-notification`.
  3. **Notification Service** listens to the `overdue-notification` topic and triggers an action to send the notification to the user.
  4. The user receives a notification about the overdue book.

- **Kafka Topics**:
  - `overdue-notification`

---

#### 2. **Gửi thông báo nhắc nhở khi sách quá hạn trả (Overdue Reminder Notifications)**

- **Workflow**:
  1. **Borrowing Service** identifies books that are close to the return deadline or are overdue.
  2. A **reminder notification event** is sent to the Kafka topic `overdue-reminder`.
  3. **Notification Service** processes the event and sends a reminder to the user, either via push notification or email.

- **Kafka Topics**:
  - `overdue-reminder`

---

#### 3. **Gửi thông báo mượn sách (Book Borrowing Notifications)**

- **Workflow**:
  1. **User Service** handles borrowing requests made by users.
  2. Upon approval or rejection of the borrowing request, **User Service** publishes a **borrow request status event** to the Kafka topic `borrow-request-status`.
  3. **Notification Service** listens to the topic and sends notifications to the user informing them about the approval or rejection of their borrowing request.

- **Kafka Topics**:
  - `borrow-request-status`

---

#### 4. **Gửi thông báo sách mới (New Book Notifications)**

- **Workflow**:
  1. **Inventory Service** manages the addition of new books into the system.
  2. When a new book is added, **Inventory Service** sends a **new book notification event** to the Kafka topic `new-book-notification`.
  3. **Notification Service** listens for new books and sends notifications to all users about the availability of the new books in the system.

- **Kafka Topics**:
  - `new-book-notification`

---

#### 5. **Xem danh sách thông báo (View Notifications)**

- **Workflow**:
  1. **User Service** stores the notifications for users in a database.
  2. Users can request to view their notifications through a query to **User Service**.
  3. **User Service** fetches the notifications from the database and returns them to the user.

- **Kafka Topics**:
  - No Kafka topic involved here since it’s a simple query from the user.

---

#### 6. **Đánh dấu thông báo đã đọc (Mark Notification as Read)**

- **Workflow**:
  1. **User Service** listens for the user’s action to mark a notification as read.
  2. The user sends a request to **User Service** to update the notification status.
  3. **User Service** marks the notification as read in the database.

- **Kafka Topics**:
  - No Kafka topic involved here since it’s a direct update in the database.

---

#### 7. **Gửi email nhắc nhở hạn trả sách (Send Email Overdue Reminder)**

- **Workflow**:
  1. **Borrowing Service** identifies books that are overdue and triggers an email reminder to the user.
  2. **Borrowing Service** sends an **email overdue reminder event** to the Kafka topic `email-overdue-reminder`.
  3. **Notification Service** listens to the `email-overdue-reminder` topic and uses an email service to send an overdue reminder email to the user.

- **Kafka Topics**:
  - `email-overdue-reminder`

---

### Services & Responsibilities

#### **Borrowing Service**
- Manages borrowing transactions.
- Detects overdue books and sends messages about overdue status and email reminders.

#### **User Service**
- Handles user data, including notification preferences and viewing notifications.
- Updates notification statuses when marked as read.

#### **Notification Service**
- Listens to various Kafka topics (e.g., overdue, borrow request status, new books) and sends corresponding notifications to users.
- Sends email notifications when required.

#### **Inventory Service**
- Manages the inventory of books.
- Publishes events about new book additions.

---

### Kafka Topics & Consumers

| **Kafka Topic**             | **Producer**         | **Consumer**           | **Event Type**            |
|----------------------------|----------------------|------------------------|---------------------------|
| `overdue-notification`      | Borrowing Service    | Notification Service   | Overdue Notification      |
| `overdue-reminder`          | Borrowing Service    | Notification Service   | Overdue Reminder          |
| `borrow-request-status`    | User Service         | Notification Service   | Borrowing Request Status  |
| `new-book-notification`     | Inventory Service    | Notification Service   | New Book Notification     |
| `email-overdue-reminder`   | Borrowing Service    | Notification Service   | Email Overdue Reminder    |

---

### Conclusion

This system integrates multiple services to manage the notification process for users borrowing books. Kafka ensures smooth communication and real-time updates between services, making it easy to handle the various notification use cases effectively.

