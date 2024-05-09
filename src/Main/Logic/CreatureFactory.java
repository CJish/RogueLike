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
//        System.out.println("Launching CreatureFactory.newPlayer");// Debugging 7
        Creature player = new Creature(world, '@', AsciiPanel.brightCyan, 100, 20, 5);
        world.addAtEmptyLocation(player, 2);
        new PlayerAi(player, messages); //TODO: difference here
//        System.out.println("Finished CreatureFactory.newPlayer");// Debugging 7
        return player;
    }

    public Creature newFungus(int depth) {
//        System.out.println("Launching CreatureFactory.newFungus");// Debugging 7
        Creature fungus = new Creature(world, 'f', AsciiPanel.green, 10, 0, 0);
        world.addAtEmptyLocation(fungus, fungus.z); //TODO: difference here
        new FungusAi(fungus, this);
//        System.out.println("Finished CreatureFactory.newFungus");// Debugging 7
        return fungus;
    }
}