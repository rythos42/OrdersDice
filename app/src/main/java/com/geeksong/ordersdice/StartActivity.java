package com.geeksong.ordersdice;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.playButton);
        fab.setOnClickListener(new View.OnClickListener() {
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

                startActivityForResult(editPlayerIntent, Request_PlayerEdit);
            }
        });


        NumberPicker playerCountPicker = (NumberPicker) findViewById(R.id.playerCounterPicker);
        NumberPicker.OnValueChangeListener numberPickerValueChanged = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int currentPlayerCount, int desiredPlayerCount) {
                if (desiredPlayerCount == currentPlayerCount)
                    return;

                if (desiredPlayerCount > currentPlayerCount) {
                    for (int i = currentPlayerCount; i < desiredPlayerCount; i++)
                        playerList.add(new Player(i));
                }

                if (desiredPlayerCount < currentPlayerCount) {
                    for (int i = currentPlayerCount-1; i >= desiredPlayerCount; i--)
                        playerList.remove(playerList.size() - 1);
                }

                playerListViewAdapter.notifyDataSetChanged();
            }
        };
        playerCountPicker.setOnValueChangedListener(numberPickerValueChanged);
        playerCountPicker.setMaxValue(Player.getMaximumPlayerCount());
        playerCountPicker.setMinValue(1);
        playerCountPicker.setValue(2);
        numberPickerValueChanged.onValueChange(playerCountPicker, 0, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Request_PlayerEdit:
                int playerId = data.getIntExtra(PlayerEditActivity.PlayerId, 1);
                int diceCount = data.getIntExtra(PlayerEditActivity.DiceCount, 6);

                Player player = playerList.get(playerId);
                player.setDiceCount(diceCount);

                playerListViewAdapter.notifyDataSetChanged();

                break;
        }
    }
}
