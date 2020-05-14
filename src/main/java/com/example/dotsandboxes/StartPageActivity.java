package com.example.dotsandboxes;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

import com.example.dotsandboxes.model.PlayerType;

class StartPageActivity extends Activity implements OnClickListener {

    public static final String GAME_SETTINGS_KEY = "game_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout);

        Button PlayButton = (Button) findViewById(R.id.Playbutton);
        PlayButton.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY, MODE_PRIVATE);
        ((Spinner) findViewById(R.id.Player_type_1_spinner)).setSelection(settings.getInt("spielerTyp1", 0));
        ((Spinner) findViewById(R.id.Player_type_2_spinner)).setSelection(settings.getInt("spielerTyp2", 2));
        ((Spinner) findViewById(R.id.Field_size_x)).setSelection(settings.getInt("feldGroesseX", 3));
        ((Spinner) findViewById(R.id.Field_size_y)).setSelection(settings.getInt("feldGroesseY", 3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.hauptmenue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.app_exit)
            finish();

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        PlayerType playerType1 = PlayerType.parse((String) ((Spinner) findViewById(R.id.Player_type_1_spinner)).getSelectedItem());
        PlayerType playerType2 = PlayerType.parse((String) ((Spinner) findViewById(R.id.Player_type_2_spinner)).getSelectedItem());

        int fieldSizeX = Integer.parseInt((String) ((Spinner) findViewById(R.id.Field_size_x)).getSelectedItem());
        int fieldSizeY = Integer.parseInt((String) ((Spinner) findViewById(R.id.Field_size_y)).getSelectedItem());

        SharedPreferences settings = getSharedPreferences(GAME_SETTINGS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("PlayerType1", ((Spinner) findViewById(R.id.Player_type_1_spinner)).getSelectedItemPosition());
        editor.putInt("PlayerType2", ((Spinner) findViewById(R.id.Player_type_2_spinner)).getSelectedItemPosition());
        editor.putInt("FieldSizeX", ((Spinner) findViewById(R.id.Field_size_x)).getSelectedItemPosition());
        editor.putInt("FieldSizeY", ((Spinner) findViewById(R.id.Field_size_y)).getSelectedItemPosition());
        editor.commit();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("PlayerType1", playerType1);
        intent.putExtra("PlayerType2", playerType2);
        intent.putExtra("FieldSizeX", fieldSizeX);
        intent.putExtra("FieldSizeY", fieldSizeY);

        startActivity(intent);
    }

}
