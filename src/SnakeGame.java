import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if(gameOver)
            gameLoop.stop();
        repaint();
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
    ArrayList<Tile> snakeBody;

    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileSize = 25;
        this.velocityX = 0;
        this.velocityY = 1;

        this.gameOver = false;

        addKeyListener(this);
        setFocusable(true);

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
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

    private void fillTile(Graphics g, Tile tile) {
//        g.fillRect(tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
        g.fill3DRect(tile.x * tileSize, tile.y * tileSize, tileSize, tileSize, true);
    }

    private void draw(Graphics g) {
        // grid
//        for(int i = 0; i < boardHeight / tileSize; i++)
//            g.drawLine(0, i *  tileSize, boardWidth, i * tileSize);
//        for(int i = 0; i < boardWidth / tileSize; i++)
//            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);

        // food color
        g.setColor(Color.red);
        fillTile(g, food);

        // snake color
        g.setColor(Color.green);
        fillTile(g, snakeHead);
        for(Tile tile : snakeBody) {
            fillTile(g, tile);
        }

        // score
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if(gameOver) {
            g.setColor(Color.red);
            g.drawString("GAME OVER (final score: " + snakeBody.size() + ")", 20, 30);
        } else {
            g.setColor(Color.green);
            g.drawString("Score: " + snakeBody.size(), 20, 30);
        }

    }

    private void placeFood() {
        food.x = random.nextInt(boardWidth /  tileSize);
        food.y = random.nextInt(boardHeight /   tileSize);
    }

    private void move() {
        if(collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for(int i = snakeBody.size() - 1; i - 1 >= 0; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
        if(!snakeBody.isEmpty()) {
            snakeBody.getFirst().x = snakeHead.x;
            snakeBody.getFirst().y = snakeHead.y;
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // game over condition

        // collision by its self
        for (Tile tile : snakeBody) {
            if (collision(snakeHead, tile)) {
                gameOver = true;
                break;
            }
        }
        // collision by wall
        if(snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    private boolean collision(Tile tile1,  Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(velocityY == 0 && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)) {
            this.velocityX = 0;
            this.velocityY = -1;
        }
        else if(velocityY == 0 && (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)) {
            velocityX = 0;
            velocityY = 1;
        }
        else if(velocityX == 0 && (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)) {
            velocityX = -1;
            velocityY = 0;
        }
        else if(velocityX == 0 && (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)) {
            velocityX = 1;
            velocityY = 0;
        }
    }
}
