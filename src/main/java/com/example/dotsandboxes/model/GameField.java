package com.example.dotsandboxes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GameField {

    private int            BoxWidth;
    private int            BoxHeight;

    private Box[][]   BoxArray;

    private List<Box> OpenBox    = new ArrayList<Box>();

    private Set<Stroke> StrokesWithoutOwner = new HashSet<Stroke>();

    private GameField(int BoxWidth, int BoxHeight) {
        this.BoxWidth = BoxWidth;
        this.BoxHeight = BoxHeight;

        this.BoxArray = new Box[BoxWidth][BoxHeight];
    }

    public List<Box> getBoxArray() {

        List<Box> boxarray = new ArrayList<Box>();

        for (int rasterX = 0; rasterX < BoxWidth; rasterX++) {
            for (int rasterY = 0; rasterY < BoxHeight; rasterY++) {
                boxarray.add(BoxArray[rasterX][rasterY]);
            }
        }

        return Collections.unmodifiableList(boxarray);
    }

    public List<Box> getOpenBoxList() {
        return Collections.unmodifiableList(OpenBox);
    }

    public Set<Stroke> getStrokesWithoutOwner() {
        return Collections.unmodifiableSet(StrokesWithoutOwner);
    }

    private void addBoxes(Box boxes) {
        BoxArray[boxes.getRasterX()][boxes.getRasterY()] = boxes;
        OpenBox.add(boxes);
    }

    private void addStroke(Stroke stroke) {
        StrokesWithoutOwner.add(stroke);
    }

    public Box getBoxes(int rasterX, int rasterY) {

        if (rasterX >= BoxWidth || rasterY >= BoxHeight)
            return null;

        return BoxArray[rasterX][rasterY];
    }

    public int getBoxWidth() {
        return BoxWidth;
    }

    public int getBoxHeight() {
        return BoxHeight;
    }

    private boolean CloseAllPossibleBoxes(Player OwnerToBeAssigned) {

        boolean BoxCouldBeClosed = false;

        Iterator<Box> OpenBoxIt = OpenBox.iterator();

        while (OpenBoxIt.hasNext()) {

            Box boxes = OpenBoxIt.next();

            if (boxes.isAllStrokesHaveOwners() && boxes.getOwner() == null) {
                boxes.setOwner(OwnerToBeAssigned);
                OpenBoxIt.remove();
                BoxCouldBeClosed = true;
            }
        }

        return BoxCouldBeClosed;
    }

    public boolean isAllBoxesHaveOwners() {
        return OpenBox.isEmpty();
    }

    public boolean SelectDash(Stroke stroke, Player player) {
        stroke.setOwner(player);
        StrokesWithoutOwner.remove(stroke);
        return CloseAllPossibleBoxes(player);
    }

    public static GameField toGenerate(int numberH, int numberV) {

        GameField gameField = new GameField(numberH,numberV);

        for (int rasterX = 0; rasterX < numberH; rasterX++) {
            for (int rasterY = 0; rasterY < numberV; rasterY++) {

                gameField.addBoxes(new Box(rasterX, rasterY));
            }
        }

        for (int rasterX = 0; rasterX < numberH; rasterX++) {
            for (int rasterY = 0; rasterY < numberV; rasterY++) {

                Box box = gameField.getBoxes(rasterX, rasterY);

                Box BoxBelow = null;
                Box BoxRight = null;

                if (rasterY < numberV - 1)
                    BoxBelow = gameField.getBoxes(rasterX, rasterY + 1);

                if (rasterX < numberH - 1)
                    BoxRight = gameField.getBoxes(rasterX + 1, rasterY);

                Stroke strokeBelow = new Stroke(box, BoxBelow, null, null);
                Stroke strokeRight = new Stroke(null, null, box, BoxRight);

                if (BoxRight != null) {
                    box.setStrokeRight(strokeRight);
                    BoxRight.setStrokeRight(strokeRight);
                    gameField.addStroke(strokeRight);
                }

                if (BoxBelow != null) {
                    box.setStrokeBelow(strokeBelow);
                    BoxBelow.setStrokeAbove(strokeBelow);
                    gameField.addStroke(strokeBelow);
                }
            }
        }

        return gameField;
    }

}

