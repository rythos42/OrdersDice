package com.geeksong.ordersdice;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.NumberPicker;

public class PlayerEditActivity extends AppCompatActivity {
    public static final String PlayerId = "PlayerEdit.PlayerId";
    public static final String DiceCount = "PlayerEdit.DiceCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NumberPicker diceCountPicker = (NumberPicker) findViewById(R.id.diceCountPicker);
        diceCountPicker.setMaxValue(20);
        diceCountPicker.setMinValue(1);
        diceCountPicker.setValue(getIntent().getIntExtra(DiceCount, 6));
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
        NumberPicker diceCountPicker = (NumberPicker) findViewById(R.id.diceCountPicker);
        Intent intent = new Intent();
        intent.putExtra(DiceCount, diceCountPicker.getValue());
        intent.putExtra(PlayerId, getIntent().getIntExtra(PlayerId, 1));
        this.setResult(RESULT_OK, intent);

        super.finish();
    }
}
