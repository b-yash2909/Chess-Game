package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

/**
 * Modal dialog prompting the user to choose a piece for Pawn promotion.
 */
public class PromotionDialog extends JDialog {
    private Class<? extends Piece> chosenPieceClass = Queen.class;

    /**
     * Constructs a PromotionDialog.
     *
     * @param parent the parent frame
     * @param color the color of the promoting pawn/piece
     */
    public PromotionDialog(Frame parent, PieceColor color) {
        super(parent, "Pawn Promotion", true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLayout(new FlowLayout());

        String queenSymbol = color == PieceColor.WHITE ? "♕" : "♛";
        String rookSymbol = color == PieceColor.WHITE ? "♖" : "♜";
        String bishopSymbol = color == PieceColor.WHITE ? "♗" : "♝";
        String knightSymbol = color == PieceColor.WHITE ? "♘" : "♞";

        add(createPromotionButton(queenSymbol, Queen.class));
        add(createPromotionButton(rookSymbol, Rook.class));
        add(createPromotionButton(bishopSymbol, Bishop.class));
        add(createPromotionButton(knightSymbol, Knight.class));

        pack();
        setLocationRelativeTo(parent);
    }

    private JButton createPromotionButton(String symbol, Class<? extends Piece> pieceClass) {
        JButton button = new JButton(symbol);
        button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 40));
        button.setPreferredSize(new Dimension(80, 80));
        button.addActionListener(e -> {
            chosenPieceClass = pieceClass;
            dispose();
        });
        return button;
    }

    /**
     * Gets the chosen piece class.
     *
     * @return the selected Piece subclass
     */
    public Class<? extends Piece> getChosenPieceClass() {
        return chosenPieceClass;
    }
}
