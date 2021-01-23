import javax.swing.*;
import java.awt.*;

class SimpleFrame extends JFrame {

    int width = Main.sSize.width;
    int height = Main.sSize.height;

    SimpleFrame() {
        setTitle("Puzzle game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(width / 2 - width / 3, height / 2 - height / 6, width / 4, height / 2);
        PuzzlePanel puzzlePanel = new PuzzlePanel(4, 550 ,30);
        addKeyListener(puzzlePanel);
        add(puzzlePanel, BorderLayout.CENTER);
        pack();
        setJMenuBar(new GameMenuBar(puzzlePanel));
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
