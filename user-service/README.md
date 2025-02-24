# Spring Boot User API

### Prerequisites

- Java (JDK 17+)
- Gradle
- PostgreSQL/pgAdmin4 or container PostgreSQL/PgAdmin4 by Docker
- IDE (IntelliJ IDEA, Eclipse, hoặc bất kỳ IDE nào hỗ trợ Java)
- Postman or ThunderClient for VSCode

### PostgreSQL for docker
- Step 1: Run container PostgreSQL by command:
```bash
docker-compose up -d
```
- Acceptable results:
![img.png](img.png)
- Step 2: Access the path: Localhost:80 or Localhost and login with auth of UI pgAdmin4 before:
![img_1.png](img_1.png)
- Step 3: Register server for UI: 
![img_2.png](img_2.png)
- Step 4: Enter Name
![img_3.png](img_3.png)
- Step 5: Enter username and password are 'root'
![img_4.png](img_4.png)
- Step 6: Value of Host name/address is IPAddress of pgadmin4-UI in Docker
- Enter this command to see PAddress of pgadmin4-UI:
```bash
docker inspect pgadmin4-container
```
- Result and save: ![img_5.png](img_5.png) ![img_6.png](img_6.png)

### Start project user-service
- Step 1: (If any) Create empty table users in DB user-service
- Step 2: Start project user-service
- Step 3: Test postman or ThunderClient
