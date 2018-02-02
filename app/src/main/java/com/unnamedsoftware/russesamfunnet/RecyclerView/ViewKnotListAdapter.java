package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Knot;
import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class ViewKnotListAdapter extends RecyclerView.Adapter<ViewKnotListAdapter.ViewHolder>
{
    private List<TempKnot> knotList;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;

        public ViewHolder(final View itemLayoutView)
        {
            super(itemLayoutView);
            title = itemLayoutView.findViewById(R.id.rvTitle);
        }
    }

    public ViewKnotListAdapter(List<TempKnot> knotList)
    {
        this.knotList = knotList;
    }

    @Override
    public ViewKnotListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.knot_list_knot_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewKnotListAdapter.ViewHolder holder,  int position)
    {
        final int pos = position;
        TempKnot tempKnot = knotList.get(position);
        holder.title.setText(tempKnot.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            TempKnot tempKnot = knotList.get(pos);
            Intent intent = new Intent(v.getContext(), Knot.class);
            intent.putExtra("knot_entity", new KnotEntity(tempKnot.getKnotID(), tempKnot.getDescription(), tempKnot.getTitle()));
            v.getContext().startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount()
    {
        return knotList.size();
    }



}
