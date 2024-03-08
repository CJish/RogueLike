package Main.Logic;

import Main.Models.Creature;
import Main.Models.World;
import asciiPanel.AsciiPanel;

public class CreatureFactory {

    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer() {
        Creature player = new Creature(world, '@', AsciiPanel.brightCyan);
        world.addAtEmptyLocation(player);
        new PlayerAi(player);
        return player;
    }
}