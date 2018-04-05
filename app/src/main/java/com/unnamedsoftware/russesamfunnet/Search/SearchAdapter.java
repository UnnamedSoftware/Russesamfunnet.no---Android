package com.unnamedsoftware.russesamfunnet.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.R;
import com.unnamedsoftware.russesamfunnet.RecyclerView.FeedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 21.02.2018.
 */

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.ViewHolder>
{


    Context context;
    LayoutInflater layoutInflater;

    private List<RussEntity> dataSet = null;
    private ArrayList<RussEntity> arrayList;

    public SearchAdapter(List<RussEntity> dataSet)
    {
        this.dataSet = dataSet;
        //layoutInflater = LayoutInflater.from(this.context);
        this.arrayList = new ArrayList<>();
        arrayList.addAll(dataSet);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        RelativeLayout layout;
        public ViewHolder(final View itemLayoutView)
        {

            super(itemLayoutView);
            layout = itemLayoutView.findViewById(R.id.searchRowLayout);
            name = itemLayoutView.findViewById(R.id.witness_name);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)

    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row,parent,false);
        return new SearchAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        RussEntity witness = arrayList.get(position);
        holder.name.setText(witness.getFirstName() + " " + witness.getLastName());
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }


}
