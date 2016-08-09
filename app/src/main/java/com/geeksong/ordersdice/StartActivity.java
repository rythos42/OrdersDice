package com.geeksong.ordersdice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StartActivity extends AppCompatActivity {
    private PlayerList playerList;
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
            this.playerList = new PlayerList();
            this.playerList.addNewPlayer();
            this.playerList.addNewPlayer();
        } else {
            this.playerList = (PlayerList) savedInstanceState.getSerializable(PlayerList);
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
                Player player = playerList.getByPosition(position);
                Intent editPlayerIntent = new Intent(thisActivity, PlayerEditActivity.class);
                editPlayerIntent.putExtra(PlayerEditActivity.DiceCount, player.getCurrentDiceCount());
                editPlayerIntent.putExtra(PlayerEditActivity.PlayerId, player.getId());
                editPlayerIntent.putExtra(PlayerEditActivity.Name, player.getName());
                editPlayerIntent.putExtra(PlayerEditActivity.Colour, player.getColour());

                startActivityForResult(editPlayerIntent, Request_PlayerEdit);
            }
        });
    }

    private void addPlayer() {
        playerList.addNewPlayer();
        playerListViewAdapter.notifyDataSetChanged();
    }

    private void removePlayer() {
        if(!playerList.hasPlayers())
            return;

        playerList.removeLastPlayer();
        playerListViewAdapter.notifyDataSetChanged();
    }

    private void reset() {
        playerList.clear();
        playerList.addNewPlayer();
        playerList.addNewPlayer();
        playerListViewAdapter.notifyDataSetChanged();
    }

    private void addDistortDice() {
        playerList.addDistortDice(getString(R.string.distort));
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

                Player editedPlayer = playerList.getById(playerId);
                editedPlayer.setDiceCount(data.getIntExtra(PlayerEditActivity.DiceCount, 6));
                editedPlayer.setName(data.getStringExtra(PlayerEditActivity.Name));
                editedPlayer.setColour(data.getIntExtra(PlayerEditActivity.Colour, Color.BLACK));

                playerListViewAdapter.notifyDataSetChanged();

                break;

            case Request_RollDice:
                PlayerList newPlayerList = (PlayerList)data.getSerializableExtra(DiceRollingActivity.PlayerList);
                this.playerList.clone(newPlayerList);
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
            case R.id.addPlayer: addPlayer(); break;
            case R.id.removePlayer: removePlayer(); break;
            case R.id.reset: reset(); break;
            case R.id.addDistortDice: addDistortDice(); break;
            default: return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
