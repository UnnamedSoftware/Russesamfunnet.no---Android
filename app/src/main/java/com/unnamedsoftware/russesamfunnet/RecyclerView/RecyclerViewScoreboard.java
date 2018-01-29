package com.unnamedsoftware.russesamfunnet.RecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */
public class RecyclerViewScoreboard extends RecyclerView.Adapter<RecyclerViewScoreboard.ViewHolder>
{
    private List<ListUser> userList;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name, position;

        public ViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.rvUser);
            position = view.findViewById(R.id.rvPosition);
        }
    }

    public RecyclerViewScoreboard(List<ListUser> userList)
    {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scoreboard_user_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListUser listUser = userList.get(position);
        holder.name.setText(listUser.getName());
        holder.position.setText(String.valueOf(listUser.getPosition()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
