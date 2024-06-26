package Main.Logic;

import Main.Models.Creature;

public class FungusAi extends CreatureAi {

    private CreatureFactory factory;
    private int spreadCount;

    public FungusAi(Creature creature, CreatureFactory factory) {
        super(creature);
//        System.out.println("Launching FungusAi.FungusAi");// Debugging 7
        this.factory = factory;
//        System.out.println("Finished FungusAi.FungusAi");// Debugging 7
    }

    public void onUpdate() {
//        System.out.println("Launched FungusAi.onUpdate");// Debugging 7
        if (spreadCount < 5 && Math.random() < 0.05) {
            spread();
        }
//        System.out.println("Finished FungusAi.onUpdate");// Debugging 7
    }

    public void spread() {
//        System.out.println("Launched FungusAi.spread");// Debugging 7
        int x = creature.x + (int)(Math.random() * 11) - 5;
        int y = creature.y + (int)(Math.random() * 11) - 5;
        int z = creature.z;

        if (!creature.canEnter(x, y, creature.z)) {return;}//TODO: difference here

        Creature child = factory.newFungus(z); //TODO: difference here
        creature.doAction("spawn a child");
        child.x = x;
        child.y = y;
        child.z = z;
        spreadCount++;//TODO: difference here
//        System.out.println("Finished FungusAi.spread");// Debugging 7

    }
}