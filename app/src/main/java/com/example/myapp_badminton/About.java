package com.example.myapp_badminton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

//08-04-2020 created by pooja
/**
 * A simple {@link Fragment} subclass.

 */
public class About extends Fragment {


    public About() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_about, container, false);

        return view;
    }


}
