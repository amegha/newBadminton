package com.example.myapp_badminton;
/*
* completed by pooja,12/01/2020;
* used to display training types,filtering those data
* */
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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersAdapterVh> implements Filterable {
    private List<UserModel> userModelList;
    private List<UserModel> getUserModelListFilterd;
    private Context context;
    private SelectedUser selectedUser;
    @NonNull
    @Override
    public UsersAdapter.UsersAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();

        return new UsersAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_layout,null));
    }

    public UsersAdapter(List<UserModel> userModelList,SelectedUser selectedUser) {
        this.selectedUser=selectedUser;
        this.getUserModelListFilterd=userModelList;
        this.userModelList = userModelList;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersAdapterVh holder, int position) {
        UserModel userModel=userModelList.get(position);

        String username=userModel.getUserName();
        String prefix=userModel.getUserName().substring(0,1);

        holder.tvusername.setText(username);
        holder.tvPrefix.setText(prefix);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults=new FilterResults();
                        if(constraint == null | constraint.length()==0 ){
                            filterResults.count=getUserModelListFilterd.size();
                            filterResults.values=getUserModelListFilterd;
                        }
                        else
                        {
                                String searchChr=constraint.toString().toLowerCase();
                                List<UserModel> resultData=new ArrayList<>();

                                for(UserModel userModel:getUserModelListFilterd){
                                    if(userModel.getUserName().toLowerCase().contains(searchChr)){
                                        resultData.add(userModel);
                                    }
                                }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userModelList= (List<UserModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
//interface to display type  selected by user
    public interface SelectedUser{
        void selectedUser(UserModel userModel);
    }

    public class UsersAdapterVh extends RecyclerView.ViewHolder{

        TextView tvPrefix;
        TextView tvusername;
        ImageView icon;
        public UsersAdapterVh(@NonNull View itemView) {
            super(itemView);
            tvPrefix=itemView.findViewById(R.id.prefix);
            tvusername=itemView.findViewById(R.id.username);
            icon=itemView.findViewById(R.id.imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedUser.selectedUser(userModelList.get(getAdapterPosition()));
                }
            });

        }
    }


}
