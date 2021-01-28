import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Projectiles {

    private int x, y;
    private int xVelocity, yVelocity;
    private int amTime = 0, timeAm = 3;
    private char direction;

    private BufferedImage invImage = Assets.knifeangle;

    private BufferedImage up = Assets.knifeup, down = Assets.knifedown, left = Assets.knifeleft,
            right = Assets.kniferight;

    public Projectiles(Player player) {
        if (player.getDirection() == 'w') {
            yVelocity = -1;
            xVelocity = 0;
        } else if (player.getDirection() == 's') {
            yVelocity = 1;
            xVelocity = 0;
        } else if (player.getDirection() == 'a') {
            yVelocity = 0;
            xVelocity = -1;
        } else if (player.getDirection() == 'd') {
            yVelocity = 0;
            xVelocity = 1;
        }
        direction = player.getDirection();
        x = player.getX();
        y = player.getY();

    }

    public void update() {
        if (amTime > timeAm) {
            move();
            amTime = 0;
        } else {
            amTime++;
        }

    }

    public void render(Graphics g) {
        if (direction == 'w') {
            g.drawImage(up, x * 64 + 24, y * 64 + 16, 16, 32, null);
        } else if (direction == 's') {
            g.drawImage(down, x * 64 + 24, y * 64 + 16, 16, 32, null);

        } else if (direction == 'a') {
            g.drawImage(left, x * 64 + 16, y * 64 + 24, 32, 16, null);
        } else {
            g.drawImage(right, x * 64 + 16, y * 64 + 24, 32, 16, null);
        }
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
