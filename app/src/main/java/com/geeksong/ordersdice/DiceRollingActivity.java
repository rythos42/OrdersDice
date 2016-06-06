package com.geeksong.ordersdice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class DiceRollingActivity extends AppCompatActivity {
    public static final String PlayerList = "DiceRolling.PlayerList";

    private ArrayList<Player> playerList;
    private PlayerArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_rolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        playerList = (ArrayList<Player>) getIntent().getSerializableExtra(PlayerList);

        final ListView remainingDiceListView = (ListView) findViewById(R.id.remainingDiceList);
        adapter = new PlayerArrayAdapter(this, R.layout.player_options_item, playerList);
        remainingDiceListView.setAdapter(adapter);
        remainingDiceListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        remainingDiceListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.player_draw_context, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_remove_dice:
                        for(Player selectedPlayer : adapter.getSelectedPlayers())
                            selectedPlayer.removeDice();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) { return false; }

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                adapter.togglePlayerSelection(position);
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                adapter.clearSelected();
            }
        });
    }

    public void drawButtonClick(View v) {
        int totalDiceInBag = 0;
        for (Player player: playerList) {
            totalDiceInBag += player.getCurrentDiceCount();
        }

        int draw = new Random().nextInt(totalDiceInBag) + 1;    // draw can't be 0, because we're dealing with real numbers

        for (Player player: playerList) {
            totalDiceInBag -= player.getCurrentDiceCount();
            if(totalDiceInBag < draw) {
                TextView drawnPlayerNameText = (TextView) findViewById(R.id.drawnPlayerName);
                drawnPlayerNameText.setText(player.getName());
                player.removeDice();
                adapter.notifyDataSetChanged();
                break;
            }
        }

        boolean isBagEmpty = true;
        for(Player player: playerList) {
            if(player.hasDiceRemaining()) {
                isBagEmpty = false;
                break;
            }
        }

        if(isBagEmpty) {
            findViewById(R.id.drawButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.nextRound).setVisibility(View.VISIBLE);
        }
    }

    public void nextRoundClick(View v) {
        for(Player player: playerList)
            player.resetDice();

        findViewById(R.id.drawButton).setVisibility(View.VISIBLE);
        findViewById(R.id.nextRound).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.drawnPlayerName)).setText("");

        adapter.notifyDataSetChanged();
    }
}
