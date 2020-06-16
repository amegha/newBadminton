package com.example.myapp_badminton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerAdapterVh>implements Filterable{

private List<PlayerModel> playerModelList;
private List<PlayerModel> getPlayerModelListFilterd;
private Context context;
private SelectedPlayer selectedPlayer;


@NonNull
@Override
public PlayerAdapterVh onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        context=parent.getContext();

        return new PlayerAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_layout,null));
        }

public PlayerAdapter(List<PlayerModel> playerModelList,SelectedPlayer selectedPlayer){
        this.selectedPlayer=selectedPlayer;
        this.getPlayerModelListFilterd=playerModelList;
        this.playerModelList=playerModelList;
        }
@Override
public void onBindViewHolder(@NonNull PlayerAdapterVh holder,int position){
        PlayerModel playerModel=playerModelList.get(position);

        String Academy=playerModel.getAcademy();
        /*String Aid=playerModel.getAid();
         String City=playerModel.getCity();
        String Level=playerModel.getLevel();*/


        String prefix_Academy=playerModel.getAcademy().substring(0,1);
  /*  String prefix_Aid=playerModel.getAid().substring(0,1);
    String prefix_City=playerModel.getCity().substring(0,1);
    String prefix_Level=playerModel.getLevel().substring(0,1);*/

        holder.tvusername.setText(Academy);
        holder.tvPrefix.setText(prefix_Academy);






        }

@Override
public int getItemCount(){
        return playerModelList.size();
        }


@Override
public Filter getFilter(){

        Filter filter=new Filter(){
@Override
protected FilterResults performFiltering(CharSequence constraint){
        FilterResults filterResults=new FilterResults();
        if(constraint==null|constraint.length()==0){
        filterResults.count=getPlayerModelListFilterd.size();
        filterResults.values=getPlayerModelListFilterd;
        }
        else
        {
        String searchChr=constraint.toString().toLowerCase();
        List<PlayerModel> resultData=new ArrayList<>();

        for(PlayerModel playerModel:getPlayerModelListFilterd){
        if(playerModel.getAid().toLowerCase().contains(searchChr)){
        resultData.add(playerModel);
        }
        }
        filterResults.count=resultData.size();
        filterResults.values=resultData;
        }
        return filterResults;
        }

@Override
protected void publishResults(CharSequence constraint,FilterResults results){
        playerModelList=(List<PlayerModel>)results.values;
        notifyDataSetChanged();
        }
        };
        return filter;
        }
    //interface to display type  selected by user
    public interface SelectedPlayer{
        void selectedPlayer(PlayerModel playerModel);
    }

public class PlayerAdapterVh extends RecyclerView.ViewHolder {

    TextView tvPrefix;
    TextView tvusername;
    ImageView icon;

    public PlayerAdapterVh(@NonNull View itemView) {
        super(itemView);
        tvPrefix = itemView.findViewById(R.id.prefix);
        tvusername = itemView.findViewById(R.id.username);
        icon = itemView.findViewById(R.id.imageview);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPlayer.selectedPlayer(playerModelList.get(getAdapterPosition()));
            }
        });

    }
}


}
