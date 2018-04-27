package com.unnamedsoftware.russesamfunnet.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Entity.ScoreboardEntity;
import com.unnamedsoftware.russesamfunnet.Global;
import com.unnamedsoftware.russesamfunnet.R;
import com.unnamedsoftware.russesamfunnet.UserProfile;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */
public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ViewHolder>
{
    private List<ScoreboardEntity> userList;
    private HashMap<ScoreboardEntity, Bitmap> scoreboardMap = new HashMap<>();

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name, position, points;
        private RelativeLayout layout;
        private CircularImageView userImage;


        public ViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.rvUser);
            points = view.findViewById(R.id.rvPoints);
            layout = view.findViewById(R.id.rvScoreboardLayout);
            position = view.findViewById(R.id.rvPosition);
            userImage = view.findViewById(R.id.userProfilePicture);
        }
    }


    public ScoreboardAdapter(List<ScoreboardEntity> userList, Context context, HashMap<ScoreboardEntity, Bitmap> scoreboardMap)
    {
        this.scoreboardMap = scoreboardMap;
        this.context = context;
        this.userList = userList;
    }
    public ScoreboardAdapter(List<ScoreboardEntity> userList, Context context)
    {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scoreboard_user_row, parent, false);

        return new ViewHolder(itemView);
    }
/*
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScoreboardEntity listUser = userList.get(position);
        if (listUser.getRussId().getRussId() == userID)
        {
            holder.layout.setBackgroundResource((R.drawable.user_scoreboard_border));
        }
        holder.name.setText(listUser.getRussId().getFirstName());
        holder.points.setText(String.valueOf(listUser.getPoints()));
        holder.position.setText(String.valueOf(listUser.getPosition()));

    }
*/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ScoreboardEntity listUser = userList.get(position);
        System.out.println("----- " + listUser.getRussId().getRussId() + " & " + ((Global) context.getApplicationContext()).getRussId() + " -----");
        if (listUser.getRussId().getRussId().equals(((Global) context.getApplicationContext()).getRussId()))
        {
            holder.layout.setBackgroundResource((R.drawable.user_scoreboard_border));
        }
        holder.name.setText(listUser.getRussId().getFirstName());
        holder.points.setText(String.valueOf(listUser.getPoints()));
        holder.position.setText(String.valueOf(listUser.getPosition()));
        try {
            if (scoreboardMap.get(listUser) != null) {
                holder.userImage.setImageBitmap(scoreboardMap.get(listUser));
            } else {
                holder.userImage.setImageResource(R.drawable.default_user);
            }
        } catch (Exception e)
        {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RussEntity tempRuss = listUser.getRussId();
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtra("russ_entity", tempRuss.getRussId());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void clear()
    {
        scoreboardMap.clear();
        final int size = userList.size();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                userList.remove(i);

            }

            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
