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
import android.view.MenuInflater;
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
    private static final int Request_RollDice = 2;

    private static final String PlayerList = "Start.PlayerList";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final StartActivity thisActivity = this;
        if(savedInstanceState == null) {
            this.playerList = new ArrayList<Player>();
            this.playerList.add(new Player(0));
            this.playerList.add(new Player(1));
        } else {
            this.playerList = (ArrayList<Player>) savedInstanceState.getSerializable(PlayerList);
        }

        FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diceRollingIntent = new Intent(thisActivity, DiceRollingActivity.class);
                diceRollingIntent.putExtra(DiceRollingActivity.PlayerList, playerList);

                startActivityForResult(diceRollingIntent, Request_RollDice);
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

        Button addDistortDiceButton = (Button) findViewById(R.id.addDistortDiceButton);
        addDistortDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerList.add(Player.createDistortDicePlayer(playerList.size()));
                playerListViewAdapter.notifyDataSetChanged();
            }
        });

        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerList.clear();
                playerList.add(new Player(0));
                playerList.add(new Player(1));
                playerListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addPlayer() {
        playerList.add(new Player(playerList.size()));
        playerListViewAdapter.notifyDataSetChanged();
    }

    private void removePlayer() {
        if(playerList.size() == 0)
            return;

        playerList.remove(playerList.size() - 1);
        playerListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(PlayerList, this.playerList);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Request_PlayerEdit:
                int playerId = data.getIntExtra(PlayerEditActivity.PlayerId, 1);

                Player editedPlayer = playerList.get(playerId);
                editedPlayer.setDiceCount(data.getIntExtra(PlayerEditActivity.DiceCount, 6));
                editedPlayer.setName(data.getStringExtra(PlayerEditActivity.Name));
                editedPlayer.setColour(data.getIntExtra(PlayerEditActivity.Colour, Color.BLACK));

                playerListViewAdapter.notifyDataSetChanged();

                break;

            case Request_RollDice:
                ArrayList<Player> newPlayerList = (ArrayList<Player>)data.getSerializableExtra(DiceRollingActivity.PlayerList);
                for(Player otherPlayer : newPlayerList) {
                    Player existingPlayer = this.playerList.get(otherPlayer.getId());
                    existingPlayer.copyFrom(otherPlayer);
                }

                playerListViewAdapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPlayer:
                addPlayer();
                return true;
            case R.id.removePlayer:
                removePlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
