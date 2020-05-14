package com.example.dotsandboxes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerManager {

    private List<Player> playerList = new ArrayList<Player>();

    private Player  CurrentPlayer;

    public PlayerManager() {
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public List<Player> getPlayerList() {
        return Collections.unmodifiableList(playerList);
    }

    public Player getCurrentPlayer() {
        if (CurrentPlayer == null)
            throw new RuntimeException("Before the player is asked,new train must have been called up at least once!");
        return CurrentPlayer;
    }

    public void NextPlayerSelection() {

        int indexCurrentPlayer = playerList.indexOf(CurrentPlayer);

        int indexNextPlayer = indexCurrentPlayer + 1;
        if (indexNextPlayer > playerList.size() - 1)
            indexNextPlayer = 0;

        CurrentPlayer = playerList.get(indexNextPlayer);
    }


}
