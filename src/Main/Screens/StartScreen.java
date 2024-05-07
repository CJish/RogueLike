package Main.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class StartScreen implements Screen {

    public void displayOutput(AsciiPanel terminal) {
//        System.out.println("Launching StartScreen.displayOutput");// Debugging 7
        terminal.write("rl tutorial", 1, 1);
        terminal.writeCenter("--press [enter] to start --", 22);
//        System.out.println("Finished StartScreen.displayOutput");// Debugging 7
    }

    public Screen respondToUserInput(KeyEvent k) {
        return k.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}