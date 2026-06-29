package ui;

import model.*;
import model.Lesson.PiecePlacement;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds and exposes the full 14-lesson curriculum for the interactive tutorial.
 */
public class TutorialContent {

    /**
     * Creates and returns an ordered list of all 14 tutorial lessons.
     *
     * @return the list of Lesson objects
     */
    public static List<Lesson> getAllLessons() {
        List<Lesson> lessons = new ArrayList<>();

        // LESSON 1: The Chessboard
        lessons.add(new Lesson(
                "1. The Chessboard",
                new String[]{
                        "The chessboard has 64 squares arranged in an 8x8 grid.",
                        "Columns are called files, labeled a through h, left to right.",
                        "Rows are called ranks, labeled 1 through 8, bottom to top from White's side.",
                        "Each square has a unique name combining its file and rank, e.g. e4."
                },
                PracticeMode.FREE_MOVE,
                new ArrayList<>(),
                "Click any square on the board to see its coordinate name."
        ));

        // LESSON 2: The Pawn
        List<PiecePlacement> lesson2Setup = new ArrayList<>();
        lesson2Setup.add(new PiecePlacement(PieceColor.WHITE, Pawn.class, new Square(6, 4))); // e2
        lesson2Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(5, 3))); // d3
        lesson2Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(5, 5))); // f3
        lessons.add(new Lesson(
                "2. The Pawn",
                new String[]{
                        "Pawns move straight forward one square, or two squares from their starting position.",
                        "Pawns capture diagonally — one square forward-left or forward-right.",
                        "Pawns cannot move backward and cannot capture pieces directly in front of them."
                },
                PracticeMode.FREE_MOVE,
                lesson2Setup,
                "Click the White pawn and try moving it forward or capturing diagonally."
        ));

        // LESSON 3: The Rook
        List<PiecePlacement> lesson3Setup = new ArrayList<>();
        lesson3Setup.add(new PiecePlacement(PieceColor.WHITE, Rook.class, new Square(4, 3))); // d4
        lesson3Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, 3))); // d7
        lesson3Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(4, 0))); // a4
        lesson3Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(7, 3))); // d1
        lessons.add(new Lesson(
                "3. The Rook",
                new String[]{
                        "The Rook moves any number of squares horizontally or vertically.",
                        "It cannot jump over other pieces — its path must be clear.",
                        "Rooks are powerful in open files and ranks, especially in the endgame."
                },
                PracticeMode.FREE_MOVE,
                lesson3Setup,
                "Click the Rook and see how far it can slide before being blocked."
        ));

        // LESSON 4: The Knight
        List<PiecePlacement> lesson4Setup = new ArrayList<>();
        lesson4Setup.add(new PiecePlacement(PieceColor.WHITE, Knight.class, new Square(4, 3))); // d4
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(5, 1))); // b3
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(3, 1))); // b5
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(6, 2))); // c2
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(2, 2))); // c6
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(6, 4))); // e2
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(2, 4))); // e6
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(5, 5))); // f3
        lesson4Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(3, 5))); // f5
        lessons.add(new Lesson(
                "4. The Knight",
                new String[]{
                        "The Knight moves in an L-shape: two squares in one direction, then one square perpendicular.",
                        "The Knight is the only piece that can jump over other pieces.",
                        "Knights are strongest in the center of the board."
                },
                PracticeMode.FREE_MOVE,
                lesson4Setup,
                "Click the Knight to see all 8 possible moves highlighted at once."
        ));

        // LESSON 5: The Bishop
        List<PiecePlacement> lesson5Setup = new ArrayList<>();
        lesson5Setup.add(new PiecePlacement(PieceColor.WHITE, Bishop.class, new Square(4, 3))); // d4
        lesson5Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(7, 0))); // a1
        lesson5Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, 6))); // g7
        lesson5Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, 0))); // a7
        lesson5Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(7, 6))); // g1
        lessons.add(new Lesson(
                "5. The Bishop",
                new String[]{
                        "The Bishop moves diagonally any number of squares.",
                        "Like the Rook, it cannot jump over pieces.",
                        "A Bishop always stays on the same color square it started on."
                },
                PracticeMode.FREE_MOVE,
                lesson5Setup,
                "Click the Bishop to see its full diagonal range."
        ));

        // LESSON 6: The Queen
        List<PiecePlacement> lesson6Setup = new ArrayList<>();
        lesson6Setup.add(new PiecePlacement(PieceColor.WHITE, Queen.class, new Square(4, 3))); // d4
        lesson6Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(0, 3))); // d8
        lesson6Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(4, 0))); // a4
        lesson6Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(7, 0))); // a1
        lesson6Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, 6))); // g7
        lessons.add(new Lesson(
                "6. The Queen",
                new String[]{
                        "The Queen combines the movement of the Rook and Bishop.",
                        "It can move horizontally, vertically, or diagonally any number of squares.",
                        "The Queen is the most powerful piece on the board."
                },
                PracticeMode.FREE_MOVE,
                lesson6Setup,
                "Click the Queen and explore how many squares it can reach."
        ));

        // LESSON 7: The King
        List<PiecePlacement> lesson7Setup = new ArrayList<>();
        lesson7Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4))); // e1
        lesson7Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(5, 2))); // c3
        lesson7Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(5, 6))); // g3
        lessons.add(new Lesson(
                "7. The King",
                new String[]{
                        "The King moves exactly one square in any direction: horizontal, vertical, or diagonal.",
                        "The King is the most important piece — if it has no way to escape capture, the game ends.",
                        "Keep your King safe; losing it means losing the game."
                },
                PracticeMode.FREE_MOVE,
                lesson7Setup,
                "Click the King and see its one-square range in every direction."
        ));

        // LESSON 8: Castling
        List<PiecePlacement> lesson8Setup = new ArrayList<>();
        lesson8Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4))); // e1
        lesson8Setup.add(new PiecePlacement(PieceColor.WHITE, Rook.class, new Square(7, 7))); // h1
        lesson8Setup.add(new PiecePlacement(PieceColor.WHITE, Rook.class, new Square(7, 0))); // a1
        lesson8Setup.add(new PiecePlacement(PieceColor.BLACK, King.class, new Square(0, 4))); // e8
        lessons.add(new Lesson(
                "8. Castling",
                new String[]{
                        "Castling is a special move involving the King and a Rook.",
                        "Kingside castling: the King moves two squares toward the h-file Rook, and the Rook jumps next to it.",
                        "Queenside castling: the same happens toward the a-file Rook.",
                        "Conditions: neither piece has moved before, no pieces between them, and the King is not in or passing through check."
                },
                PracticeMode.GAME_RULE_DEMO,
                lesson8Setup,
                "Click the King, then click two squares toward a Rook to castle."
        ));

        // LESSON 9: En Passant
        List<PiecePlacement> lesson9Setup = new ArrayList<>();
        lesson9Setup.add(new PiecePlacement(PieceColor.WHITE, Pawn.class, new Square(3, 4))); // e5
        lesson9Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(3, 3))); // d5
        lesson9Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4))); // e1
        lesson9Setup.add(new PiecePlacement(PieceColor.BLACK, King.class, new Square(0, 4))); // e8
        lessons.add(new Lesson(
                "9. En Passant",
                new String[]{
                        "En passant is a special pawn capture.",
                        "If an enemy pawn moves two squares forward and lands beside your pawn, you may capture it as if it had only moved one square.",
                        "This capture must be made immediately on your very next move."
                },
                PracticeMode.GAME_RULE_DEMO,
                lesson9Setup,
                "Click the White pawn on e5 and look for the diagonal capture move onto d6."
        ));

        // LESSON 10: Pawn Promotion
        List<PiecePlacement> lesson10Setup = new ArrayList<>();
        lesson10Setup.add(new PiecePlacement(PieceColor.WHITE, Pawn.class, new Square(1, 0))); // a7
        lesson10Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4))); // e1
        lesson10Setup.add(new PiecePlacement(PieceColor.BLACK, King.class, new Square(0, 4))); // e8
        lessons.add(new Lesson(
                "10. Pawn Promotion",
                new String[]{
                        "When a pawn reaches the opposite end of the board, it must be promoted.",
                        "You can choose to promote it to a Queen, Rook, Bishop, or Knight.",
                        "Most players choose the Queen since it is the most powerful piece."
                },
                PracticeMode.GAME_RULE_DEMO,
                lesson10Setup,
                "Move the pawn forward to a8 and choose a piece to promote to."
        ));

        // LESSON 11: Check
        List<PiecePlacement> lesson11Setup = new ArrayList<>();
        lesson11Setup.add(new PiecePlacement(PieceColor.WHITE, Queen.class, new Square(3, 7))); // h5
        lesson11Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4))); // e1
        lesson11Setup.add(new PiecePlacement(PieceColor.BLACK, King.class, new Square(0, 4))); // e8
        lessons.add(new Lesson(
                "11. Check",
                new String[]{
                        "A King is in 'check' when it is under attack and could be captured next turn.",
                        "When your King is in check, you must respond immediately: move the King, block the attack, or capture the attacking piece.",
                        "You are never allowed to make a move that leaves your own King in check."
                },
                PracticeMode.GAME_RULE_DEMO,
                lesson11Setup,
                "Move the Queen to a square that attacks the Black King and see the red highlight."
        ));

        // LESSON 12: Checkmate
        List<PiecePlacement> lesson12Setup = new ArrayList<>();
        lesson12Setup.add(new PiecePlacement(PieceColor.BLACK, King.class, new Square(0, 7))); // h8
        lesson12Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, 5))); // f7
        lesson12Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, 6))); // g7
        lesson12Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, 7))); // h7
        lesson12Setup.add(new PiecePlacement(PieceColor.WHITE, Rook.class, new Square(7, 0))); // a1
        lesson12Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4))); // e1
        lessons.add(new Lesson(
                "12. Checkmate",
                new String[]{
                        "Checkmate happens when a King is in check and has no legal way to escape.",
                        "This immediately ends the game — the side that delivered checkmate wins.",
                        "Look for positions where the enemy King has no safe squares to move to."
                },
                PracticeMode.GAME_RULE_DEMO,
                lesson12Setup,
                "Move the Rook along the 8th rank to deliver checkmate."
        ));

        // LESSON 13: Stalemate
        List<PiecePlacement> lesson13Setup = new ArrayList<>();
        lesson13Setup.add(new PiecePlacement(PieceColor.BLACK, King.class, new Square(0, 7))); // h8
        lesson13Setup.add(new PiecePlacement(PieceColor.WHITE, Queen.class, new Square(2, 6))); // g6
        lesson13Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4))); // e1
        lessons.add(new Lesson(
                "13. Stalemate",
                new String[]{
                        "Stalemate occurs when a player has no legal moves but their King is NOT in check.",
                        "Unlike checkmate, stalemate ends the game as a DRAW — nobody wins.",
                        "Be careful when you have a big material advantage — an accidental stalemate can throw away a win."
                },
                PracticeMode.GAME_RULE_DEMO,
                lesson13Setup,
                "Notice the Black King has no legal moves here, but it is not in check — that's stalemate."
        ));

        // LESSON 14: Basic Strategy Tips
        List<PiecePlacement> lesson14Setup = new ArrayList<>();
        // Set standard starting position
        for (int i = 0; i < 8; i++) {
            lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Pawn.class, new Square(1, i)));
            lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Pawn.class, new Square(6, i)));
        }
        // Black pieces
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Rook.class, new Square(0, 0)));
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Knight.class, new Square(0, 1)));
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Bishop.class, new Square(0, 2)));
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Queen.class, new Square(0, 3)));
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, King.class, new Square(0, 4)));
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Bishop.class, new Square(0, 5)));
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Knight.class, new Square(0, 6)));
        lesson14Setup.add(new PiecePlacement(PieceColor.BLACK, Rook.class, new Square(0, 7)));

        // White pieces
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Rook.class, new Square(7, 0)));
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Knight.class, new Square(7, 1)));
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Bishop.class, new Square(7, 2)));
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Queen.class, new Square(7, 3)));
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, King.class, new Square(7, 4)));
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Bishop.class, new Square(7, 5)));
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Knight.class, new Square(7, 6)));
        lesson14Setup.add(new PiecePlacement(PieceColor.WHITE, Rook.class, new Square(7, 7)));

        lessons.add(new Lesson(
                "14. Basic Strategy Tips",
                new String[]{
                        "Control the center: pieces in the center of the board have more influence.",
                        "Develop your pieces early: get your Knights and Bishops into play before moving the same piece twice.",
                        "Castle early: get your King to safety before the middle of the game.",
                        "Don't move your Queen out too early — it can be attacked and forced to retreat.",
                        "Think before every capture: ask if it improves your position or just trades material.",
                        "Always check if your move leaves your own King exposed to an attack."
                },
                PracticeMode.FREE_MOVE,
                lesson14Setup,
                "Take your time exploring the starting position before moving to practice."
        ));

        return lessons;
    }

    /**
     * Instantiates a concrete Piece subclass from its Class representation.
     *
     * @param pieceClass the Class of the piece (e.g. Pawn.class)
     * @param color      the PieceColor of the piece
     * @return the concrete Piece instance
     */
    public static Piece newPieceInstance(Class<? extends Piece> pieceClass, PieceColor color) {
        if (pieceClass == Pawn.class) return new Pawn(color);
        if (pieceClass == Knight.class) return new Knight(color);
        if (pieceClass == Bishop.class) return new Bishop(color);
        if (pieceClass == Rook.class) return new Rook(color);
        if (pieceClass == Queen.class) return new Queen(color);
        if (pieceClass == King.class) return new King(color);
        throw new IllegalArgumentException("Unknown piece class: " + pieceClass);
    }
}
