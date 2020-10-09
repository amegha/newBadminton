package com.example.myapp_badminton;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreObtained_fragment extends Fragment {

    String user_id, username, utype, lastdate, score, scoreFilter;

    public ScoreObtained_fragment(String name, String id, String type) {
        // Required empty public constructor coach
        this.username = name;
        this.user_id = id;
        this.utype = type;
    }

    public ScoreObtained_fragment(String name, String id, String type, String lastdate, String Score) {
        // Required empty public constructor
        this.username = name;
        this.user_id = id;
        this.utype = type;
        this.lastdate = lastdate;
        this.score = Score;
        this.scoreFilter = scoreFilter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score_obtained_fragment, container, false);
        Intent intent1 = getActivity().getIntent();
      /*  Bundle b = intent1.getExtras();

        username = b.getString("Name");
        user_id=b.getString("Id");
        utype=b.getString("type");
*/

        if (utype.equalsIgnoreCase("Coach")) { //for play game module since bar graph not in use
            Bundle b1 = new Bundle();
            b1.putString("x", username);
            b1.putString("id", user_id);
            b1.putString("type", utype);
//            b1.putString("Module", "BarGraph");
            b1.putString("Module", "playGameScore");

            Intent intentCoach = new Intent(this.getActivity(), Coach.class).putExtras(b1);
            startActivity(intentCoach);
        } else if (utype.equalsIgnoreCase("Player")) {
            Bundle bundle1 = new Bundle();
            bundle1.putString("Pname", username);
            bundle1.putString("Pid", user_id);
            bundle1.putString("type", utype);
            bundle1.putString("lastScoreDate", lastdate);
            bundle1.putString("ScoreLast", score);
            bundle1.putString("scoreFilter",scoreFilter);
            Intent intentPlayer = new Intent(this.getActivity(), GraphDisplay.class).putExtras(bundle1).putExtra("From", bundle1);
            startActivity(intentPlayer);
        }


        return view;
    }
}
