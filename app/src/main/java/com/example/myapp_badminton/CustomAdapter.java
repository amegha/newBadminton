package com.example.myapp_badminton;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomAdapter extends ArrayAdapter<PlayerModel> implements View.OnClickListener {

    private PlayerModel playerModel;

    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        playerModel = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_layout, parent, false); // your layout where u are designed the image view and player name and id
          /*  viewHolder.City = (TextView) convertView.findViewById(R.id.my_answer_title);
            viewHolder.Academy = (TextView) convertView.findViewById(R.id.correct_answer_title);
            viewHolder.Level = (TextView) convertView.findViewById(R.id.correct_shot_loc);
*/
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.Academy.setText(playerModel.Academy);
        viewHolder.Aid.setText(playerModel.Aid);
        viewHolder.City.setText(playerModel.City);
        viewHolder.Level.setText(playerModel.Level);
        //set the image too;
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

    private static class ViewHolder {
        TextView City;
        TextView Academy;
        TextView Aid;
        TextView Level;

    }
}