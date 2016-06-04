package com.geeksong.ordersdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

public class DiceRollingActivity extends AppCompatActivity {
    public static final String PlayerList = "DiceRolling.PlayerList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_rolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<Player> playerList = (ArrayList<Player>) getIntent().getSerializableExtra(PlayerList);


        ListView remainingDiceList = (ListView) findViewById(R.id.remainingDiceList);
        remainingDiceList.setAdapter(new PlayerArrayAdapter(this, R.layout.player_options_item, playerList));

    }
}
