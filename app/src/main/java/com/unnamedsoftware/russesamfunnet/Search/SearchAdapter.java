package com.unnamedsoftware.russesamfunnet.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.Entity;
import com.unnamedsoftware.russesamfunnet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexander Eilert Berg on 21.02.2018.
 */

public class ListViewAdapter extends BaseAdapter
{

    Context context;
    LayoutInflater layoutInflater;

    private List<Entity> dataSet = null;
    private ArrayList<Entity> arrayList;

    public ListViewAdapter(Context context, List<Entity> dataSet)
    {
        this.context = context;
        this.dataSet = dataSet;
        layoutInflater = LayoutInflater.from(this.context);
        this.arrayList = new ArrayList<>();
        arrayList.addAll(dataSet);
    }

    public class ViewHolder
    {
        TextView textView;
    }


    @Override
    public int getCount()
    {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position)
    {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        final ViewHolder viewHolder;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.search_listview_item, null);

            viewHolder.textView = view.findViewById(R.id.searchResult);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(dataSet.get(position).getSearchName());
        return view;
    }

    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        dataSet.clear();
        if (charText.length() == 0)
        {
            dataSet.addAll(arrayList);
        }else
        {
            for (Entity entity : arrayList)
            {
                if (entity.getSearchName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    dataSet.add(entity);
                }
            }
        }
        notifyDataSetChanged();
    }

}
