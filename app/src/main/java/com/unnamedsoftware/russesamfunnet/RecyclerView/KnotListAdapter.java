package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
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

public class KnotListAdapter extends RecyclerView.Adapter<KnotListAdapter.ViewHolder>
{
    private List<KnotEntity> knotEntityList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private FloatingActionButton completeKnotView;

        public ViewHolder(final View itemLayoutView)
        {
            super(itemLayoutView);
            this.title = itemLayoutView.findViewById(R.id.rvTitle);
            this.completeKnotView = itemLayoutView.findViewById(R.id.completeKnotView);
        }
    }

    public KnotListAdapter(List<KnotEntity> knotList, Context context)
    {
        this.knotEntityList = knotList;
        this.context = context;
    }

    @Override
    public KnotListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.knot_list_knot_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KnotListAdapter.ViewHolder holder, int position)
    {
        final int pos = position;
        KnotEntity knotEntity = knotEntityList.get(position);
        holder.title.setText(knotEntity.getTitle());

        if(knotEntity.getCompleted())
        {
            holder.completeKnotView.setImageResource(R.drawable.ic_check_black_48dp);
            holder.completeKnotView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorKnotCompleted)));

        }else
        {
            holder.completeKnotView.setImageResource(R.drawable.ic_close_black_48dp);
            holder.completeKnotView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorKnotNotCompleted)));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            KnotEntity knotEntity = knotEntityList.get(pos);
            Intent intent = new Intent(v.getContext(), Knot.class);
            intent.putExtra("knot_entity", new KnotEntity(knotEntity.getKnotId(), knotEntity.getDetails(), knotEntity.getTitle(),knotEntity.getCompleted()));
            v.getContext().startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount()
    {
        return knotEntityList.size();
    }
}
