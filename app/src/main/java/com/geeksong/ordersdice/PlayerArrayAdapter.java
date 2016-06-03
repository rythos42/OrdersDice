package com.geeksong.ordersdice;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerArrayAdapter extends ArrayAdapter<Player> {
    private Activity activity;
    private ArrayList<Player> playerList;
    private static LayoutInflater inflater = null;
    private int textViewResourceId;

    public PlayerArrayAdapter(Activity activity, int textViewResourceId, ArrayList<Player> playerList) {
        super(activity, textViewResourceId, playerList);

        this.activity = activity;
        this.playerList = playerList;
        this.textViewResourceId = textViewResourceId;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return playerList.size(); }

    @Override
    public Player getItem(int position) { return this.playerList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    public static class ViewHolder {
        public TextView name;
        public TextView colour;
        public TextView diceCount;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(textViewResourceId, null);
            holder = new ViewHolder();
            holder.name = (TextView) vi.findViewById(R.id.playerName);
            holder.colour = (TextView) vi.findViewById(R.id.playerColour);
            holder.diceCount = (TextView) vi.findViewById(R.id.diceCount);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        Player player = playerList.get(position);
        holder.name.setText(player.getName());
        holder.colour.setBackgroundColor(player.getColour());
        holder.diceCount.setText(String.valueOf(player.getDiceCount()));

        return vi;
    }
}
