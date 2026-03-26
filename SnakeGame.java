import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {

    private static final int TILE_SIZE = 25;
    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 600;

    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;

    private Timer timer;
    private JButton restartButton;

    private int score = 0;
    private int highScore = 0;

    public SnakeGame() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);

        restartButton = new JButton("Restart");
        restartButton.setBounds(230, 320, 140, 40);
        restartButton.setVisible(false);

        restartButton.addActionListener(e -> {
            startGame();
            restartButton.setVisible(false);
            requestFocusInWindow();
        });

        add(restartButton);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {

            @Override 
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> { if (direction != 'D') direction = 'U'; }
                    case KeyEvent.VK_DOWN -> { if (direction != 'U') direction = 'D'; }
                    case KeyEvent.VK_LEFT -> { if (direction != 'R') direction = 'L'; }
                    case KeyEvent.VK_RIGHT -> { if (direction != 'L') direction = 'R'; }
                }
            }
        });

        startGame();
    }

    private void startGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));

        direction = 'R';
        running = true;
        score = 0;

        spawnFood();

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(150, this);
        timer.start();

        requestFocusInWindow();
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(
                rand.nextInt(GAME_WIDTH / TILE_SIZE),
                rand.nextInt(GAME_HEIGHT / TILE_SIZE)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running) return;

        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U' -> newHead.y--;
            case 'D' -> newHead.y++;
            case 'L' -> newHead.x--;
            case 'R' -> newHead.x++;
        }

        if (newHead.x < 0 || newHead.y < 0 ||
                newHead.x >= GAME_WIDTH / TILE_SIZE ||
                newHead.y >= GAME_HEIGHT / TILE_SIZE ||
                snake.contains(newHead)) {

            running = false;
            timer.stop();

            if (score > highScore) {
                highScore = score;
            }

            restartButton.setVisible(true);
            repaint();
            return;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
            score += 10;

            int delay = Math.max(50, 150 - (score / 10) * 5);
            timer.setDelay(delay);

        } else {
            snake.remove(snake.size() - 1);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("High Score: " + highScore, 10, 40);

        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        if (!running) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", GAME_WIDTH / 2 - 80, GAME_HEIGHT / 2 - 40);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Final Score: " + score, GAME_WIDTH / 2 - 70, GAME_HEIGHT / 2);
            g.drawString("High Score: " + highScore, GAME_WIDTH / 2 - 70, GAME_HEIGHT / 2 + 25);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}