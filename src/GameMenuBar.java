import java.awt.event.*;
import javax.swing.*;

class GameMenuBar extends JMenuBar {
    JLabel jlab;

    private final  String   TITLE_confirm = "Окно подтверждения";

    GameMenuBar(PuzzlePanel puzzlePanel) {
        jlab = new JLabel();

        JMenu jmGame = new JMenu("Game");
        jmGame.setMnemonic(KeyEvent.VK_G);
        add(jmGame);

        JMenuItem jmiNew = new JMenuItem("New");
        jmiNew.setMnemonic(KeyEvent.VK_N);
        KeyStroke ctrlN = KeyStroke.getKeyStroke("ctrl N");
        jmiNew.setAccelerator(ctrlN);
        jmGame.add(jmiNew);
        jmiNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        GameMenuBar.this,
                        "Весь процесс удалится, хотите начать новую игру?",
                        TITLE_confirm,
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    puzzlePanel.newGame();
            }
        });

        jmGame.addSeparator();

        JMenuItem jmiExit = new JMenuItem("Exit");
        jmiExit.setMnemonic(KeyEvent.VK_E);
        KeyStroke ctrlE = KeyStroke.getKeyStroke("ctrl E");
        jmiExit.setAccelerator(ctrlE);
        jmGame.add(jmiExit);
        jmiExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        GameMenuBar.this,
                        "Весь процесс удалится, хотите выйти?",
                        TITLE_confirm,
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });

        JMenu jmAbout = new JMenu("About");
        jmAbout.setMnemonic(KeyEvent.VK_A);
        add(jmAbout);

        JMenuItem jmiRules = new JMenuItem("Rules");
        jmiRules.setMnemonic(KeyEvent.VK_R);
        KeyStroke altR = KeyStroke.getKeyStroke("alt R");
        jmiRules.setAccelerator(altR);
        jmAbout.add(jmiRules);
        jmiRules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GameMenuBar.this,
                        "<html><h2>Правила игры</h2><i>Чтобы завершить игру необходимо собрать числа по порядку от 1 до 15</i>");
            }
        });

        jmAbout.addSeparator();

        JMenuItem jmiInfo = new JMenuItem("Info");
        jmiInfo.setMnemonic(KeyEvent.VK_I);
        KeyStroke altI = KeyStroke.getKeyStroke("alt I");
        jmiInfo.setAccelerator(altI);
        jmAbout.add(jmiInfo);
        jmiInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GameMenuBar.this,
                        "<html><h2>Работу выполнил</h2><i>Терехов Никита Геннадьевич P3169</i>");
            }
        });
    }
}

