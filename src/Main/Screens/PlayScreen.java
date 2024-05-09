package Main.Screens;

import Main.Logic.CreatureFactory;
import Main.Logic.WorldBuilder;
import Main.Models.Creature;
import Main.Models.World;
import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// responsible for showing the main content
public class PlayScreen implements Screen {

    private World world;
//    private int centerX;
//    private int centerY;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        messages = new ArrayList<String>();
        createWorld();
        CreatureFactory creatureFactory = new CreatureFactory(world);
        createCreatures(creatureFactory);
    }

    private int assignRandomDepth() {
        Random rand = new Random();

        int randomDepth = rand.nextInt(5); // 0 - 4
        randomDepth++;
        return randomDepth;
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        player = creatureFactory.newPlayer(messages);
        Random rand = new Random();
        int randomDepth;

        for (int i = 0; i < 8; i++) {
            creatureFactory.newFungus(assignRandomDepth()); // TODO: all spawning at floor 1; isn't assigning a random depth
        }
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = screenHeight - messages.size() + 1;
        for (int i = 0; i < messages.size(); i++) {
            terminal.writeCenter(messages.get(i), top + i);
        }
        // may want to save messages into a stored list before clearing them
        messages.clear();
    }

    private void createWorld() {
//        System.out.println("Launching PlayScreen.createWorld");// Debugging 7
        world = new WorldBuilder(90, 31, 5) // TODO: implement ask the user for the dimensions instead of hardcoding it
                .makeCaves()
                .build();
    }

    // controls how far along the x-axis we can scroll
    public int getScrollX() {
        return Math.max(0, Math.min(player.x - screenWidth/2, world.width() - screenWidth));
    }

    // controls how far along the y-axis we can scroll
    public int getScrollY() {
        return Math.max(0, Math.min(player.y - screenHeight/2, world.height() - screenHeight));
    }

    // writes the tiles onto a JPanel
    private void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;
                int wz = player.z;
// TODO: this is super inefficient; should draw tiles and then foreach creature draw it if it's in the viewable region of the screen
                Creature creature = world.creature(wx, wy, wz);
                if (creature != null && creature.z == wz) {
                    terminal.write(creature.glyph(), creature.x - left, creature.y - top, creature.color());
                } else {
                    terminal.write(world.glyph(wx, wy, wz), x, y, world.color(wx, wy, wz));
                }
            }
        }
    }



    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();

        displayTiles(terminal, left, top);
        displayMessages(terminal, messages);

        terminal.write(player.glyph(), player.x - left, player.y - top, player.color());

        String stats = String.format(" %3d/%3d hp", player.hp(), player.maxHP());
        terminal.write(stats, 1, 23);

        String level = String.format("Floor: %3d", player.z + 1);
        terminal.write(level, 1, 22);

        world.update();

    }

    // TODO: Need to add an explainer for controls to either the start screen or in-game
    @Override
    public Screen respondToUserInput(KeyEvent k) {
        switch (k.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_NUMPAD4: player.moveBy(-1, 0, 0); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_NUMPAD6: player.moveBy( 1, 0, 0); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_NUMPAD8: player.moveBy( 0,-1, 0); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_NUMPAD2: player.moveBy( 0, 1, 0); break;
            case KeyEvent.VK_NUMPAD9: player.moveBy(1, -1, 0); break;
            case KeyEvent.VK_NUMPAD7: player.moveBy(-1, -1, 0); break;
            case KeyEvent.VK_NUMPAD3: player.moveBy(1, 1, 0); break;
            case KeyEvent.VK_NUMPAD1: player.moveBy(-1, 1, 0); break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_ADD: player.moveBy(0, 0, -1); break; // up one floor
            case KeyEvent.VK_SUBTRACT:
            case KeyEvent.VK_S: player.moveBy(0, 0, 1); break; // down one floor
        }
        return this;
    }
}