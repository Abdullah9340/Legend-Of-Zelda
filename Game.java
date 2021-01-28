import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game implements Runnable, KeyListener {
    // Declaring Private Variables
    private int round = 1;
    private Display display;
    private boolean running = false;

    // Variables for graphics objects
    private Graphics g;
    private BufferStrategy bs;

    private final int WIDTH, HEIGHT, FPS = 60;

    private String title;

    private Thread thread;
    private Player player;

    // ArrayList for enemies
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectiles> projectiles;

    // 2D Array that holds all our objects
    private int objects[][];

    // Image for which way the npc is looking
    private BufferedImage npcStance;

    // Variable for if the player died
    private boolean isDead = false;

    // Frame counter for starting new rounds
    private int roundAm = 0;

    /*-
     * Game() 
     * Description: This calls the title, sets the width and height Also
     * starts the game 
     * Pre: the width, height dimensions, title 
     * Post: Starts game
     */
    public Game(String title, int width, int height) {
        this.title = title;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.start();
    }

    /*-
     * init() 
     * Description: This initializes every aspect of the game Creates the
     * objects for the following variables inside the method Displays the JFrame and
     * initializes the KeyListener 
     * Pre: None 
     * Post: Initializes all the variables
     */
    public void init() {
        display = new Display(title, WIDTH, HEIGHT); // Set up display
        Assets.init(); // Sets up the asset images
        npcStance = Assets.npcright; // Sets the default npc stance
        player = new Player();
        enemies = new ArrayList<Enemy>();
        enemies.add(new Enemy()); // Adds one enemmy to start
        projectiles = new ArrayList<Projectiles>();
        display.getJFrame().addKeyListener(player); // Set up keylisteners
        display.getJFrame().addKeyListener(this);
        objects = new int[5][2];
        setObjectCoord(); // Sets the object coordinates
    }

    /*-
     * setObjectCoord() 
     * Description: This method contains a 2D array that holds
     * the sprite objects on to the game
     * frame which the player can interact with 
     * Pre: none 
     * Post: Displays the sprite object
     */
    public void setObjectCoord() {
        // object 1(tree)
        objects[0][0] = 0;
        objects[0][1] = 0;
        // object 2(rock)
        objects[2][0] = 0;
        objects[2][1] = 1;
        // object 3(tree)
        objects[3][0] = 0;
        objects[3][1] = 2;
        // object 4(NPC)
        objects[4][0] = 0;
        objects[4][1] = 3;
    }

    /*-
    * update()
    * Description: This method allows for the movement
    * Render update for all objects in the game
    * Pre : None
    * Post: Makes sure that all user inputs 
    * update the controls within the game
    */
    public void update() {
        if (!isDead) { // Only run if the player is not dead
            player.update(); // Update the player
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).update(player); // Call each enemies update method
            }
            // If the enemy is 0 wait 300 frames(5 seconds)
            // then spawns 1 extra per round
            if (enemies.size() == 0) {
                if (roundAm == 300) {
                    round++;
                    if (round == 10) {

                    }
                    for (int i = 0; i < round; i++) {
                        enemies.add(new Enemy());
                    }
                    roundAm = 0;
                } else {
                    roundAm++;
                }
            }

            // Update each projectile
            for (int i = projectiles.size() - 1; i >= 0; i--) {
                if (projectiles.size() == 0) { // Helps to fix bug
                    break;
                }
                projectiles.get(i).update();
                // If projectile goes off the screen, remove it
                if (projectiles.get(i).getX() > WIDTH / 64 || projectiles.get(i).getX() < -1) {
                    projectiles.remove(i);
                } else if (projectiles.get(i).getY() > HEIGHT / 64 || projectiles.get(i).getY() < -1) {
                    projectiles.remove(i);
                }
            }
            // If the player has 0 health, isDead is true
            if (player.getHealth() == 0) {
                isDead = true;
            }
            checkCollision(); // Check for collisions
        } else {

        }
    }

    /*-
    * GameOver()
    * Description: This screen is visible after the player has died
    * Pre: None
    * Post: Displays a game over screen
    */
    public void GameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        g.drawString("Game Over", WIDTH / 2 / 2, HEIGHT / 2);
    }

    /*-
        Method: BackGroundGo()
        pre: none
        post: draws the gameover background
    */
    public void BackGroundGO(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /*-
        Method:render()
        pre: none
        post: draws the graphics
    */
    public void render() {
        // Set up the graphic objects
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) { // Create a buffer strategy
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT); // Clear the background
        // Draw
        if (!isDead) { // If the player is not dead
            drawBackground(g);// Draw the background
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).render(g); // Render each enemy
            }
            for (int i = 0; i < projectiles.size(); i++) {
                projectiles.get(i).render(g); // Render each projectile
            }
            player.render(g); // Render the player
        } else { // If the player is dead
            BackGroundGO(g);
            GameOver(g);
        }
        // End Draw
        bs.show();
        g.dispose();
    }

    /*-
    * drawBackGround()
    * Description: Method holds the location for the in game
    * objects
    * Pre: None
    * Post: Displays ingame objects
    */
    public void drawBackground(Graphics g) {
        for (int i = 0; i < HEIGHT / 64; i++) {
            for (int j = 0; j < WIDTH / 64; j++) {
                g.drawImage(Assets.grass, j * 64, i * 64, 64, 64, null);
            }
        }
        g.drawImage(Assets.tree, 0, 0, 64, 64, null);
        g.drawImage(Assets.tree, 0, 2 * 64, 64, 64, null);

        g.drawImage(Assets.stone, 0, 1 * 64, 64, 64, null);
        g.drawImage(npcStance, 0, 3 * 64, 64, 64, null);
    }

    /*-
    * checkCollision()
    * Description: This method contains all the necessary x and y
    * coordinates that any character in the game is not allowed to 
    * get in contact with
    * Pre: None
    * Post: Makes sure that the characters on screen are not 
    * just running off the screen and also does not 
    * run through any background objects
    */
    public void checkCollision() {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            for (int j = enemies.size() - 1; j >= 0; j--) {
                if (projectiles.size() != 0) { // If the projectiles are not empty
                    // If the projectiles hits and enemy, remove the projectile and lower enemy
                    // health
                    if (projectiles.get(i).getX() == enemies.get(j).getX()
                            && projectiles.get(i).getY() == enemies.get(j).getY()) {
                        projectiles.remove(i);
                        enemies.get(j).setHealth(enemies.get(j).getHealth() - 1);
                    }
                }
                // If enemey health is 0, remove it
                if (enemies.get(j).getHealth() == 0) {
                    enemies.remove(j);
                    j--;
                }

            }
        }

        // Check of collision of enemy/player with objects
        for (int i = 0; i < objects.length; i++) {
            // If the player is colliding move it back 1 block
            if (player.getX() == objects[i][0] && player.getY() == objects[i][1]) {
                setPlayerCoord(player);
            }
            // Check if enemies are colliding with objects or player
            for (int j = 0; j < enemies.size(); j++) {
                // If the enemy hits an object, move it back a block
                if (enemies.get(j).getX() == objects[i][0] && enemies.get(j).getY() == objects[i][1]) {
                    setEnemyCoord(enemies.get(j));
                }
                // If the enemy hits the player, reduce player health
                if (enemies.get(j).getY() == player.getY() && enemies.get(j).getX() == player.getX()) {
                    player.setHealth(player.getHealth() - 1);
                    setEnemyCoord(enemies.get(j));
                }
            }
        }
        // If the enemies collide with eachother, move one of them back
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = i + 1; j < enemies.size(); j++) {
                if (enemies.get(i).getX() == enemies.get(j).getX() && enemies.get(i).getY() == enemies.get(j).getY()) {
                    setEnemyCoord(enemies.get(i));
                }
            }
        }

        // Prevent player from going off map
        if (player.getX() >= WIDTH / 64) {
            player.setX(WIDTH / 64 - 1);
        }
        if (player.getX() <= 0) {
            player.setX(0);
        }
        if (player.getY() <= 0) {
            player.setY(0);
        }
        if (player.getY() >= HEIGHT / 64) {
            player.setY(HEIGHT / 64 - 1);
        }

    }

    /*- 
    * setPlayerCoord()
    * Description: This method controls the interaction between
    * a playable character any object or enemy on screen
    * Pre: player object
    * Post: Moves player back by one unit of the coordinate
    */
    public void setPlayerCoord(Player player) {
        if (player.getDirection() == 's') {
            player.setY(player.getY() - 1);
        } else if (player.getDirection() == 'w') {
            player.setY(player.getY() + 1);
        } else if (player.getDirection() == 'a') {
            player.setX(player.getX() + 1);
        } else {
            player.setX(player.getX() - 1);
        }
    }

    /*- 
    * setEnemyCoord()
    * Description: This method controls the interaction between
    * an enemy to a playable character  or any object on screen
    * Pre: enemy object
    * Post: Moves player back by one unit of the coordinate
    */
    public void setEnemyCoord(Enemy enemy) {
        if (enemy.getDirection() == 's') {
            enemy.setY(enemy.getY() - 1);
        } else if (enemy.getDirection() == 'w') {
            enemy.setY(enemy.getY() + 1);
        } else if (enemy.getDirection() == 'a') {
            enemy.setX(enemy.getX() + 1);
        } else {
            enemy.setX(enemy.getX() - 1);
        }
    }

    /*-
    * run()
    * Description: Method contains the game loop
    * This makes sure all objets called within this method
    * gets run inside the game loop
    * Game is set to 60fps(tile based)
    * Pre: None
    * Post: Starts the game thread
    */
    public void run() {
        init();

        // Create constant framerate
        double timePerTick = 1000000000 / FPS;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;

            if (delta >= 1) {
                update();
                render();
                delta--;
            }

        }
    }

    /*-
    * start()
    * Description: Starts the whole game
    * initializes the thread
    * Pre: None
    * Post: Starts the game thread 
    */
    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /*-
    * stop()
    * Pre: None
    * Post: Starts the game thread 
    */
    public void stop() {
        if (!running) {
            return;
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    /*-
     * KeyTyped() 
     * Pre: None 
     * Post: If player clicks q The selected weapon of choice
     * gets shot
     */
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'q') {
            if (projectiles.size() < player.getMaxArrows()) {
                projectiles.add(new Projectiles(player, Assets.rasegan, Assets.rasegan, Assets.rasegan, Assets.rasegan,
                        Assets.rasegan));
            }
        }

    }

    @Override
    /*-
    * KeyPressed()
    * Pre: None
    * Post: If the player selects enter
    * Their health bar is regenerated
    */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (player.getX() == 1 && player.getY() == 3 && player.getDirection() == 'a') {
                player.setHealth(player.getMaxHealth());
                npcStance = Assets.npcright;
            } else if (player.getX() == 0 && player.getY() == 4 && player.getDirection() == 'w') {
                player.setHealth(player.getMaxHealth());
                npcStance = Assets.npcdown;
            }
        }
    }

    @Override
    /*- 
    * KeyReleased()
    * Pre: None
    * Post: None
    */
    public void keyReleased(KeyEvent e) {
    }

    /*-
    * getProjectiles()
    * Pre: None
    * Post: Returns projectile
    */
    public ArrayList<Projectiles> getProjectiles() {
        return projectiles;
    }
}
