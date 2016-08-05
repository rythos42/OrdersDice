package com.geeksong.ordersdice;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.rarepebble.colorpicker.ColorObserver;
import com.rarepebble.colorpicker.ColorPickerView;
import com.rarepebble.colorpicker.ObservableColor;

public class PlayerEditActivity extends AppCompatActivity {
    public static final String PlayerId = "PlayerEdit.PlayerId";
    public static final String DiceCount = "PlayerEdit.DiceCount";
    public static final String Name = "PlayerEdit.Name";
    public static final String Colour = "PlayerEdit.Colour";

    private int playerId;
    private int diceCount;
    private String name;
    private int colour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_edit);
        final PlayerEditActivity activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(savedInstanceState == null) {
            this.diceCount = getIntent().getIntExtra(DiceCount, 6);
            this.playerId = getIntent().getIntExtra(PlayerId, 0);
            this.name = getIntent().getStringExtra(Name);
            this.colour = getIntent().getIntExtra(Colour, Color.BLACK);
        } else {
            this.diceCount = savedInstanceState.getInt(DiceCount);
            this.playerId = savedInstanceState.getInt(PlayerId);
            this.name = savedInstanceState.getString(Name);
            this.colour = savedInstanceState.getInt(Colour);
        }

        ((TextView)findViewById(R.id.currentDiceCount)).setText(String.valueOf(this.diceCount));

        Button increaseDiceCount = (Button) findViewById(R.id.increaseDiceCount);
        increaseDiceCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceCount++;
                ((TextView) findViewById(R.id.currentDiceCount)).setText(String.valueOf(diceCount));
            }
        });

        Button decreaseDiceCount = (Button) findViewById(R.id.decreaseDiceCount);
        decreaseDiceCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceCount--;
                if(diceCount < 0)
                    diceCount = 0;

                ((TextView) findViewById(R.id.currentDiceCount)).setText(String.valueOf(diceCount));
            }
        });

        EditText name = (EditText)findViewById(R.id.playerName);
        name.setText(String.valueOf(this.name));
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                activity.name = s.toString();
            }
        });

        ColorPickerView colourPicker = (ColorPickerView) findViewById(R.id.colourPicker);
        colourPicker.setColor(this.colour);
        colourPicker.addColorObserver(new ColorObserver() {
            @Override
            public void updateColor(ObservableColor observableColor) {
                activity.colour = observableColor.getColor();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(PlayerId, this.playerId);
        savedInstanceState.putSerializable(Name, this.name);
        savedInstanceState.putSerializable(DiceCount, this.diceCount);
        savedInstanceState.putSerializable(Colour, this.colour);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(DiceCount, diceCount);
        intent.putExtra(PlayerId, playerId);
        intent.putExtra(Name, name);
        intent.putExtra(Colour, colour);
        this.setResult(RESULT_OK, intent);

        super.finish();
    }
}
