package com.example.dotsandboxes.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.dotsandboxes.GameFieldView;

import java.util.ArrayList;
import java.util.List;

public class Box {

    private int     rasterX;
    private int     rasterY;

    private Player Owner;

    private Stroke StrokeAbove;
    private Stroke StrokeBelow;
    private Stroke StrokeLeft;
    private Stroke StrokeRight;

    private Paint framePaint = new Paint();

    public Box(int rasterX, int rasterY) {
        this.rasterX = rasterX;
        this.rasterY = rasterY;

        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(5);
    }

    public int getRasterX() {
        return rasterX;
    }

    public int getRasterY() {
        return rasterY;
    }

    public int getPixelX() {
        return rasterX * GameFieldView.Box_Side_Length + GameFieldView.PADDING;
    }

    public int getPixelY() {
        return rasterY * GameFieldView.Box_Side_Length + GameFieldView.PADDING;
    }

    public Player getOwner() {
        return Owner;
    }

    public void setOwner(Player Owner) {
        this.Owner = Owner;
    }

    public Stroke getStrokeAbove() {
        return StrokeAbove;
    }

    public void setStrokeAbove(Stroke StrokeAbove) {
        this.StrokeAbove = StrokeAbove;
    }

    public Stroke getStrokeBelow() {
        return StrokeBelow;
    }

    public void setStrokeBelow(Stroke StrokeBelow) {
        this.StrokeBelow = StrokeBelow;
    }

    public Stroke getStrokeLeft() {
        return StrokeLeft;
    }

    public void setStrokeLeft(Stroke StrokeLeft) {
        this.StrokeLeft = StrokeLeft;
    }

    public Stroke getStrokeRight() {
        return StrokeRight;
    }

    public void setStrokeRight(Stroke StrokeRight) {
        this.StrokeRight = StrokeRight;
    }

    public List<Stroke> getStrokes() {

        List<Stroke> strokes = new ArrayList<Stroke>();
        if (StrokeAbove != null)
            strokes.add(StrokeAbove);
        if (StrokeBelow != null)
            strokes.add(StrokeBelow);
        if (StrokeLeft != null)
            strokes.add(StrokeLeft);
        if (StrokeRight != null)
            strokes.add(StrokeRight);
        return strokes;
    }

    public List<Stroke> getStrokesWithoutOwner() {

        List<Stroke> strokes = new ArrayList<Stroke>();
        if (StrokeAbove != null && StrokeAbove.getOwner() == null)
            strokes.add(StrokeAbove);
        if (StrokeBelow != null && StrokeBelow.getOwner() == null)
            strokes.add(StrokeBelow);
        if (StrokeLeft != null && StrokeLeft.getOwner() == null)
            strokes.add(StrokeLeft);
        if (StrokeRight != null && StrokeRight.getOwner() == null)
            strokes.add(StrokeRight);
        return strokes;
    }

    public boolean isAllStrokesHaveOwners() {
        return getStrokesWithoutOwner().size() == 0;
    }

    public Rect getRectStrokedabove() {

        if (StrokeAbove == null)
            return null;

        return new Rect(getPixelX() + GameFieldView.Box_Side_Length/ 4, getPixelY() - GameFieldView.Box_Side_Length/ 4, getPixelX() + (int) (GameFieldView.Box_Side_Length* 0.75), getPixelY() + GameFieldView.Box_Side_Length / 4);
    }


    public Rect getRectStrokedbelow() {

        if (StrokeBelow== null)
            return null;

        return new Rect(getPixelX() + GameFieldView.Box_Side_Length / 4, getPixelY() + (int) (GameFieldView.Box_Side_Length  * 0.75), getPixelX() + (int) (GameFieldView.Box_Side_Length * 0.75), getPixelY() + GameFieldView.Box_Side_Length  + GameFieldView.Box_Side_Length  / 4);
    }

    public Rect getRectStrokedleft() {

        if (StrokeLeft == null)
            return null;

        return new Rect(getPixelX() - GameFieldView.Box_Side_Length  / 4, getPixelY() + GameFieldView.Box_Side_Length  / 4, getPixelX() + GameFieldView.Box_Side_Length  / 4, getPixelY() + (int) (GameFieldView.Box_Side_Length  * 0.75));
    }

    public Rect getRectStrokedright() {

        if (StrokeRight == null)
            return null;

        return new Rect(getPixelX() + (int) (GameFieldView.Box_Side_Length * 0.75), getPixelY() + GameFieldView.Box_Side_Length / 4, getPixelX() + GameFieldView.Box_Side_Length + GameFieldView.Box_Side_Length  / 4, getPixelY() + (int) (GameFieldView.Box_Side_Length  * 0.75));
    }

    public Stroke investigatestroke(int pixelX, int pixelY) {

        if (getRectStrokedabove() != null && getRectStrokedabove().contains(pixelX, pixelY))
            return StrokeAbove;

        if (getRectStrokedbelow() != null && getRectStrokedbelow().contains(pixelX, pixelY))
            return StrokeBelow;

        if (getRectStrokedleft() != null && getRectStrokedleft().contains(pixelX, pixelY))
            return StrokeLeft;

        if (getRectStrokedright() != null && getRectStrokedright().contains(pixelX, pixelY))
            return StrokeRight;

        return null;
    }

    public void onDraw(Canvas canvas) {

        if (Owner!= null) {

            Paint fillingPaint = new Paint();
            fillingPaint.setColor(Owner.getColor());

            Rect destRect = new Rect(getPixelX(), getPixelY(), getPixelX() + GameFieldView.Box_Side_Length , getPixelY() + GameFieldView.Box_Side_Length );
            canvas.drawBitmap(Owner.getSymbol(), null, destRect, framePaint);
        }

        if (StrokeAbove == null) {
            framePaint.setColor(Color.BLACK);
            canvas.drawLine(getPixelX(), getPixelY(), getPixelX() + GameFieldView.Box_Side_Length , getPixelY(), framePaint);
        }

        if (StrokeBelow != null && StrokeBelow.getOwner() != null)
            framePaint.setColor(StrokeBelow.getOwner().getColor());
        else if (StrokeBelow != null)
            framePaint.setColor(Color.LTGRAY);
        else
            framePaint.setColor(Color.BLACK);

        canvas.drawLine(getPixelX(), getPixelY() + GameFieldView.Box_Side_Length , getPixelX() + GameFieldView.Box_Side_Length , getPixelY() + GameFieldView.Box_Side_Length , framePaint);

        if (StrokeLeft == null) {
            framePaint.setColor(Color.BLACK);
            canvas.drawLine(getPixelX(), getPixelY(), getPixelX(), getPixelY() + GameFieldView.Box_Side_Length , framePaint);
        }

        if (StrokeRight != null && StrokeRight.getOwner() != null)
            framePaint.setColor(StrokeRight.getOwner().getColor());
        else if (StrokeRight != null)
            framePaint.setColor(Color.LTGRAY);
        else
            framePaint.setColor(Color.BLACK);

        canvas.drawLine(getPixelX() + GameFieldView.Box_Side_Length , getPixelY(), getPixelX() + GameFieldView.Box_Side_Length , getPixelY() + GameFieldView.Box_Side_Length , framePaint);

        framePaint.setColor(Color.BLACK);
        canvas.drawRect(getPixelX() - 1, getPixelY() - 1, getPixelX() + 1, getPixelY() + 1, framePaint);
        canvas.drawRect(getPixelX() + GameFieldView.Box_Side_Length  - 1, getPixelY() - 1, getPixelX() + GameFieldView.Box_Side_Length  + 1, getPixelY() + 1, framePaint);
        canvas.drawRect(getPixelX() - 1, getPixelY() + GameFieldView.Box_Side_Length  - 1, getPixelX() + 1, getPixelY() + GameFieldView.Box_Side_Length  + 1, framePaint);
        canvas.drawRect(getPixelX() + GameFieldView.Box_Side_Length  - 1, getPixelY() + GameFieldView.Box_Side_Length - 1, getPixelX() + GameFieldView.Box_Side_Length  + 1, getPixelY() + GameFieldView.Box_Side_Length  + 1, framePaint);
    }

    @Override
    public String toString() {
        return "Box [rasterX=" + rasterX + ", rasterY=" + rasterY + ", Owner=" + Owner + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + rasterX;
        result = prime * result + rasterY;
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
        Box other = (Box) obj;
        if (rasterX != other.rasterX)
            return false;
        if (rasterY != other.rasterY)
            return false;
        return true;
    }

}
