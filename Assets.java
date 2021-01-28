import java.awt.image.BufferedImage;

public class Assets {

    private static final int width = 16, height = 16;

    public static BufferedImage dirt, grass, stone, tree, npcright, npcdown, fillheart, emptyheart;

    public static BufferedImage[][] player = new BufferedImage[4][3];

    public static BufferedImage[][] darksoildersprite = new BufferedImage[4][5];

    public static BufferedImage[][] skeletonsprite = new BufferedImage[4][3];

    public static void init() {
        SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("rpg.png"));
        SpriteSheet players = new SpriteSheet(ImageLoader.loadImage("players.png"));
        SpriteSheet darksoilder = new SpriteSheet(ImageLoader.loadImage("darksoilder.png"));

        fillheart = ImageLoader.loadImage("heart.png");
        emptyheart = ImageLoader.loadImage("emptyheart.png");

        grass = sheet.crop(0, height * 3, width, height);
        tree = sheet.crop(0, 0, width, height);
        stone = sheet.crop(0, height, width, height);
        npcright = sheet.crop(width * 2, height * 1, width, height);
        npcdown = sheet.crop(width * 2, 0, width, height);

        darksoildersprite[0][0] = darksoilder.crop(0, 0, 48, 64);
        darksoildersprite[0][1] = darksoilder.crop(1 * 60, 0, 60, 64);
        darksoildersprite[0][2] = darksoilder.crop(2 * 60, 0, 60, 64);
        darksoildersprite[0][3] = darksoilder.crop(3 * 60, 0, 60, 64);
        darksoildersprite[0][4] = darksoilder.crop(4 * 60, 0, 76, 64);

        darksoildersprite[1][0] = darksoilder.crop(0, 64, 48, 64);
        darksoildersprite[1][1] = darksoilder.crop(1 * 60, 64, 60, 64);
        darksoildersprite[1][2] = darksoilder.crop(2 * 60, 64, 60, 64);
        darksoildersprite[1][3] = darksoilder.crop(3 * 60, 64, 60, 64);
        darksoildersprite[1][4] = darksoilder.crop(4 * 60 + 4, 64, 68, 64);

        darksoildersprite[2][0] = darksoilder.crop(0, 64 * 2, 48, 64);
        darksoildersprite[2][1] = darksoilder.crop(1 * 60, 64 * 2, 60, 64);
        darksoildersprite[2][2] = darksoilder.crop(2 * 60, 64 * 2, 60, 64);
        darksoildersprite[2][3] = darksoilder.crop(3 * 60, 64 * 2, 60, 64);
        darksoildersprite[2][4] = darksoilder.crop(4 * 60, 64 * 2, 60, 64);

        darksoildersprite[3][0] = darksoilder.crop(0, 64 * 3, 48, 64);
        darksoildersprite[3][1] = darksoilder.crop(1 * 60, 64 * 3, 60, 64);
        darksoildersprite[3][2] = darksoilder.crop(2 * 60, 64 * 3, 60, 64);
        darksoildersprite[3][3] = darksoilder.crop(3 * 60, 64 * 3, 60, 64);
        darksoildersprite[3][4] = darksoilder.crop(4 * 60, 64 * 3, 76, 64);

        player[0][0] = players.crop(4 * 32, 0, 32, 32);
        player[0][1] = players.crop(5 * 32, 0, 32, 32);
        player[0][2] = players.crop(3 * 32, 0, 32, 32);
        player[1][0] = players.crop(4 * 32, 32, 32, 32);
        player[1][1] = players.crop(5 * 32, 32, 32, 32);
        player[1][2] = players.crop(3 * 32, 32, 32, 32);
        player[2][0] = players.crop(4 * 32, 32 * 2, 32, 32);
        player[2][1] = players.crop(5 * 32, 32 * 2, 32, 32);
        player[2][2] = players.crop(3 * 32, 32 * 2, 32, 32);
        player[3][0] = players.crop(4 * 32, 32 * 3, 32, 32);
        player[3][1] = players.crop(5 * 32, 32 * 3, 32, 32);
        player[3][2] = players.crop(3 * 32, 32 * 3, 32, 32);
    }
}