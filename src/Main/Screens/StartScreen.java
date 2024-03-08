package Main.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class StartScreen implements Screen {

    public void displayOutput(AsciiPanel terminal) {
        terminal.write("rl tutorial", 1, 1);
        terminal.writeCenter("--press [enter] to start --", 22);
    }

    public Screen respondToUserInput(KeyEvent k) {
        return k.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}