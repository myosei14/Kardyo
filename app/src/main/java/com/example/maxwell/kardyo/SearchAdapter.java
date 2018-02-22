package com.example.maxwell.kardyo;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maxwell on 2/21/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> signedUpUsersList;
    ArrayList<String> signedUpUsersStatusList;

    class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePic;
        TextView fullName, statusName;
        public SearchViewHolder(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            fullName = itemView.findViewById(R.id.statusName);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> signedUpUsersList, ArrayList<String> signedUpUsersStatusList) {
        this.context = context;
        this.signedUpUsersList = signedUpUsersList;
        this.signedUpUsersStatusList = signedUpUsersStatusList;
    }



    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items,parent,false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.fullName.setText(signedUpUsersList.get(position));
        holder.statusName.setText(signedUpUsersStatusList.get(position));
    }


    @Override
    public int getItemCount() {
        return signedUpUsersList.size();
    }
}
