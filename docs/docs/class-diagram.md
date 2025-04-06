---
title: Class Diagram
---

```mermaid
classDiagram
    %% ====== USER SERVICE ====== %%
    class User {
        +id: UUID
        +username: String
        +fullName: String
        +gender: Enum
        +phoneNumber: String
        +email: String
        +password: String
        +role: Enum
        +isActive: Boolean
    }

    %% ====== BOOK SERVICE ====== %%
    class Book {
        +id: UUID
        +bookCode: String
        +title: String
        +topic: String
        +description: Text
        +note: Text
        +categoryId: UUID
        +yearPublished: Int
        +publisher: String
    }

    class Category {
        +id: UUID
        +name: String
    }

    class Author {
        +id: UUID
        +name: String
    }

    class BookAuthor {
        +bookId: UUID
        +authorId: UUID
    }

    %% ====== BORROWING SERVICE ====== %%
    class ReaderRequest {
        +id: UUID
        +readerId: UUID
        +librarianId: UUID
        +status: Enum
        +dateBorrowed: Date
        +dateReturned: Date
        +returnDate: Date
        +penaltyFee: Double
        +createdAt: DateTime
        +updatedAt: DateTime
        +borrowingPeriod: Int
    }

    class ReaderRequestDetail {
        +readerRequestId: UUID
        +bookCopyId: UUID
    }

    %% ====== INVENTORY SERVICE ====== %%
    class BookCopy {
        +id: UUID
        +bookId: UUID
        +inventoryId: UUID
        +copyCode: String
        +location: String
        +status: Enum
    }

    class Inventory {
        +id: UUID
        +bookId: UUID
        +totalQuantity: Int
        +available: Int
        +borrowed: Int
        +lost: Int
        +damaged: Int
    }

    %% ====== NOTIFICATION SERVICE ====== %%
 class NotificationEvent {
        +eventId: UUID
        +eventType: Enum
        +eventData: JSON
        +createdAt: DateTime
    }

    class Notification {
        +id: UUID
        +userId: UUID
        +message: Text
        +type: Enum
        +isRead: Boolean
        +createdAt: DateTime
    }

    class NotificationTemplate {
        +id: UUID
        +type: Enum
        +name: String
        +content: Text
    }

   %% ====== RELATIONSHIPS ====== %%
    User "1" --> "many" ReaderRequest : makes
    User "1" --> "many" ReaderRequest : approves
    ReaderRequest "1" --> "many" ReaderRequestDetail : contains
    ReaderRequestDetail "many" --> "1" BookCopy : includes
    Book "1" <-- "many" BookCopy : consists of
    Book "1" <-- "many" BookAuthor : written by
    BookAuthor "many" --> "1" Author : has
    Book "many" <-- "1" Category : belongs to

    Book "1" --> "1" Inventory : tracked in
    BookCopy "1" --> "1" Inventory : counted in
    Inventory "1" --> "many" BookCopy : manages

    Notification "many" --> "1" User : sent to
    NotificationEvent "many" --> "1" Notification : triggers
    NotificationTemplate "1" --> "many" Notification : uses

```


**1. User Service:** Quản lý thông tin người dùng.

- Bảng: User
- Cơ sở dữ liệu: user_service_db

**2. Book Service:** Quản lý thông tin sách, thể loại và tác giả.

- Bảng: Book, Category, Author, BookAuthor
- Cơ sở dữ liệu: book_service_db

**3. Borrowing Service:** Quản lý các yêu cầu mượn sách của độc giả.

- Bảng: ReaderRequest, ReaderRequestDetail
- Cơ sở dữ liệu: borrowing_service_db

**4. Inventory Service:** Quản lý thông tin về các bản sao sách và tình trạng kho.

- Bảng: BookCopy, Inventory
- Cơ sở dữ liệu: inventory_service_db

**5. Notification Service:** Quản lý các sự kiện và thông báo gửi đến người dùng.

- Bảng: NotificationEvent, Notification, NotificationTemplate
- Cơ sở dữ liệu: notification_service_db
