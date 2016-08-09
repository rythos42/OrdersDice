package com.geeksong.ordersdice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class DiceRollingActivity extends AppCompatActivity {
    public static final String PlayerList = "DiceRolling.PlayerList";
    public static final String DiceList = "DiceRolling.DiceList";

    private PlayerList playerList;
    private PlayerArrayAdapter playerAdapter;

    private DrawnDiceList drawnDiceList;
    private DrawnDiceArrayAdapter drawnDiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_rolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(savedInstanceState == null) {
            this.playerList = (PlayerList) getIntent().getSerializableExtra(PlayerList);
            this.drawnDiceList = new DrawnDiceList();
        } else {
            this.playerList = (PlayerList) savedInstanceState.getSerializable(PlayerList);
            this.drawnDiceList = (DrawnDiceList) savedInstanceState.getSerializable(DiceList);
        }
        checkDiceInBag();

        final ListView drawnDiceListView = (ListView) findViewById(R.id.drawnDiceList);
        drawnDiceAdapter = new DrawnDiceArrayAdapter(this, R.layout.drawn_dice_item, drawnDiceList, playerList);
        drawnDiceListView.setAdapter(drawnDiceAdapter);

        final ListView remainingDiceListView = (ListView) findViewById(R.id.remainingDiceList);
        playerAdapter = new PlayerArrayAdapter(this, R.layout.player_options_item, playerList);
        remainingDiceListView.setAdapter(playerAdapter);
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
                        for(Player selectedPlayer : playerAdapter.getSelectedPlayers())
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
                playerAdapter.togglePlayerSelection(position);
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                playerAdapter.clearSelected();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(PlayerList, this.playerList);
        savedInstanceState.putSerializable(DiceList, this.drawnDiceList);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void checkDiceInBag() {
        if(this.playerList.hasDiceRemaining())
            return;

        findViewById(R.id.drawButton).setVisibility(View.GONE);
        findViewById(R.id.nextRound).setVisibility(View.VISIBLE);
    }

    private void addDrawnPlayerToUi(Player player) {
        if(player != null) {
            drawnDiceList.addDrawnPlayer(player.getId());
            drawnDiceAdapter.notifyDataSetChanged();

            playerAdapter.notifyDataSetChanged();
        }

        checkDiceInBag();
    }

    public void drawButtonClick(View v) {
        Player drawnPlayer = this.playerList.drawDiceForPlayer();
        addDrawnPlayerToUi(drawnPlayer);
        findViewById(R.id.block).setVisibility(View.VISIBLE);
    }

    public void nextRoundClick(View v) {
        this.playerList.resetBag();
        playerAdapter.notifyDataSetChanged();

        drawnDiceList.clear();
        drawnDiceAdapter.notifyDataSetChanged();

        findViewById(R.id.drawButton).setVisibility(View.VISIBLE);
        findViewById(R.id.nextRound).setVisibility(View.GONE);
        findViewById(R.id.block).setVisibility(View.GONE);
    }

    public void blockClick(View view) {
        DrawnDice lastDrawnPlayer = drawnDiceList.get(drawnDiceList.size() - 1);
        lastDrawnPlayer.setBlocked();
        Player redrawnPlayer = this.playerList.block(lastDrawnPlayer.getPlayerId());
        addDrawnPlayerToUi(redrawnPlayer);
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
        intent.putExtra(PlayerList, this.playerList);
        this.setResult(RESULT_OK, intent);

        super.finish();
    }
}
