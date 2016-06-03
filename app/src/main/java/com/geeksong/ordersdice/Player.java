package com.geeksong.ordersdice;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Player {
    private int id;
    private String name;
    private int diceCount;

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

        MaxPlayers = colours.size();
    }

    public Player(int id) {
        this.id = id;
        this.name = "PLAYER " + (id + 1);
        this.diceCount = 6;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public int getDiceCount() { return this.diceCount; }
    public void setDiceCount(int diceCount) { this.diceCount = diceCount; }

    public int getColour() { return colours.get(this.id); }

    public static int getMaximumPlayerCount() { return MaxPlayers; }
}
