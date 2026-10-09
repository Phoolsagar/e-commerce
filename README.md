# E-Commerce Application

A full-stack e-commerce web application built with Spring Boot, Spring Security, Spring Data JPA, MySQL, and Thymeleaf.

The project includes a customer storefront, authentication, product browsing, search and category filters, cart and checkout, order history with tracking, and a role-based multi-admin management area.

## Features

### Customer Features

- Browse a responsive product catalog
- Search products by name
- Filter products by category
- View product details and images
- Register and log in securely
- Add products to a cart
- Update item quantities or remove items
- Checkout with a shipping address
- View complete order history
- Track order status:
  - Pending
  - Shipped
  - Delivered
- Responsive storefront layout for desktop, tablet, and mobile

### Admin Features

- Dedicated admin navigation and dashboard
- Manage products created by the logged-in admin
- Add new products
- Edit existing products
- Delete products
- Update product price, stock, image, description, and category
- View product images in the current collection
- Paginated admin product management
- Create and manage admin-owned categories
- View orders related to the admin's products
- Update order status
- Delete orders related to the admin's products
- Admin-specific product and category ownership
- Five seeded admin accounts for multi-admin testing

### UI and UX

- Premium editorial storefront design
- Responsive navigation with mobile menu
- Responsive product cards and forms
- Premium status badges and controls
- Low-stock indicators
- Responsive admin tables
- Consistent currency formatting
- Bottom-pinned, responsive footer
- Keyboard focus states and touch-friendly controls

## Technology Stack

- Java 17 source compatibility
- Spring Boot 4.1.1
- Spring MVC
- Spring Security 7
- Spring Data JPA
- Hibernate
- MySQL 8+
- Thymeleaf
- Jakarta Validation
- Lombok
- Maven
- JUnit and Spring Boot Test

## Requirements

Install the following before running the project:

- JDK 17 or newer
- MySQL 8 or newer
- Git, optional
- Internet connection for the first Maven dependency download and external product images

The project has been tested with Java 21 as well as Java 17-compatible compilation.

## Database Setup

Start MySQL and create the database:

```sql
CREATE DATABASE ecommerceapplication;
```

Create a local configuration file by copying the example:

```powershell
Copy-Item src/main/resources/application.example.properties src/main/resources/application.properties
```

Then update the local values as required. The local `application.properties` file is ignored by Git and is not pushed to GitHub.

The default example configuration is:

```properties
Database: ecommerceapplication
Username: root
Password: change-me
Host: localhost
Port: 3306
```

The local values are stored in:

`src/main/resources/application.properties`

For a real deployment, change the database password and move credentials to environment variables or a secrets manager.

## Run the Project

Open a terminal in the project root:

```powershell
cd D:\JavaFullStack\Internship\E-Commerce\E-Commerce
```

Run with the Maven wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Run with Maven on macOS or Linux:

```bash
./mvnw spring-boot:run
```

The application starts on:

http://localhost:9090

If port `9090` is unavailable, run on another port:

```powershell
.\mvnw.cmd spring-boot:run "-Dserver.port=9091"
```

Then open:

http://localhost:9091

## Seeded Admin Accounts

The application automatically creates five admin accounts on startup. All five accounts use the same password.

| Admin   | Email                  | Password   |
| ------- | ---------------------- | ---------- |
| Admin 1 | `admin@ecommerce.com`  | `admin123` |
| Admin 2 | `admin2@ecommerce.com` | `admin123` |
| Admin 3 | `admin3@ecommerce.com` | `admin123` |
| Admin 4 | `admin4@ecommerce.com` | `admin123` |
| Admin 5 | `admin5@ecommerce.com` | `admin123` |

Admin login page:

http://localhost:9090/login

Admin dashboard:

http://localhost:9090/admin

Each admin receives twenty owned products. Product and category ownership is isolated, so one admin cannot edit or delete another admin's products.

## Customer Workflow

1. Open http://localhost:9090/register.
2. Create a customer account.
3. Sign in at http://localhost:9090/login.
4. Open the Shop page.
5. Search or filter products.
6. Open a product detail page.
7. Add the product to the cart.
8. Open the cart and update quantities if needed.
9. Enter a shipping address.
10. Place the order.
11. Open Orders to see order history and tracking status.

New orders start with the `PENDING` status.

## Admin Workflow

1. Sign in with an admin account.
2. Open the admin dashboard.
3. Use the admin navigation:
   - Overview
   - Products
   - Categories
   - Sign out
4. Open Products to add, edit, or delete owned products.
5. Use the product form to manage:
   - Name
   - Description
   - Price
   - Stock
   - Image URL
   - Category
6. Open Categories to create admin-owned categories.
7. Open Overview to manage related orders.
8. Change an order from `PENDING` to `SHIPPED` or `DELIVERED`.
9. Delete an order when required.

Customer checkout is the normal order-creation workflow. Admins manage fulfillment after the order is created.

## Important Routes

### Public Routes

| Method | Route           | Purpose                                              |
| ------ | --------------- | ---------------------------------------------------- |
| GET    | `/`             | Storefront homepage                                  |
| GET    | `/products`     | Product catalog with search, filters, and pagination |
| GET    | `/product/{id}` | Product detail page                                  |
| GET    | `/login`        | Login page                                           |
| GET    | `/register`     | Registration page                                    |
| POST   | `/register`     | Create a customer account                            |

### Customer Routes

| Method | Route                      | Purpose                         |
| ------ | -------------------------- | ------------------------------- |
| GET    | `/cart`                    | View the cart                   |
| POST   | `/cart/add/{productId}`    | Add a product to the cart       |
| POST   | `/cart/update/{productId}` | Update cart quantity            |
| POST   | `/cart/remove/{productId}` | Remove a cart item              |
| POST   | `/cart/checkout`           | Place an order                  |
| GET    | `/orders`                  | View order history and tracking |
| POST   | `/logout`                  | End the current session         |

### Admin Routes

All admin routes require the `ADMIN` role.

| Method | Route                         | Purpose                                  |
| ------ | ----------------------------- | ---------------------------------------- |
| GET    | `/admin`                      | Admin overview and order management      |
| GET    | `/admin/products`             | Owned product collection with pagination |
| GET    | `/admin/products?editId={id}` | Open a product in edit mode              |
| POST   | `/admin/products/save`        | Add a product                            |
| POST   | `/admin/products/update`      | Update an owned product                  |
| POST   | `/admin/products/delete/{id}` | Delete an owned product                  |
| GET    | `/admin/categories`           | View owned categories                    |
| POST   | `/admin/categories/save`      | Create an owned category                 |
| POST   | `/admin/orders/{id}/status`   | Update an owned order status             |
| POST   | `/admin/orders/{id}/delete`   | Delete an owned order                    |

## Pagination

- Public catalog: 12 products per page
- Admin product collection: 24 products per page
- Search keywords and category filters are preserved while moving between public catalog pages
- Admin product pagination is scoped to the logged-in admin's products

## Project Structure

```text
src/
  main/
    java/com/ecommerce/
      ECommerceApplication.java
      config/
        GlobalModelAttributes.java
        PasswordConfig.java
        SecurityConfig.java
        WebConfig.java
      controller/
        AdminController.java
        AuthController.java
        CartController.java
        HomeController.java
        OrderController.java
        ProductController.java
      dto/
        OrderDto.java
        ProductDto.java
      model/
        CartItem.java
        Category.java
        Order.java
        OrderItem.java
        Product.java
        Review.java
        User.java
        Wishlist.java
      repository/
        CartItemRepository.java
        CategoryRepository.java
        OrderRepository.java
        ProductRepository.java
        UserRepository.java
      service/
        CartService.java
        OrderService.java
        ProductService.java
        UserService.java
    resources/
      application.properties
      static/
        css/app.css
        js/app.js
      templates/
        index.html
        products.html
        product-detail.html
        cart.html
        orders.html
        login.html
        register.html
        admin-dashboard.html
        admin-products.html
        admin-categories.html
  test/
    java/com/ecommerce/
      ECommerceApplicationTests.java
```

## Security and Data Isolation

- Passwords are stored using BCrypt hashing.
- Customer pages are available to guests where appropriate.
- Cart, checkout, and order history require authentication.
- Admin pages require `ROLE_ADMIN`.
- Admin navigation is hidden from normal users.
- Admin products have an owning admin account.
- Admin categories have an owning admin account.
- Admin product editing and deletion verify ownership on the server.
- Admin order management is restricted to orders containing that admin's products.
- The database currently uses `spring.jpa.hibernate.ddl-auto=update` for local development.

## Testing

Run the complete test suite:

```powershell
.\mvnw.cmd test
```

The tests verify:

- Spring application context loading
- Database-backed application startup
- Homepage availability
- Homepage response content

The project has also been manually verified for:

- Guest navigation
- Customer registration and login
- Admin login
- Cart operations
- Checkout
- Order history
- Order status updates
- Admin access restrictions
- Product creation, editing, and deletion
- Multi-admin product isolation
- Responsive desktop and mobile layouts

## Troubleshooting

### MySQL connection error

Confirm that:

1. MySQL is running.
2. The `ecommerceapplication` database exists.
3. The username and password in `application.properties` are correct.
4. MySQL is listening on port `3306`.

### Port already in use

Run the application on another port:

```powershell
.\mvnw.cmd spring-boot:run "-Dserver.port=9091"
```

### Products or categories do not appear

Restart the application after confirming MySQL connectivity. The startup seed runner creates missing admins, categories, and products without duplicating seeded products.

### Browser shows an old design

Because Thymeleaf and static resources may be cached by the browser, use a hard refresh:

- Windows: `Ctrl + F5`
- macOS: `Cmd + Shift + R`

## Submission Checklist

- [x] Spring Boot backend
- [x] MySQL persistence
- [x] User authentication
- [x] Role-based admin access
- [x] Responsive storefront
- [x] Product catalog and search
- [x] Category filtering
- [x] Cart and checkout
- [x] Order history and tracking
- [x] Admin product management
- [x] Admin category management
- [x] Admin order management
- [x] Multi-admin ownership isolation
- [x] Pagination for large product collections
- [x] Automated startup and homepage tests
- [x] Local run instructions

## License

This project is intended for internship submission and educational use.
