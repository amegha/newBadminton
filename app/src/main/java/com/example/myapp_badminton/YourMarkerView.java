package com.example.myapp_badminton;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class YourMarkerView extends MarkerView {
    TextView tvContent;

    public void setCategory(List<String> category) {
        this.category = category;
    }

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    // String[] Fitness={" Agility (indoor)","Strength","Power","Speed","Stamina(Endurance)","Core-Strength /Stability","Reaction /Reflex Training","Flexibility"," Forehand Forward","Forehand Flank","Forehand Back-Court","Backhand Tap","Backhand Edge/Back-Court","Backhand Keep","Neutral","Forehand Net Approach","Forehand Net Stroke-Execution","Forehand Net Recovery"," Backhand Net","Forehand Flank","Backhand Flank","Forehand SideCourt(Middle)","Backhand SideCourt(Middle)","Overhead /Around the Head","Centre Positioning/Transition","Defense Block","Defense Drive/Counter","Defense Clear/Lift","Offence(BackCourt) Smash","Offence(BackCourt) Half-Smash","Offence(BackCourt) Drop","Offence(MidCourt) Smash","Offence(MidCourt) Parallel-Drive","Offence(MidCourt) Drop/Slice","Offence(FrontCourt) Push","Offence(FrontCourt) Attack","Offence(FrontCourt) Blocks/Interception","Service Short","Service Flick","Service Receiving Short","Service Receiving Flick","Net Play","Forehand Net Defensive Keep/Block","Forehand Net Lift/Clear","Forehand Net Flick/Push","Forehand Net Dribble","Forehand Net Cross/Netshot",  "Forehand Net Tap", "Backhand Net Defensive Keep/Block", "Backhand Net Lift/Clear", "Backhand Net Flick/Push", "Backhand Net Dribble", "Backhand Net Cross/Netshot", "Backhand Net Tap", "Forehand Flank Toss/ Clear(Defensive/Attacking)", "Forehand Flank Drop", "Forehand Flank Half-Smash", "Forehand Flank Smash", "Forehand Flank Reverse Slice", "Forehand Flank Defensive Drive(Low)", "Backhand Flank Toss/ Clear(Defensive/Attacking)", "Backhand Flank Drop", "Backhand Flank Half-Smash", "Backhand Flank Smash", "Backhand Flank Reverse Slice", "Backhand Flank Defensive Drive(Low)", "Forehand Side-Court(Middle)Defensive-Block/Straight/Cross", " Forehand Side-Court(Middle)Counter-Drive", " Forehand Side-Court(Middle)Attacking-Strokes", " Backhand Side-Court(MiddleDefensive-Block/Straight/Cross)", " Backhand Side-Court(Middle)Counter-Drive", " Backhand Side-Court(Middle)Attacking-Strokes", " Overhead/Around the head Toss/Clear", " Overhead/Around the head Drop", "Overhead/Around the head Half-Smash", "Overhead/Around the head Smash", "Overhead/Around the head Reverse-Slice", "Service Forehand-Short", "Service Forehand Flick", "Service Forehand-High", "Service Backhand Short", "Service Backhand Flick"};

    public List<String> category=new ArrayList<>();

    public YourMarkerView(Context context, int layoutResource, List<String> category) {
        super(context, layoutResource);
        this.category = category;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }



    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        System.out.println("Categories"+category.get((int) e.getX()));
        tvContent.setText("" + category.get((int) e.getX()));

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-getWidth() , -getHeight());
        }

        return mOffset;
    }

}
