# Puzzle Universe (COMP 2522 Term Project)

A collection of console-based puzzle games developed for a BCIT COMP 2522 Object-Oriented Programming II course term project.

## Project Overview

This repository contains the source code for three classic console games: Mastermind, Number Guessing, and Word Guessing. The project demonstrates core Java programming concepts, including object-oriented design, interfaces, basic input handling, and modular structure.

## Features

*   **Three Classic Games:** Includes implementations of Mastermind, Number Guessing, and Word Guessing.
*   **Console Interface:** Utilizes a text-based interface for gameplay interaction.
*   **Input Validation:** Basic handling for user input is implemented.
*   **Modular Design:** Organized using packages and interfaces to promote code organization.

## Getting Started

### Prerequisites

*   Java Development Kit (JDK) installed (specify required version if known, e.g., JDK 11 or later).
*   Git (for cloning the repository).

### Installation & Running

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url> 
    ```
    (Replace `<your-repo-url>` with the actual URL of this repository)
2.  **Navigate to the source directory:** Change your working directory to the root of the cloned project.
3.  **Compile the Java files:** Open a terminal or command prompt in the project root directory and compile the source code.
    *   *Using an IDE:* Import the project into your preferred Java IDE (like IntelliJ IDEA, Eclipse, VS Code with Java extensions). The IDE should handle compilation.
    *   *Manual Compilation (Example):*
        ```bash
        # Navigate into the src directory first
        cd src 
        # Compile all java files within the code directory and its subdirectories
        javac $(find code -name '*.java') # Linux/macOS/WSL
        # or using specific paths (adjust if necessary):
        # javac code/ca/bcit/comp2522/gameproject/*.java code/ca/bcit/comp2522/gameproject/mastermind/*.java code/ca/bcit/comp2522/gameproject/numbergame/*.java code/ca/bcit/comp2522/gameproject/wordgame/*.java code/ca/bcit/comp2522/gameproject/interfaces/*.java

        # On Windows Command Prompt (adjust paths/command if needed)
        # cd src
        # javac code\ca\bcit\comp2522\gameproject\*.java code\ca\bcit\comp2522\gameproject\mastermind\*.java code\ca\bcit\comp2522\gameproject\numbergame\*.java code\ca\bcit\comp2522\gameproject\wordgame\*.java code\ca\bcit\comp2522\gameproject\interfaces\*.java 
        ```
        *(Note: Using a build tool like Maven or Gradle would simplify this process significantly).*

4.  **Run the Main Application:** From the `src` directory (after compiling):
    ```bash
    java code.ca.bcit.comp2522.gameproject.Main 
    ```
5.  Follow the on-screen prompts to select and play a game.

## Project Purpose

This project was developed as a requirement for the COMP 2522 course at BCIT to apply and demonstrate understanding of object-oriented programming principles in Java.

## Built With

*   **Java:** Core programming language.
*   **IntelliJ IDEA:** Development environment used (optional, other IDEs/editors can be used).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions, issues, and feature requests are welcome. Feel free to check [issues page](<link-to-issues-page-if-applicable>) if you want to contribute.

## Acknowledgments

*   British Columbia Institute of Technology (BCIT) - COMP 2522 Course Instructors and Curriculum.


