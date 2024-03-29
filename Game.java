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
    private int kills = 0; // Number of kills the player has
    private Display display;
    private boolean running = false; // Is the program running
    private int selectedWeapon = 1; // The weapon that is currently selected
    private boolean won = false; // Has the player won
    private boolean isMenu = true; // Is the player in the menu state

    // Variables for framerate
    private double timePerTick, delta;
    long now, lastTime;
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
        this.start(); // Start the thread/game
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
        Music.backgroundMusic(); // Start the background music
        display = new Display(title, WIDTH, HEIGHT); // Set up display
        Assets.init(); // Sets up the asset images
        npcStance = Assets.npcright; // Sets the default npc stance
        player = new Player();
        enemies = new ArrayList<Enemy>(); // Create arraylist of enemies
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
     * the location of the sprite objects on to the game
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
        if (won) { // If the player has won the game, nothing to update

        } else if (!isDead) { // Only run if the player is not dead
            player.update(); // Update the player
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).update(player); // Call each enemies update method
            }
            // If the enemy is 0 wait 300 frames(5 seconds)
            // then spawns 1 extra per round
            if (enemies.size() == 0) {
                if (roundAm == 300) {
                    round++; // Start the next round
                    if (round == 11) {
                        won = true; // If player hits round 11, they won
                    }
                    for (int i = 0; i < round; i++) {
                        enemies.add(new Enemy()); // Spawn enemies
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
                projectiles.get(i).update(); // Update each projectile (Move it)
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
        }
    }

    /*-
    * GameOver()
    * Description: This screen is visible after the player has died
    * Pre: None
    * Post: Displays a game over screen
    */
    public void gameOver(Graphics g) {
        g.drawImage(Assets.gameover, 0, 0, 768, 576, null);
        g.setColor(new Color(180, 0, 0));
        g.setFont(new Font("Old English Text MT", Font.PLAIN, 60));
        g.drawString(round + "", WIDTH / 2 + 210, HEIGHT - 90);
    }

    /*-
        Method: BackGroundGo()
        pre: none
        post: draws the gameover background
    */
    public void backGroundGO(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void gameWin(Graphics g) {
        g.drawImage(Assets.gamewon, 0, 0, 768, 576, null);
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
        if (won) { // If the player has won, draw the game won screen
            backGroundGO(g);
            gameWin(g);
        } else if (!isDead) { // If the player is not dead
            drawBackground(g);// Draw the background
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).render(g); // Render each enemy
            }
            for (int i = 0; i < projectiles.size(); i++) {
                projectiles.get(i).render(g); // Render each projectile
            }
            player.render(g, selectedWeapon); // Render the player
            drawRound(g);
        } else { // If the player is dead
            backGroundGO(g);
            gameOver(g);
        }
        // End Draw
        bs.show();
        g.dispose();
    }

    /*-
        Method: drawRound()
        pre: none
        post: shows the current round number on the screen
    */
    public void drawRound(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        String roundNumber = "Round: " + round;
        g.drawString(roundNumber, WIDTH - 100, 30);
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
        g.drawImage(npcStance, 10, 3 * 64, 48, 48, null);
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
        // Loop through every projectile and enemy to check for collision
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            for (int j = enemies.size() - 1; j >= 0; j--) {
                if (projectiles.size() != 0) {
                    // If the projectiles are not empty
                    // If the projectiles hits and enemy, remove the projectile and lower enemy
                    // health
                    if (projectiles.get(i).getX() == enemies.get(j).getX()
                            && projectiles.get(i).getY() == enemies.get(j).getY()) {
                        projectiles.remove(i); // If the projectile hits the enemy, remove the projectile
                        // If the enemy gets hit, decrease its health
                        enemies.get(j).setHealth(enemies.get(j).getHealth() - 1);
                    }
                }
                // If enemey health is 0, remove it
                if (enemies.get(j).getHealth() == 0) {
                    enemies.remove(j);
                    j--; // To prevent out of bounds exception
                    kills++; // Update player kills
                    // Every 3 kills increase player max health
                    if (kills % 3 == 0 && player.getMaxHealth() < 10) {
                        player.setMaxHealth(player.getMaxHealth() + 1);
                        player.setHealth(player.getHealth() + 1);
                    }
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
                    setEnemyCoord(enemies.get(i)); // Move the enemy back 1 block
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

    public void menuRender() {
        // Set up the graphic objects
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) { // Create a buffer strategy
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT); // Clear the background
        // Draw
        g.drawImage(Assets.menuScreen, 0, 0, WIDTH, HEIGHT, null); // Draw the menu image
        // End Draw
        bs.show();
        g.dispose();
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
        // Main game loop
        while (running) {
            // If the player is in the menu state
            if (isMenu) {
                menuRender();
            } else { // If the player is has started the game
                now = System.nanoTime();
                delta += (now - lastTime) / timePerTick;
                lastTime = now;

                // Main game loop
                if (delta >= 1) {
                    update();
                    render();
                    delta--;
                }
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
        thread.start(); // Start the thread
    }

    /*-
    * stop()
    * Pre: None
    * Post: Starts the game thread 
    */
    public void stop() { // Stop the thread
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
        if (isMenu) {
            isMenu = false;
            timePerTick = 1000000000 / FPS;
            delta = 0;
            lastTime = System.nanoTime();
        } else {
            // user presses q to shoot a projectile
            if (e.getKeyChar() == 'q' || e.getKeyChar() == 'Q') {
                if (projectiles.size() < player.getMaxArrows()) {
                    // user selects the weapon
                    // if user selects 1
                    // inventory slot weapon 1 is used
                    if (selectedWeapon == 1) {
                        projectiles.add(new Projectiles(player, Assets.rasegan, Assets.rasegan, Assets.rasegan,
                                Assets.rasegan, 32, 32, 32, 32));
                        Music.projectileNoise("Assets/projectile_noise.wav");
                        // if user selects 2
                        // inventory slot weapon 2 is used
                    } else if (selectedWeapon == 2) {
                        projectiles.add(new Projectiles(player, Assets.knifeup, Assets.knifedown, Assets.kniferight,
                                Assets.knifeleft, 16, 32, 32, 16));
                        Music.projectileNoise("Assets/projectile_noise.wav");
                        // if user selects 3
                        // inventory slot weapon 3 is used
                    } else if (selectedWeapon == 3) {
                        projectiles.add(new Projectiles(player, Assets.bullet, Assets.bullet, Assets.bullet,
                                Assets.bullet, 32, 32, 32, 32));
                        Music.projectileNoise("Assets/projectile_noise.wav");
                        // if user selects 4
                        // inventory slot weapon 4 is used
                    } else if (selectedWeapon == 4) {
                        projectiles.add(new Projectiles(player, Assets.arrowup, Assets.arrowdown, Assets.arrowright,
                                Assets.arrowleft, 16, 32, 32, 16));
                        Music.projectileNoise("Assets/projectile_noise.wav");
                        // if user selects 5
                        // inventory slot weapon 5 is used
                    } else if (selectedWeapon == 5) {
                        projectiles.add(new Projectiles(player, Assets.spearup, Assets.speardown, Assets.spearright,
                                Assets.spearleft, 32, 32, 32, 32));
                        Music.projectileNoise("Assets/projectile_noise.wav");
                    }
                }
            }

            // user selects the weapon of choice
            // each choice correlates with the inventory slot number
            // value of "selected Weapon" is then used to
            // identify which weapon is being used
            if (e.getKeyChar() == '1') {
                selectedWeapon = 1;
            } else if (e.getKeyChar() == '2') {
                selectedWeapon = 2;
            } else if (e.getKeyChar() == '3') {
                selectedWeapon = 3;
            } else if (e.getKeyChar() == '4') {
                selectedWeapon = 4;
            } else if (e.getKeyChar() == '5') {
                selectedWeapon = 5;
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
        if (!isMenu) {
            // user presses enter to interact with the NPC
            // if enter is pressed, user regenerates health bar to full
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (player.getX() == 1 && player.getY() == 3 && player.getDirection() == 'a') {
                    player.setHealth(player.getMaxHealth());
                    // depending on the direction that the player approaches the enemy from
                    // the enemy faces towards the player
                    npcStance = Assets.npcright;
                } else if (player.getX() == 0 && player.getY() == 4 && player.getDirection() == 'w') {
                    player.setHealth(player.getMaxHealth());
                    npcStance = Assets.npcdown;
                }
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
