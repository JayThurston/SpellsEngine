package driver;

import View.Ui;
import java.util.Scanner;
import model.SpellsEngine;
import model.Player;


public class driver {

    
    public static void main(String[] args) {
        
        SpellsEngine spellsEngine = new SpellsEngine();
        Scanner in = new Scanner(System.in);
        String input;
        
        Player c = new Player();
        Player m = new Player();
        Player w = new Player();
        
        spellsEngine.subscribe(c);
        spellsEngine.subscribe(m);
        spellsEngine.subscribe(w);
        
        while(true){
            Ui.menu();
            
            input = in.nextLine();
            
            if(input.equals("q") == true || input.equals("Q") == true){
                break;
            }
            
            switch(input){
                case "A":
                case "a":
                    spellsEngine.publish("ArmorSpell");
                    break;
                case "D":
                case "d":
                    spellsEngine.publish("DamageSpell");
                    break;
                case "H":
                case "h":
                    spellsEngine.publish("HealthSpell");
                    break;
            }
            
            Ui.partyStats(c, m, w);   
        }
    }
}




package View;
import model.Player;

public class Ui {

    public static void menu() {
        System.out.println("Cast a Spell:\n");
        System.out.println("(A)rmor \n(D)amage \n(H)ealth \n(Q)uit \n:");
    }
    
    public static void partyStats(Player c, Player m, Player w) {
        System.out.println("Party Stats:\n");
        System.out.println(String.format("%20s %7s %10s", "Cleric","Mage","Warrior"));
        System.out.println(String.format("Armor %14s %7s %10s",c.getArmor(),m.getArmor(),w.getArmor()));
        System.out.println(String.format("Damage %13s %7s %10s",c.getDamage(),m.getDamage(),w.getDamage()));
        System.out.println(String.format("Health %13s %7s %10s\n\n",c.getHealth(),m.getHealth(),w.getHealth()));    
    }
}




package model;

public abstract class Spell {
    protected Player player;
    protected abstract void doSpell();    
}



package model;

public class ArmorSpell extends Spell{

    public ArmorSpell(Player player) {
        this.player = player;
    }

    @Override
    protected void doSpell() {
        player.addArmor(3);
    }    
}




package model;

public class HealthSpell extends Spell{

    public HealthSpell(Player player) {
        this.player = player;
    }

    @Override
    protected void doSpell() {
        player.addHealth(4);
    }
    
}



package model;

public class Player {
    
    private int armor;
    private int damage;
    private int health;

    public Player() {
        this.armor = 0;
        this.damage = 1;
        this.health = 10;
    }
    
    public void doStuff(String type) {
        SpellsFactory.getInstance().requestSpell(this, type).doSpell();
    }
    
    public void addArmor(int armor) {
        this.armor += armor;
    }

    public int getArmor() {
        return armor;
    }

    public void addDamage(int damage) {
        this.damage += damage;
    }

    public int getDamage() {
        return damage;
    }
    
    public void addHealth(int health) {
        this.health += health;
    }

    public int getHealth() {
        return health;
    }
}



package model;

public class SpellsFactory {
    
    private static SpellsFactory instance = null;
    
    public static SpellsFactory getInstance() {
        if(instance == null){
            instance = new SpellsFactory();
        }
        return instance;
    }
    
    public Spell requestSpell(Player player, String type) {
        
        if(type.equals("ArmorSpell")){    
            ArmorSpell spell = new ArmorSpell(player);
            return spell;
        }
        
        if(type.equals("DamageSpell")){    
            DamageSpell spell = new DamageSpell(player);
            return spell;
        }
        
        if(type.equals("HealthSpell")){    
            HealthSpell spell = new HealthSpell(player);
            return spell;
        }
        
        return null;
    }
}



package model;
import java.util.Vector;

public class SpellsEngine {
    
    private Vector<Player> subscribers;

    public SpellsEngine() {
        subscribers = new Vector<>(5);
    }
    
    public void subscribe(Player player) {
        subscribers.add(player);
    }
    
    public void publish(String type) {
        for(int x = 0; x < subscribers.size(); x++){
            subscribers.get(x).doStuff(type);
        }
    }
    
    public void unsubscribe(Player player) {
        subscribers.removeElement(player);
    }
}