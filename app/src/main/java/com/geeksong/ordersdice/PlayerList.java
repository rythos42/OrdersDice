package com.geeksong.ordersdice;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PlayerList implements Serializable {
    protected ArrayList<Player> playerList;
    protected int nextPlayerId = 0;

    public PlayerList() {
        this.playerList = new ArrayList<>();
    }

    public int playerCount() { return this.playerList.size(); }

    public void clear() {
        this.playerList.clear();
        this.nextPlayerId = 0;
    }

    public boolean hasPlayers() { return !this.playerList.isEmpty(); }

    private int getNextPlayerId() {
        return nextPlayerId++;
    }

    public Player addNewPlayer() {
        Player newPlayer = new Player(getNextPlayerId());
        this.playerList.add(newPlayer);
        return newPlayer;
    }

    public Player addDistortDice(String name) {
        Player distort = new Player(getNextPlayerId(), name, 1);
        distort.setColour(Color.BLACK);
        this.playerList.add(distort);
        return distort;
    }

    public Player getById(int id) {
        for(Player player : this.playerList) {
            if(player.getId() == id)
                return player;
        }
        return null;
    }

    public Player getByPosition(int position) {
        // Asked for a player that isn't in this list
        if(position >= this.playerList.size())
            return null;

        // right now, the index is the ID because there is no player shifting
        return this.playerList.get(position);
    }

    private Player getByName(String name) {
        for(Player player : this.playerList) {
            if(player.getName().equals(name))
                return player;
        }
        return null;
    }

    public void removeLastPlayer() {
        this.playerList.remove(playerList.size() - 1);
    }

    public void removeById(int id) {
        Player remove = getById(id);
        this.playerList.remove(remove);
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

    public void clone(PlayerList theirPlayerList) {
        for(Player theirPlayer : theirPlayerList.playerList) {
            Player ourPlayer = this.getById(theirPlayer.getId());
            if(ourPlayer == null)
                ourPlayer = addNewPlayer();

            ourPlayer.clone(theirPlayer);
        }

        // Ensure we don't have players that they don't have
        ArrayList<Player> toRemove = new ArrayList<>();
        for(Player ourPlayer : playerList) {
            Player theirPlayer = theirPlayerList.getById(ourPlayer.getId());
            if(theirPlayer == null)
                toRemove.add(ourPlayer);
        }

        for(Player ourPlayer : toRemove)
            playerList.remove(ourPlayer);
    }

    public Player block(int blockedPlayerId) {
        // put the given dice back and draw a new one
        Player blockedPlayer = this.getById(blockedPlayerId);
        blockedPlayer.putDiceBack();

        return drawDiceForPlayer();
    }

    public void addVorpalCharge(String vorpalChargeFor) {
        // If there is an existing Vorpal Charge for this player, get it.
        Player vorpalCharge = getByName(vorpalChargeFor);

        if(vorpalCharge == null) {
            // If not, create a new one and add it.
            vorpalCharge = new Player(getNextPlayerId(), vorpalChargeFor, 0);
            this.playerList.add(vorpalCharge);
        }

        vorpalCharge.addDice();
    }
}
