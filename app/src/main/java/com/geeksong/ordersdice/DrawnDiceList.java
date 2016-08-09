package com.geeksong.ordersdice;

import java.io.Serializable;
import java.util.ArrayList;

public class DrawnDiceList implements Serializable {
    protected ArrayList<DrawnDice> drawnDiceList;

    public DrawnDiceList() {
        this.drawnDiceList = new ArrayList<>();
    }

    public int size() { return this.drawnDiceList.size(); }
    public DrawnDice get(int index) { return this.drawnDiceList.get(index); }
    public void clear() { this.drawnDiceList.clear(); }
    public DrawnDice remove(int index) { return this.drawnDiceList.remove(index); }

    public void addDrawnPlayer(int playerId) {
        this.drawnDiceList.add(new DrawnDice(playerId));
    }
}
