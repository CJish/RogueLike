package Main.Logic;

import Main.Models.Creature;
import Main.Models.Tile;

import java.util.List;

public class PlayerAi extends CreatureAi {

    private List<String> messages;

    public PlayerAi(Creature creature, List<String> messages){
        super(creature);
        this.messages = messages;
    }

    @Override
    public void onNotify(String message) {
        messages.add(message);
    }

    public void onEnter(int x, int y, int z, Tile tile) {
        if (tile.isGround()) {
            creature.x = x;
            creature.y = y;
            creature.z = z;
        } else if (tile.isDiggable()) {
            creature.dig(x, y, z);
        }
    }
}