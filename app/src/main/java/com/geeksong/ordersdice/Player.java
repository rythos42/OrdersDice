package com.geeksong.ordersdice;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private int id;
    private String name;
    private int diceCount;
    private int initialDiceCount;
    private int colour;

    private static ArrayList<Integer> colours = new ArrayList<Integer>();
    private static int MaxPlayers;

    static {
        colours.add(Color.BLUE);
        colours.add(Color.RED);
        colours.add(Color.YELLOW);
        colours.add(Color.GREEN);
        colours.add(Color.BLACK);
        colours.add(Color.CYAN);
        colours.add(Color.DKGRAY);
        colours.add(Color.GRAY);
        colours.add(Color.LTGRAY);
        colours.add(Color.MAGENTA);
    }

    public Player(int id) {
        this.id = id;
        this.name = "PLAYER " + (id + 1);
        this.diceCount = 6;
        this.initialDiceCount = 6;
        this.colour = colours.get(id % colours.size());
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public void setDiceCount(int diceCount) {
        this.diceCount = diceCount;
        this.initialDiceCount = diceCount;
    }
    public int getCurrentDiceCount() { return this.diceCount; }
    public void removeDice() { this.diceCount--; }
    public void resetDice() { this.diceCount = this.initialDiceCount; }
    public boolean hasDiceRemaining() { return this.diceCount != 0; }

    public int getColour() { return this.colour; }
    public void setColour(int colour) { this.colour = colour; }
}
