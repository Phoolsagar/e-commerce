
<div align="center">

# 🛒 E-Commerce Application

### Java • Spring Boot • Spring Security • Thymeleaf • MySQL

A full-stack e-commerce web application featuring secure authentication, product management, shopping cart, checkout, order tracking, and multi-admin management.

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-7-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![MySQL](https://img.shields.io/badge/MySQL-8%2B-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Template_Engine-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)

</div>

---

## 📌 About The Project

The **E-Commerce Application** is a full-stack web application developed using Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate, MySQL, and Thymeleaf.

It provides customers with an online shopping experience, including product browsing, search, category filtering, cart management, checkout, and order tracking.

The application also includes a multi-admin management system with separate product and category ownership.

## 🔗 Internship Task

**Task:** [Java Full Stack Internship – E-Commerce Shopping Platform](https://www.freeinternships.in/java-full-stack-internship/free-java-full-stack-internship-online-ecommerce-shopping-platform-jv-ec-001.php)

## ✨ Features

### 🛍️ Customer Features

- Browse products and view product details.
- Search products by name and filter by category.
- Register and log in securely.
- Add products to the cart.
- Update quantities and remove cart items.
- Checkout with a shipping address.
- View order history and track order status.
- Responsive design for desktop, tablet, and mobile.

### 👨‍💼 Admin Features

- Dedicated admin dashboard.
- Add, edit, and delete products.
- Manage product prices, stock, descriptions, images, and categories.
- Create and manage admin-owned categories.
- View and manage related orders.
- Update order status: `PENDING`, `SHIPPED`, and `DELIVERED`.
- Paginated product management.
- Multi-admin ownership isolation.

## 🛠️ Tech Stack

| Category | Technologies |
|---|---|
| Language | Java 17+ (tested with Java 21) |
| Backend | Spring Boot 4.1.1, Spring MVC |
| Security | Spring Security 7, BCrypt |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL 8+ |
| Frontend | Thymeleaf, HTML, CSS, JavaScript |
| Validation | Jakarta Validation |
| Tools | Maven, Lombok, Git, GitHub |
| Testing | JUnit, Spring Boot Test |

## 🏗️ Architecture

```text
Browser
   ↓
Thymeleaf + HTML/CSS/JavaScript
   ↓
Spring MVC Controllers
   ↓
Service Layer
   ↓
Spring Data JPA / Hibernate
   ↓
MySQL Database
```

## 📂 Project Structure

```text
E-Commerce/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   └── js/
│   │       ├── templates/
│   │       └── application.example.properties
│   └── test/java/com/ecommerce/
├── ecommerce_db.sql
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## 🚀 Run Locally

### Prerequisites

- JDK 17 or newer
- MySQL 8 or newer
- Git (optional)

### 1. Clone the Repository

Replace `YOUR_USERNAME` with your GitHub username and use your actual repository URL.

```bash
git clone https://github.com/YOUR_USERNAME/E-Commerce.git
cd E-Commerce
```

### 2. Create the Database

```sql
CREATE DATABASE ecommerceapplication;
```

### 3. Import the Database Dump

The repository includes `ecommerce_db.sql` for database initialization.

Run the following command from the project root:

```bash
mysql -u root -p ecommerceapplication < ecommerce_db.sql
```

Enter your MySQL password when prompted.

**Note:** Ensure `ecommerce_db.sql` exists in the repository root before importing it. If the dump already contains a `CREATE DATABASE` or `USE` statement, verify that it targets the correct database.

### 4. Configure MySQL

Copy the example configuration file on Windows:

```powershell
Copy-Item src/main/resources/application.example.properties src/main/resources/application.properties
```

Update `src/main/resources/application.properties` with your local database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerceapplication
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Keep any other required properties from the example configuration.

Do not commit your actual database credentials.

### 5. Start the Application

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

### 6. Open in Browser

| Page | URL |
|---|---|
| Storefront | http://localhost:9090 |
| Product Catalog | http://localhost:9090/products |
| Registration | http://localhost:9090/register |
| Login | http://localhost:9090/login |
| Admin Dashboard | http://localhost:9090/admin |

If port `9090` is unavailable, run:

```powershell
.\mvnw.cmd spring-boot:run "-Dserver.port=9091"
```

## 🔑 Demo Admin Accounts

Five admin accounts are seeded automatically for local development.

| Admin | Email | Password |
|---|---|---|
| Admin 1 | `admin@ecommerce.com` | `admin123` |
| Admin 2 | `admin2@ecommerce.com` | `admin123` |
| Admin 3 | `admin3@ecommerce.com` | `admin123` |
| Admin 4 | `admin4@ecommerce.com` | `admin123` |
| Admin 5 | `admin5@ecommerce.com` | `admin123` |

Each admin receives twenty owned products.

> ⚠️ These are development credentials. Replace them before public deployment.

## 🔐 Security

- BCrypt password hashing.
- Spring Security authentication and role-based authorization.
- Protected cart, checkout, and order history.
- Server-side product ownership verification.
- Admin-specific product, category, and order management.

## 🧪 Testing

Run the test suite from the project root.

**Windows:**

```powershell
.\mvnw.cmd test
```

**macOS / Linux:**

```bash
./mvnw test
```

Automated tests cover application context loading, database-backed startup, and homepage availability and response content.

## 🔮 Future Enhancements

- Payment gateway integration.
- Product reviews and ratings.
- Email notifications.
- Advanced sales analytics.
- Docker and cloud deployment.
- Swagger/OpenAPI documentation.
- Expanded automated testing.

## 👨‍💻 Author

**Phoolsagar Singh**

Java Developer | Java Backend Developer | Full Stack Developer

- **GitHub:** [PhoolSagar](https://github.com/PhoolSagar)
- **LinkedIn:** [Phoolsagar Singh](https://www.linkedin.com/in/phoolsagar/)
- **Portfolio:** [phoolsagar.netlify.app](https://phoolsagar.netlify.app/)

---

<div align="center">

⭐ If you like this project, consider giving it a star!

</div>
