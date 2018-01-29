package com.unnamedsoftware.russesamfunnet;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Alexander Eilert Berg on 29.01.2018.
 */
public abstract class RecyclerViewScoreboard extends RecyclerView.Adapter<RecyclerViewScoreboard.ViewHolder>
{
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
