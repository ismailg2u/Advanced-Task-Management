# Advanced Task Management Application

## Overview

Lorem Ipsum Corporation has transitioned from an outdated task management system to a new Advanced Task Management application built in-house. This application offers project and task management capabilities, team member assignments, progress tracking, priority management, and file attachment support, aiming to provide an efficient and modern solution for task and project management.

## Features

### 1. **Project and Task Management**
   - Manage projects and tasks related to different departments.
   - Track task progress through various states.
   
### 2. **Team Member Assignment**
   - Project Managers and Team Leaders can assign team members to specific tasks.

### 3. **Progress Tracking**
   - Tasks progress through states: Backlog, In Analysis, In Development, Completed, Cancelled, and Blocked.

### 4. **Priority Management**
   - Assign priorities to tasks: Critical, High, Medium, and Low.

### 5. **File Attachment Support**
   - Team members can attach files to tasks, which are stored on the disk.

### 6. **Role-Based Permissions**
   - Permissions are based on roles (Team Member, Team Leader, Project Manager,Project_Group_Manager,Admin).
   - Team Members cannot modify task titles or descriptions, but they can change task state, add comments, and attach files.

### 7. **State Management Rules**
   - **Happy Path**: Backlog ⇔ In Analysis ⇔ In Development ⇔ Completed.
   - **Cancel Path**: A task can be assigned as Cancelled from any state except Completed.
   - **Blocked Paths**: Tasks can be Blocked from In Analysis or In Development and can return from these states.
   - **Completed Tasks**: Once a task is completed, it cannot be moved to any other state.

### 8. **Required Information for Tasks and Projects**
   - **Task**: User Story, Acceptance Criteria, State, Priority, Comments, Attachments, Assignee.
   - **Project**: Department Name, Status (In Progress, Cancelled, Completed), Title, Description, Team Members.

## Requirements

- **Java Version**: 21
- **Spring Boot Version**: 3
- **PostgreSQL** for the database
- **Maven** for build and dependency management

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/ismailg2u/Advanced-Task-Management
   cd advanced-task-management
   
2. Configure your PostgreSQL database:

   - Ensure that PostgreSQL is installed and running.
   - Create a new database for the application (e.g., advancedtaskmanager).

3. Update your application.properties with the correct database credentials:

    ```bash
    spring.datasource.url=jdbc:postgresql://localhost:5432/advancedtaskmanager
    spring.datasource.username=your_username
    spring.datasource.password=your_password

4. Build and run the application:
    ```bash
    mvn clean install
    mvn spring-boot:run

## Testing

The application includes unit tests to ensure business logic is functioning correctly, and the code coverage meets the required 80%.

### Run the tests:
   ```bash
   mvn test

### Verify the application:
   ```bash
   mvn install
   mvn verify


