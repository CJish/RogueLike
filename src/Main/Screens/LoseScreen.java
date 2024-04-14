package Main.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class LoseScreen implements Screen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        System.out.println("Launching LoseScreen.displayOutput");
        terminal.write("You lost", 1,1);
        terminal.writeCenter("-- press [enter] to restart --", 22);
        System.out.println("Finished LoseScreen.displayOutput");
    }

    @Override
    public Screen respondToUserInput(KeyEvent k) {
        System.out.println("Launching LoseScreen.respondToUserInput");
//        if (k.getKeyCode() == KeyEvent.VK_ENTER) {
//            return new PlayScreen();
//        } else {
//            return this;
//        }
        // Ternary operator; a shorthand if-else statement
        System.out.println("Finished LoseScreen.respondToUserInput");
        return k.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}