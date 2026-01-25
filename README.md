<img width="285" height="152" alt="image" src="https://github.com/user-attachments/assets/d2352b36-eb94-48d3-972d-398a783d1ca5" /># Durable Execution Engine

## Overview
This project implements a native durable workflow execution engine in Java.
It ensures that completed steps are not re-executed after failures.

## Features
- Durable step execution
- Crash recovery
- SQLite persistence
- Parallel execution
- Thread-safe processing
- CLI-based runner

## Tech Stack
- Java
- Maven
- SQLite
- Gson
- SLF4J

## Project Structure

## How It Works
Each workflow step is executed with a unique ID.
Step results are stored in SQLite.
On restart, completed steps are loaded and skipped.

## How to Run
1. Clone the repository
2. Open in Eclipse / VS Code
3. Run MainApp.java

Or using terminal:
mvn clean compile
mvn exec:java -Dexec.mainClass="app.MainApp"


## Crash Simulation
Run the program and choose to crash after step 1.
Re-run with the same workflow ID to resume execution.

##OUTPUT
1. if choose yes
Enter Workflow ID: test5
Crash after step 1? (yes/no): yes
Skipped (cached): create-record-1
Crashing after Step 1...

Enter Workflow ID: test5
Crash after step 1? (yes/no): no
Executing: create-record-1
Executing: assign-laptop-2
Executing: give-access-3
Executing: send-email-4
Welcome email sent to EMP-1769338484302 
Workflow Completed

