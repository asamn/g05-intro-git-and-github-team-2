import java.util.*;
public class Player {
    private String name;
    private boolean hurt;
    private boolean inCombat;
    private ArrayList<String> inventory;
    //========================== ITEM DEFINITIONS ==============================
    private String ring="Ring - A red shiny ring.", potion="Potion of Strength - A flask with a translucent orange fluid.";

    public Player(String name) {
        this.name = name;
        this.hurt = false;
        this.inCombat = false;
        this.inventory = new ArrayList<String>();
    }

    public String getName() {
        return this.name;
    }
    public boolean hurt() {
        return this.hurt;
    }
    public boolean getEmpowered() {
        return this.inventory.contains((potion));
    }
    public boolean getRing() {
        return this.inventory.contains((ring));
    }
    public void setRing(){
        addToInv(ring);
    }
    public void setEmpowered(){
        addToInv(potion);
    }
    public void injure() {
        this.hurt = true;
    }

    public void heal() {
        this.hurt = false;
    }

    public void fight() {
        this.inCombat = true;
    }

    public void endFight() {
        this.inCombat = false;
    }

    public boolean combatState(){
        return this.inCombat;
    }

    public void addToInv(String s)
    {
        this.inventory.add(s);
    }
    public boolean checkIfHasItem(String s)
    {
        return this.inventory.contains((s));
    }
    public String getInv()
    {
        if (this.inventory.isEmpty())
        {
            return "You have no items!";
        }
        String s = "[INVENTORY]\n";
        for (String x : this.inventory)
        {
            s = s.concat(x + "\n");
        }
        return s;
    }
}
