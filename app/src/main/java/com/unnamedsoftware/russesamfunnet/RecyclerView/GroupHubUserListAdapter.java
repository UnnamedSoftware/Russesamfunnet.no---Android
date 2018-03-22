package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.R;
import com.unnamedsoftware.russesamfunnet.UserProfile;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 21.03.2018.
 */

public class GroupHubUserListAdapter extends RecyclerView.Adapter<GroupHubUserListAdapter.ViewHolder>
{
    private List<RussEntity> russEntityList;

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(View view)
        {
            super(view);
        }
    }

    public GroupHubUserListAdapter(List<RussEntity> russList)
    {
        this.russEntityList = russList;
    }

    @Override
    public GroupHubUserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_hub_user_row, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(GroupHubUserListAdapter.ViewHolder holder, int position)
    {
        final RussEntity russEntity = russEntityList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                intent.putExtra("russ_entity", russEntity.getRussId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){return russEntityList.size();}
}
