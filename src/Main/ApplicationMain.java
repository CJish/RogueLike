package Main;//TODO: figure out how to make this an applet and run it from a browser

import Main.Screens.Screen;
import Main.Screens.StartScreen;
import asciiPanel.AsciiPanel;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// initializes the game then delegates input and output
public class ApplicationMain extends JFrame implements KeyListener {

    private AsciiPanel terminal; // Creates what is essentially a customized JPanel
    private Screen screen; // creates a screen Object

    public ApplicationMain() {
        super(); // Call to the JFrame
//        System.out.println("Launching ApplicationMain()"); // Debugging 7
        terminal = new AsciiPanel(); // creating a JPanel to hold graphics
        terminal.write("rl tutorial", 1, 1); // Throw an up an intro screen with a string //TODO: Keep?
        add(terminal); // putting the JPanel onto the JFrame
        pack(); // putting it all together; sets the size etc
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
//        System.out.println("Finished ApplicationMain()"); // Debugging 7
    }

    public void repaint() {
//        System.out.println("Launching ApplicationMain.repaint"); // Debugging 7
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
//        System.out.println("Finished ApplicationMain.repaint"); // Debugging 7
    }

    public void keyPressed(KeyEvent k) {
        screen = screen.respondToUserInput(k);
        repaint();
    }

    public void keyReleased(KeyEvent k) {}

    public void keyTyped(KeyEvent k) {
//        screen = screen.respondToUserInput(e);
//        repaint();
    }

    public static void main(String[] args) {
//        System.out.println("Launching ApplicationMain.main"); // Debugging 7
        ApplicationMain app = new ApplicationMain(); // initialize the game
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // kills the game when the window is closed
        app.setVisible(true); // because sometimes it won't be visible
//        System.out.println("Finished ApplicationMain.main"); // Debugging 7
    }
}