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

import com.facebook.AccessToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;
import com.unnamedsoftware.russesamfunnet.GroupList;
import com.unnamedsoftware.russesamfunnet.JSONObjectParser;
import com.unnamedsoftware.russesamfunnet.R;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander Eilert Berg on 12.04.2018.
 */
public class SearchAddUserToGroupAdapter extends RecyclerView.Adapter<SearchAddUserToGroupAdapter.ViewHolder>
{
    Context context;
    LayoutInflater layoutInflater;

    private List<RussEntity> dataSet = null;
    private ArrayList<RussEntity> arrayList;
    private Long groupID;
    private HashMap<RussEntity, Bitmap> russMap = new HashMap<>();
    private String token;

    public SearchAddUserToGroupAdapter(List<RussEntity> dataSet, Long groupId, String token, HashMap<RussEntity, Bitmap> russMap)
    {
        this.russMap = russMap;
        this.dataSet = dataSet;
        this.arrayList = new ArrayList<>();
        arrayList.addAll(dataSet);
        this.groupID = groupId;
        this.token = token;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        ConstraintLayout layout;
        private CircularImageView userImage;

        public ViewHolder(final View itemLayoutView)
        {

            super(itemLayoutView);
            layout = itemLayoutView.findViewById(R.id.searchRowLayout);
            userImage = itemLayoutView.findViewById(R.id.userProfilePictureSearchRow);
            name = itemLayoutView.findViewById(R.id.witness_name);
        }
    }


    @Override
    public SearchAddUserToGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)

    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new SearchAddUserToGroupAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final RussEntity russEntity = arrayList.get(position);
        holder.name.setText(russEntity.getFirstName() + " " + russEntity.getLastName());
        try {
            if (russMap.get(russEntity) != null) {
                holder.userImage.setImageBitmap(russMap.get(russEntity));
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
                System.out.println(" ¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨ " + groupID);
                System.out.println(" ¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨ " + russEntity.getRussId());
                String url = "";

                if (AccessToken.getCurrentAccessToken() != null)
                {
                    url = "http://158.38.101.146:8080/addGroupMember?accessToken=" + AccessToken.getCurrentAccessToken().getToken() + "&type=facebook";
                } else
                {
                    url = "http://158.38.101.146:8080/addGroupMember?accessToken=" + token + "&type=russesamfunnet";
                }

                url = url + "&groupId=" + groupID + "&russId=" + russEntity.getRussId();

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + url);

                try
                {
                    new JSONObjectParser(new JSONObjectParser.OnPostExecute()
                    {
                        @Override
                        public void onPostExecute(JSONObject jsonObject)
                        {
                            if (jsonObject != null){}
                        }
                    }).execute(new URL(url));
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }

                Intent intent = new Intent(v.getContext(), GroupList.class);
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
