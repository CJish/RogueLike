package Main.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class LoseScreen implements Screen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You lost", 1,1);
        terminal.writeCenter("-- press [enter] to restart --", 22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent k) {
//        if (k.getKeyCode() == KeyEvent.VK_ENTER) {
//            return new PlayScreen();
//        } else {
//            return this;
//        }
        // Ternary operator; a shorthand if-else statement
        return k.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}