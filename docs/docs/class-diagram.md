---
title: Class Diagram
---

```mermaid
classDiagram
    %% ====== USER SERVICE ====== %%
    class User {
        +id: UUID
        +fullName: String
        +dob: Date
        +gender: Enum
        +phoneNumber: String
        +address: String
        +email: String
        +password: String
        +roleId: TinyInt
        +isActive: Boolean
    }

    class Role {
        +id: TinyInt
        +name: String
    }

    class Librarian {
        +userId: UUID
        +department: String
        +position: String
        +workplace: String
    }

    class Reader {
        +userId: UUID
        +studentId: String
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
        +userId: UUID
        +status: Enum
        +dateBorrowed: Date
        +dateReturned: Date
        +penaltyFee: Double
        +notes: Text
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
    class Notification {
        +id: UUID
        +userId: UUID
        +message: Text
        +type: Enum
        +isRead: Boolean
        +createdAt: DateTime
    }

    %% ====== RELATIONSHIPS ====== %%
    User "1" --> "1" Role : has
    User "1" <|-- Librarian
    User "1" <|-- Reader

    Reader "1" --> "many" ReaderRequest : makes
    ReaderRequest "1" --> "many" ReaderRequestDetail : contains
    ReaderRequestDetail "many" --> "1" BookCopy : includes
    Book "1" <-- "many" BookCopy : consists of
    Book "1" <-- "many" BookAuthor : written by
    BookAuthor "many" --> "1" Author : has
    Book "1" <-- "1" Category : belongs to

    Book "1" --> "1" Inventory : tracked in
    BookCopy "1" --> "1" Inventory : counted in
    Inventory "1" --> "many" BookCopy : manages

    Notification "many" --> "1" User : sent to
```


**1️⃣ User Service**

- User: Chứa thông tin người dùng.

- Role: Xác định vai trò của người dùng (Admin, Reader, Librarian,...).

- Librarian: Người quản lý thư viện (mở rộng từ User).

- Reader: Độc giả thư viện (mở rộng từ User).

**2️⃣ Book Service**

- Book: Chứa thông tin sách.

- Category: Thể loại sách.

- Author: Tác giả sách.

- BookAuthor: Liên kết giữa sách và tác giả.

**3️⃣ Borrowing Service**

- ReaderRequest: Đơn mượn/trả sách.

- ReaderRequestDetail: Danh sách các bản sao sách trong đơn mượn.

**4️⃣ Inventory Service**

- BookCopy: Quản lý từng bản sao của sách (định danh bằng copyCode).

- Inventory: Theo dõi tổng số lượng sách, số lượng sách có sẵn, bị mất hoặc hỏng.

**5️⃣ Notification Service**

- Notification: Gửi thông báo cho người dùng về các sự kiện quan trọng.

