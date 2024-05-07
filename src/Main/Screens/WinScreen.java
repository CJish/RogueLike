package Main.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class WinScreen implements Screen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
//        System.out.println("Launching WinScreen.displayOutput");// Debugging 7
        terminal.write("You won.", 1, 1);
        terminal.writeCenter("-- press [enter] to restart --", 22);
//        System.out.println("Finished Winscreen.displayOutput");// Debugging 7
    }

    @Override
    public Screen respondToUserInput(KeyEvent k) {
        return k.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}