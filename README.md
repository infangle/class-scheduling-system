# class-scheduling-system
 Designed and implemented an Automated Class Scheduling System for Addis Ababa Science and Technology University (AASTU) using Java (backend), React (frontend), and SQL (database). The system streamlined scheduling, reduced errors, and improved stakeholder satisfaction by minimizing conflicts and enhancing user experience.

## pages

![Screenshot 2024-05-28 232639](https://github.com/user-attachments/assets/9840b87a-b245-4282-b765-543c367b2e7a)


**classroom**
![classroom](https://github.com/user-attachments/assets/76fccef1-9333-420f-88fb-113ce2aa7ab7)

**students**
![student](https://github.com/user-attachments/assets/eed77271-267e-4c4f-be54-4f300ccac491)


**courses**
![course](https://github.com/user-attachments/assets/6585245b-c407-4b85-b3f6-11a4a8966a22)


Here's a template for your README file:

---

# Automated Class Scheduling System

## Project Overview

This project is an **Automated Class Scheduling System** developed for Addis Ababa Science and Technology University (AASTU). The system aims to streamline the class scheduling process, reduce errors, and enhance user satisfaction. It is built using **Java** for the backend, **Java Swing** for the desktop user interface, **React** for the web interface, and **MySQL** as the database.

## Features

- **Automated Scheduling**: Automatically generates class schedules based on predefined rules and constraints.
- **Conflict Resolution**: Identifies and resolves scheduling conflicts to ensure a smooth scheduling process.
- **User Management**: Admins can manage user roles and permissions, including the ability to add, update, or remove courses, rooms, and instructors.
- **Interactive UI**: Provides both a desktop interface using Java Swing and a web interface using React for easy access and management.
- **Database Integration**: Utilizes MySQL to store and manage scheduling data, ensuring data integrity and accessibility.

## Technologies Used

- **Backend**: Java
- **UI**: React
- **Database**: MySQL

## Installation and Setup

### Prerequisites

- **Java Development Kit (JDK) 11 or higher**
- **MySQL**
- **Node.js and npm** (for React frontend)
- **Maven** (for managing Java dependencies)

### Steps

1. **Clone the Repository**:
   ```bash
   git clone [https://github.com/your-username/class-scheduling-system.git](https://github.com/infangle/class-scheduling-system.git)
   cd class-scheduling-system
   ```

2. **Set Up the MySQL Database**:
   - Create a new database in MySQL.
   - Run the provided SQL scripts in the `database` folder to set up the necessary tables.

3. **Configure Database Connection**:
   - Update the database connection settings in the `application.properties` file located in the `src/main/resources` directory.

4. **Build and Run the Backend**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Set Up and Run the React Frontend**:
   ```bash
   cd frontend
   npm install
   npm start
   ```

6. **Launch the Desktop UI**:
   - Run the `Main.java` file located in the `src/main/java/com/scheduling` directory using your preferred IDE.

## Usage

- **Admin Access**: Admins can log in to manage courses, rooms, and instructors, and to generate and review schedules.
- **View Schedules**: Instructors and students can log in to view their respective schedules.
- **Conflict Alerts**: The system alerts users of any scheduling conflicts, allowing for manual adjustments if necessary.

## Contributing

If you would like to contribute to this project, please fork the repository and submit a pull request with your changes. Contributions are welcome and appreciated.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


**instuctor**
![instructor](https://github.com/user-attachments/assets/53c9e941-0480-4f2c-9d03-d7ca3ab2cfc6)

**Schedule**
![schedule](https://github.com/user-attachments/assets/679f1a73-aa5a-4e84-b6aa-498450101ca4)

