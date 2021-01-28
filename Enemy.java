
import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {
    // Declare variables
    private int health = 2;
    private int x, y; // x and y coords
    private int yVelocity = 0, xVelocity = 0; // x and y velocity
    private char direction = 'w'; // The direction the enemy is currently facing
    // amTime is how many frames have passed and timeAm is how many frames must pass
    // for the enemy to be able to move
    private int amTime = 0, timeAm = 30;

    // Sprites for the eneemy
    private BufferedImage[][] enemySprites = Assets.darksoildersprite;

    /*-
        Method: Enemy()
        pre: none
        post: sets a random starting position 
    */

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

    /*-
        Method: update()
        pre: none
        post: Moves the enemy towards the player
    */
    public void update(Player player) {
        if (amTime > timeAm) {
            calculateNextMove(player);
            move();
            amTime = 0;
        } else {
            amTime++;
        }
    }

    /*-
        Method: calculateNextMove()
        pre: none
        post: determines which tile the enemy should move to
    */

    public void calculateNextMove(Player player) {
        // First is to make them be on the same x-axis
        if (x == player.getX()) { // If they are set up the y axis
            if (y == player.getY()) { // If the enemey is already on the player dont mvoe
                setEDirection(-1);
            }
            if (y > player.getY()) { // If the enemy is below the player, move up
                setEDirection(1);
            }
            if (y < player.getY()) { // If the enemy is above the player move down
                setEDirection(0);
            }
        } else { // Make the enemy be on the same x-axis as player
            if (x > player.getX()) { // If the enemy is to the left, move right
                setEDirection(3);
            } else { // Otherwise move left
                setEDirection(2);
            }
        }

    }

    /*-
        Method: render();
        pre: none
        post: renders the player
    */
    public void render(Graphics g) {
        int renderAm = 0; // Determines which moving animation to use
        if (amTime < 10) { // If less than 10 frames, use the standstill sprite
            renderAm = 0;
        } else if (amTime < 20) { // If greater than 10 but less than 20, use the walking 1 sprite
            renderAm = 1;
        } else { // Else using walking 2 sprite
            renderAm = 2;
        }

        if (direction == 'w') { // If the direction is up, render the looking up sprite
            g.drawImage(enemySprites[1][renderAm], x * 64, y * 64 - renderAm * 16, 64, 64, null);
        } else if (direction == 's') { // Render looking down sprite
            g.drawImage(enemySprites[0][renderAm], x * 64, y * 64 + renderAm * 16, 64, 64, null);
        } else if (direction == 'a') { // Render looking left sprite
            g.drawImage(enemySprites[3][renderAm], x * 64 - renderAm * 16, y * 64, 64, 64, null);
        } else { // Render looking right sprite
            g.drawImage(enemySprites[2][renderAm], x * 64 + renderAm * 16, y * 64, 64, 64, null);
        }
    }

    /*-
        Method: setEDirection();
        pre: none
        post: sets the velocity of the enemy depending on the number passed in
    */
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

    /*
     * Method: move() pre: none post: Moves the enemy by its y and x velocity, also
     * creates a border for the enemy
     */
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

    /*-
        Method: getX()
        pre: none
        post: returns the x position
    */
    public int getX() {
        return x;
    }

    /*-
        Method: getY()
        pre: none
        post: returns the y position
    */
    public int getY() {
        return y;
    }

    /*-
        Method: setX()
        pre: none
        post: sets the x position
    */
    public void setX(int x) {
        this.x = x;
    }

    /*-
        Method: setY()
        pre: none
        post: sets the y position
    */
    public void setY(int y) {
        this.y = y;
    }

    /*-
        Method: getDirection()
        pre: none
        post: returns the direction the enemy is facing
    */
    public char getDirection() {
        return direction;
    }

    /*-
        Method: getHealth()
        pre: none
        post: returns the current health
    */
    public int getHealth() {
        return health;
    }

    /*-
        Method: setHealth()
        pre: health > 0
        post: sets the health
    */
    public void setHealth(int health) {
        this.health = health;
    }
}
