import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeGame extends JFrame {

    private SnakePanel snakePanel;

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        snakePanel = new SnakePanel();
        add(snakePanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGame();
        });
    }
}
class SnakePanel extends JPanel implements ActionListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int DOT_SIZE = 20;
    private static final int ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private static final int DELAY = 150;

    private int[] x;
    private int[] y;
    private int dots;
    private int appleX;
    private int appleY;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private Timer timer;

    public SnakePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new SnakeKeyListener());
        initGame();
    }

    private void initGame() {
        x = new int[ALL_DOTS];
        y = new int[ALL_DOTS];
        dots = 3;

        for (int i = 0; i < dots; i++) {
            x[i] = 60 - i * DOT_SIZE;
            y[i] = 60;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void locateApple() {
        int r = (int) (Math.random() * (WIDTH / DOT_SIZE));
        appleX = r * DOT_SIZE;

        r = (int) (Math.random() * (HEIGHT / DOT_SIZE));
        appleY = r * DOT_SIZE;
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
            gameOver();
        }

        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            locateApple();
        }
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.PLAIN_MESSAGE);
    }

    private class SnakeKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, DOT_SIZE, DOT_SIZE);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            // Code to display the game over message
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkCollision();
        }
        repaint();
    }


}


