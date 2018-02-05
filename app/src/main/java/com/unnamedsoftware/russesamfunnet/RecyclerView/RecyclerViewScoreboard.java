package com.unnamedsoftware.russesamfunnet.RecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */
public class RecyclerViewScoreboard extends RecyclerView.Adapter<RecyclerViewScoreboard.ViewHolder>
{
    private List<ScoreboardEntity> userList;

    //Replace with the users ID!
    private Integer userID = 13;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name, position;
        private RelativeLayout layout;

        public ViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.rvUser);
            position = view.findViewById(R.id.rvPosition);
            layout = view.findViewById(R.id.rvScoreboardLayout);
        }
    }

    public RecyclerViewScoreboard(List<ScoreboardEntity> userList)
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
        ScoreboardEntity listUser = userList.get(position);
        if (listUser.getRussId().getRussId() == userID)
        {
            holder.layout.setBackgroundResource((R.drawable.user_scoreboard_border));
        }
        holder.name.setText(listUser.getRussId().getFirstName());
        holder.position.setText(String.valueOf(listUser.getPosition()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
