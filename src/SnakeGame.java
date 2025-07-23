import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_UP) {
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyChar() == KeyEvent.VK_DOWN) {
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyChar() == KeyEvent.VK_LEFT) {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyChar() == KeyEvent.VK_RIGHT) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private static class Tile {
        int x;
        int y;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize;

    // snake
    Tile snakeHead;

    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = 25;
        this.velocityX = 0;
        this.velocityY = 1;

        addKeyListener(this);
        setFocusable(true);

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        snakeHead = new Tile(5, 5);
        food = new Tile(0, 0);

        random = new Random();
        placeFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // grid
        for(int i = 0; i < boardHeight / tileSize; i++)
            g.drawLine(0, i *  tileSize, boardWidth, i * tileSize);
        for(int i = 0; i < boardWidth / tileSize; i++)
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);

        // food color
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        // snake color
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
    }

    private void placeFood() {
        food.x = random.nextInt(boardWidth /  tileSize);
        food.y = random.nextInt(boardHeight /   tileSize);
    }

    private void move() {
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    }
}
