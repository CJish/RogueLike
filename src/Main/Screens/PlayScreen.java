package Main.Screens;

import Main.Logic.CreatureFactory;
import Main.Logic.WorldBuilder;
import Main.Models.Creature;
import Main.Models.World;
import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

// responsible for showing the main content
public class PlayScreen implements Screen {

    private World world;
//    private int centerX;
//    private int centerY;
    private Creature player;
    private int screenWidth;
    private int screenHeight;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        createWorld();
        CreatureFactory creatureFactory = new CreatureFactory(world);
        player = creatureFactory.newPlayer();
    }

    private void createWorld() {
        world = new WorldBuilder(90, 31)
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

                terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
            }
        }
    }

    // taken out in Step04 to add player
//    private void scrollBy(int mx, int my) {
//        centerX = Math.max(0, Math.min(centerX + mx, world.width() - 1));
//        centerY = Math.max(0, Math.min(centerY + my, world.height() - 1));
//    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();
        displayTiles(terminal, left, top);
        terminal.write(player.glyph(), player.x - left, player.y - top, player.color());
//        terminal.write('X', player.x - left, player.y - top);
//        terminal.write("You are having fun", 1, 1);
//        terminal.writeCenter("--press [escape] to lose or [enter] to win --", 22);
    }

    // TODO: fix these key bindings to make more sense
    @Override
    public Screen respondToUserInput(KeyEvent k) {
        switch (k.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_H: player.moveBy(-1, 0); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_L: player.moveBy( 1, 0); break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_K: player.moveBy( 0,-1); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_J: player.moveBy( 0, 1); break;
            case KeyEvent.VK_Y: player.moveBy(-1,-1); break;
            case KeyEvent.VK_U: player.moveBy( 1,-1); break;
            case KeyEvent.VK_B: player.moveBy(-1, 1); break;
            case KeyEvent.VK_N: player.moveBy( 1, 1); break;
        }
        return this;
    }
}