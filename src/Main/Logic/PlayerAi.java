package Main.Logic;

import Main.Models.Creature;
import Main.Models.Tile;

import java.util.List;

public class PlayerAi extends CreatureAi {

    private List<String> messages;

    public PlayerAi(Creature creature, List<String> messages){ //TODO: Difference here
        super(creature);
//        System.out.println("Launched PlayerAi.PlayerAi");// Debugging 7
        this.messages = messages;
//        System.out.println("Finished PlayerAi.PlayerAi");// Debugging 7
    }

    @Override
    public void onNotify(String message) {
//        System.out.println("Launched PlayerAi.onNotify");// Debugging 7
        messages.add(message);
//        System.out.println("Finished PlayerAi.onNotify");// Debugging 7
    }

    public void onEnter(int x, int y, int z, Tile tile) {
//        System.out.println("Launched PlayerAi.onEnter");// Debugging 7
        if (tile.isGround()) {
            creature.x = x;
            creature.y = y;
            creature.z = z;
        } else if (tile.isDiggable()) {
            creature.dig(x, y, z);
        }
//        System.out.println("Finished PlayerAi.onEnter");// Debugging 7
    }
}