package Main.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public interface Screen {

    // takes a AsciiPanel (JPanel) and displays things to it
    public void displayOutput(AsciiPanel terminal);

    // takes a KeyEvent and returns a Screen
    public Screen respondToUserInput(KeyEvent k);
}