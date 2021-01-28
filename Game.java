import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game implements Runnable, KeyListener {

    private int round = 0;
    private Display display;
    private boolean running = false;

    private Graphics g;
    private BufferStrategy bs;

    private final int WIDTH, HEIGHT, FPS = 60;

    private String title;

    private Thread thread;
    private Player player;
    private ArrayList<Enemy> enemies;

    private int objects[][];
    private ArrayList<Arrow> projectiles;

    private BufferedImage npcStance;

    private boolean isDead = false;

    private int roundAm = 0;

    public Game(String title, int width, int height) {
        this.title = title;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.start();
    }

    // Init objects
    public void init() {
        display = new Display(title, WIDTH, HEIGHT);
        Assets.init();
        npcStance = Assets.npcright;
        player = new Player();
        enemies = new ArrayList<Enemy>();
        enemies.add(new Enemy());
        projectiles = new ArrayList<Arrow>();
        display.getJFrame().addKeyListener(player);
        display.getJFrame().addKeyListener(this);
        objects = new int[5][2];
        setObjectCoord();
    }

    // Setting object coordinates
    public void setObjectCoord() {
        // object 1(tree)
        objects[0][0] = 0;
        objects[0][1] = 0;
        // object 3(rock)
        objects[2][0] = 0;
        objects[2][1] = 1;
        // object 4(tree)
        objects[3][0] = 0;
        objects[3][1] = 2;
        // object 5(NPC)
        objects[4][0] = 0;
        objects[4][1] = 3;
    }

    public void update() {
        if (!isDead) {
            player.update();
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).update(player);
            }
            if (enemies.size() == 0) {
                if (roundAm == 300) {
                    enemies.add(new Enemy());
                    enemies.add(new Enemy());
                    roundAm = 0;
                } else {
                    roundAm++;
                }
            }

            for (int i = projectiles.size() - 1; i >= 0; i--) {
                if (projectiles.size() == 0) {
                    break;
                }
                projectiles.get(i).update();
                if (projectiles.get(i).getX() > WIDTH / 64 || projectiles.get(i).getX() < -1) {
                    projectiles.remove(i);
                } else if (projectiles.get(i).getY() > HEIGHT / 64 || projectiles.get(i).getY() < -1) {
                    projectiles.remove(i);
                }
            }
            if (player.getHealth() == 0) {
                isDead = true;
            }
            checkCollision();
        } else {

        }
    }

    public void GameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        g.drawString("Game Over", 250, HEIGHT / 2);
    }

    public void BackGroundGO(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        // Draw
        if (!isDead) {
            drawBackground(g);
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).render(g);
            }
            for (int i = 0; i < projectiles.size(); i++) {
                projectiles.get(i).render(g);
            }
            player.render(g);
        } else {
            BackGroundGO(g);
            GameOver(g);
        }
        // End Draw
        bs.show();
        g.dispose();
    }

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

    public void checkCollision() {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            for (int j = enemies.size() - 1; j >= 0; j--) {
                if (projectiles.size() != 0) {
                    if (projectiles.get(i).getX() == enemies.get(j).getX()
                            && projectiles.get(i).getY() == enemies.get(j).getY()) {
                        projectiles.remove(i);
                        enemies.get(j).setHealth(enemies.get(j).getHealth() - 1);
                    }
                }
                if (enemies.get(j).getHealth() == 0) {
                    enemies.remove(j);
                    j--;
                }

            }
        }
        for (int i = 0; i < objects.length; i++) {
            if (player.getX() == objects[i][0] && player.getY() == objects[i][1]) {
                setPlayerCoord(player);
            }
            for (int j = 0; j < enemies.size(); j++) {
                if (enemies.get(j).getX() == objects[i][0] && enemies.get(j).getY() == objects[i][1]) {
                    setEnemyCoord(enemies.get(j));
                }
                if (enemies.get(j).getY() == player.getY() && enemies.get(j).getX() == player.getX()) {
                    player.setHealth(player.getHealth() - 1);
                    setEnemyCoord(enemies.get(j));
                }
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = i + 1; j < enemies.size(); j++) {
                if (enemies.get(i).getX() == enemies.get(j).getX() && enemies.get(i).getY() == enemies.get(j).getY()) {
                    setEnemyCoord(enemies.get(i));
                }
            }
        }
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

    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

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
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'q') {
            if (projectiles.size() < player.getMaxArrows()) {
                projectiles.add(new Arrow(player));
            }
        }

    }

    @Override
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
    public void keyReleased(KeyEvent e) {
    }

    public ArrayList<Arrow> getProjectiles() {
        return projectiles;
    }
}
