# Arcade-Simulation-System
This system simulates the operational backend of a modern arcade. It manages complex interactions between different game types (Virtual Reality, Cabinet, Active), customer accounts, and real-time transaction processing. The project emphasizes Object-Oriented Design (OOD) principles to handle dynamic pricing models and strict validation rules.

## Key Features

***Polymorphic Game Engine***: Implements a base ArcadeGame class with specialized behavior for ActiveGame, CabinetGame, and VirtualRealityGame, utilizing method overriding for context-specific pricing.

***Robust Transaction Logic***: A centralized processTransaction engine that validates age requirements, account balances, and game availability using custom-built Exception classes.

***Dynamic Pricing Models***: Automated price calculation based on "Peak" vs "Off-Peak" hours and payout/non-payout game statuses.

***Data Structure Optimization***: Includes a refactored implementation (Arcade_1.java) migrating from ArrayList to HashMap to achieve O(1) lookup time for customer and game records.

## Tech Stack
Language: Java (JDK 11+)

Architecture: Object-Oriented (Inheritance, Abstraction, Polymorphism)

Data Management: File I/O (Parsing # and @ delimited data), Java Collections Framework.

Error Handling: Custom Exception Hierarchy (InsufficientBalanceException, AgeLimitException, etc.).

## System Architecture
The system is designed with strict encapsulation and separation of concerns:

Entity Layer: Models for Customer (with Staff/Student/Standard types) and ArcadeGame.

Logic Layer: Arcade class acts as the system controller, managing revenue and collection integrity.

Validation Layer: Custom exception classes ensure the system fails gracefully under invalid inputs.

## Performance Benchmarking
This repository includes two versions of the core engine:

Standard (Arcade.java): Utilizes ArrayList for sequential data handling.

Optimized (Arcade_1.java): Implements HashMap to optimize retrieval speeds for large-scale datasets, demonstrating an understanding of algorithmic efficiency.

## Installation & Usage
Clone the repository:

Bash
```
git clone https://github.com/your-username/Arcade-Simulation-System.git
```
Ensure customers.txt and games.txt are in the root directory.

Compile and run:

Bash
```
javac *.java
java Arcade
```
Note: Developed as the final assessment for CMP-4008Y at the University of East Anglia (UEA).
