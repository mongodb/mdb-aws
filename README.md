# Customer Management Application

A lightweight full-stack web app to **list, add, edit and delete** customer records.
The entire stack runs inside a single Spring Boot process and stores data in MongoDB.

---

## ✨ Features

* Responsive HTML/Bootstrap UI served by Spring Boot
* Customer CRUD:  
  • **List** existing customers  
  • **Add** new customer  
  • **Edit** customer inline (form switches to *edit mode*)  
  • **Delete** single customer
* RESTful JSON API (`/customers`)
* 5 dummy customers inserted automatically on first start – try the UI immediately
* Conservative, production-ready MongoDB connection-pool tuning with graceful shutdown

---

## 🛠️ Tech Stack

| Layer       | Technology                                   |
|-------------|----------------------------------------------|
| Backend     | Java 8 • Spring Boot 2.1.x • Spring Data MongoDB |
| Database    | MongoDB 3.6             |
| Front-end   | HTML5 • CSS3 (Bootstrap 4) • Vanilla JS (Fetch API) |
| Build/Run   | Maven 3.5 +                                  |

The UI lives in `src/main/resources/static`, so no separate web server is needed.

---

## 📂 Project Structure (important paths)

```
customer-management-app/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/example/customermanagement/
    │   │   ├── CustomerManagementApplication.java
    │   │   ├── config/MongoConfig.java
    │   │   ├── controller/CustomerController.java
    │   │   ├── model/Customer.java
    │   │   └── repository/CustomerRepository.java
    │   └── resources/
    │       ├── application.properties
    │       └── static/
    │           ├── index.html
    │           ├── css/styles.css
    │           └── js/main.js
```

---

## 🚀 Quick Start

### 1 · Prerequisites

| Tool            | Version (min) | Purpose                    |
|-----------------|--------------|----------------------------|
| JDK             | 8            | Compiles & runs backend    |
| Maven           | 3.5          | Builds project             |
| MongoDB server  | 3.6          | Data store                 |

Ensure `mongod` is running and accessible.

### 2 · Build & Run

```bash
git clone <repo-url>
cd customer-management-app

# build
mvn clean package

# run (dev)
mvn spring-boot:run
```

Spring Boot starts on **http://localhost:8080**  
Open **http://localhost:8080/index.html** in the browser.

### 3 · Docker (optional)

```bash
# build image
docker build -t customer-app .

# run MongoDB
docker run --name mongo -d -p 27017:27017 mongo:6

# run app container
docker run --name customer-app --link mongo -p 8080:8080 customer-app
```

---

## ⚙️ MongoDB Configuration

Spring Boot reads settings from `src/main/resources/application.properties`.

```
# simplest – single line
spring.data.mongodb.uri = mongodb://<HOST>:27017/customerdb
```

If you prefer individual properties:

```
spring.data.mongodb.host   = <HOST>
spring.data.mongodb.port   = 27017
spring.data.mongodb.database = customerdb
```

### Connection-Pool Tuning (already set)

| Property                                    | Value | Reason |
|---------------------------------------------|-------|--------|
| `max-connections-per-host`                  | 10    | avoid exhausting DB |
| `min-connections-per-host`                  | 1     | keep one warm       |
| `maxConnectionIdleTime` (MongoConfig)       | 5 min | close idle sockets  |
| `heartbeatFrequency`                        | 10 s  | lighter monitoring  |

Spring closes the pool on shutdown via `@PreDestroy` and a JVM hook.

---

## 🖥️ API Reference

Base URL `/customers`

| Method | Endpoint        | Description          |
|--------|-----------------|----------------------|
| GET    | `/`             | List all customers   |
| GET    | `/{id}`         | Get single customer  |
| POST   | `/`             | Create customer      |
| PUT    | `/{id}`         | Update customer      |
| DELETE | `/{id}`         | Delete customer      |
| DELETE | `/`             | Delete **all**       |

Sample JSON:

```json
{
  "name"   : "Jane Smith",
  "email"  : "jane.smith@example.com",
  "phone"  : "555-123-4567",
  "address": "123 Main St"
}
```

`id` field is auto-generated ObjectId and returned as plain string.

---

## 🧩 Troubleshooting

| Symptom | Cause | Fix |
|---------|-------|-----|
| `Cannot connect to server localhost:27017` | Wrong host/port | Update `spring.data.mongodb.uri` |
| `state should be: sizeMaintenanceFrequency > 0` at startup | Pool frequency set to 0 | **Fixed** in `MongoConfig` (uses 60 000 ms) |
| `Bean of type CustomerRepository not found` | Repositories disabled | Ensure `@EnableMongoRepositories` present & `spring.data.mongodb.repositories.type=auto` |
| Edit/Delete silently fail | ID serialization mismatch | **Fixed** by exposing `id` as string in `Customer` model |
| `MongoWaitQueueFullException` | Pool exhausted | Increase `max-connections-per-host` or reduce concurrent requests |

Check server connections:

```js
db.serverStatus().connections
```

---

## 👐 Contributing

Pull requests are welcome!  
Open an issue for bugs or feature ideas.

---

## 📜 License

MIT – see `LICENSE` file for full text.  
Enjoy building! 🚀
