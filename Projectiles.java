import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Projectiles {

    // Declare variables
    private int x, y;
    private int xVelocity, yVelocity;
    private int amTime = 0, timeAm = 3; // Time per frame for projectile to move
    private char direction; // Direction of projectile

    private int vXSize, vYSize, hXSize, hYSize;

    private BufferedImage up, down, right, left; // Images for each sprite

    /*-
     * Method: Projectiles() 
     * pre: none 
     * post: assigns all the variables needed to
     * render and update the projectile
     */
    public Projectiles(Player player, BufferedImage up, BufferedImage down, BufferedImage right, BufferedImage left,
            int vXSize, int vYSize, int hXSize, int hYSize) {

        this.vXSize = vXSize;
        this.vYSize = vYSize;
        this.hXSize = hXSize;
        this.hYSize = hYSize;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
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

    /*-
        Method: update()
        pre: none
        post: updates and moves the projectile
    */
    public void update() {
        if (amTime > timeAm) {
            move();
            amTime = 0;
        } else {
            amTime++;
        }

    }

    /*-
        Method: render()
        pre: none
        post: renders the projectile
    */
    public void render(Graphics g) {
        if (direction == 'w') {
            g.drawImage(up, x * 64 + 24, y * 64 + 16, vXSize, vYSize, null);
        } else if (direction == 's') {
            g.drawImage(down, x * 64 + 24, y * 64 + 16, vXSize, vYSize, null);

        } else if (direction == 'a') {
            g.drawImage(left, x * 64 + 16, y * 64 + 24, hXSize, hYSize, null);
        } else {
            g.drawImage(right, x * 64 + 16, y * 64 + 24, hXSize, hYSize, null);
        }
    }

    /*-
        Method: move()
        pre: none
        post: moves the projectile by x and y velocity
    */
    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    /*-
        Method: getX()
        pre: none
        post: returns x coord
    */
    public int getX() {
        return x;
    }

    /*-
        Method: getY()
        pre: none
        post: return y coord
    */
    public int getY() {
        return y;
    }

}
