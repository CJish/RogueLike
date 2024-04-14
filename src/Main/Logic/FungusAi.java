package Main.Logic;

import Main.Models.Creature;

public class FungusAi extends CreatureAi {

    private CreatureFactory factory;
    private int spreadCount;

    public FungusAi(Creature creature, CreatureFactory factory) {
        super(creature);
        System.out.println("Launching FungusAi.FungusAi");
        this.factory = factory;
        System.out.println("Finished FungusAi.FungusAi");
    }

    public void onUpdate() {
        System.out.println("Launched FungusAi.onUpdate");
        if (spreadCount < 5 && Math.random() < 0.02) {
            spread();
        }
        System.out.println("Finished FungusAi.onUpdate");
    }

    public void spread() {
        System.out.println("Launched FungusAi.spread");
        int x = creature.x + (int)(Math.random() * 11) - 5;
        int y = creature.y + (int)(Math.random() * 11) - 5;
        int z = creature.z;

        if (!creature.canEnter(x, y, creature.z)) {return;}

        Creature child = factory.newFungus();
        creature.doAction("spawn a child");
        child.x = x;
        child.y = y;
        child.z = z;
        spreadCount++;
        System.out.println("Finished FungusAi.spread");

    }
}