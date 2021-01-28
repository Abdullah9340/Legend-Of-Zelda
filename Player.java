import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Player implements KeyListener {
    private int maxHealth = 10, health = 10;
    private int x = 7, y = 7;
    private int speed = 1;
    private int xVelocity = 0;
    private int yVelocity = 0;

    private int maxArrows = 1;

    private boolean moving = false;

    private int amTime = 0, timeAm = 9;
    private BufferedImage[][] playerSprite = Assets.player;

    private char direction = 's';

    public void render(Graphics g) {
        int renderAm = 0;
        if (moving) {
            if (amTime == 0) {
                renderAm = 0;
            } else if (amTime <= 6) {
                renderAm = 1;
            } else {
                renderAm = 2;
            }
        }
        if (direction == 'w') {
            g.drawImage(playerSprite[3][renderAm], x * 64 + 8, y * 64 + 8 - renderAm * 5, 48, 48, null);
        } else if (direction == 's') {
            g.drawImage(playerSprite[0][renderAm], x * 64 + 8, y * 64 + 8 + renderAm * 5, 48, 48, null);
        } else if (direction == 'a') {
            g.drawImage(playerSprite[1][renderAm], x * 64 + 8 - renderAm * 5, y * 64 + 8, 48, 48, null);
        } else if (direction == 'd') {
            g.drawImage(playerSprite[2][renderAm], x * 64 + 8 + renderAm * 5, y * 64 + 8, 48, 48, null);
        }

        // Render health
        if (health < 0) {
            health = 0;
        }

        for (int i = 0; i < maxHealth; i++) {
            if (i + 1 <= health) {
                g.drawImage(Assets.fillheart, i * 20, 0, 32, 32, null);
            } else {
                g.drawImage(Assets.emptyheart, i * 20, 0, 32, 32, null);
            }
        }
    }

    public void update() {
        if (amTime > timeAm) {
            move();
            amTime = 0;
        } else {
            amTime++;
        }

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (!moving) {
            if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == 38) {
                if (direction == 'w') {
                    setYDirection(-speed);
                    moving = true;
                } else {
                    direction = 'w';
                }
            }
            if (e.getKeyChar() == 's' || e.getKeyChar() == 'S' || e.getKeyCode() == 40) {
                if (direction == 's') {
                    setYDirection(speed);
                    moving = true;
                } else {
                    direction = 's';
                }

            }
            if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A' || e.getKeyCode() == 37) {
                if (direction == 'a') {
                    setXDirection(-speed);
                    moving = true;
                } else {
                    direction = 'a';
                }
            }
            if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == 39) {
                if (direction == 'd') {
                    setXDirection(speed);
                    moving = true;
                } else {
                    direction = 'd';
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == 38) {
            setYDirection(0);
            moving = false;
            amTime = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == 40) {
            setYDirection(0);
            moving = false;
            amTime = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == 37) {
            setXDirection(0);
            moving = false;
            amTime = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == 39) {
            setXDirection(0);
            moving = false;
            amTime = 0;
        }
    }

    public void setYDirection(int yDirection) {
        this.yVelocity = yDirection;
    }

    public void setXDirection(int xDirection) {
        this.xVelocity = xDirection;
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    public char getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMaxArrows() {
        return maxArrows;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
