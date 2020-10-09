package com.example.myapp_badminton.PlayModule;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp_badminton.R;

/*import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;*/

/**
 * Created by megha on 12/7/19.
 */

public class SubmitTypeOfShot extends Fragment implements View.OnLongClickListener {
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shot_type, container, false);
        Bundle b = this.getArguments();
        if (b != null) {
            String s = b.getString("location");
            Log.e("shot", "onSaveInstanceState: " + s);
        }
        b1 = v.findViewById(R.id.dd);
        b2 = v.findViewById(R.id.long_defence);
        b3 = v.findViewById(R.id.smash);
        b4 = v.findViewById(R.id.straight_block);
        b5 = v.findViewById(R.id.cross_court_block);
        b6 = v.findViewById(R.id.spin_net);
        b7 = v.findViewById(R.id.cross_court_net);
        b8 = v.findViewById(R.id.slice);
        b9 = v.findViewById(R.id.reverse_slice);
        b10 = v.findViewById(R.id.straight_slice);


        b1.setOnLongClickListener(this);
        b2.setOnLongClickListener(this);
        b3.setOnLongClickListener(this);
        b4.setOnLongClickListener(this);
        b5.setOnLongClickListener(this);
        b6.setOnLongClickListener(this);
        b7.setOnLongClickListener(this);
        b8.setOnLongClickListener(this);
        b9.setOnLongClickListener(this);
        b10.setOnLongClickListener(this);

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putString("type",);
        super.onSaveInstanceState(outState);

    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.out_left:
                answerLoc = "out_left";
                break;
            case R.id.out_net:
                answerLoc = "out_net";
                break;
            case R.id.out_right:
                answerLoc = "out_right";
                break;


            case R.id.top_left:
                answerLoc = "top_right";
                break;
            case R.id.top_middle:
                answerLoc = "top_left";
                break;
            case R.id.top_right:
                answerLoc = "top_middle";
                break;
        }
    }*/

    @Override
    public boolean onLongClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.dd:
                    shotName(v, "driveDefence", "drive");
                    break;
                case R.id.long_defence:
                    shotName(v, "longDefence", "all_shots");
                    break;
                case R.id.smash:
                    shotName(v, "smash", "smash");
                    break;

                case R.id.straight_block:
                    shotName(v, "straightBlock", "all_shots");
                    break;
                case R.id.cross_court_block:
                    shotName(v, "crossCourtBlock", "all_shots");
                    break;
                case R.id.spin_net:
                    shotName(v, "spinNet", "net");
                    break;
                case R.id.slice:
                    shotName(v, "slice", "all_shots");
                    break;
                case R.id.reverse_slice:
                    shotName(v, "reverseSlice", "reverse_slice");
                    break;
                case R.id.straight_slice:
                    shotName(v, "straightSlice", "all_shots");
                    break;
                case R.id.cross_court_net:
//                    shotName(v, "smash", "smash");
                    shotName(v, "crossCourtNet", "all_shots");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void shotName(View v, String shotType, String resName) {
        LinearLayout layout = new LinearLayout(v.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setWeightSum(2);
        layout.setOrientation(LinearLayout.VERTICAL);

        ImageView iv = new ImageView(v.getContext());
        switch (resName) {
            case "drive":
                iv.setImageResource(R.drawable.drive);
                break;
            case "smash":
                iv.setImageResource(R.drawable.smash);
                break;
            case "net":
                iv.setImageResource(R.drawable.net);
                break;
            case "reverse_slice":
                iv.setImageResource(R.drawable.reverse_slice);
                break;
            case "all_shots":
                iv.setImageResource(R.drawable.all_shots);

        }

//        iv.setImageResource(resourceId);
//        iv.setImageResource(R.drawable.all_shots);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                500, 1f);
        iv.setLayoutParams(lp);

        TextView textview = new TextView(v.getContext());
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        textview.setLayoutParams(lp1);
        int resId = getResources().getIdentifier(shotType, "string", getActivity().getPackageName());
        String data = getResources().getString(resId);
        textview.setText(data);

        layout.addView(iv);
        layout.addView(textview);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Shot Type");
        builder.setView(layout);
        builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

