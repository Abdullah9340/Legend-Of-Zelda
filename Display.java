
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
    // Create Variables for frame and cavas
    private JFrame frame;
    private Canvas canvas;

    /*-
     * Method: Display() pre: width > 0, height > 0 
     * post: Creates the jframe and
     * drawable canvas
     */
    public Display(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); // Set non resizable

        canvas = new Canvas();
        // Set canvas dimensions
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setFocusable(false); // Make the canvas not focusable so event listener works better

        frame.add(canvas);
        frame.pack(); // Make the frame only hold the canvas fully
        frame.setVisible(true);
    }

    /*-
        Method: getCanvas()
        pre: none
        post: returns the canvas object
    */
    public Canvas getCanvas() {
        return canvas;
    }

    /*-
        Method: getJFrame()
        pre: none
        post: returns the JFrame
    */
    public JFrame getJFrame() {
        return frame;
    }
}
