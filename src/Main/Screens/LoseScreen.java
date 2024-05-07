package Main.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class LoseScreen implements Screen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        System.out.println("Launching LoseScreen.displayOutput");//TODO: Debugging 7
        terminal.write("You lost", 1,1);
        terminal.writeCenter("-- press [enter] to restart --", 22);
        System.out.println("Finished LoseScreen.displayOutput");//TODO: Debugging 7
    }

    @Override
    public Screen respondToUserInput(KeyEvent k) {
        System.out.println("Launching LoseScreen.respondToUserInput");//TODO: Debugging 7
//        if (k.getKeyCode() == KeyEvent.VK_ENTER) {
//            return new PlayScreen();
//        } else {
//            return this;
//        }
        // Ternary operator; a shorthand if-else statement
        System.out.println("Finished LoseScreen.respondToUserInput");//TODO: Debugging 7
        return k.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}