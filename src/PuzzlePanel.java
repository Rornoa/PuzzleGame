import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.*;

public class PuzzlePanel extends JPanel implements KeyListener {

    //Размер нашего экземпляра игры
    private int size;
    // Количество плиток
    private int nbTiles;
    // Размер сетки
    private int dimension;
    // Цвет фишек
    private static final Color TILE_COLOR = new Color(103, 100, 239);
    // Случайный объект, чтобы перемешать плитки
    private static final Random RANDOM = new Random();
    // Хранение плиток в одномерном массиве целых чисел
    private int[] tiles;
    // Размер плитки на интерфейсе
    private int tileSize;
    // Положение пустой плитки
    private int blankPos;
    // параметр для сетки на фрейме
    private int margin;
    // Размер пользовательского интерфейса сетки
    private int gridSize;
    // Переменная для определния окончена игра или нет
    private boolean gameOver;

    private void setBlankPos(int x) { blankPos = x; }

    private void setTiles(int[] x) { tiles = x; }

    private void setGameOver(boolean x) { gameOver = x; }


    PuzzlePanel(int size, int dim, int mar) {
        this.size = size;
        dimension = dim;
        margin = mar;

        nbTiles = size * size - 1; // size-1 поскольку не учитываем пустую ячейку
        tiles = new int[size * size];

        //рассчитываем размер сетки и размер плитки
        gridSize = (dim - 2 * margin);
        tileSize = gridSize / size;

        setPreferredSize(new Dimension(dimension, dimension + margin));
        setBackground(Color.LIGHT_GRAY);
        setForeground(TILE_COLOR);
        setFont(new Font("SansSerif", Font.BOLD, 6*(tileSize/10)));

        gameOver = true;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (gameOver) {
                    newGame();
                } else {

                    // получить позицию нажатия
                    int ex = e.getX() - margin;
                    int ey = e.getY() - margin;

                    // было ли в сетке нажатие ?
                    if (ex < 0 || ex > gridSize || ey < 0 || ey > gridSize)
                        return;

                    // получить положение в сетке
                    int c1 = ex / tileSize;
                    int r1 = ey / tileSize;

                    // получаем позицию пустой ячейки
                    int c2 = blankPos % size;
                    int r2 = blankPos / size;

                    // мы конвертируем в 1D координаты
                    int clickPos = r1 * size + c1;

                    int dir = 0;

                    if (c1 == c2 && Math.abs(r1 - r2) > 0)
                        dir = (r1 - r2) > 0 ? size : -size;
                    else if (r1 == r2 && Math.abs(c1 - c2) > 0)
                        dir = (c1 - c2) > 0 ? 1 : -1;

                    if (dir != 0) {
                        // мы перемещаем плитки в направлении
                        do {
                            int newBlankPos = blankPos + dir;
                            tiles[blankPos] = tiles[newBlankPos];
                            blankPos = newBlankPos;
                        } while (blankPos != clickPos);
                        tiles[blankPos] = 0;
                    }
                    gameOver = isSolved();
                }
                repaint();
            }
        });
        newGame();
    }

    /**
     * newGame - требуется для создания новой игры.
     * Для этого мы сбрасываем игровое поле, затем перетасовываем его и продолжаем до тех пор,
     * пока игровая позиция не будет разрешима.
     */
    void newGame() {
        do {
            reset(); // сброс в исходное состояние
            shuffle();
        } while (!isSolvable());
        repaint();
        gameOver = false;
    }

    /**
     * reset - используемый для инициализации новой игровой позиции.
     * Так мы устанавливаем значение для каждого элемента массива пятнашек
     */
    private void reset() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }
        //помещаем blankPos в последнюю позицию массива.
        blankPos = tiles.length - 1;
    }

    /**
     * shuffle - метод нужен для перетасовки задания нового массива.
     * Мы не включаем пустую пятнашку в процесс перетасовки, чтобы оставить ее в прежнем положении.
     */
    private void shuffle() {
        int n = nbTiles;

        while (n > 1) {
            int r = RANDOM.nextInt(n--);
            int tmp = tiles[r];
            tiles[r] = tiles[n];
            tiles[n] = tmp;
        }
    }

    /**
     * Поскольку только половина возможных стартовых позиций головоломки имеет решение,
     * нужно проверить получившийся результат перемешивания,
     * чтобы убедиться в том, что текущий расклад вообще решаем.
     * Чтобы это сделать, определяем метод isSolvable.
     */
    private boolean isSolvable() {
        int countInversions = 0;

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    countInversions++;
            }
        }
        return countInversions % 2 == 0;
    }

    /**
     * isSolved - позволяет проверить, решена ли наша игра.
     * Сначала мы смотрим, где находится пустая пятнашка.
     * Если в начальной позиции, то текущий расклад — новый, не решенный ранее.
     * Затем мы перебираем плитки в обратном порядке, и, если значение пятнашки отличается от соответствующего индекса +1, возвращаем false.
     * В противном случае в конце метода пора вернуть true, потому что головоломка уже решена.
     */
    private boolean isSolved() {
        if (tiles[tiles.length - 1] != 0)
            return false;

        for (int i = nbTiles - 1; i >= 0; i--) {
            if (tiles[i] != i + 1)
                return false;
        }
        return true;
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < tiles.length; i++) {
            // мы конвертируем 1D координаты в 2D координаты, учитывая размер 2D массива
            int r = i / size;
            int c = i % size;
            // мы конвертируем в координаты на интерфейсе
            int x = margin + c * tileSize;
            int y = margin + r * tileSize;

            // проверка специального случая для пустой плитки
            if (tiles[i] == 0) {
                if (gameOver) {
                    g.setColor(TILE_COLOR);
                    drawCenteredString(g, "\u2714", x, y);
                }
                continue;
            }

            g.setColor(getForeground());
            g.fillRoundRect(x, y, tileSize, tileSize, 35, 35);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, tileSize, tileSize, 35, 35);
            g.setColor(Color.WHITE);

            drawCenteredString(g, String.valueOf(tiles[i]), x, y);
        }
    }

    private void drawWinMessage(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(TILE_COLOR);
            String s = "Click or press button to start new game";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {

        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s, x + (tileSize - fm.stringWidth(s)) / 2,
                y + (asc + (tileSize - (asc + desc)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2D);
        drawWinMessage(g2D);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) newGame();

        int x = blankPos % size;//положение пустой клетки на данный момент
        int y = blankPos / size;
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_DOWN:
                if (y <= 3 && 0 < y) {
                    int newBlankPosition = blankPos - size;
                    tiles[blankPos] = tiles[newBlankPosition];
                    blankPos = newBlankPosition;
                    tiles[blankPos] = 0;
                    setBlankPos(blankPos);
                    setTiles(tiles);

                }
                break;
            case KeyEvent.VK_UP:
                if (y < 3 && 0 <= y) {
                    int newBlankPosition = blankPos + size;
                    tiles[blankPos] = tiles[newBlankPosition];
                    blankPos = newBlankPosition;
                    tiles[blankPos] = 0;
                    setBlankPos(blankPos);
                    setTiles(tiles);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (x < 3 && 0 <= x) {
                    int newBlankPosition = blankPos + 1;
                    tiles[blankPos] = tiles[newBlankPosition];
                    blankPos = newBlankPosition;
                    tiles[blankPos] = 0;
                    setBlankPos(blankPos);
                    setTiles(tiles);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (x <= 3 && 0 < x) {
                    int newBlankPosition = blankPos - 1;
                    tiles[blankPos] = tiles[newBlankPosition];
                    blankPos = newBlankPosition;
                    tiles[blankPos] = 0;
                    setBlankPos(blankPos);
                    setTiles(tiles);
                }
                break;
        }
        gameOver = isSolved();
        setGameOver(gameOver);
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
