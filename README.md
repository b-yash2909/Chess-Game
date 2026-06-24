# ♔ OOP Chess Game — Java Mini Project

A fully playable chess game built in Java with a graphical GUI (Swing), AI opponent, and local multiplayer support. This project demonstrates core object-oriented programming principles through a real-world application that evolved from a console-based implementation to a modern GUI.

## ✨ Features

**Core Chess Rules**

* All standard piece movements (pawn, rook, knight, bishop, queen, king)
* Castling (kingside and queenside for both colours)
* En passant capture
* Pawn promotion to Queen with choice dialog
* Check detection with visual highlighting
* Checkmate and stalemate detection
* 50-move rule draw detection
* Insufficient material draw detection

**User Interface**

* Graphical board with Unicode chess symbols
* Click-to-move interface (select piece, click destination)
* Legal move highlighting (green dots for empty squares, rings for capturable pieces)
* Check indicator (red highlight on king)
* Last move tracking (yellow tint on previous move squares)
* Pawn promotion dialog
* Game mode selection launch screen
* Status bar showing current turn, warnings, and results

**Game Modes**

* Human vs Computer (AI opponent)
* Human vs Human (local two-player)

**AI Opponent**

* Minimax algorithm with Alpha-Beta pruning
* Search depth: 3 plies
* Material-based evaluation with piece-square tables
* Non-blocking background computation using SwingWorker threads
* Small randomness to avoid repetitive play

## 🛠️ Technology Stack

|Component|Technology|
|-|-|
|**Language**|Java 11+|
|**GUI Framework**|Java Swing (javax.swing)|
|**Game Logic**|Pure Java standard library|
|**AI Algorithm**|Minimax with Alpha-Beta Pruning|
|**Data Structures**|ArrayList, HashMap, custom classes|
|**Compilation**|javac command-line compiler|

## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 11 or higher
* Check version: `java -version`
* Download from [adoptium.net](https://adoptium.net) if needed

### Installation \& Running

**Windows (Recommended):**

```bash
run.bat
```

The batch file will:

1. Configure terminal for Unicode support
2. Compile all Java source files
3. Run the application
4. Keep the window open to show any errors

**Manual Compilation (All Platforms):**

```bash
javac -encoding UTF-8 -d out -sourcepath src src/Main.java
java -Dfile.encoding=UTF-8 -cp out Main
```

Command explanation:

* `-encoding UTF-8` ensures Unicode chess symbols display correctly
* `-d out` outputs compiled .class files to the out/ folder
* `-sourcepath src` finds Java source files in src/
* `-cp out` uses compiled classes from out/ folder

## 🎮 How to Play

**Starting a Game**

1. Run the application using run.bat or manual compilation
2. A launch screen appears with two game mode options
3. Select "Human vs Computer" or "Human vs Human"

**Making Moves**

1. Click a piece of your colour to select it

   * The selected piece highlights in green
   * All legal moves display as green dots (empty square) or rings (capturable piece)
2. Click a green-marked destination square to move the piece
3. The board updates and it becomes the opponent's turn

**Special Moves**

Castling: Select the king and click the destination square (two squares toward the rook). Both king and rook move simultaneously.

En Passant: After an opponent's pawn moves two squares forward, your pawn can capture it on the passed square if positioned beside it.

Pawn Promotion: When a pawn reaches the opponent's back rank, a dialog appears. Select Queen, Rook, Bishop, or Knight. The pawn is replaced with your choice.

**Game Status**

Check: The king square turns red when under attack. You must immediately resolve the check by moving the king, blocking, or capturing the attacker.

Checkmate: Game ends with a result dialog. The player without legal moves and whose king is in check has lost.

Stalemate: Game ends as a draw. The current player has no legal moves but is not in check.

## 📂 Project Structure

```
src/
  Main.java                      Entry point, launches application

  model/                         Game logic layer
    Piece.java                   Abstract base class for all pieces
    Pawn.java, Rook.java,        Six concrete piece classes
    Knight.java, Bishop.java,    (each implements piece-specific movement)
    Queen.java, King.java
    Board.java                   8x8 grid, piece positions, game state
    Square.java                  Board coordinate representation
    Move.java                    Represents a single chess move
    GameState.java               Coordinates game flow, move application
    PieceColor.java              Enum: WHITE, BLACK
    GameStatus.java              Enum: ONGOING, CHECK, CHECKMATE, STALEMATE, DRAW
    GameMode.java                Enum: HUMAN\_VS\_COMPUTER, HUMAN\_VS\_HUMAN

  interfaces/                    Interface definitions
    Movable.java                 Contract: isValidMove(), getLegalMoves()

  exceptions/                    Custom exception classes
    InvalidMoveException.java    Thrown for illegal moves
    InvalidInputException.java   Thrown for malformed input

  engine/                        AI logic
    ChessEngine.java             Minimax algorithm with Alpha-Beta pruning

  ui/                            User interface (Swing)
    ChessFrame.java              Main window (JFrame)
    ChessBoardPanel.java         Board rendering and mouse input handling
    LaunchScreen.java            Game mode selection screen
    PromotionDialog.java         Pawn promotion piece selector

out/                            Compiled .class files (generated after compilation)

run.bat                         Windows batch script to compile and run
.gitignore                      Git ignore rules
README.md                       This file
```

## 🏗️ Architecture \& OOP Design

**Piece Class Hierarchy**

The project demonstrates inheritance and polymorphism through a clean piece hierarchy:

```
Piece (abstract)
  ├── Pawn
  ├── Rook
  ├── Knight
  ├── Bishop
  ├── Queen
  └── King
```

All pieces implement the Movable interface which defines two methods:

* `isValidMove(Board, Square, Square)` — validates if a move is legal
* `getLegalMoves(Board, Square)` — returns all legal moves from a position

Each concrete piece class overrides these methods with its own movement logic. The GameState class treats all pieces uniformly through the Piece reference type, and the correct implementation is resolved at runtime — this is runtime polymorphism.

**Separation of Concerns**

The codebase is organized into three logical layers:

Model Layer (model/ package): Contains all game logic — board representation, piece definitions, move validation, game status. This layer has zero dependencies on the UI and can be reused with any interface.

Engine Layer (engine/ package): Implements the Minimax AI algorithm. Operates independently of both the model and UI layers.

UI Layer (ui/ package): Handles all visual rendering and user input using Swing. Calls into the model and engine layers but never exposes implementation details.

This layered design means the entire UI layer was replaced (console to Swing) without modifying any game logic — demonstrating the power of good OOP architecture.

## 📚 Development Process

The project evolved in two major phases:

**Phase 1 - Console Implementation (Weeks 1-4)**

* Designed piece class hierarchy with abstract base class and concrete subclasses
* Implemented all chess rules and move validation logic
* Built board state management and check/checkmate detection
* Created console-based rendering using ANSI escape codes
* Implemented Minimax AI with Alpha-Beta pruning
* Tested AI performance and evaluation function

**Phase 2 - Swing GUI Conversion (Weeks 5-8)**

* Designed Swing application architecture (JFrame, JPanel, custom painting)
* Implemented click-to-move interface with visual feedback
* Built multi-screen application (launch screen, game board, dialogs)
* Integrated SwingWorker for non-blocking AI computation
* Added visual enhancements (check highlighting, move tracking, promotion dialog)
* Tested all game modes and edge cases

The evolution from console to GUI demonstrates that good OOP design allows major architectural changes without touching game logic.

## 💡 Key Design Decisions

**Why Abstract Class for Piece?** Each piece type shares common state (color, hasMoved flag) and a common interface. An abstract class provides both shared implementation and enforces the contract that all pieces must implement getSymbol().

**Why SwingWorker for AI?** The Minimax algorithm can take time to compute. Running it on the Event Dispatch Thread would freeze the UI. SwingWorker executes the computation on a background thread and updates the UI when complete.

**Why Separate Move Validation?** Rather than checking legality during move generation, moves are generated first then filtered to exclude illegal moves (those leaving the king in check). This separation makes the code clearer and allows the same move generator to be used in different contexts.

## ✅ OOP Concepts Demonstrated

* **Abstraction**: Movable interface defines the contract; Piece abstract class hides implementation details
* **Inheritance**: All piece classes inherit from Piece base class, reusing common fields and methods
* **Polymorphism**: GameState calls getLegalMoves() on Piece references without knowing concrete types; correct implementation resolved at runtime
* **Encapsulation**: All fields are private with public getters/setters; internal state not exposed
* **Interfaces**: Movable interface separates contract from implementation
* **Custom Exceptions**: InvalidMoveException and InvalidInputException handle domain-specific error conditions
* **Collections**: ArrayList for move lists, HashMap for piece-square table lookups
* **Threading**: SwingWorker demonstrates proper background computation in GUI applications

## 🔧 Compilation \& Troubleshooting

If run.bat doesn't work:

1. Ensure JDK 11+ is installed: `java -version`
2. Try manual compilation: `javac -encoding UTF-8 -d out -sourcepath src src/Main.java`
3. If javac is not found, add JDK/bin to your system PATH
4. Run: `java -Dfile.encoding=UTF-8 -cp out Main`

For Unicode symbol issues: Ensure your terminal supports UTF-8 encoding. Windows Command Prompt does this automatically with run.bat; on other systems check terminal settings.

## 💬 Connect with Me

If you find this project useful or have suggestions, feel free to:

* Star this repo ⭐ — it helps others discover it
* Create issues — for bugs or feature requests
* Submit pull requests — improvements are always welcome

You can also reach me at:- 

* \*\*LinkedIn\*\*: \[Yash Bhardwaj](https://www.linkedin.com/in/yash-bhardwaj-5b9040320/)
* \*\*Email\*\*: \[yashbhardwaj290906@gmail.com](mailto:yashbhardwaj290906@gmail.com)

\---

**Year II, Semester IV | Object-Oriented Programming with Java**

Built to demonstrate core OOP principles through a complete, playable chess application.

