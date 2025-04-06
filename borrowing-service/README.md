# Borrowing Service

### 1 Người đọc gửi yêu cầu mượn sách (POST /api/borrow-requests)
```
    http://localhost:8080/api/borrow-requests
```
#### POSTMAN
###### Thành công
![img.png](img.png)
###### Thất bại khi để không có readerId,book,borrowPeriod phải lớn hơn 0
![img_1.png](img_1.png)

![img_2.png](img_2.png)

### 2 Thủ thư xác nhận hoặc từ chối yêu cầu mượn sách (PUT /api/borrow-requests/{requestId}/status)
```
http://localhost:8080/api/borrow-requests/{requestId}/status
```
###### Thành công
![img_3.png](img_3.png)
###### Thất bại khi bị sai status,không tìm thấy requestId
![img_5.png](img_5.png)
![img_4.png](img_4.png)

### 3 Cập nhật trạng thái sách khi được mượn (PUT /api/borrow-requests/{requestId}/borrow)
```
http://localhost:8080/api/borrow-requests/{requestId}/borrow
```
###### Thành công
![img_6.png](img_6.png)
###### Thất bại khi không tìm thấy requestId
![img_7.png](img_7.png)

### 4 Cập nhật trạng thái sách khi được trả (PUT /api/borrow-requests/{requestId}/return)
```
http://localhost:8080/api/borrow-requests/{requestId}/return
```
###### Thành công
![img_8.png](img_8.png)
##### Thất bại khi không tìm thấy requestId
![img_9.png](img_9.png)
### 5 Tính phí phạt khi sách bị mất/hỏng hoặc trả trễ (PUT /api/borrow-requests/{requestId}/penalty)
```
http://localhost:8080/api/borrow-requests/{requetsId}/penalty
```
###### Thất bại khi requestId sai, request có status không phải OVERDUE,chi phí không hợp lệ
![img_10.png](img_10.png)   
![img_11.png](img_11.png)

###### Thành công
![img_12.png](img_12.png)

### 6 Người dùng(người đọc) xem danh sách sách đã mượn và trả (GET /api/users/{userId}/borrow-history)
```
http://localhost:8080/api/borrow-requests/users/{readerId}/borrow-history
```
![img_13.png](img_13.png)
###### Nếu không có thì trả về rỗng
![img_14.png](img_14.png)