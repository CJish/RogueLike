package Main.Logic;

import Main.Models.Creature;
import Main.Models.Tile;

public class CreatureAi {

    protected Creature creature;

    public CreatureAi(Creature creature) {
        this.creature = creature;
        this.creature.setCreatureAi(this);
    }

    public void onEnter(int x, int y, Tile tile) {}
}