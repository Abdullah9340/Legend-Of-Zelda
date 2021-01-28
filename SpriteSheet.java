import java.awt.image.BufferedImage;

public class SpriteSheet {

    // Variable for sheet
    private BufferedImage sheet;

    /*-
     * Method: SpriteSheet()
     *  pre: sheet != null 
     * post: assigns the spritesheet
     */

    public SpriteSheet(BufferedImage sheet) {
        this.sheet = sheet;
    }

    /*-
     * Method: crop() 
     * pre: dimensions and coords must be on the spritesheet
     * post:returns the cropped image
     */
    public BufferedImage crop(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }
}