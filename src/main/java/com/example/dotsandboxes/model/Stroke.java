package com.example.dotsandboxes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stroke {


    private Box  BoxAbove;
    private Box  BoxBelow;
    private Box  BoxLeft;
    private Box  BoxRight;

    private List<Box> boxList = new ArrayList<Box>();

    private Player Owner;

    public Stroke(Box boxAbove, Box boxBelow,
                  Box boxLeft, Box boxRight) {

        this.BoxAbove = boxAbove;
        this.BoxBelow = boxBelow;
        this.BoxLeft = boxLeft;
        this.BoxRight = boxRight;

        if (boxAbove!= null)
            boxList.add(boxAbove);

        if (boxBelow != null)
            boxList.add(boxBelow);

        if (boxLeft != null)
            boxList.add(boxLeft);

        if (boxRight != null)
            boxList.add(boxRight);
    }

    public Box getBoxAbove() {
        return BoxAbove;
    }

    public Box getBoxBelow() {
        return BoxBelow;
    }

    public Box getBoxLeft() {
        return BoxLeft;
    }

    public Box getBoxRight() {
        return BoxRight;
    }

    public List<Box> getBoxList() {
        return Collections.unmodifiableList(boxList);
    }

    public boolean isCouldCloseTheSorroundingBox() {

        for (Box box : boxList)
            if (box.getStrokesWithoutOwner().size() <= 2)
                return true;

        return false;
    }

    public Player getOwner() {
        return Owner;
    }

    public void setOwner(Player owner) {
        this.Owner = owner;
    }

    @Override
    public String toString() {
        return "Stroke [BoxAbove=" + BoxAbove + ", BoxBelow="
                + BoxBelow + ", BoxLeft=" + BoxLeft
                + ", BoxRight=" + BoxRight + ", Owner="
                + Owner + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((BoxLeft == null) ? 0 : BoxLeft.hashCode());
        result = prime * result + ((BoxAbove == null) ? 0 : BoxAbove.hashCode());
        result = prime * result + ((BoxRight == null) ? 0 : BoxRight.hashCode());
        result = prime * result + ((BoxBelow == null) ? 0 : BoxBelow.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stroke other = (Stroke) obj;
        if (BoxLeft == null) {
            if (other.BoxLeft != null)
                return false;
        } else if (!BoxLeft.equals(other.BoxLeft))
            return false;
        if (BoxAbove == null) {
            if (other.BoxAbove != null)
                return false;
        } else if (!BoxAbove.equals(other.BoxAbove))
            return false;
        if (BoxRight == null) {
            if (other.BoxRight != null)
                return false;
        } else if (!BoxRight.equals(other.BoxRight))
            return false;
        if (BoxBelow == null) {
            if (other.BoxBelow != null)
                return false;
        } else if (!BoxBelow.equals(other.BoxBelow))
            return false;
        return true;
    }

}
