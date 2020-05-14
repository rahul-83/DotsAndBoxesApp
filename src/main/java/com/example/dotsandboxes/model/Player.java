package com.example.dotsandboxes.model;

import android.graphics.Bitmap;

public class Player {
    private String     name;
    private Bitmap     symbol;
    private int        color;
    private PlayerType playerType;

    public Player(String name, Bitmap symbol, int color, PlayerType playerType) {
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.playerType = playerType;
    }

    public String getName() {
        return name;
    }

    public Bitmap getSymbol() {
        return symbol;
    }

    public int getColor() {
        return color;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public boolean isComputerOpponent() {
        return playerType.isComputerOpponent();
    }

    @Override
    public String toString() {
        return "Player [name=" + name + ", Color=" + color + "]";
    }

}
