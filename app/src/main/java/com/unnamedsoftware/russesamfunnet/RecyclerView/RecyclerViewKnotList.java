package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 30.01.2018.
 */

public class RecyclerViewKnotList extends RecyclerView.Adapter<RecyclerViewKnotList.ViewHolder>
{
    private List<TempKnot> knotList;
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;

        public ViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.rvTitle);
        }
    }

    public RecyclerViewKnotList(List<TempKnot> knotList)
    {
        this.knotList = knotList;
    }

    @Override
    public RecyclerViewKnotList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.knot_list_knot_row,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewKnotList.ViewHolder holder, int position)
    {
        TempKnot tempKnot = knotList.get(position);
        holder.title.setText(tempKnot.getTitle());
    }

    @Override
    public int getItemCount()
    {
        return knotList.size();
    }

}
