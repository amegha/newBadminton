package com.example.myapp_badminton.megha;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp_badminton.R;

/*import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;*/


/**
 * Created by megha on 12/7/19.
 */

public class SubmitShotLocation extends Fragment implements View.OnClickListener {
    static String answerLoc;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;

    @SuppressLint("CutPasteId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shot_location, container, false);

       /* b1 = (Button) v.findViewById(R.id.out_net);
        b2 = (Button) v.findViewById(R.id.out_left);
        b3 = (Button) v.findViewById(R.id.out_right);

        b4 = (Button) v.findViewById(R.id.top_left);
        b5 = (Button) v.findViewById(R.id.top_middle);
        b6 = (Button) v.findViewById(R.id.top_right);

        b7 = (Button) v.findViewById(R.id.middle_left);
        b8 = (Button) v.findViewById(R.id.middle_middle);
        b9 = (Button) v.findViewById(R.id.middle_right);

        b10 = (Button) v.findViewById(R.id.bottom_left);
        b11 = (Button) v.findViewById(R.id.bottom_middle);
        b12 = (Button) v.findViewById(R.id.bottom_right);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b10.setOnClickListener(this);
        b11.setOnClickListener(this);
        b12.setOnClickListener(this);
*/
        return v;
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {

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


            case R.id.middle_left:
                answerLoc = "middle_left";
                break;
            case R.id.middle_middle:
                answerLoc = "middle_middle";
                break;
            case R.id.middle_right:
                answerLoc = "middle_right";
                break;


            case R.id.bottom_left:
                answerLoc = "bottom_left";
                break;
            case R.id.bottom_middle:
                answerLoc = "bottom_middle";
                break;
            case R.id.bottom_right:
                answerLoc = "bottom_right";
                break;

            default:
                break;
//        answerLoc=
        }*/

    }


}


