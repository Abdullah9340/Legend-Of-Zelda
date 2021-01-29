import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Player implements KeyListener {
    // Declaring Private variables
    private int maxHealth = 4, health = 4;
    private int x = 7, y = 7;
    private int speed = 1;
    private int xVelocity = 0;
    private int yVelocity = 0;

    private int maxArrows = 1;

    // Declare a boolean moving variable
    // Set to false
    private boolean moving = false;

    // Declaring animation time variables
    private int amTime = 0, timeAm = 9;

    // create a 2D array for the player sprite images
    private BufferedImage[][] playerSprite = Assets.player;

    // Direction for player character is set to down
    private char direction = 's';

    /*-
    * render()
    * Pre: None
    * Post: Renders the animation speed and the 
    * player sprites. Player sprites are set to the default
    * location somewhere on the lower half of the screen
    */
    public void render(Graphics g, int selectedWeapon) {
        // Rendering the animation time for the player
        // movements
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
        // Moves the player depending on which direction is set
        // Following methods contain the player controls which
        // set the direction of the player
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

        // If statement that controls the players health
        // the for loop goes through each of the hearts
        // in the health bar
        for (int i = 0; i < maxHealth; i++) {
            if (i + 1 <= health) {

                // The following statements draws a red heart of
                // if the player is hit, then an empty heart
                g.drawImage(Assets.fillheart, i * 20, 0, 32, 32, null);
            } else {
                g.drawImage(Assets.emptyheart, i * 20, 0, 32, 32, null);
            }
        }

        // Draws the player inventory
        drawInv(g, selectedWeapon);
    }

    /*- 
    * update()
    * Pre: None
    * Post: shows that the player moves across the screen
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
    * drawInv()
    * Pre: None
    * Post: Displays the location of the inventory on screen
    * This includes the items in the inventory
    */
    public void drawInv(Graphics g, int selectedWeapon) {
        // sets the color for the following
        g.setColor(Color.black);
        // draws the boxes of the inventory
        for (int i = 0; i < 5; i++) {
            if (i + 1 == selectedWeapon) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.black);
            }
            g.drawRect(300 + (i * 31), LegendOfZelda.HEIGHT - 36, 32, 32);
        }
        // draws the weapons inside the inventory
        g.drawImage(Assets.rasegan, 300 + (0 * 32), LegendOfZelda.HEIGHT - 36, 32, 32, null);
        g.drawImage(Assets.knifeangle, 300 + (1 * 32), LegendOfZelda.HEIGHT - 36, 32, 32, null);
        g.drawImage(Assets.gun, 300 + (2 * 32), LegendOfZelda.HEIGHT - 36, 32, 32, null);
        g.drawImage(Assets.bow, 300 + (3 * 32), LegendOfZelda.HEIGHT - 36, 28, 28, null);
        g.drawImage(Assets.spearangle, 300 + (4 * 32), LegendOfZelda.HEIGHT - 36, 28, 28, null);
    }

    /*-
    * KeyTyped()
    * Pre: None
    * Post: None 
    */
    public void keyTyped(KeyEvent e) {

    }

    /*-
    * KeyPressed()
    * Pre: None
    * Post: Gets the player moving across the screen using
    * specific key presses
    */
    public void keyPressed(KeyEvent e) {
        // If statement with a boolean variable
        // If player is not moving then these conditions
        // Will apply
        if (!moving) {
            // Movement direction (front)
            // set to the letter w or the up arrow on the keyboard
            if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == 38) {
                setYDirection(-speed);
                moving = true;
                direction = 'w';

            }
            // Movement direction (back)
            // set to the letter s or down arrow on the keyboard
            if (e.getKeyChar() == 's' || e.getKeyChar() == 'S' || e.getKeyCode() == 40) {
                setYDirection(speed);
                moving = true;
                direction = 's';

            }
            // Movement direction (left)
            // set to the letter a or left arrow on the keyboard
            if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A' || e.getKeyCode() == 37) {
                setXDirection(-speed);
                moving = true;
                direction = 'a';

            }
            // Movement direction (right)
            // set to the letter d or right arrow on the keyboard
            if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == 39) {
                setXDirection(speed);
                moving = true;
                direction = 'd';

            }
        }
    }

    /*-
    * KeyReleased()
    * Pre: None
    * Post: Makes the player 
    * stop if the designated 
    * direction key is released
    */
    public void keyReleased(KeyEvent e) {
        // player movement stops if W or up arrow is released
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == 38) {
            setYDirection(0);
            moving = false;
            amTime = 0;
            // player movement stops if S or down arrow is released
        } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == 40) {
            setYDirection(0);
            moving = false;
            amTime = 0;
            // player movement stops if A or left arrow is released
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == 37) {
            setXDirection(0);
            moving = false;
            amTime = 0;
            // player movement stops if D or left arrow is released
        } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == 39) {
            setXDirection(0);
            moving = false;
            amTime = 0;
        }
    }

    /*-
    * setYDirection()
    * Pre: None
    * Post: Player chooses the direction that is set by the
    * KeyPressed()
    */
    public void setYDirection(int yDirection) {
        this.yVelocity = yDirection;
    }

    /*-
    * setXDirection()
    * Pre: None
    * Post: Player chooses the direction that is set by the
    * KeyPressed()
    */
    public void setXDirection(int xDirection) {
        this.xVelocity = xDirection;
    }

    /*-
    * move()
    * Pre: None
    * Post: Player moves the direction that is set by the
    * setXDirection() or setYDirection
    */
    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    /*-
        Method: getDirection()
        pre: none
        post: returns the current direction
    */
    public char getDirection() {
        return direction;
    }

    /*
     * Method: getX() pre: none post: returns the current x value
     */
    public int getX() {
        return x;
    }

    /*
     * Method: getY() pre: none post: returns the current y value
     */
    public int getY() {
        return y;
    }

    /*-
        Method: getMaxArrows()
        pre: none
        post: returns the max number of projectiles
    */
    public int getMaxArrows() {
        return maxArrows;
    }

    /*
     * Method: getHealth() pre: none post: returns the players current health
     */
    public int getHealth() {
        return health;
    }

    /*
     * Method: getMaxHealth() pre: none post: returns the players max health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /*
     * Method: setX() pre: x must be a valid coordinate post: sets new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /*-
        Method: setY()
        pre: y must be a valid coordinate
        post: sets new y coord
    */
    public void setY(int y) {
        this.y = y;
    }

    /*-
        Method: setHealth
        pre: health > 0
        post: sets the new health
        
    */
    public void setHealth(int health) {
        this.health = health;
    }

    /*-
    Method: setMaxHealth()
    pre: health > 0
    post: sets the new max health
    
    */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
}
