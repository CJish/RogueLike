//TODO: figure out how to make this an applet and run it from a browser

import asciiPanel.AsciiPanel;
import javax.swing.*;

public class ApplicationMain extends JFrame {

    private AsciiPanel terminal;

    public ApplicationMain() {
        super(); // JFrame
        terminal = new AsciiPanel(); // creating a JPanel to hold graphics
        terminal.write("rl tutorial", 1, 1); // simple load screen
        add(terminal); // putting the JPanel onto the JFrame
        pack(); // putting it all together; sets the size etc
    }

    public static void main(String[] args) {
        ApplicationMain app = new ApplicationMain(); // initialize the game
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // kills the game when the window is closed
        app.setVisible(true); // because sometimes it won't be visible
    }
}