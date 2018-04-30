package com.unnamedsoftware.russesamfunnet.Search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.unnamedsoftware.russesamfunnet.Entity.KnotEntity;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.Knot;
import com.unnamedsoftware.russesamfunnet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by Alexander Eilert Berg on 21.02.2018.
 */

public class SearchWitnessAdapter extends RecyclerView.Adapter<SearchWitnessAdapter.ViewHolder>
{
    Context context;
    LayoutInflater layoutInflater;

    private List<RussEntity> dataSet = null;
    private ArrayList<RussEntity> arrayList;
    private HashMap<RussEntity, Bitmap> russMap = new HashMap<>();
    private KnotEntity knotEntity;

    public SearchWitnessAdapter(List<RussEntity> dataSet, KnotEntity knotEntity, HashMap<RussEntity, Bitmap> russMap)
    {
        this.russMap = russMap;
        this.knotEntity = knotEntity;
        this.dataSet = dataSet;
        this.arrayList = new ArrayList<>();
        arrayList.addAll(dataSet);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private CircularImageView userImage;
        ConstraintLayout layout;

        public ViewHolder(final View itemLayoutView)
        {

            super(itemLayoutView);
            layout = itemLayoutView.findViewById(R.id.searchRowLayout);
            userImage = itemLayoutView.findViewById(R.id.userProfilePictureSearchRow);
            name = itemLayoutView.findViewById(R.id.witness_name);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)

    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new SearchWitnessAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final RussEntity witness = arrayList.get(position);
            holder.name.setText(witness.getFirstName() + " " + witness.getLastName());
        try {
            if (russMap.get(witness) != null) {
                holder.userImage.setImageBitmap(russMap.get(witness));
            } else {
                holder.userImage.setImageResource(R.drawable.default_user);
            }
        } catch (Exception e)
        {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(v.getContext(), Knot.class);
                    intent.putExtra("knot_entity", knotEntity);
                    intent.putExtra("witness", witness);
                    System.out.println(knotEntity.getKnotId());
                    v.getContext().startActivity(intent);
                }
            });
        }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }
}
