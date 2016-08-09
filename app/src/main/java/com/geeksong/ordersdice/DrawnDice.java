package com.geeksong.ordersdice;

import java.io.Serializable;

public class DrawnDice implements Serializable {
    private int drawnPlayerId;
    private boolean blocked;

    public DrawnDice(int drawnPlayerId) {
        this.drawnPlayerId = drawnPlayerId;
        this.blocked = false;
    }

    public void setBlocked() { this.blocked = true; }
    public boolean isBlocked() { return this.blocked; }
    public int getPlayerId() { return this.drawnPlayerId; }
}
