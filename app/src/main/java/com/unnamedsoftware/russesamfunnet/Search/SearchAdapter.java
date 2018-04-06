package com.unnamedsoftware.russesamfunnet.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;

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
        layoutInflater = LayoutInflater.from(this.context);
        this.arrayList = new ArrayList<>();
        arrayList.addAll(dataSet);
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(final View itemLayoutView)
        {
            super(itemLayoutView);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }


}
