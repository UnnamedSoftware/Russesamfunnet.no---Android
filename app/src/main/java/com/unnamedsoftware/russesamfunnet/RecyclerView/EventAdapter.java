package com.unnamedsoftware.russesamfunnet.RecyclerView;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.unnamedsoftware.russesamfunnet.Entity.EventEntity;
import com.unnamedsoftware.russesamfunnet.R;

import java.util.List;

/**
 * Created by Alexander Eilert Berg on 15.03.2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>
{

    private List<EventEntity> eventEntityList;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView eventName;

        public ViewHolder(View view)
        {
            super(view);
            eventName = view.findViewById(R.id.eaEventName);
        }
    }

    public EventAdapter(List<EventEntity> eventEntityList)
    {
        this.eventEntityList = eventEntityList;
    }


    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position)
    {
        final EventEntity event = eventEntityList.get(position);
        final EventAdapter.ViewHolder viewHolder = holder;
        final String eventName = event.getEventName();

        viewHolder.eventName.setText(eventName);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("I've been clicked");
                final Dialog dialog = new Dialog(viewHolder.itemView.getContext());
                dialog.setTitle(eventName);
                dialog.setContentView(R.layout.event_dialog);

                try
                {

                    TextView eventDialogDescription = dialog.findViewById(R.id.eventDialogDescription);
                    eventDialogDescription.setText(event.getEventDescription());

                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

                Button okButton = dialog.findViewById(R.id.eventOkButton);
                okButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount(){return eventEntityList.size();}

}
