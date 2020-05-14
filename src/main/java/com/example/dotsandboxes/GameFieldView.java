package com.example.dotsandboxes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.dotsandboxes.model.Box;
import com.example.dotsandboxes.model.GameField;
import com.example.dotsandboxes.model.Stroke;


public class GameFieldView extends View implements OnTouchListener {

    public static int       Box_Side_Length = 50;
    public static int       PADDING         = 5;

    private GameField gameField;

    private volatile Stroke lastEntry;

    public GameFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(GameField gameField) {
        this.gameField = gameField;
        setOnTouchListener(this);
    }

    public Stroke getLastEntry() {
        return lastEntry;
    }

    public void resetLastEntry() {
        lastEntry = null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (gameField == null)
            return;

        int maxWidth = (w - PADDING * 2) / gameField.getBoxWidth();
        int maxHeight = (h - PADDING * 2) / gameField.getBoxHeight();
        Box_Side_Length= Math.min(maxWidth, maxHeight);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(getResources().getColor(R.color.hintergrund_farbe));

        if (gameField== null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint());
            return;
        }

        for (Box box : gameField.getBoxArray())
            box.onDraw(canvas);
    }

    public boolean onTouch(View view, MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return true;

        if (lastEntry != null)
            return true;

        int calculatedRasterX = (int) event.getX() / Box_Side_Length;
        int calculatedRasterY = (int) event.getY() / Box_Side_Length;

        Box box = gameField.getBoxes(calculatedRasterX, calculatedRasterY);

        if (box== null || box.getOwner() != null)
            return true;

        Stroke stroke = box.investigatestroke((int) event.getX(), (int) event.getY());

        if (stroke == null)
            return true;

        lastEntry = stroke;

        synchronized (this) {
            this.notifyAll();
        }

        return true;
    }

    public void displayUpdate() {
        postInvalidate();
    }

}