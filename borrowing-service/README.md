# Borrowing Service API Documentation

## Table of Contents
1. [ReaderRequestController](#readerrequestcontroller)
2. [ReaderRequestService](#readerrequestservice)
3. [Authentication and Authorization](#authentication-and-authorization)

---

## ReaderRequestController

### Base URL
`/api/v1/borrowing-service/borrow-requests`

### Endpoints

#### 1. Create Borrow Request
- **URL**: `/`
- **Method**: `POST`
- **Description**: Creates a new borrow request.
- **Authentication**: Requires a valid `X-User-Id` header from the API Gateway.
- **Request Body**:
  ```json
  {
    "readerId": "UUID",
    "borrowingPeriod": "Integer",
    "bookCopyIds": ["UUID"]
  }
  ```
- **Response**:
  - **201 Created**: Borrow request successfully created.
  - **400 Bad Request**: Invalid input data.

#### 2. Get Borrow Request by ID
- **URL**: `/{id}`
- **Method**: `GET`
- **Description**: Retrieves a borrow request by its ID.
- **Authentication**: Requires a valid `X-User-Id` header from the API Gateway.
- **Response**:
  - **200 OK**: Borrow request details.
  - **404 Not Found**: Borrow request not found.

#### 3. Update Borrow Request Status
- **URL**: `/{requestId}/status`
- **Method**: `PUT`
- **Description**: Updates the status of a borrow request.
- **Authentication**: Requires a valid `X-User-Id` and `X-User-Role` header. The `X-User-Role` must include `ADMIN`.
- **Request Body**:
  ```json
  {
    "status": "BorrowStatus"
  }
  ```
- **Response**:
  - **200 OK**: Status successfully updated.
  - **400 Bad Request**: Invalid status or input data.
  - **404 Not Found**: Borrow request not found.

#### 4. Update Borrow Date
- **URL**: `/{requestId}/borrow`
- **Method**: `PUT`
- **Description**: Updates the borrow date of a request.
- **Authentication**: Requires a valid `X-User-Id` and `X-User-Role` header. The `X-User-Role` must include `ADMIN`.
- **Response**:
  - **200 OK**: Borrow date successfully updated.
  - **404 Not Found**: Borrow request not found.

#### 5. Update Return Date
- **URL**: `/{requestId}/return`
- **Method**: `PUT`
- **Description**: Updates the return date of a request.
- **Authentication**: Requires a valid `X-User-Id` and `X-User-Role` header. The `X-User-Role` must include `ADMIN`.
- **Response**:
  - **200 OK**: Return date successfully updated.
  - **404 Not Found**: Borrow request not found.

#### 6. Update Penalty Fee
- **URL**: `/{requestId}/penalty`
- **Method**: `PUT`
- **Description**: Updates the penalty fee for an overdue borrow request.
- **Authentication**: Requires a valid `X-User-Id` and `X-User-Role` header. The `X-User-Role` must include `ADMIN`.
- **Request Body**:
  ```json
  {
    "penaltyFee": "Double"
  }
  ```
- **Response**:
  - **200 OK**: Penalty fee successfully updated.
  - **400 Bad Request**: Request is not overdue.
  - **404 Not Found**: Borrow request not found.

#### 7. Get Borrow History
- **URL**: `/users/borrow-history`
- **Method**: `GET`
- **Description**: Retrieves the borrow history of the current user.
- **Authentication**: Requires a valid `X-User-Id` header from the API Gateway.
- **Response**:
  - **200 OK**: List of borrow requests.

---

## ReaderRequestService

### Description
The `ReaderRequestService` handles the business logic for managing borrow requests, including creating, retrieving, updating, and deleting requests. It also supports updating borrow dates, return dates, penalties, and retrieving user borrow history.

---

## Authentication and Authorization

### How `userId` is Retrieved
- The `userId` is extracted from the `X-User-Id` header, which is added by the API Gateway after validating the user's token.
- If the `X-User-Id` header is missing, the request will fail with an error indicating that the user is not authenticated.

### How `role` is Checked
- The `X-User-Role` header, also added by the API Gateway, contains the user's roles (e.g., `ADMIN`, `USER`).
- For APIs requiring admin privileges, the `@RequireAdmin` annotation is used. This triggers an aspect (`AdminPermissionAspect`) that checks:
  1. If the `X-User-Role` header contains `ADMIN`.
  2. If the header is missing or does not contain `ADMIN`, the request is rejected with an error.

### Example Workflow
1. **API Gateway**:
   - Validates the user's token.
   - Extracts `userId` and `role` from the token.
   - Adds `X-User-Id` and `X-User-Role` headers to the request.
2. **Borrowing Service**:
   - Reads `X-User-Id` and `X-User-Role` headers.
   - Ensures the user has the necessary permissions before processing the request.