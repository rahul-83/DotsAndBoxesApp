package com.example.dotsandboxes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dotsandboxes.model.Box;
import com.example.dotsandboxes.model.GameField;
import com.example.dotsandboxes.model.Player;
import com.example.dotsandboxes.model.PlayerManager;
import com.example.dotsandboxes.model.PlayerType;
import com.example.dotsandboxes.model.Stroke;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@SuppressWarnings("ALL")
public class GameActivity extends AppCompatActivity {

    private GameFieldView gameFieldView;
    private GameField gameField;
    private PlayerManager playerManager;

    private final Handler    mHandler = new Handler();

    private volatile boolean running  = true;

    public GameActivity(GameFieldView gameFieldView) {
        this.gameFieldView = gameFieldView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Bundle intentExtras = getIntent().getExtras();

        PlayerType playerType1 = (PlayerType) intentExtras.get("PlayerType1");
        PlayerType playerType2 = (PlayerType) intentExtras.get("PlayerType2");

        int fieldSizeX = intentExtras.getInt("FieldSizeX");
        int fieldSizeY = intentExtras.getInt("FieldSizeY");

        gameField= GameField.toGenerate(fieldSizeX, fieldSizeY);
        playerManager = new PlayerManager();

        gameFieldView = (GameFieldView) findViewById(R.id.spielfeldView);
        gameFieldView.init(gameField);

        playerManager.addPlayer(
                new Player(getResources().getString(R.string.spieler_1_name),
                        BitmapFactory.decodeResource(getResources(), R.drawable.spieler_symbol_kaese),
                        getResources().getColor(R.color.spieler_1_farbe), playerType1));
        playerManager.addPlayer(
                new Player(getResources().getString(R.string.spieler_2_name),
                        BitmapFactory.decodeResource(getResources(), R.drawable.spieler_symbol_maus),
                        getResources().getColor(R.color.spieler_2_farbe), playerType2));

        startGameLoop();
    }

    @Override
    protected void onStop() {
        running = false;
        super.onStop();
    }

    public void startGameLoop() {
        Thread thread = new Thread(new GameLoopRunnable());
        thread.start();
        running = true;
    }

    private class GameLoopRunnable implements Runnable {

        public void run() {

            playerManager.NextPlayerSelection();

            while (!isGameOver()) {

                final Player player = playerManager.getCurrentPlayer();

                mHandler.post(new Runnable() {
                    public void run() {

                        ImageView imageView = (ImageView) findViewById(R.id.aktuellerSpielerSymbol);
                        imageView.setImageBitmap(player.getSymbol());

                        TextView textView = (TextView) findViewById(R.id.punkteAnzeige);
                        textView.setText(String.valueOf(determineScore(player)));
                    }
                });

                Stroke entry = null;

                if (!player.isComputerOpponent()) {

                    gameFieldView.resetLastEntry();

                    while ((entry = gameFieldView.getLastEntry()) == null) {
                        try {
                            synchronized (gameFieldView) {
                                gameFieldView.wait();
                            }
                        } catch (InterruptedException ignore) {

                        }
                    }

                } else {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignore) {
                    }

                    entry = computerOpponentTrain(player.getPlayerType());
                }

                SelectDash(entry);

                if (!running)
                    return;
            }


            if (isGameOver()) {

                mHandler.post(new Runnable() {

                    public void run() {

                        Player winner = IdentifyWinner();

                        int CupImageID = 0;
                        if (winner.getName().equals(getResources().getString(R.string.spieler_1_name)))
                            CupImageID = R.drawable.pokal_kaese;
                        else
                            CupImageID = R.drawable.pokal_maus;

                        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this)
                                .setTitle(getResources().getText(R.string.game_score))
                                .setIcon(getResources().getDrawable(CupImageID))
                                .setMessage(getGameOverDialogMessage())
                                .setCancelable(false)
                                .setPositiveButton(getResources().getText(R.string.play_again),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(getIntent());
                                            }
                                        })
                                .setNegativeButton(getResources().getText(R.string.zurueck_zum_hauptmenue),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                GameActivity.this.finish();
                                            }
                                        })
                                .create();

                        alertDialog.show();
                    }
                });
            }
        }

    }

    private String getGameOverDialogMessage() {

        Player winner = IdentifyWinner();

        StringBuilder sb = new StringBuilder();

        sb.append(getResources().getString(R.string.gewinner) + ": " + winner.getName() + "\n\n");

        for (Player player : playerManager.getPlayerList())
            sb.append(player.getName() + ":\t\t" + determineScore(player) + "\n");

        return sb.toString();
    }

    private Stroke computerOpponentTrain(PlayerType playerType) {

        Stroke stroke = LastOpenStrokeForBoxes();

        if (stroke != null)
            return stroke;

        Stroke randomStroke = selectRandomStroke();

        if (playerType == playerType.COMPUTER_MEDIUM) {

            int loopCounter = 0;

            while (randomStroke.isCouldCloseTheSorroundingBox()) {

                randomStroke = selectRandomStroke();

                if (++loopCounter >= 30)
                    break;
            }
        }

        return randomStroke;
    }

    private Stroke LastOpenStrokeForBoxes() {

        for (Box box : gameField.getOpenBoxList())
            if (box.getStrokesWithoutOwner().size() == 1)
                return box.getStrokesWithoutOwner().get(0);

        return null;
    }

    private Stroke selectRandomStroke() {

        List<Stroke> strokesWithoutOwner = new ArrayList<Stroke>(gameField.getStrokesWithoutOwner());
        Stroke randomStroke = strokesWithoutOwner.get(new Random().nextInt(strokesWithoutOwner.size()));

        return randomStroke;
    }

    public Player IdentifyWinner() {

        Player winner = null;
        int maxPoints = 0;

        for (Player player : playerManager.getPlayerList()) {

            int points = determineScore(player);

            if (points > maxPoints) {
                winner = player;
                maxPoints = points;
            }
        }

        return winner;
    }

    private void SelectDash(Stroke stroke) {

        if (stroke.getOwner() != null)
            return;

        Player CurrentPlayer = playerManager.getCurrentPlayer();

        boolean boxCouldClose = gameField.SelectDash(stroke,CurrentPlayer);

        if (!boxCouldClose)
            playerManager.NextPlayerSelection();

        gameFieldView.displayUpdate();
    }

    public boolean isGameOver() {
        return gameField.isAllBoxesHaveOwners();
    }



    public int determineScore(Player player) {

        int points= 0;

        for (Box box : gameField.getBoxArray())
            if (box.getOwner() == player)
                points++;

        return points;
    }

}

