package Main.Logic;

import Main.Models.Creature;
import Main.Models.World;
import asciiPanel.AsciiPanel;

import java.util.List;

public class CreatureFactory {

    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(world, '@', AsciiPanel.brightCyan, 100, 20, 5);
        world.addAtEmptyLocation(player, 2);
        new PlayerAi(player, messages);
        return player;
    }

    public Creature newFungus() {
        Creature fungus = new Creature(world, 'f', AsciiPanel.green, 10, 0, 0);
        world.addAtEmptyLocation(fungus, fungus.z);
        new FungusAi(fungus, this);
        return fungus;
    }
}