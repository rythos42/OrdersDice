package com.geeksong.ordersdice;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawnDiceArrayAdapter extends ArrayAdapter<Integer> {
    private ArrayList<Integer> drawnDiceList;
    private ArrayList<Player> playerList;
    private int textViewResourceId;

    private static LayoutInflater inflater = null;

    public DrawnDiceArrayAdapter(Activity activity, int textViewResourceId, ArrayList<Integer> drawnDiceList, ArrayList<Player> playerList) {
        super(activity, textViewResourceId, drawnDiceList);

        this.drawnDiceList = drawnDiceList;
        this.playerList = playerList;
        this.textViewResourceId = textViewResourceId;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return drawnDiceList.size(); }

    @Override
    public Integer getItem(int position) { return this.drawnDiceList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    public static class ViewHolder {
        public TextView drawnPlayerName;
        public TextView drawnPlayerColour;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(textViewResourceId, null);
            holder = new ViewHolder();
            holder.drawnPlayerName = (TextView) vi.findViewById(R.id.drawnPlayerName);
            holder.drawnPlayerColour = (TextView) vi.findViewById(R.id.drawnPlayerColour);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        int playerId = drawnDiceList.get(position);
        Player player = playerList.get(playerId);
        holder.drawnPlayerName.setText(player.getName());
        holder.drawnPlayerColour.setBackgroundColor(player.getColour());

        return vi;
    }
}
