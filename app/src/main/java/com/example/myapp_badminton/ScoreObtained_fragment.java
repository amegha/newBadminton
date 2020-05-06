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
    String user_id;
    public ScoreObtained_fragment(String name) {
        // Required empty public constructor
        this.user_id=name;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_obtained, container, false);
        Intent intent1 = getActivity().getIntent();
        Bundle b = intent1.getExtras();

        user_id = b.getString("x");

        Bundle b1 = new Bundle();

        b1.putString("user_id", user_id);
        Intent intent = new Intent(this.getActivity(), GraphDisplay.class).putExtras(b1);
        startActivity(intent);

        return view;
    }
}
