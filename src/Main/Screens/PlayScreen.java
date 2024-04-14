package Main.Screens;

import Main.Logic.CreatureFactory;
import Main.Logic.WorldBuilder;
import Main.Models.Creature;
import Main.Models.World;
import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println("Launched PlayScreen");
        screenWidth = 80;
        screenHeight = 21;
        messages = new ArrayList<String>();
        System.out.println("PlayScreen launched createWorld");
        createWorld();
        System.out.println("PlayScreen launched creatureFactory");
        CreatureFactory creatureFactory = new CreatureFactory(world);
        System.out.println("PlayScreen launched createCreatures");
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        System.out.println("Launching PlayScreen.createCreatures");
        player = creatureFactory.newPlayer(messages);

        for (int i = 0; i < 8; i++) {
            creatureFactory.newFungus();
        }
        System.out.println("Finished PlayScreen.createCreatures");
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        System.out.println("Launching PlayScreen.displayMessages");
        int top = screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.writeCenter(messages.get(i), top + i);
        }
        // may want to save messages into a stored list before clearing them
        messages.clear();
        System.out.println("Finished PlayScreen.displayMessages");
    }

    private void createWorld() {
        System.out.println("Launching PlayScreen.createWorld");
        world = new WorldBuilder(90, 31, 5)
                .makeCaves()
                .build();
        System.out.println("Finished PlayScreen.createWorld");
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
        System.out.println("Displaying tiles (PlayScreen.displayTiles");
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;
// TODO: this is super inefficient; should draw tiles and then foreach creature draw it if it's in the viewable region of the screen
                Creature creature = world.creature(wx, wy, player.z);
                if (creature != null) {
                    terminal.write(creature.glyph(), creature.x - left, creature.y - top, creature.color());
                } else {
                    terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
                }
            }
        }
        System.out.println("Finished displaying tiles (PlayScreen.displayTiles");
    }

    // taken out in Step04 to add player
//    private void scrollBy(int mx, int my) {
//        centerX = Math.max(0, Math.min(centerX + mx, world.width() - 1));
//        centerY = Math.max(0, Math.min(centerY + my, world.height() - 1));
//    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        System.out.println("Launching PlayScreen.displayOutput");
        int left = getScrollX();
        int top = getScrollY();
        displayTiles(terminal, left, top);
        terminal.write(player.glyph(), player.x - left, player.y - top, player.color());
        world.update();
        String stats = String.format(" %3d/%3d hp", player.hp(), player.maxHP());
        terminal.write(stats, 1, 23);
        displayMessages(terminal, messages);
//        terminal.write('X', player.x - left, player.y - top);
//        terminal.write("You are having fun", 1, 1);
//        terminal.writeCenter("--press [escape] to lose or [enter] to win --", 22);
        System.out.println("Finished PlayScreen.displayOutput");
    }

    // TODO: fix these key bindings to make more sense
    @Override
    public Screen respondToUserInput(KeyEvent k) {
        switch (k.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_H: player.moveBy(-1, 0, 0); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_L: player.moveBy( 1, 0, 0); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_K: player.moveBy( 0,-1, 0); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_J: player.moveBy( 0, 1, 0); break;
            case KeyEvent.VK_Y: player.moveBy(-1,-1, 0); break;
            case KeyEvent.VK_U: player.moveBy( 1,-1, 0); break;
            case KeyEvent.VK_B: player.moveBy(-1, 1, 0); break;
            case KeyEvent.VK_N: player.moveBy( 1, 1, 0); break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_GREATER: player.moveBy(0, 0, -1); break;
            case KeyEvent.VK_MINUS:
            case KeyEvent.VK_LESS: player.moveBy(0, 0, 1); break;
        }
        return this;
    }
}