package com.example.myapp_badminton.megha;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapp_badminton.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapter extends ArrayAdapter<AnswersModel> implements View.OnClickListener {

    Context mContext;
    private ArrayList<AnswersModel> dataSet;
    private int lastPosition = -1;
    private AnswersModel answersModel;


    public CustomAdapter(ArrayList<AnswersModel> data, Context context) {
        super(context, R.layout.cutom_lisview_row, data);
        this.dataSet = data;
        this.mContext = context;

    }


    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        AnswersModel dataModel = (AnswersModel) object;
        Snackbar.make(v, "view id " + v.getId(), Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();

        switch (v.getId()) {

            case R.id.shot_type:

                Snackbar.make(v, "Release date " + dataModel.getQuestionNumber(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                Log.e("adapter", "id is****** " + dataModel.questionNumber);


                break;


        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        answersModel = getItem(position);
        Log.e("answer model", "-->" + answersModel.toString());
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cutom_lisview_row, parent, false);
            viewHolder.myAnswerTitle = (TextView) convertView.findViewById(R.id.my_answer_title);
            viewHolder.correctAnswerTitle = (TextView) convertView.findViewById(R.id.correct_answer_title);
            viewHolder.correctShotLoc = (TextView) convertView.findViewById(R.id.correct_shot_loc);
            viewHolder.correctShotType = (TextView) convertView.findViewById(R.id.correct_shot_type);
            viewHolder.myShotLoc = (TextView) convertView.findViewById(R.id.shot_loc);
            viewHolder.myShotType = (TextView) convertView.findViewById(R.id.shot_type);
            viewHolder.timeTaken = (TextView) convertView.findViewById(R.id.time_taken);
            viewHolder.correctTimeTaken = (TextView) convertView.findViewById(R.id.correct_time_taken);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "item "+answersModel.elapsedTime, Toast.LENGTH_SHORT).show();
            }
        });*/

        viewHolder.myShotType.setText(answersModel.shotType);
        viewHolder.myShotLoc.setText(answersModel.ShotLocation);
        viewHolder.myAnswerTitle.setText("My selection " + answersModel.questionNumber);
        viewHolder.correctAnswerTitle.setText("Correct answers");
        viewHolder.correctShotLoc.setText(answersModel.correctShotLocation);
        viewHolder.correctShotType.setText(answersModel.correctShotType);
        viewHolder.timeTaken.setText(String.valueOf(answersModel.elapsedTime) + " sec");
        viewHolder.correctTimeTaken.setText(String.valueOf(answersModel.maxTime) + " sec");
        return convertView;
    }

    private static class ViewHolder {
        TextView myAnswerTitle;
        TextView correctAnswerTitle;
        TextView correctShotLoc;
        TextView correctShotType;
        TextView myShotLoc;
        TextView myShotType;
        TextView timeTaken;
        TextView correctTimeTaken;

    }


}
