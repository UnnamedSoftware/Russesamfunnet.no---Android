package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.GroupEntity;
import com.unnamedsoftware.russesamfunnet.GroupHub;
import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 13.03.2018.
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder>
{
    private List<GroupEntity> groupEntitiesList;


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView groupName;
        private RelativeLayout layout;

        public ViewHolder(View view)
        {
            super(view);
            groupName = view.findViewById(R.id.glaGroupName);
            layout = view.findViewById(R.id.glaGroupListRowLayout);
        }
    }


    public GroupListAdapter(List<GroupEntity> groupEntityList){this.groupEntitiesList = groupEntityList;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final GroupEntity groupEntity = groupEntitiesList.get(position);
        holder.groupName.setText(groupEntity.getSearchName());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), GroupHub.class);
                intent.putExtra("groupName", groupEntity.getSearchName());
                intent.putExtra("groupID", groupEntity.getGroupID());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {return groupEntitiesList.size();}

}
