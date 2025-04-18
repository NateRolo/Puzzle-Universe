# Puzzle Universe (COMP 2522 Term Project)

Welcome to the source code behind **Puzzle Universe**! This is a collection of console-based puzzle games originally developed for a BCIT COMP 2522 Object-Oriented Programming II term project. If you need a simple, text-based distraction, you've come to the right place.

## Project Overview

This repository contains the source code for three classic console games: Mastermind, Number Guessing, and Word Guessing. It served as a practical exercise in applying core Java concepts like object-oriented design, interfaces, basic input handling, and creating a (somewhat) modular structure.

## Features

*   **Triple Threat Gaming:** Includes implementations of Mastermind, Number Guessing, and Word Guessing. Why settle for one when you can have three? 🎲🔢🅰️
*   **Pure Console Action:** A straightforward, text-based interface for gameplay interaction.
*   **Input Validation:** Attempts to gracefully handle user input.
*   **Modular-ish Design:** Organized using packages and interfaces to keep things tidy.

## Getting Started

### Prerequisites

*   Java Development Kit (JDK) installed (Tested with JDK 11, other versions might work).
*   Git (for cloning the repository).

### Installation & Running

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url> 
    ```
    (Replace `<your-repo-url>` with the actual URL of this repository)
2.  **Navigate to the project directory:** Change your working directory to the root of the cloned project.
3.  **Compile the Java files:** Open a terminal or command prompt in the project root directory.
    *   *Using an IDE (Recommended):* Import the project into your preferred Java IDE (like IntelliJ IDEA, Eclipse, VS Code with Java extensions). The IDE should handle compilation smoothly.
    *   *Manual Compilation:*
        ```bash
        # Navigate into the src directory first
        cd src 
        # Compile all java files within the code directory and its subdirectories
        # (This example uses 'find' common on Linux/macOS/WSL; adapt if needed)
        javac $(find code -name '*.java') 
        # Or provide specific paths if 'find' isn't suitable:
        # javac code/ca/bcit/comp2522/gameproject/*.java ... [etc.] ...

        # On Windows Command Prompt, you might need to list files explicitly:
        # cd src
        # javac code\ca\bcit\comp2522\gameproject\*.java code\ca\bcit\comp2522\gameproject\mastermind\*.java ... [etc.] ...
        ```
        *(Strongly Consider a Build Tool: Using Maven or Gradle would automate compilation, testing, and packaging, simplifying this step considerably).*

4.  **Run the Main Application:** From the `src` directory (after compiling):
    ```bash
    # Ensure your classpath includes the compiled code root (src)
    java code.ca.bcit.comp2522.gameproject.Main 
    ```
5.  Follow the on-screen prompts and enter the Puzzle Universe!

## Project Purpose

Primarily, this project fulfilled a requirement for the COMP 2522 course at BCIT, aiming to solidify understanding of object-oriented programming in Java. Secondary purpose? Proving that classic console games are still fun to build (and maybe play).

## Built With

*   **Java:** The core programming language.
*   **IntelliJ IDEA:** The trusty IDE used during development.
*   **Caffeine:** An essential (unofficial) dependency. ☕

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. Feel free to explore, learn from, or adapt the code.

## Contributing

While this was primarily a student project, suggestions or bug reports are welcome! Feel free to open an issue or submit a pull request if you spot something amiss or have an idea for improvement.

## Acknowledgments

*   **BCIT COMP 2522:** For providing the framework and motivation for this project.
*   **Online Resources:** Countless tutorials, documentation pages, and Stack Overflow threads that aided development.


