package Main.Logic;

import Main.Models.Creature;
import Main.Models.Tile;

public class CreatureAi {

    protected Creature creature;

    public CreatureAi(Creature creature) {
        System.out.println("Launching CreatureAi.CreatureAi"); //TODO: Debugging 7
        this.creature = creature;
        this.creature.setCreatureAi(this);
        System.out.println("Finished CreatureAi.CreatureAi");//TODO: Debugging 7
    }

    public void onEnter(int x, int y, int z, Tile tile) {}

    public void onUpdate() {}

    public void onNotify(String format) {}
}