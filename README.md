# RecipeApp

## Overview
RecipeApp is a digital cookbook application built with JavaFX. It allows users to browse, search, create, edit, and delete recipes. Each recipe includes detailed information such as ingredients, preparation and cooking time, instructions, and images. Users can also register and log in, like and comment on recipes, and sort recipes by popularity.

## Environment Requirements

- **Java 17** or higher
- **JavaFX** SDK (version matching your JDK, e.g., JavaFX 17+)
- **Maven** (for dependency management and build)
- **MySQL** (for data storage)

## Setup Instructions

### 1. Install Java and JavaFX

- Download and install [Java JDK 17+](https://adoptium.net/).
- Download and unzip [JavaFX SDK](https://gluonhq.com/products/javafx/) (e.g., to `C:\javafx-sdk-17`).

### 2. Install Maven

- Download and install [Maven](https://maven.apache.org/download.cgi).

### 3. Database Setup

- Install MySQL and create a database (e.g., `cookbook`).
- Import the provided `CookBook.sql` file to initialize tables and sample data:
  ```bash
  mysql -u youruser -p cookbook < CookBook.sql
  ```
- Update your database connection settings in `src/main/resources/mybatis_config.xml` if needed.

### 4. Configure JavaFX in Your IDE

- Add JavaFX SDK to your project/module dependencies.
- Set VM options for running JavaFX (example for Windows):
  ```
  --module-path C:/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml
  ```

### 5. Build the Project

- In the project root directory, run:
  ```bash
  mvn clean install
  ```

## Running the Application

1. **Via IDE**  
   - Open the project in your IDE (IntelliJ IDEA, VS Code, Eclipse, etc.).
   - Set the main class to `Init.App` or `Init.AppEntrance`.
   - Make sure to add JavaFX VM options as described above.
   - Run the application.

2. **Via Command Line**  
   - After building, run:
     ```bash
     mvn javafx:run
     ```
   - Or, if running the jar directly:
     ```bash
     java --module-path C:/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml -jar target/your-app.jar
     ```

## Features

- User registration and login
- Recipe browsing and keyword search
- Recipe creation, editing, and deletion
- Like and comment on recipes
- Sort recipes by number of likes
- Input validation and user-friendly error prompts
