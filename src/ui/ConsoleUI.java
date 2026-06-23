package ui;

import exceptions.InvalidInputException;
import exceptions.InvalidMoveException;
import model.GameState;
import model.Move;
import model.PieceColor;
import model.Square;
import engine.ChessEngine;

import java.util.List;
import java.util.Scanner;

/**
 * Handles terminal I/O logic for grabbing Human moves and parsing them.
 */
public class ConsoleUI {
    private BoardRenderer renderer;
    private Scanner scanner;
    private ChessEngine engine;

    /**
     * Constructs the UI with dependencies.
     */
    public ConsoleUI(BoardRenderer renderer, ChessEngine engine) {
        this.renderer = renderer;
        this.engine = engine;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the Game Loop.
     */
    public void start() {
        boolean playAgain = true;
        while (playAgain) {
            GameState state = new GameState();
            playGame(state);
            System.out.print("Play again? (y/n): ");
            String ans = scanner.nextLine().trim().toLowerCase();
            playAgain = ans.equals("y") || ans.equals("yes");
        }
        System.out.println("Thanks for playing!");
    }

    private void playGame(GameState state) {
        while (state.getStatus() == model.GameStatus.ONGOING || state.getStatus() == model.GameStatus.CHECK) {
            renderer.render(state);

            if (state.getCurrentTurn() == PieceColor.WHITE) {
                // Human Turn
                handleHumanTurn(state);
            } else {
                // AI Turn
                System.out.println("Computer is thinking...");
                Move aiMove = engine.getBestMove(state);
                if (aiMove != null) {
                    System.out.println("Computer played: " + aiMove);
                    state.applyMove(aiMove);
                } else {
                    System.out.println("Computer error: Null move returned.");
                    break;
                }
            }
        }
        renderer.render(state); // Final render
    }

    private void handleHumanTurn(GameState state) {
        boolean validMoveMade = false;
        while (!validMoveMade) {
            System.out.print("Your move (e.g. e2e4): ");
            String input = scanner.nextLine().trim();

            try {
                if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                    System.out.println("Exiting game...");
                    System.exit(0);
                } else if (input.equalsIgnoreCase("help")) {
                    System.out.println("Commands:");
                    System.out.println("  [file][rank][file][rank] - move piece (e.g. e2e4)");
                    System.out.println("  O-O                      - King-side castle");
                    System.out.println("  O-O-O                    - Queen-side castle");
                    System.out.println("  moves                    - list all legal moves");
                    System.out.println("  quit / q                 - exit game");
                    continue;
                } else if (input.equalsIgnoreCase("moves")) {
                    List<Move> allMoves = state.generateAllLegalMoves(PieceColor.WHITE);
                    System.out.println("Legal moves:");
                    for (Move m : allMoves) {
                        System.out.print(m + " ");
                    }
                    System.out.println();
                    continue;
                }

                Move m = parseInput(input, state);
                state.applyMove(m);
                validMoveMade = true;

            } catch (InvalidInputException | InvalidMoveException e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    /**
     * Parses the user string into a Move object relative to the GameState.
     */
    public Move parseInput(String input, GameState state) throws InvalidInputException, InvalidMoveException {
        List<Move> legals = state.generateAllLegalMoves(state.getCurrentTurn());
        
        // Handle castling alias
        if (input.equalsIgnoreCase("O-O")) {
            for (Move m : legals) {
                if (m.isCastling() && m.getTo().getCol() == 6) return m;
            }
            throw new InvalidMoveException("King-side castling is not legal right now");
        } else if (input.equalsIgnoreCase("O-O-O")) {
            for (Move m : legals) {
                if (m.isCastling() && m.getTo().getCol() == 2) return m;
            }
            throw new InvalidMoveException("Queen-side castling is not legal right now");
        }

        // Clean input of spaces (e.g., e2 e4 -> e2e4)
        input = input.replaceAll("\\s+", "");
        if (input.length() != 4 || !input.matches("[a-h][1-8][a-h][1-8]")) {
            throw new InvalidInputException("Format must be like 'e2e4'");
        }

        int fromCol = input.charAt(0) - 'a';
        int fromRow = 8 - (input.charAt(1) - '0');
        int toCol = input.charAt(2) - 'a';
        int toRow = 8 - (input.charAt(3) - '0');

        Square from = new Square(fromRow, fromCol);
        Square to = new Square(toRow, toCol);

        for (Move m : legals) {
            if (m.getFrom().equals(from) && m.getTo().equals(to)) {
                return m;
            }
        }

        throw new InvalidMoveException("Illegal move for the given piece");
    }
}
