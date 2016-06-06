package com.geeksong.ordersdice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {
    private ArrayList<Player> playerList;
    private ArrayAdapter<Player> playerListViewAdapter;

    private static final int Request_PlayerEdit = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Activity thisActivity = this;
        playerList = new ArrayList<Player>();
        playerList.add(new Player(0));
        playerList.add(new Player(1));

        FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diceRollingIntent = new Intent(thisActivity, DiceRollingActivity.class);
                diceRollingIntent.putExtra(DiceRollingActivity.PlayerList, playerList);

                startActivity(diceRollingIntent);
            }
        });

        ListView playerListView = (ListView) findViewById(R.id.playerList);
        playerListViewAdapter = new PlayerArrayAdapter(this, R.layout.player_options_item, playerList);
        playerListView.setAdapter(playerListViewAdapter);
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = playerList.get(position);
                Intent editPlayerIntent = new Intent(thisActivity, PlayerEditActivity.class);
                editPlayerIntent.putExtra(PlayerEditActivity.DiceCount, player.getCurrentDiceCount());
                editPlayerIntent.putExtra(PlayerEditActivity.PlayerId, position);
                editPlayerIntent.putExtra(PlayerEditActivity.Name, player.getName());
                editPlayerIntent.putExtra(PlayerEditActivity.Colour, player.getColour());

                startActivityForResult(editPlayerIntent, Request_PlayerEdit);
            }
        });

        Button increasePlayerCount = (Button) findViewById(R.id.increasePlayerCount);
        increasePlayerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerList.add(new Player(playerList.size()));
                playerListViewAdapter.notifyDataSetChanged();
            }
        });

        Button decreasePlayerCount = (Button) findViewById(R.id.decreasePlayerCount);
        decreasePlayerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerList.remove(playerList.size() - 1);
                playerListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Request_PlayerEdit:
                int playerId = data.getIntExtra(PlayerEditActivity.PlayerId, 1);

                Player player = playerList.get(playerId);
                player.setDiceCount(data.getIntExtra(PlayerEditActivity.DiceCount, 6));
                player.setName(data.getStringExtra(PlayerEditActivity.Name));
                player.setColour(data.getIntExtra(PlayerEditActivity.Colour, Color.BLACK));

                playerListViewAdapter.notifyDataSetChanged();

                break;
        }
    }
}
