package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.SchoolEntity;
import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 12.03.2018.
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder>
{
    private List<SchoolEntity> schoolEntityList;
    private String schoolNameString;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView schoolName;

        public ViewHolder(View view)
        {
            super(view);
            schoolName = view.findViewById(R.id.saSchoolName);
        }
    }

    public SchoolAdapter(List<SchoolEntity> schoolList)
    {
        this.schoolEntityList = schoolList;
    }

    @Override
    public SchoolAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.school_row, parent, false);

        return new SchoolAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SchoolAdapter.ViewHolder holder, int position)
    {
        SchoolEntity schoolEntity = schoolEntityList.get(position);
        final String stringSchoolName = schoolEntity.getSchoolName();
        try
        {
            holder.schoolName.setText(stringSchoolName);
        } catch (NullPointerException e)
        {
            System.out.println(e);
        }
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    setSchoolNameString(stringSchoolName);
                    System.out.println("Skole Valgt! " + stringSchoolName);
                    // Remove graying of OK button
                }
            });
        }
        }

        @Override
        public int getItemCount ()
        {
            return schoolEntityList.size();
        }

    public void clear()
    {
        final int size = schoolEntityList.size();
        if (size > 0)
        {
            for (int i = 0; i < size; i++)
            {
                schoolEntityList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    private void setSchoolNameString(String name)
    {
        this.schoolNameString = name;
    }

    public String getSchoolName()
    {
        return this.schoolNameString;
    }

}

