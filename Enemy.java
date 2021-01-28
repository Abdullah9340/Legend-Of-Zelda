
import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {
    private int health = 2;
    private int x, y;
    private int yVelocity = 0, xVelocity = 0;
    private char direction = 'w';

    private int amTime = 0, timeAm = 40;
    private BufferedImage[][] enemySprites = Assets.darksoildersprite;

    public void update(Player player) {
        if (amTime > timeAm) {
            calculateNextMove(player);
            move();
            amTime = 0;
        } else {
            amTime++;
        }
    }

    public void calculateNextMove(Player player) {
        if (x == player.getX()) {
            if (y == player.getY()) {
                setEDirection(-1);
            }
            if (y > player.getY()) {
                setEDirection(1);
            }
            if (y < player.getY()) {
                setEDirection(0);
            }
        } else {
            if (x > player.getX()) {
                setEDirection(3);
            } else {
                setEDirection(2);
            }
        }

    }

    public void render(Graphics g) {
        int renderAm = 0;
        if (amTime < 10) {
            renderAm = 0;
        } else if (amTime < 30) {
            renderAm = 1;
        } else {
            renderAm = 2;
        }

        if (direction == 'w') {
            g.drawImage(enemySprites[1][renderAm], x * 64, y * 64 - renderAm * 16, 64, 64, null);
        } else if (direction == 's') {
            g.drawImage(enemySprites[0][renderAm], x * 64, y * 64 + renderAm * 16, 64, 64, null);
        } else if (direction == 'a') {
            g.drawImage(enemySprites[3][renderAm], x * 64 - renderAm * 16, y * 64, 64, 64, null);
        } else {
            g.drawImage(enemySprites[2][renderAm], x * 64 + renderAm * 16, y * 64, 64, 64, null);
        }
    }

    public Enemy() {
        int position = (int) (Math.random() * 2);
        if (position == 0) {
            y = 0;
            x = (int) (Math.random() * LegendOfZelda.WIDTH / 64);
        } else {
            x = LegendOfZelda.WIDTH / 64 + 1;
            y = (int) (Math.random() * LegendOfZelda.HEIGHT / 64);
        }
    }

    public void setEDirection(int eDirect) {
        if (eDirect == 0) {
            yVelocity = 1;
            xVelocity = 0;
            direction = 's';
        } else if (eDirect == 1) {
            yVelocity = -1;
            xVelocity = 0;
            direction = 'w';
        } else if (eDirect == 2) {
            yVelocity = 0;
            xVelocity = 1;
            direction = 'd';
        } else if (eDirect == 3) {
            yVelocity = 0;
            xVelocity = -1;
            direction = 'a';
        } else {
            yVelocity = 0;
            xVelocity = 0;
        }
    }

    public void move() {
        y += yVelocity;
        if (y >= LegendOfZelda.HEIGHT / 64 - 1) {
            y = LegendOfZelda.HEIGHT / 64 - 1;
        } else if (y <= 0) {
            y = 0;
        }
        x += xVelocity;
        if (x >= LegendOfZelda.WIDTH / 64 - 1) {
            x = LegendOfZelda.WIDTH / 64 - 1;
        } else if (x <= 0) {
            x = 0;
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getDirection() {
        return direction;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
