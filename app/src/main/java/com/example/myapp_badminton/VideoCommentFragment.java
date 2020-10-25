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
public class VideoCommentFragment extends Fragment {

    String user_id, username, utype, lastdate, score, scoreFilter;

    public VideoCommentFragment(String id) {
        // Required empty public constructor coach
        this.user_id = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_obtained_fragment, container, false);
        Intent intent1 = getActivity().getIntent();

            Bundle bundle1 = new Bundle();
            bundle1.putString("userId", user_id);
            Intent intentPlayer = new Intent(this.getActivity(), VideoCommentActivity.class).putExtras(bundle1);
            startActivity(intentPlayer);
        return view;
    }
}
