import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
    /*-
     * Method: backgroundMusic() 
     * pre: none 
     * post: starts out background music
     */
    public static void backgroundMusic() {
        try {
            File file = new File("Assets/backgroundmusic.wav"); // Read file
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file); // Create an audio stream for the file
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Set the clip to endlessly loop
            clip.start(); // Start the clip
        } catch (Exception e) {
            System.err.println("Could not find music"); // Incase file can't be loaded
        }
    }

    /*-
     * projectileNoise()
     * Pre: path must be a valid file path 
     * Post: starts projectile sound
     */
    public static void projectileNoise(String path) {
        try {
            File file = new File(path); // Read file
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file); // Create an audio stream for the file
            clip.open(inputStream);
            clip.start(); // Start the clip
        } catch (Exception e) {
            System.err.println("Could not find music"); // Incase file can't be loaded
        }
    }

}
