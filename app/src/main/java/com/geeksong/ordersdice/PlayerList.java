package com.geeksong.ordersdice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PlayerList implements Serializable {
    protected ArrayList<Player> playerList;

    public PlayerList() {
        this.playerList = new ArrayList<>();
    }

    public int size() { return this.playerList.size(); }
    public void add(Player player) { this.playerList.add(player); }
    public void clear() { this.playerList.clear(); }


    public Player getById(int id) {
        // right now, the index is the ID because there is no player shifting
        return this.playerList.get(id);
    }

    public void removeById(int id) {
        this.playerList.remove(id);
    }

    public boolean hasDiceRemaining() {
        for(Player player: this.playerList) {
            if(player.hasDiceRemaining())
                return true;
        }
        return false;
    }

    public int getTotalDiceRemaining() {
        int totalDiceInBag = 0;
        for (Player player: this.playerList) {
            totalDiceInBag += player.getCurrentDiceCount();
        }
        return totalDiceInBag;
    }

    public Player drawDiceForPlayer() {
        int totalDiceInBag = this.getTotalDiceRemaining();
        int draw = new Random().nextInt(totalDiceInBag) + 1;    // draw can't be 0, because we're dealing with real numbers

        for (Player player: this.playerList) {
            totalDiceInBag -= player.getCurrentDiceCount();
            if(totalDiceInBag < draw) {
                player.drawDice();
                return player;
            }
        }

        return null;
    }

    public void resetBag() {
        for(Player player: this.playerList)
            player.resetDice();
    }

    public void copyFrom(PlayerList otherPlayerList) {
        for(Player otherPlayer : otherPlayerList.playerList) {
            Player existingPlayer = this.getById(otherPlayer.getId());
            existingPlayer.copyFrom(otherPlayer);
        }
    }

    public Player block(int blockedPlayerId) {
        // put the given dice back and draw a new one
        Player blockedPlayer = this.getById(blockedPlayerId);
        blockedPlayer.putDiceBack();

        return drawDiceForPlayer();
    }
}
